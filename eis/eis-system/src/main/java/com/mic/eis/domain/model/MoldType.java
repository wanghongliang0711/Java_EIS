package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author blake.wang
 * @date 2020-08-13 14:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_mold_type")
public class MoldType extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 模具种类、子类
     */
    private String moldType;

    /**
     * 父类id/ 父种类默认是0
     */
    private Long parentId;

}
