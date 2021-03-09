package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 部门实体类
 * @author blake.wang
 * @date 2020-07-07 15:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_dept")
public class Dept extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     *部门名
     */
    @NotNull(message = "部门名不能为空")
    private String name;

    /**
     *父部门id
     */
    private Long parentId;


}
