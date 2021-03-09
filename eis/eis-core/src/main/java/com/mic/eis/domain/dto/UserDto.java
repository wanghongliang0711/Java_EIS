package com.mic.eis.domain.dto;

import com.mic.eis.domain.model.User;
import lombok.Data;

import java.util.List;

/**
 * @author calisto
 * @date 2020-07-09 1:46 下午
 */
@Data
public class UserDto extends User {

    /**
     * 用户拥有的角色id
     */
    private List<Long> roleIds;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     *  项目成员角色  0:普通用户  1:项目管理员
     */
    private Integer member;
}
