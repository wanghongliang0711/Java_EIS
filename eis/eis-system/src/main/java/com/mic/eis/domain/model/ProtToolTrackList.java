package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-10-15 14:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_prot_ToolingTrackingList")
public class ProtToolTrackList extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 子项目id
     */
    @NotNull(message = "子项目id不能为空")
    private Long protId;

    /**
     * 零件名称
     */
    private String partName;

    /**
     * 零件编号
     */
    private String partNo;

    /**
     * 是否是第一个  第一个 true  不是第一个 false
     */
    private String isFirst;

    /**
     * 是否显示， true false 显示 true 不显示 false
     */
    private String isShow;

    /**
     * 厂商
     */
    private String vendor;

    /**
     * 计划日期
     */

    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String datePlan;

    /**
     * 执行日期
     */

    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String dateAct;

    /**
     * 父类id/ 主父类默认是0
     */
    @NotNull(message = "parentId不能为空")
    private Long parentId;

    /**
     * 版本T1、T2、T3。。。
     */
    private Integer tx;

    /**
     *  版本 排序、个数、类型编号
     */
    private Integer item;

    /**
     * 问题描述
     */
    private String issueDescription;

    /**
     * 行为
     */
    private String action;

    /**
     * bug等级   Serious  Medium  Low
     */
    private String issuePriority;

    /**
     * status 状态 Open Closed WAVE
     */
    private String status;

    /**
     * remark 备注
     */
    private String remark;

    /**
     * 根本原因
     */
    private String rootCause;

}
