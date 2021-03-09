package com.mic.eis.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-08-10 13:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtFileUploadVo {

    /**
     * 上传的文件
     */
    @NotNull(message = "上传的文件不能为空")
    private MultipartFile file;

    /**
     * 子项目id
     */
    @NotNull(message = "子项目id不能为空")
    private Long protId;

    /**
     * 文件备注
     */
    private String remark;

    /**
     * 类别A、B、C。。。。一共八个类别 待定 还有其他类别
     *
     * uploadTemplateFile
     * 类别： T1 --> MePartList
     *       T2 --> toolingTrackingList
     *       T3 --> ToolingPlan
     *       T4 --> PilotRunReport
     */
    @NotNull(message = "文件类别不能为空")
    private String category;


}
