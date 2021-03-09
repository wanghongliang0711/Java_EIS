package com.mic.eis.service.impl;

import com.mic.eis.domain.model.Role;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.mapper.RoleMapper;
import com.mic.eis.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Role服务实现类
 *
 * @author calisto
 * @since 2020-07-07
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public Set<Role> findRolesByUserId(Long id) {
        return roleMapper.selectRolesByUserId(id);
    }
}
