package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-07-08 15:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_prot")
public class Prot extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 项目名
     */
    @NotNull(message = "项目名不能为空")
    private String name;

    /**
     * 项目code
     */
    private String code;

    /**
     * 项目状态
     */
    private Integer status;

    /**
     * 项目描述
     */
    private String remark;

    /**
     * 父项目id
     */
    private Long parentId;

}
