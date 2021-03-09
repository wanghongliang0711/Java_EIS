package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author calisto
 * @date 2020-07-08 1:15 下午
 */
@Data
@Accessors(chain = true)
@TableName("sys_user_role")
public class UserRole extends BaseModel {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;
}
