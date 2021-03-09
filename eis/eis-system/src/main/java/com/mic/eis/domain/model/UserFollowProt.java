package com.mic.eis.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mic.eis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author blake.wang
 * @date 2020-08-24 8:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user_follow_prot")
public class UserFollowProt extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     *  用户id
     */
    private Long userId;


    /**
     *  项目id
     */
    private Long protId;



}
