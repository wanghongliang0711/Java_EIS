package com.mic.eis.mapper;

import com.mic.eis.domain.model.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 *  角色Mapper 接口
 *
 * @author calisto
 * @since 2020-07-07
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 查询用户的所有角色
     * @author calisto
     * @date 2020-07-07 14:43:56
     * @param
     * @param userId
     **/
    Set<Role> selectRolesByUserId(@Param("userId") Long userId);
}
