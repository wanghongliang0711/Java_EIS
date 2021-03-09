package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.model.UserFollowProt;

import java.util.List;

public interface UserFollowProtService extends IService<UserFollowProt> {

    /**
     * 通过 userId 查询 这个 用户关注的 项目
     */
    List<UserFollowProt> selectByUserId(Long id);

    /**
     * 通过 userId 查询 用户关注的 项目 的详细信息
     * 返回项目 信息 list
     */
//    List<ProtDto> selectByUserIdReturnProt();


    /**
     * 通过 userId protId 删除这个用户关注的项目
     */
    void deleteByUserIdAndProtId(UserFollowProt userFollowProt);

    /**
     * 通过  protId 删除 用户关注的项目
     * 这个是 删除项目时使用
     * @param id
     */
    void deleteByProtId(Long id);

    /**
     * 通过 userId protId 添加 用户 关注的项目
     */
    void addUserFollowProt(UserFollowProt userFollowProt);

}
