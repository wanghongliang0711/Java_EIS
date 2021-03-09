package com.mic.eis.domain.vo;

import com.mic.eis.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author calisto
 * @date 2020-07-09 1:34 下午
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserQueryVo extends User {

    @Min(1)
    @NotNull(message = "页码不能为空")
    private Integer pageNum;

    @Min(10)
    @NotNull(message = "分页大小不能为空")
    private Integer pageSize;
}
