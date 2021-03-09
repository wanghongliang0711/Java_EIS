package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author calisto
 * @since 2020-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_part_test")
public class PartTest extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 不知道什么名字
     */
    @TableField("`name`")
    private String name;

    /**
     * 部件
     */
    private String parts;

    /**
     * 部件描述
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 行数
     */
    private Integer line;

    /**
     * 项目id
     */
    private Long protId;

}
