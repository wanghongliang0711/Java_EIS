package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-09-07 14:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_prot_MePartList")
public class ProtMePartList extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 子项目id
     */
    @NotNull(message = "子项目id不能为空")
    private Long protId;

    /**
     *  文件 版本  当前版本 ：now
     */
    @NotNull(message = "文件版本不能为空")
    private String fileVer;

    /**
     * 排序 No.
     */
//    @NotNull(message = "排序No.不能为空")
    private Integer no;

    /**
     * BOM Level
     */
    private String bomLevel;


    /**
     * 零件编号
     */
    private String partNumber;

    /**
     * 零件描述
     */
    private String partDescription;

    /**
     * 材料种类、子类
     */
    private String meterial;

    /**
     * 重量
     */
    private String weight;

    /**
     * Q'ty
     */
    private String qty;


    /**
     * 卖家
     */
    private String vendor;


    /**
     * 单价
     */
    private String unitPrice;


    /**
     * 总价
     */
    private String totalPrice;


    /**
     * 备注
     */
    private String remark;

}
