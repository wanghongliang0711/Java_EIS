package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;

/**
 * @author blake.wang
 * @date 2020-08-05 14:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user_prot")
public class UserProt extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     *  用户id
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;


    /**
     *  项目id
     */
    @NotNull(message = "项目id不能为空")
    private Long protId;


    /**
     *  项目成员角色  0:普通用户  1:项目管理员
     */
    private Integer member;





}
