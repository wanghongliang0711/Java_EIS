package com.mic.eis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mic.eis.domain.dto.UserDto;
import com.mic.eis.domain.model.User;
import com.mic.eis.domain.model.UserRole;
import com.mic.eis.domain.vo.UserQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  用户Mapper 接口
 * @author calisto
 * @since 2020-07-07
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User selectUserByName(@Param("username") String username);

    /**
     * 为用户分配角色
     * @param userRole
     */
    void insertUserRoles(UserRole userRole);

    /**
     * 删除用户角色
     * @param userId
     */
    void deleteUserRoles(@Param("userId")Long userId);

    /**
     * 批量删除用户角色
     * @param userIds
     */
    void deleteUsersRoles(@Param("userIds") List<Long> userIds);

    /**
     * 根据条件查询用户列表
     * @param userQueryVo
     * @param deptIds
     * @return
     */
    List<UserDto> selectUserList(@Param("uqv") UserQueryVo userQueryVo, @Param("deptIds") List<Long> deptIds);


    /**
     * 分配角色集合给用户
     * @param userRoles
     */
    void addRoleToUser(@Param("userRoles") List<UserRole> userRoles);

    /**
     * 通过id更新用户信息
     * @param user
     * @return
     */
    int updateUserById(User user);

    /**
     * 更新用户密码
     * @param userId
     * @param newPwd
     * @return
     */
    int updateUserPwd(@Param("userId") Long userId, @Param("newPwd") String newPwd);

    /**
     * 查询用户的角色id集合
     * @author calisto
     * @date 2020-07-31 09:59:47
     * @param userId
     **/
    List<Long> selectRoleIds(@Param("userId") Long userId);

    /**
     * 查询部门的所有子部门id
     * @param deptId
     * @return
     */
    List<Long> findALlSubDeptId(@Param("deptId") Long deptId);

    /**
     * 查询所有 admin 角色的用户
     * @return
     */
    List<UserRole> selectAllAdmin();
}
