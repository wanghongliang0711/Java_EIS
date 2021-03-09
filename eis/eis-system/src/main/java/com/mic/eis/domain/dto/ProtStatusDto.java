package com.mic.eis.domain.dto;

import com.mic.eis.domain.model.ProtStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author blake.wang
 * @date 2020-11-27 9:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtStatusDto extends ProtStatus {


    /**
     *  PVTSys
     */
    private List<String> pvtSysData;


    /**
     *  DVTSys
     */
    private List<String> dvtSysData;

    /**
     *  EVTSys
     */
    private List<String> evtSysData;

    /**
     * 主项目 Code
     */
    private String protCode;

    /**
     * 主项目 描述
     */
    private String protDes;

    /**
     * 子项目 描述
     */
    private String sonProtDes;

    /**
     * 子项 Fact
     */
    private List<ProtStatusDto> children;



}
