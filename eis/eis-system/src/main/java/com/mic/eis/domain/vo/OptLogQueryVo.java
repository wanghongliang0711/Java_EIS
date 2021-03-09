package com.mic.eis.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-07-10 13:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptLogQueryVo {

    /**
     * 用户名
     */
    private String username;

    @Min(1)
    @NotNull(message = "页码不能为空")
    private Integer pageNum;


    @Min(10)
    @NotNull(message = "分页大小不能为空")
    private Integer pageSize;

}
