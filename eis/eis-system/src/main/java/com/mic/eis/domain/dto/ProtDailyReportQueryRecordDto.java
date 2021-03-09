package com.mic.eis.domain.dto;

import com.mic.eis.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-11-18 9:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtDailyReportQueryRecordDto extends BaseModel {



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
    private List<String> issuePriority;




}
