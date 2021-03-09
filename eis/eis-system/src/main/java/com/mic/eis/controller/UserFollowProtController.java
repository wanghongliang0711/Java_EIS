package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.model.UserFollowProt;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.UserFollowProtService;
import com.mic.eis.util.HttpContextUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author blake.wang
 * @date 2020-08-24 9:38
 */
@Slf4j
@RestController
@RequestMapping("/userFollowProt")
public class UserFollowProtController {

    @Resource
    private UserFollowProtService userFollowProtService;


    @OptionLog("添加 用户 Follow 项目关系")
    @ApiOperation("添加 用户 Follow 项目关系")
    @GetMapping("/add/{protId}")
    public ResponseEntity<?> addUserFollowProt(@PathVariable Long protId){
        try {
            UserFollowProt userFollowProt = new UserFollowProt();
            userFollowProt.setProtId(protId);
            userFollowProt.setUserId(HttpContextUtil.getCurrentUser().getId());
            userFollowProtService.addUserFollowProt(userFollowProt);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    @OptionLog("删除 用户 Follow 项目信息")
    @ApiOperation("删除 用户 Follow 项目信息")
    @GetMapping("/delete/{protId}")
    public ResponseEntity<?> deleteByUserIdProtId(@PathVariable Long protId){
        try {
            UserFollowProt userFollowProt = new UserFollowProt();
            userFollowProt.setProtId(protId);
            userFollowProt.setUserId(HttpContextUtil.getCurrentUser().getId());
            userFollowProtService.deleteByUserIdAndProtId(userFollowProt);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    /**
     * 查看 用户所有关注的项目
     * @return
     */
//    @OptionLog("查看 用户 Follow 项目 详细信息")
//    @ApiOperation("查看 用户 Follow 项目 详细信息")
//    @PostMapping("/selectByUserId")
//    public ResponseEntity<?> selectByUserId(){
//        return ResponseHelper.success(userFollowProtService.selectByUserIdReturnProt());
//    }


}
