package com.mic.eis.domain.vo;

import lombok.Data;

/**
 * @author calisto
 * @date 2020-07-07 3:09 下午
 */

@Data
public class LoginVo {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
