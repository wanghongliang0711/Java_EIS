package com.mic.eis.annotation;

import java.lang.annotation.*;

/**
 * @author calisto
 * @date 2020-07-08 1:59 下午
 */

@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented //生成文档
public @interface OptionLog {
    String value() default "";
}