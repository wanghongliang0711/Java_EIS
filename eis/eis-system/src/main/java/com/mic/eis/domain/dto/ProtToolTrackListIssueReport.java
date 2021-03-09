package com.mic.eis.domain.dto;

import com.mic.eis.domain.model.ProtFile;
import com.mic.eis.domain.model.ProtToolTrackList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author blake.wang
 * @date 2020-10-21 15:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtToolTrackListIssueReport extends ProtToolTrackList {

    /**
     * Re-Open  reopen 按钮是否生效  true false
     */
    private String reopen;


    /**
     * txList  CLOSE 和 WAIVE 是 O ; OPEN 是  X;  没出现是 -
     */
    private List<String> txList;

    /**
     * 图片 列表
     */
    private List<ProtFile> picture;

}
