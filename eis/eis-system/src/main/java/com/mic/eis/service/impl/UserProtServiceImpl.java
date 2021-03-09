package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.dto.UserDto;
import com.mic.eis.domain.model.Dept;
import com.mic.eis.domain.model.User;
import com.mic.eis.domain.model.UserProt;
import com.mic.eis.domain.model.UserRole;
import com.mic.eis.mapper.DeptMapper;
import com.mic.eis.mapper.UserMapper;
import com.mic.eis.mapper.UserProtMapper;
import com.mic.eis.service.UserProtService;
import com.mic.eis.util.HttpContextUtil;
import com.mic.eis.utils.GeneratorCodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-08-05 14:59
 */
@Service
public class UserProtServiceImpl extends ServiceImpl<UserProtMapper, UserProt> implements UserProtService {

    @Resource
    private UserProtMapper userProtMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private DeptMapper deptMapper;

    @Override
    public void deleteUserProtByUserIdProtId(UserProt userProt) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            // 自定义删除
            map.put("user_id", userProt.getUserId());
            map.put("prot_id", userProt.getProtId());
            userProtMapper.deleteByMap(map);
        }catch (Exception e){
            log.error("删除用户项目表异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteUserProtByProtId(Long protId) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            // 自定义删除
            map.put("prot_id", protId);
            userProtMapper.deleteByMap(map);
        }catch (Exception e){
            log.error("删除用户项目异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteUserProtByUserId(Long userId) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            // 自定义删除
            map.put("user_id", userId);
            userProtMapper.deleteByMap(map);
        }catch (Exception e){
            log.error("删除用户项目异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addUserProt(UserProt userProt) {
        try{
            deleteUserProtByUserIdProtId(userProt);
            userProt.setId(GeneratorCodeUtil.generateKey());
            userProtMapper.insert(userProt);
        }catch (Exception e){
            log.error("添加用户项目异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<UserDto> findByProtId(Long id) {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("prot_id", id);
        map.put("is_delete", 0);
        List<UserProt> userProts = userProtMapper.selectByMap(map);
        List<UserDto> userDtoList = new ArrayList<>();
        for (UserProt userProt : userProts) {
            User user = userMapper.selectById(userProt.getUserId());
            if (user != null) {
                UserDto userDto = new UserDto();
                userDto.setMember(userProt.getMember());
                userDto.setUsername(user.getUsername());
                userDto.setJobNum(user.getJobNum());
                userDto.setId(user.getId());
                Dept dept = deptMapper.selectById(user.getDeptId());
                if (dept != null) {
                    userDto.setDeptName(dept.getName());
                }
                userDtoList.add(userDto);
            }
        }
        return userDtoList;
    }

    @Override
    public List<UserDto> addMemList(Long id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0)
                .ne("id", 0);
        List<User> users = userMapper.selectList(wrapper);
        List<UserDto> userDtoList = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("prot_id", id);
        map.put("is_delete", 0);
        List<UserProt> userProts = userProtMapper.selectByMap(map);
        for (User user : users) {
            if(!isAddProt(user, userProts)){
                UserDto userDto = new UserDto();
                userDto.setJobNum(user.getJobNum());
                userDto.setUsername(user.getUsername());
                userDto.setId(user.getId());
                Dept dept = deptMapper.selectById(user.getDeptId());
                if (dept != null) {
                    userDto.setDeptName(dept.getName());
                }
                userDtoList.add(userDto);
            }
        }
        return userDtoList;
    }

    @Override
    public List<UserDto> changeOwnerList(Long id) {
        // 0. 根据 userProt表获取 member 是 1 的 用户id 排除root
        QueryWrapper<UserProt> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0)
                .eq("prot_id", id)
                .eq("member", 1)
                .ne("user_id", 0);
        // 项目中 除了 root 用户以外的 项目管理员
        List<UserProt> userProts = userProtMapper.selectList(wrapper);
        // 1. 用户-角色表 查找 所有Admin角色用户  排除root
        List<UserRole> userRoles = userMapper.selectAllAdmin();
        // 2. 根据 1 的用户 id 获取用户信息 参考 addMemList
        List<UserDto> userDtoList = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            User user = userMapper.selectById(userRole.getUserId());
            if (!isAddProt(user, userProts) && user != null) {
                UserDto userDto = new UserDto();
                userDto.setJobNum(user.getJobNum());
                userDto.setId(user.getId());
                Dept dept = deptMapper.selectById(user.getDeptId());
                if (dept != null) {
                    userDto.setDeptName(dept.getName());
                }
                userDto.setUsername(user.getUsername());
                userDtoList.add(userDto);
            }
        }
        return userDtoList;
    }

    @Override
    public void changeOwner(UserProt userProt) {
        try{
            // 1. 下面的代码 是从 addUserProt 复制过来的，参数 UserProt 要从新构造 // 20200828不用从新构造
            // 2. 不是 root, 是 member=1项目管理员， 新 member=1是admin角色 // 20200828 新 member=1是admin角色 没判断

            // 3. 删除 当前的用户 和 新用户 member=1 ， 添加新的 member=1
            // 删除新用户在项目中的身份
            deleteUserProtByUserIdProtId(userProt);
            // 添加新用户 在项目中的管理员身份
            userProt.setMember(1);
            userProt.setId(GeneratorCodeUtil.generateKey());
            userProtMapper.insert(userProt);
            // 删除 老用户 在项目中的身份
            userProt.setUserId(HttpContextUtil.getCurrentUser().getId());
            deleteUserProtByUserIdProtId(userProt);
        }catch (Exception e){
            log.error("改变项目管理员--(用户项目表)异常");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 判断这个用户是否在 项目中
     * @param user 用户实体
     * @param userProts 在这个项目中的 用户
     * @return true or false
     */
    private boolean isAddProt(User user, List<UserProt> userProts){
        boolean repeat = false;
        if (user != null) {
            for (UserProt userProt : userProts) {
                if(userProt.getUserId().equals(user.getId())){
                    repeat = true;
                }
            }
        }
        return repeat;
    }


}
