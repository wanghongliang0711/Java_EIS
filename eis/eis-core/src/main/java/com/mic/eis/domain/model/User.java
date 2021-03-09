package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 用户实体类
 *
 * @author calisto
 * @since 2020-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user")
public class User extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 工号
     */
    @NotNull(message = "工号不能为空")
    private String jobNum;

    /**
     * 是否禁用
     */
    @Min(value = 0, message = "请正确选择状态")
    @Max(value = 1, message = "请正确选择状态")
    private Integer status;

    /**
     * 邮箱
     */
    @NotNull(message = "邮箱不能为空")
    private String email;

    /**
     * 部门id
     */
    @NotNull(message = "所属部门不能为空")
    private Long deptId;

}
