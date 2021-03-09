package com.mic.eis.controller;

import com.github.pagehelper.PageInfo;
import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.dto.UserDto;
import com.mic.eis.domain.model.User;
import com.mic.eis.domain.vo.UserQueryVo;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.RoleService;
import com.mic.eis.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author calisto
 * @date 2020-07-08 12:52 下午
 */

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;


    @RequiresPermissions("user:add")
    @ApiOperation("新增用户")
    @PostMapping("/add")
    @OptionLog("添加用户")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
        if (ObjectUtils.isNotEmpty(userService.findUserByName(user.getUsername())) ||
                ObjectUtils.isNotEmpty(userService.findUserByJobNum(user.getJobNum()))) {
            return ResponseHelper.failed(BizCode.USERNAME_REPEAT);
        }
        if (StringUtils.isNotBlank(user.getUsername())) {
            try {
                userService.saveUser(user);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseHelper.failed();
            }
            return ResponseHelper.success(user);
        }
        return ResponseHelper.failed();
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/delete")
    @RequiresPermissions("user:del")
    @OptionLog("删除用户")
    public ResponseEntity<?> removeUser(@RequestParam("userId")Long userId) {
        if (isRoot(userId)) {
            return ResponseHelper.failed(BizCode.ROOT_NOTALLOW_DELETE);
        }
        try {
            userService.removeUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
        return ResponseHelper.success();
    }

    @ApiOperation("批量删除用户")
    @DeleteMapping("/batchDelete")
    @RequiresPermissions("user:del")
    @OptionLog("批量删除用户")
    public ResponseEntity<?> batchRemoveUser(@RequestBody List<Long> userIds) {
        for (Long userId : userIds) {
            if (isRoot(userId)) {
                return ResponseHelper.failed(BizCode.ROOT_NOTALLOW_DELETE);
            }
        }
        try {
            userService.removeUserByIds(userIds);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
        return ResponseHelper.success();
    }

    @ApiOperation("分页查询用户列表")
    @PostMapping("/list")
    public ResponseEntity<PageInfo<UserDto>> selectPage(@RequestBody UserQueryVo userQueryVo) {
        return ResponseHelper.success(userService.findUserListByPage(userQueryVo));
    }

    @ApiOperation("分配用户角色")
    @PostMapping("/assignRoles")
    @RequiresPermissions("user:assign:role")
    @OptionLog("分配用户角色")
    public ResponseEntity<?> modifyUserRoles(@RequestBody UserDto userDto) {
        Boolean result = userService.modifyUserRoles(userDto);
        return result? ResponseHelper.success() : ResponseHelper.failed();
    }

    @ApiOperation("编辑用户信息")
    @PostMapping("/edit")
    @OptionLog("编辑用户")
    @RequiresPermissions("user:edit")
    public ResponseEntity<?> updateUserInfo(@Valid @RequestBody User user) {
        boolean result = userService.updateUserInfo(user);
        return result ? ResponseHelper.success() : ResponseHelper.failed();
    }

    @ApiOperation("重置用户密码为工号")
    @PostMapping("/resetPwd/{userId}")
    @OptionLog("重置用户密码")
    @RequiresPermissions("user:reset:pwd")
    public ResponseEntity<?> resetUserPwd(@PathVariable String userId) {
        if (StringUtils.isNotBlank(userId)) {
            boolean result = userService.resetUserPwdById(userId);
            return result ? ResponseHelper.success() : ResponseHelper.failed();
        }
        return ResponseHelper.failed();
    }

    @ApiOperation("查询所有角色")
    @PostMapping("/role/all")
    public ResponseEntity<?> findAllRole() {
        return ResponseHelper.success(roleService.list());
    }

    /**
     * 校验是否为超级管理员
     * @param userId
     * @return
     */
    private boolean isRoot(Long userId) {
        return userId == 0L;
    }

}
