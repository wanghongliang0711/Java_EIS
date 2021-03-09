package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-08-13 15:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_part_number")
public class PartNumber extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 零件编号
     */
    @NotNull(message = "零件编号不能为空")
    private String partNumber;

    /**
     * 零件描述
     */
    @NotNull(message = "零件描述不能为空")
    private String partDescription;

}
