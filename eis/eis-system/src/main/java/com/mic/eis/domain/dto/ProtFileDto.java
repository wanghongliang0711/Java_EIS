package com.mic.eis.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author blake.wang
 * @date 2020-12-24 16:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtFileDto {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 上传时间
     */
    private String createTime;

    /**
     * 上传人
     */
    private String createUser;

    /**
     *      * subProtFile_A  ME Test Report
     *      * subProtFile_B  Dfx Guidline
     *      * subProtFile_C  Feasibility Study Report
     *      * subProtFile_D  Structure Analysis Report
     *      * subProtFile_E  Themal Analysis Report
     *      * subProtFile_F  Tooling DFM Reports
     *      * subProtFile_G  DFMEA
     *      * subProtFile_H  Tolerance Analysis Report
     *      * subProtFile_I  Assembly Notice
     */
    private String fileType;

    /**
     * 文件名
     */
    private String name;

    /**
     * 前台 upload 功能是否有效
     */
    private String isUpload;

    /**
     * 文件备注
     */
    private String remark;

    /**
     * 文件类别
     */
    private String category;
}
