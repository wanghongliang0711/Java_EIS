package com.mic.eis.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-09-07 15:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtMePartListQueryVo {

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

}
