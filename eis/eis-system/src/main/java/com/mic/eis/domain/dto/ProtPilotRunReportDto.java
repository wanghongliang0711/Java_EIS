package com.mic.eis.domain.dto;

import com.mic.eis.domain.model.ProtFile;
import com.mic.eis.domain.model.ProtPilotRunReport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author blake.wang
 * @date 2021-01-07 13:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtPilotRunReportDto extends ProtPilotRunReport {

    /**
     * 图片 列表
     */
    private List<ProtFile> picture;

}
