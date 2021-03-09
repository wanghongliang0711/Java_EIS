package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-10-27 9:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_prot_ToolingPlan")
public class ProtToolPlan extends BaseModel {

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
    private Integer no;


    /**
     * 零件编号
     */
    private String partNo;

    /**
     * 零件描述
     */
    private String partDes;

    /**
     * ProE文件名
     */
    private String proEFileName;

    /**
     * 数据版本
     */
    private String version;

    /**
     * 重量
     */
    private String weight;

    /**
     * 中文品名
     */
    private String chineseName;


    /**
     * CAV.
     */
    private String cav;


    /**
     * 材料种类、子类 品牌.
     */
    private String material;

    /**
     * 颜色编号
     */
    private String colorNo;

    /**
     * 油漆颜色编号
     */
    private String paintingColorNo;

    /**
     * 打印颜色编号
     */
    private String printingColorNo;

    /**
     * 涂层类别
     */
    private String coatingCategory;

    /**
     * 单价
     */
    private String unitPrice;

    /**
     * Q'TY
     */
    private Integer qty;

    /**
     * 工具供应商
     */
    private String toolingVender;

    /**
     * 工装请购单成本
     */
    private Integer toolingPrCost;

    /**
     * 请购单编号
     */
    private String prNumber;

    /**
     * 供应商
     */
    private String suppliedVendor;


    /**
     * 备注
     */
    private String remark;

    /**
     * 父类id/ 主父类默认是0
     */
    @NotNull(message = "parentId不能为空")
    private Long parentId;



}
