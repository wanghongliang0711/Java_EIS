package com.mic.eis.aspect;

import com.alibaba.fastjson.JSON;
import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.model.OptLog;
import com.mic.eis.mapper.OptLogMapper;
import com.mic.eis.util.HttpContextUtil;
import com.mic.eis.utils.DateUtil;
import com.mic.eis.utils.IPUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author calisto
 * @date 2019-12-06 2:19 下午
 */
@Aspect
@Component
public class LogAspect {

    @Resource(name = "defaultThreadPool")
    private ThreadPoolTaskExecutor executor;

    @Resource
    private OptLogMapper optLogMapper;

    @Pointcut("@annotation(com.mic.eis.annotation.OptionLog)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        try {
            // 执行方法
            result = point.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        // 执行时长(毫秒)
        long elapsedTime = System.currentTimeMillis() - beginTime;
        // 保存日志
        saveLog(point, elapsedTime);
        return result;
    }

    /**
     * 保存日志
     *
     * @param joinPoint
     * @param elapsedTime
     */
    private void saveLog(ProceedingJoinPoint joinPoint, long elapsedTime) {
        // 保存的日志
        OptLog sysOptLog = new OptLog();
        //获取用户名
        sysOptLog.setUsername(HttpContextUtil.getCurrentUser().getUsername());
        //获取用户ip地址
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        String ipAddress;
        if (Objects.nonNull(servletRequestAttributes)) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            ipAddress = IPUtil.getIpAddress(request);
            sysOptLog.setIpAddr(ipAddress);
        }
        // 另起线程查询后再存储用户归属地，免得请求缓慢
        executor.execute(() -> {
            // 获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            OptionLog optionLog = method.getAnnotation(OptionLog.class);
            if (optionLog != null) {
                String value = optionLog.value();
                // 保存获取的操作
                sysOptLog.setOperation(value);
            }
            //获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            //获取请求的方法名
            String methodName = method.getName();
            sysOptLog.setMethod(className + "." + methodName);
            //请求的参数
            Object[] args = joinPoint.getArgs();
            //将参数所在的数组转换成json
            String params = JSON.toJSONString(args);
            sysOptLog.setParams(params);
            // 设置执行的时间
            sysOptLog.setElapsedTime(elapsedTime);
            // 设置发生的时间
            sysOptLog.setCreateTime(DateUtil.getCurrentTime());
            optLogMapper.insert(sysOptLog);
        });
    }

}