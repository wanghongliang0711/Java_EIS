package com.mic.eis.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-09-07 11:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtFileVerQueryVo {

    /**
     * 子项目id
     */
    @NotNull(message = "子项目id不能为空")
    private Long protId;

    /**
     *  文件 版本
     */
    private String fileVer;

    /**
     * 类别
     * A: Me Part List
     * B: Tool plan
     * C: Pilot Run Report
     */
    @NotNull(message = "文件类别不能为空")
    private String category;

}
