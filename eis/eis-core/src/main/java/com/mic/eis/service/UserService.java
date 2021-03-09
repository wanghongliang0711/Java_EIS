package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mic.eis.domain.dto.ChangePasswordDto;
import com.mic.eis.domain.dto.UserDto;
import com.mic.eis.domain.model.User;
import com.mic.eis.domain.vo.UserQueryVo;

import java.util.HashMap;
import java.util.List;


/**
 * 用户服务接口
 * @author calisto
 * @since 2020-07-07
 */
public interface UserService extends IService<User> {

    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    User findUserByName(String username);


    /**
     * 通过工号查询用户
     * @param jobNum
     * @return
     */
    User findUserByJobNum(String jobNum);

    /**
     * 用户新增
     * @param user
     */
    void saveUser(User user);

    /**
     * 删除用户
     * @param userId
     */
    void removeUserById(Long userId);

    /**
     * 批量删除用户
     * @param userIds
     */
    void removeUserByIds(List<Long> userIds);

    /**
     * 根据条件分页查询用户
     * @param userQueryVo
     * @return
     */
    PageInfo<UserDto> findUserListByPage(UserQueryVo userQueryVo);

    /**
     * 修改用户角色
     * @param userDto
     * @return
     */
    Boolean modifyUserRoles(UserDto userDto);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    boolean updateUserInfo(User user);

    /**
     * 重置用户密码
     * @param userId
     * @return
     */
    boolean resetUserPwdById(String userId);

    /**
     *  修改密码
     * @param changePasswordDto ChangePasswordDto 实体
     * @return {"" : "", "" : ""}
     */
    HashMap<String, Object> ChangePassword(ChangePasswordDto changePasswordDto);
}
