package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.dto.UserDto;
import com.mic.eis.domain.model.UserProt;

import java.util.List;

public interface UserProtService extends IService<UserProt> {


    /**
     * 通过 userId 和 protId 删除 用户项目表数据
     * @param userProt
     */
    void deleteUserProtByUserIdProtId(UserProt userProt);


    /**
     * 通过 protId 删除 用户项目表数据
     * @param protId
     */
    void deleteUserProtByProtId(Long protId);

    /**
     * 通过 userId 删除 用户项目表数据
     * 删除用户时使用
     * @param userId
     */
    void deleteUserProtByUserId(Long userId);

    /**
     * 添加 用户项目表 数据
     * @param userProt
     */
    void addUserProt(UserProt userProt);

    /**
     * 通过 主项目id 查出所有在项目的人的信息
     * @param id 主项目id
     * @return 项目中的用户
     */
    List<UserDto> findByProtId(Long id);

    /**
     * 获取 添加 member 的 list,查询出所有 user + admin 排除现有的人
     * @param id 主项目id
     * @return 所有 user + admin 角色用户的列表 排除现有的人
     */
    List<UserDto> addMemList(Long id);


    /**
     * 获取 改变 owner 的 list ， 查询出所有 admin
     * @param id 主项目id
     * @return 所有 admin 角色用户的列表
     */
    List<UserDto> changeOwnerList(Long id);

    /**
     * 改变 项目 管理员
     * @param userProt 用户项目表实体
     */
    void changeOwner(UserProt userProt);


}
