package com.mic.eis.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author blake.wang
 * @date 2021-01-05 10:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtPilotRunReportVo {

    /**
     * 项目id
     */
    @NotNull(message = "项目id不能为空")
    private Long protId;

    /**
     *  文件 版本  当前版本 ：now
     */
    @NotNull(message = "文件版本不能为空")
    private String fileVer;

    /**
     * 严重程度
     */
    private List<String> severity;

    /**
     *  状态
     */
    private List<String> status;

}
