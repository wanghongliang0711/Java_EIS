package com.mic.eis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author calisto
 * @date 2020-07-07 11:28 上午
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {

    @Min(1)
    @NotNull(message = "页码不能为空")
    private Integer pageNum;

    @Min(10)
    @NotNull(message = "分页大小不能为空")
    private Integer pageSize;
}
