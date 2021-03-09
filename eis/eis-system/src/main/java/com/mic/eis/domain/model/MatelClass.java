package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author blake.wang
 * @date 2020-08-13 15:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_material_classification")
public class MatelClass extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 材料种类、子类
     */
    private String material;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 层级 1-2-3 目前是3级
     */
    private String code;

    /**
     * 父类id/ 父种类默认是0
     */
    private Long parentId;

}
