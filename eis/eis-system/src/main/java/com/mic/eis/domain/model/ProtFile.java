package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author blake.wang
 * @date 2020-08-10 10:34
 */
@Data
@Accessors(chain = true)
@TableName("sys_file")
public class ProtFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID",hidden = true)
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
//    @JsonIgnore
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @JsonIgnore
    private Date updateTime;

    /**
     * 创建人
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    @JsonIgnore
    private String createUser;

    /**
     * 修改人
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    @JsonIgnore
    private String updateUser;

    /**
     * 是否删除 : 0 未删除,1 已删除
     */
    @TableLogic
    @ApiModelProperty(hidden = true)
    @TableField(value = "is_delete")
    @JsonIgnore
    private Integer isDelete;


    /**
     * 文件名
     */
//    @NotNull(message = "文件名不能为空")
    private String name;

    /**
     * 文件大小
     */
    private Double size;

    /**
     * 子项目id
     */
    @NotNull(message = "子项目id不能为空")
    private Long protId;

    /**
     * 文件地址
     */
    private String resource;

    /**
     * 文件备注
     */
    private String remark;

    /**
     * 类别A、B、C。。。。一共八个类别 待定 还有其他类别
     * subProtFile_A  ME Test Report
     * subProtFile_B  Dfx Guidline
     * subProtFile_C  Feasibility Study Report
     * subProtFile_D  Structure Analysis Report
     * subProtFile_E  Themal Analysis Report
     * subProtFile_F  Tooling DFM Reports
     * subProtFile_G  DFMEA
     * subProtFile_H  Tolerance Analysis Report
     * subProtFile_I  Assembly Notice
     * P1  ToolTrackList Issue Picture
     * P2  Tool Plan Picture
     * P3  Pilot Run Report Picture
     */
    private String category;

}
