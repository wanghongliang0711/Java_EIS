package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.model.UserProt;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.DataPermissionService;
import com.mic.eis.service.UserProtService;
import com.mic.eis.util.HttpContextUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author blake.wang
 * @date 2020-08-05 15:02
 */
@Slf4j
@RestController
@RequestMapping("/userProt")
public class UserProtController {

    @Resource
    private UserProtService userProtService;

    @Resource
    private DataPermissionService dataPermissionService;


    @OptionLog("添加 用户-项目关系")
    @ApiOperation("添加 用户-项目关系")
    @PostMapping("/add")
    public ResponseEntity<?> addUserProt(@Valid @RequestBody UserProt userProt){
        try {
            if (!dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), userProt.getProtId())){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            userProtService.addUserProt(userProt);
            return ResponseHelper.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    @OptionLog("添加 用户-项目关系 的 列表 查询所有 user + admin")
    @ApiOperation("添加 用户-项目关系 的 列表 查询所有user + admin")
    @GetMapping("/addMemList/{id}")
    public ResponseEntity<?> addMemList(@PathVariable Long id){
        return ResponseHelper.success(userProtService.addMemList(id));
    }

    @OptionLog("查询所有 admin，排除 root 和 当前项目管理员")
    @ApiOperation("查询所有 admin，排除 root 和 当前项目管理员")
    @GetMapping("/changeOwnerList/{id}")
    public ResponseEntity<?> changeOwerList(@PathVariable Long id){
        return ResponseHelper.success(userProtService.changeOwnerList(id));
    }

    @OptionLog("更改项目管理员")
    @ApiOperation("更改项目管理员")
    @PostMapping("/changeOwner")
    public ResponseEntity<?> changeOwner(@Valid @RequestBody UserProt userProt){
        try {
            if (!dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), userProt.getProtId())){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            if (HttpContextUtil.getCurrentUser().getId() == 0L) {
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            userProtService.changeOwner(userProt);
            return ResponseHelper.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    @OptionLog("查看 项目中的成员")
    @ApiOperation("查看 项目中的成员")
    @GetMapping("/findByProtId/{id}")
    public ResponseEntity<?> findByProtId(@PathVariable Long id){
        return ResponseHelper.success(userProtService.findByProtId(id));
    }

    @OptionLog("删除 项目中的成员")
    @ApiOperation("删除 项目中的成员")
    @PostMapping("/deleteByUserIdProtId")
    public ResponseEntity<?> deleteUserProtByUserIdProtId(@Valid @RequestBody UserProt userProt){
        if (!dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), userProt.getProtId())){
            return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
        }
        userProtService.deleteUserProtByUserIdProtId(userProt);
        return ResponseHelper.success();
    }


}
