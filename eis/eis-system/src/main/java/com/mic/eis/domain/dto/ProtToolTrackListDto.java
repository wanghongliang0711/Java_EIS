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
public class ProtToolTrackListDto extends ProtToolTrackList {

    /**
     * 子种类
     */
    private List<ProtToolTrackListIssueReport> children;

}
