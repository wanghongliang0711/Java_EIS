package com.mic.eis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author calisto
 * @date 2020-07-07 11:19 上午
 */
@Component
@Slf4j
public class GeneratorCodeUtil {

    private static final IdWorkerUtil ID_WORKER_UTIL = new IdWorkerUtil();


    private GeneratorCodeUtil() {
    }

    /**
     * <p>description:[生成一个唯一ID]</p>
     * Created on 2019/3/29 10:30
     *
     * @return: java.lang.Long
     * @author <a href="mailto: cickmal@gmail.com">maliang</a>
     */
    public static Long generateKey() {
        int jvmId = getJvmPid();
        if (jvmId == -1) {
            return ID_WORKER_UTIL.nextId();
        } else {
            jvmId = jvmId % 1000;
            log.info("Jvm id is : {}", jvmId);
            ID_WORKER_UTIL.setWorkerId(jvmId);
            return ID_WORKER_UTIL.nextId();
        }

    }

    /**
     * <p>description:[获取JVM进程id,取后三位值]</p>
     * Created on 2019/3/29 10:29
     *
     * @return: int
     * @author <a href="mailto: cickmal@gmail.com">maliang</a>
     */
    private static int getJvmPid() {
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            Field jvm = runtime.getClass().getDeclaredField("jvm");
            jvm.setAccessible(true);
            Object mgmt = jvm.get(runtime);
            Method pidMethod = mgmt.getClass().getDeclaredMethod("getProcessId");
            pidMethod.setAccessible(true);
            return (int) pidMethod.invoke(mgmt);
        } catch (Exception e) {
            log.error("get jvm pid error:{}", e.toString());
            return -1;
        }
    }
}
