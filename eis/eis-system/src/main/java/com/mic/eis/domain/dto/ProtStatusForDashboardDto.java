package com.mic.eis.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author blake.wang
 * @date 2020-12-21 14:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtStatusForDashboardDto {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID",hidden = true)
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 项目id
     */
    private Long mainProtId;

    /**
     * 主项目 Code
     */
    private String protCode;

    /**
     * 主项目 描述
     */
    private String protDes;

    /**
     * 子项目 描述
     */
    private String sonProtDes;

    /**
     * toolStart 对应 prot status --> tooling
     */
    private String toolStart;

    /**
     * DVT 实际的结束的最大值
     */
    private String dvt;

    /**
     * PVT 实际的结束的最大值
     */
    private String pvt;
}
