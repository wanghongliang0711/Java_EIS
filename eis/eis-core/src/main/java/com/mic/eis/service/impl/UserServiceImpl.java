package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mic.eis.domain.dto.ChangePasswordDto;
import com.mic.eis.domain.dto.UserDto;
import com.mic.eis.domain.model.Role;
import com.mic.eis.domain.model.User;
import com.mic.eis.domain.model.UserRole;
import com.mic.eis.domain.vo.UserQueryVo;
import com.mic.eis.mapper.RoleMapper;
import com.mic.eis.mapper.UserMapper;
import com.mic.eis.service.UserService;
import com.mic.eis.util.HttpContextUtil;
import com.mic.eis.utils.GeneratorCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用户服务接口实现类
 * @author calisto
 * @date 2020-07-07 1:56 下午
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Override
    public User findUserByName(String username) {
        return userMapper.selectUserByName(username);
    }

    @Override
    public User findUserByJobNum(String jobNum) {
        QueryWrapper<User> wrapper = new QueryWrapper();
        wrapper.lambda().eq(User::getJobNum, jobNum);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public void saveUser(User user) {
        try {
            user.setPassword(DigestUtils.md5Hex(user.getJobNum()));
            user.setId(GeneratorCodeUtil.generateKey());
            userMapper.insert(user);
        } catch (Exception e) {
            log.error("插入用户异常");
            e.printStackTrace();
            throw e;
        }
        try {
            //分配普通用户角色
            UserRole userRole = new UserRole();
            userRole.setId(GeneratorCodeUtil.generateKey());
            QueryWrapper<Role> wrapper = new QueryWrapper();
            wrapper.lambda().eq(Role::getCode,"USER");
            Role role = roleMapper.selectOne(wrapper);
            userRole.setRoleId(role.getId());
            userRole.setUserId(userMapper.selectUserByName(user.getUsername()).getId());
            userMapper.insertUserRoles(userRole);
        } catch (Exception e) {
            log.error("为用户分配角色异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void removeUserById(Long userId) {
        try {
            userMapper.deleteUserRoles(userId);
        } catch (Exception e) {
            log.error("删除用户角色异常");
            e.printStackTrace();
            throw e;
        }
        try {
            userMapper.deleteById(userId);
        } catch (Exception e) {
            log.error("删除用户异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void removeUserByIds(List<Long> userIds) {
        try {
            userMapper.deleteUsersRoles(userIds);
        } catch (Exception e) {
            log.error("批量删除用户角色异常");
            e.printStackTrace();
            throw e;
        }
        try {
            userMapper.deleteBatchIds(userIds);
        } catch (Exception e) {
            log.error("批量删除用户异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public PageInfo<UserDto> findUserListByPage(UserQueryVo userQueryVo) {
        Long deptId = userQueryVo.getDeptId();
        // 迭代查询id下的所有子部门id
        List<Long> deptIds = userMapper.findALlSubDeptId(deptId);
        if (deptId != null) {
            // 把当前的查询条件的id
            deptIds.add(userQueryVo.getDeptId());
        }
        // 分页
        PageHelper.startPage(userQueryVo.getPageNum(), userQueryVo.getPageSize());
        // 查询
        List<UserDto> userDtos = userMapper.selectUserList(userQueryVo, deptIds);
        // 查询角色集合
/*        for (UserDto userDto : userDtos) {
            List<Long> roleIds = userMapper.selectRoleIds(userDto.getId());
            userDto.setRoleIds(roleIds);
        }*/
        userDtos.forEach(o -> o.setRoleIds(userMapper.selectRoleIds(o.getId())));
        return new PageInfo<>(userDtos);
    }

    @Override
    public Boolean modifyUserRoles(UserDto userDto) {
        try {
            List<Long> roleIds = userDto.getRoleIds();
            // 清空用户角色
            userMapper.deleteUserRoles(userDto.getId());
            List<UserRole> userRoles = new ArrayList<>();
            for (Long roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setId(GeneratorCodeUtil.generateKey());
                userRole.setRoleId(roleId);
                userRole.setUserId(userDto.getId());
                userRoles.add(userRole);
            }
            // 为用户重新分配权限
            userMapper.addRoleToUser(userRoles);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean updateUserInfo(User user) {
        int result = userMapper.updateUserById(user);
        return result == 1;
    }

    @Override
    public boolean resetUserPwdById(String userId) {
        User user = userMapper.selectById(userId);
        String newPwd =DigestUtils.md5Hex(user.getJobNum().trim());
        int result = userMapper.updateUserPwd(user.getId(), newPwd);
        return result == 1;
    }

    @Override
    public HashMap<String, Object> ChangePassword(ChangePasswordDto changePasswordDto) {
        HashMap<String, Object> resultMap = new HashMap<>();
        if (changePasswordDto.getPassword().equals(changePasswordDto.getConfirmPassword())) {
            User currentUser = HttpContextUtil.getCurrentUser();
            String oldPwd = DigestUtils.md5Hex(changePasswordDto.getLastPassword().trim());
            String newPwd = DigestUtils.md5Hex(changePasswordDto.getPassword().trim());
            if (currentUser.getPassword().equals(oldPwd)) {
                int result = userMapper.updateUserPwd(currentUser.getId(), newPwd);
                if (result == 1) {
                    resultMap.put("msgSuccess", "修改密码成功!!!");
                } else {
                    resultMap.put("msgFail", "修改密码失败!!!");
                }
            } else {
                resultMap.put("msgFail", "原密码输入错误!!!");
            }
        } else {
            resultMap.put("msgFail", "两次输入的新密码不一致!!!");
        }
        return resultMap;
    }
}
