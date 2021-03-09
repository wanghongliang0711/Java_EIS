package com.mic.eis.config;

import com.mic.eis.constant.SystemConstant;
import com.mic.eis.filter.ShiroLogoutFilter;
import com.mic.eis.filter.ShiroUnLoginFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author calisto
 * @date 2020-07-07 10:50 上午
 */
@Configuration
public class ShiroConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Value("${spring.redis.password}")
    private String redisPwd;

    @Bean
    public ShiroFilterFactoryBean shireFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //将自定义 的Filter注入shiroFilter中
        shiroFilterFactoryBean.getFilters()
                .put("authc", new ShiroUnLoginFilter());
        shiroFilterFactoryBean.getFilters()
                .put("logout", new ShiroLogoutFilter());
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //放通swagger
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        //可在DefaultFilter查
        //退出过滤器
        filterChainDefinitionMap.put("/logout", "logout");
        //游客，开发权限
        filterChainDefinitionMap.put("/guest/**", "anon");
        //druid接口
        filterChainDefinitionMap.put("/druid/**","anon");
        //其余接口一律拦截,主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
        filterChainDefinitionMap.put("/**", "authc");

        //注入拦截器map
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 注入一个securityManager
     * @author calisto
     * @date 2019-10-09 15:37:38
     * @param
     **/
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        defaultSecurityManager.setRealm(customRealm());
        defaultSecurityManager.setCacheManager(redisCacheManager());
        defaultSecurityManager.setSessionManager(sessionManager());
        return defaultSecurityManager;
    }

    /**
     * 注入自定义Realm
     * @author calisto
     * @date 2019-10-09 18:06:50
     * @param
     **/
    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        //设置加密算法验证器
        customRealm.setCredentialsMatcher(credentialsMatcher());
        return customRealm;
    }

    /**
     * 自定义加密算法
     * @return
     */
    @Bean
    public HashedCredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法
        credentialsMatcher.setHashAlgorithmName("md5");
        //设置散列次数
        credentialsMatcher.setHashIterations(1);
        return credentialsMatcher;
    }

    /**
     * 注入自定义sessionManager
     * @return
     */
    @Bean
    public SessionManager sessionManager() {
        CustomSessionManager customSessionManager = new CustomSessionManager();
        //设置session过期时间,传入单位毫秒
        customSessionManager.setGlobalSessionTimeout(SystemConstant.SESSION_EXPIRE);
        //设置redis持久化,过期时间是session的过期时间
        customSessionManager.setSessionDAO(redisSessionDAO());
        return customSessionManager;
    }

    /**
     *  开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启aop注解支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * redis管理器
     * @return
     */
    public RedisManager getRedisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisHost + ":" +  redisPort);
        if (StringUtils.isNotBlank(redisPwd)) {
            redisManager.setPassword(redisPwd);
        }
        return redisManager;
    }

    /**
     * 配置缓存管理
     * @return
     */
    public RedisCacheManager redisCacheManager() {
        org.crazycake.shiro.RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(getRedisManager());
        //设置权限缓存过期时间(30分钟)
        redisCacheManager.setExpire(SystemConstant.PERMISSION_EXPIRE);
        return redisCacheManager;
    }

    /**
     * redis持久化
     * @return
     */
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(getRedisManager());
        // 自定义sessionId
        redisSessionDAO.setSessionIdGenerator(new CustonSessionIdGenerator());
        return redisSessionDAO;
    }

    /**
     * 管理shiro bean生命周期
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
        return lifecycleBeanPostProcessor;
    }

    /**
     * 用来扫描上下文寻找所有的Advistor(通知器), 将符合条件的Advisor应用到切入点的Bean中，需要在LifecycleBeanPostProcessor创建后才可以创建
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public  DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
