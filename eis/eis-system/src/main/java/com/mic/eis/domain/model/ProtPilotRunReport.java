package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2021-01-04 15:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_prot_PilotrunReport")
public class ProtPilotRunReport extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    @NotNull(message = "项目id不能为空")
    private Long protId;

    /**
     *  文件 版本  当前版本 ：now
     */
    @NotNull(message = "文件版本不能为空")
    private String fileVer;

    /**
     *  Item 编号
     */
    @NotNull(message = "Item不能为空")
    private Integer item;

    /**
     *  问题描述
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String problemDes;

    /**
     *  时间
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String dateTime;

    /**
     *  失败率
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String failRate;

    /**
     *  严重程度
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String severity;

    /**
     *  发行人
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String issuer;

    /**
     *  所有者
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String owner;

    /**
     *  设计
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String design;

    /**
     *  工作
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String work;

    /**
     *  材料
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String material;

    /**
     *  根本原因
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String rootCause;

    /**
     *  行动
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String action;

    /**
     *  到期日
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String dueDate;

    /**
     *  状态
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String status;




}
