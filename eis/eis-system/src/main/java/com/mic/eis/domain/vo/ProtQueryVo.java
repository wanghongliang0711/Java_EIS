package com.mic.eis.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author blake.wang
 * @date 2020-07-09 16:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtQueryVo {

    /**
     *  项目名
     */
    private String protName;

    /**
     * 项目状态
     */
//    @NotNull(message = "项目状态不能为空")
    private Integer status;

//    @Min(1)
//    @NotNull(message = "页码不能为空")
    private Integer pageNum;

//    @Min(10)
//    @NotNull(message = "分页大小不能为空")
    private Integer pageSize;
}
