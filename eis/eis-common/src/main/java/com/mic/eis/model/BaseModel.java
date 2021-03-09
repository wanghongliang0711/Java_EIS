package com.mic.eis.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author calisto
 * @date 2020-07-07 11:27 上午
 */

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class BaseModel implements Serializable {

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
    @JsonIgnore
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
}
