package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.dto.ChangePasswordDto;
import com.mic.eis.domain.model.Role;
import com.mic.eis.domain.model.User;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.RoleService;
import com.mic.eis.service.UserService;
import com.mic.eis.util.HttpContextUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author calisto
 * @date 2020-07-17 10:19 上午
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private RoleService roleService;

    @Resource
    private UserService userService;

    @RequiresUser
    @ApiOperation("拉取用户角色信息")
    @GetMapping("/info")
    public ResponseEntity<?> findUserInfo() {
        User user = HttpContextUtil.getCurrentUser();
        Set<Role> userRoles = roleService.findRolesByUserId(user.getId());
        List<String> userRolesCode = userRoles.stream().map(Role::getCode).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>(1);
        map.put("name",user.getUsername());
        map.put("roles",userRolesCode);
        return Objects.nonNull(map) ? ResponseHelper.success(map) : ResponseHelper.failed();
    }

    @OptionLog("修改密码")
    @ApiOperation("修改密码")
    @PostMapping("/changePassword")
    public ResponseEntity<?> ChangePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        try {
            return ResponseHelper.success(userService.ChangePassword(changePasswordDto));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }
}
