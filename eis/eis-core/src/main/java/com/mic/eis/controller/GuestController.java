package com.mic.eis.controller;

import com.mic.eis.domain.vo.LoginVo;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author calisto
 * @date 2020-07-07 3:07 下午
 */

@Slf4j
@RestController
@RequestMapping("/guest")
public class GuestController {

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginVo loginVo) {
        UsernamePasswordToken usernamePasswordToken =
                new UsernamePasswordToken(loginVo.getUsername(),loginVo.getPassword());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);
            String sessionId = subject.getSession().getId().toString();
            Map<String, String> map = new HashMap<>();
            map.put("token", sessionId);
            return ResponseHelper.success(map);
        } catch (AuthenticationException e) {
            log.error("用户:{},尝试登录失败({})",loginVo.getUsername(), BizCode.LOGIN_FAILED.getMessage());
        }
        return ResponseHelper.failed(BizCode.LOGIN_FAILED);
    }
}
