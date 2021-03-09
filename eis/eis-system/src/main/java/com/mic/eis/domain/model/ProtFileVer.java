package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-09-07 10:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_prot_file_ver")
public class ProtFileVer extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 子项目id
     */
    @NotNull(message = "子项目id不能为空")
    private Long protId;

    /**
     *  文件 版本
     */
    @NotNull(message = "文件版本不能为空")
    private String fileVer;


    /**
     * 类别
     * A: Me Part List
     * B: Tool Plan
     * C: Pilot Run Report
     */
    @NotNull(message = "文件类别不能为空")
    private String category;

    /**
     * 文件备注
     */
    private String remark;

    /**
     * 文件 approval
     */
    private String approval;

}
