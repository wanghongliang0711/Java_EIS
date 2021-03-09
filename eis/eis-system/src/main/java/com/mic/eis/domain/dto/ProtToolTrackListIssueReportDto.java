package com.mic.eis.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author blake.wang
 * @date 2020-10-21 15:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtToolTrackListIssueReportDto extends ProtToolTrackListIssueReport{

    /**
     * 子种类
     */
    private List<ProtToolTrackListIssueReport> children;

}
