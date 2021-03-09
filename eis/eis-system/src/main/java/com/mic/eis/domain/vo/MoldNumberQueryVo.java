package com.mic.eis.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-08-19 9:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoldNumberQueryVo {

    /**
     * 模穴数
     */
    private String moldNumber;

    /**
     * 页码
     */
    @Min(1)
    @NotNull(message = "页码不能为空")
    private Integer pageNum;

    /**
     * 分页大小 一页显示几个
     */
    @Min(10)
    @NotNull(message = "分页大小不能为空")
    private Integer pageSize;
}
