package com.mic.eis.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-11-30 13:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtStatusInsertDto {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID",hidden = true)
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 子 数据 的 id
     */
    @NotNull(message = "子数据 的 id 不能为空")
    private Long sonId;

    /**
     * 主项目id
     */
    private Long mainProtId;

    /**
     *  阶段
     */
    private String phase;

    /**
     *  拥有者
     */
    private String ownedBy;

    /**
     *  项目类型
     */
    private String protType;

    /**
     *  期间
     */
    private String period;

    /**
     *  实现我的成本
     */
    private String achievedMeCost;

    /**
     *  我的成本目标
     */
    private String meCostTarget;

    /**
     *  供应商
     */
    private String vender;

    /**
     *  剖面 输入一个 只在主里面显示
     */
    private String section;

    /**
     *  团队    只在主里面显示
     */
    private String team;

    /**
     *  任务负责人   只在主里面显示
     */
    private String taskLeader;

    /**
     *  工程师
     */
    private String engineers;

    /**
     *  项目启动会议 时间
     */
    private String protKickoffMeetPlan;
    private String protKickoffMeetFact;

    /**
     *  工业设计
     */
    private String industrialDesignPlanStart;
    private String industrialDesignPlanEnd;
    private String industrialDesignFactStart;
    private String industrialDesignFactEnd;

    /**
     *  ID虚拟样本
     */
    private String idDummySamplePlanStart;
    private String idDummySamplePlanEnd;
    private String idDummySampleFactStart;
    private String idDummySampleFactEnd;

    /**
     *  机械设计
     */
    private String mechanicalDesignPlanStart;
    private String mechanicalDesignPlanEnd;
    private String mechanicalDesignFactStart;
    private String mechanicalDesignFactEnd;

    /**
     *  CNC样品
     */
    private String cncSamplePlanStart;
    private String cncSamplePlanEnd;
    private String cncSampleFactStart;
    private String cncSampleFactEnd;

    /**
     *  软工具零件
     */
    private String softToolPartPlanStart;
    private String softToolPartPlanEnd;
    private String softToolPartFactStart;
    private String softToolPartFactEnd;

    /**
     *  Tooling  时间
     */
    private String toolingPlan;
    private String toolingFact;

    /**
     * EVTSys
     */
    private Long evtSysId;
    private String evtSysPlanStart;
    private String evtSysPlanEnd;
    private String evtSysFactStart;
    private String evtSysFactEnd;


    /**
     * EVTRev
     */
    private Long evtRevId;
    private String evtRevPlanStart;
    private String evtRevFactStart;


    /**
     * DVTSys
     */
    private Long dvtSysId;
    private String dvtSysPlanStart;
    private String dvtSysPlanEnd;
    private String dvtSysFactStart;
    private String dvtSysFactEnd;


    /**
     * DVTRev
     */
    private Long dvtRevId;
    private String dvtRevPlanStart;
    private String dvtRevFactStart;


    /**
     * PVTSys
     */
    private Long pvtSysId;
    private String pvtSysPlanStart;
    private String pvtSysPlanEnd;
    private String pvtSysFactStart;
    private String pvtSysFactEnd;


    /**
     * PVTRev
     */
    private Long pvtRevId;
    private String pvtRevPlanStart;
    private String pvtRevFactStart;

}
