package com.mic.eis.util;

import com.mic.eis.domain.model.User;
import org.apache.shiro.SecurityUtils;

/**
 * @author calisto
 * @date 2020-07-08 2:09 下午
 */
public class HttpContextUtil {

    /**
     * 获取当前已经登录的用户
     * @return
     */
    public static User getCurrentUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }
}
