package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author blake.wang
 * @date 2020-11-26 14:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_prot_statusAux")
public class ProtStatusAux extends BaseModel {

    private static final long serialVersionUID = 1L;


    /**
     *  Project Status中的数据 主数据id
     */
    private Long protStatusId;

    /**
     * EVTSys  EVTRev  DVTSys  DVTRev  PVTSys PVTRev
     * Review Meeting 两个时间 plan_start  fact_start
     * System Assembly   四个时间
     */
    private String dataType;


    /**
     * 排序  EVTSys 1\2\3
     */
    private Integer dataNum;


    /**
     * 计划开始时间
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String planStart;

    /**
     * 计划结束时间
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String planEnd;

    /**
     * 实际开始时间
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String factStart;

    /**
     * 实际结束时间
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String factEnd;

}
