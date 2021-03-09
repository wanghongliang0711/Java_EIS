package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-08-13 14:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_mold_number")
public class MoldNumber extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 模穴数
     */
    @NotNull(message = "模穴数不能为空")
    private String moldNumber;

}
