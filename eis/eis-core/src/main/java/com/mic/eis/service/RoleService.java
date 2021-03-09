package com.mic.eis.service;

import com.mic.eis.domain.model.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * Role 服务类
 *
 * @author calisto
 * @since 2020-07-07
 */
public interface RoleService extends IService<Role> {

    /**
     * 通过用户id查询用户角色
     * @param id
     * @return
     */
    Set<Role> findRolesByUserId(Long id);
}
