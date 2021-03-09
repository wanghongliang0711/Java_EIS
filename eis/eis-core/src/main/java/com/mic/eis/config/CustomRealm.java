package com.mic.eis.config;

import com.mic.eis.domain.model.Acl;
import com.mic.eis.domain.model.Role;
import com.mic.eis.domain.model.User;
import com.mic.eis.mapper.AclMapper;
import com.mic.eis.mapper.RoleMapper;
import com.mic.eis.mapper.UserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author calisto
 * @date 2020-07-07 10:52 上午
 */
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private AclMapper aclMapper;

    /**
     * 授权流程
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User loginUser = (User) principalCollection.getPrimaryPrincipal();
        User user = userMapper.selectUserByName(loginUser.getUsername());
        //初始化角色权限集合
        Set<String> roles;
        Set<String> acls = new HashSet<>();
        //查询角色集合
        Set<Role> rolesByRoleId = roleMapper.selectRolesByUserId(user.getId());
        //手机角色对象集合中的角色code集合
        roles = rolesByRoleId.stream().map(Role::getCode).collect(Collectors.toSet());
        for (Role role : rolesByRoleId) {
            //查询角色对应的权限
            Set<Acl> aclByRoleId = aclMapper.selectAclsByRoleId(role.getId());
            //将权限code添加到权限集合
            acls.addAll(aclByRoleId.stream().map(Acl::getCode).collect(Collectors.toSet()));
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(acls);
        return simpleAuthorizationInfo;
    }

    /**
     * 登录认证流程
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        User user = userMapper.selectUserByName(username);
        if (Objects.isNull(user)) {
            throw new UnknownAccountException("账号不存在");
        }
        //将此用户存放到登录认证info中，无需自己做密码对比Shiro会为我们进行密码对比校验
        return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
    }
}
