package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author blake.wang
 * @date 2021-01-04 15:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_prot_PilotrunReportQueryRecord")
public class ProtPilotRunReportQueryRecord extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     *  查询 severity 记录
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String severity;

    /**
     *  查询 status 记录
     */
    @TableField(updateStrategy  = FieldStrategy.IGNORED)
    private String status;

}
