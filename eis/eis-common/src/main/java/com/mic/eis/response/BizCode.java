package com.mic.eis.response;

import java.io.Serializable;

/**
 * @author calisto
 * @date 2019-10-07 3:45 下午
 */
public enum BizCode implements Serializable {

    /**
     * 错误码解读：
     * 第一位：1代表原封不动提示错误码与错误信息给用户，2代表仅在控制台打印，展示统一话术,如系统正在开小差...
     * 第二至三位：代表功能模块('00'代表全局)
     * 第四至六位：代表模块下的错误码
     */
    SUCCESS("100000","操作成功"),
    FAILED("100999", "操作失败"),
    NO_LOGIN("-100000","用户未登录"),
    LOG_OUT("-100001", "用户已注销"),
    NO_PERMISSION("-100999", "无权限访问"),
    NO_DATA_PERMISSION("-100002", "无数据权限"),
    LOGIN_FAILED("100003","用户名或密码不正确"),
    OLDPWD_ERROR("100004","旧密码输入错误"),
    ROLE_CONTAINS_USER("100005","角色下包含用户不允许删除"),
    ROOT_NOTALLOW_DELETE("100007","超级管理员/角色不允许删除"),
    USERNAME_REPEAT("100009","用户名或工号已存在"),
    PROJECT_REPEAT("100008","项目名或项目code已存在"),
    MOLD_REPEAT("100008","已存在, 请勿重复插入！");

    BizCode() {}

    BizCode(String bizCode, String description) {
        this.code = bizCode;
        this.message = description;
    }

    /**
     * 状态码
     */
    private String code;

    /**
     * 信息
     */
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
