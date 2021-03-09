package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 权限实体类
 *
 * @author calisto
 * @since 2020-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_acl")
public class Acl extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 权限码
     */
    private String code;

    /**
     * 权限名
     */
    private String name;

}
