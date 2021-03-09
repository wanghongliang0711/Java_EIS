package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-11-18 9:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_prot_dailyReportQueryRecord")
public class ProtDailyReportQueryRecord extends BaseModel {

    private static final long serialVersionUID = 1L;


    /**
     * 项目id
     */
    @NotNull(message = "项目id不能为空")
    private Long protId;


    /**
     * 子项目id
     */
    private Long subProtId;


    /**
     * bug等级   Serious  Medium  Low
     */
    private String issuePriority;




}
