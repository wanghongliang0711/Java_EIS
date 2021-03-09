package com.mic.eis.domain.dto;

import com.mic.eis.domain.model.ProtToolTrackList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author blake.wang
 * @date 2020-10-16 13:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtToolTrackListForDailyReportDto extends ProtToolTrackList {


    /**
     * 子项目 名
     */
    private String sonProtName;

    /**
     * 这个 子项目中 最新 的 ToolTrackList数据
     */
    private List<ProtToolTrackListDto> children;
}
