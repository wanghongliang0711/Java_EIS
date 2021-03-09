package com.mic.eis.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-12-23 16:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {


    /**
     * 旧密码
     */
    @NotNull(message = "旧密码不能为空")
    private String lastPassword;

    /**
     * 新密码
     */
    @NotNull(message = "新密码不能为空")
    private String password;

    /**
     * 确认密码
     */
    @NotNull(message = "确认密码不能为空")
    private String confirmPassword;
}
