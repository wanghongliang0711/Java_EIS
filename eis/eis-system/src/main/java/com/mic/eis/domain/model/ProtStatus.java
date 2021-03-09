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
 * @date 2020-11-26 13:56
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_prot_status")
public class ProtStatus extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 主项目id
     */
    @NotNull(message = "主项目id不能为空")
    private Long mainProtId;

    /**
     * 父类id/ 主父类默认是0
     */
    @NotNull(message = "parentId不能为空")
    private Long parentId;

    /**
     *  阶段
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String phase;

    /**
     *  拥有者
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String ownedBy;

    /**
     *  项目类型
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String protType;


    /**
     *  期间
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String period;

    /**
     *  实现我的成本
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String achievedMeCost;

    /**
     *  我的成本目标
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String meCostTarget;

    /**
     *  供应商
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String vender;

    /**
     *  剖面 输入一个 只在主里面显示
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String section;

    /**
     *  团队    只在主里面显示
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String team;

    /**
     *  任务负责人   只在主里面显示
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String taskLeader;

    /**
     *  工程师
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String engineers;

    /**
     *  计划还是实际  Plan  Fact
     */
    private String planOrFact;

    /**
     *  项目启动会议 时间
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String protKickoffMeet;

    /**
     *  工业设计 开始时间
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String industrialDesignStart;

    /**
     *  工业设计 结束时间
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String industrialDesignEnd;


    /**
     *  ID虚拟样本  开始时间----2020 1216 删除该字段，数据库没删除
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String idDummySampleStart;

    /**
     *  ID虚拟样本  结束时间----2020 1216 删除该字段，数据库没删除
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String idDummySampleEnd;

    /**
     *  机械设计  开始时间
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String mechanicalDesignStart;

    /**
     *  机械设计  结束时间
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String mechanicalDesignEnd;

    /**
     *  CNC样品  开始时间----2020 1216 该字段被修改为mockup,数据库没修改
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String cncSampleStart;

    /**
     *  CNC样品  结束时间----2020 1216 该字段被修改为mockup,数据库没修改
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String cncSampleEnd;

    /**
     *  软工具零件  开始时间----2020 1216 删除该字段，数据库没删除
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String softToolPartStart;

    /**
     *  软工具零件  结束时间----2020 1216 删除该字段，数据库没删除
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String softToolPartEnd;

    /**
     *  Tooling  时间----2020 1216 该字段被修改为tooling start,数据库没修改
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String tooling;

}
