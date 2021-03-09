package com.mic.eis.mapper;

import com.mic.eis.domain.model.Acl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Set;

/**
 * 权限 Mapper 接口
 *
 * @author calisto
 * @since 2020-07-07
 */
public interface AclMapper extends BaseMapper<Acl> {

    /**
     * 查询角色的所有权限
     * @author calisto
     * @date 2020-07-07 14:43:24
     * @param
     * @param id
     **/
    Set<Acl> selectAclsByRoleId(Long id);
}
