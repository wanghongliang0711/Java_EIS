package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-10-27 9:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_prot_ToolingPlanAux")
public class ProtToolPlanAux extends BaseModel {

    private static final long serialVersionUID = 1L;


    /**
     * 所属数据id
     */
    @NotNull(message = "所属数据id不能为空")
    private Long dataId;

    /**
     *  数据类型
     *  A: TEXTURE  CATEGORY
     *  B: INSERT NUT SPEC.
     */
    private String dataType;

    /**
     * 数据 内容
     */
    private String dataContent;


}
