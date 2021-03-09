package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.vo.OptLogQueryVo;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.OptLogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-07-10 9:36
 */
@Slf4j
@RestController
@RequestMapping("/optLog")
public class OptLogController {


    @Resource
    private OptLogService optLogService;

    @OptionLog("通过 用户名 模糊查询log表匹配的用户名")
    @ApiOperation("通过 用户名 模糊查询log表匹配的用户名")
    @GetMapping("/findByFuzzyName")
    public ResponseEntity<?> selectUsernameByFuzzyUsername(@RequestParam(value="username", required = false, defaultValue = "") String username){
        try {
            return ResponseHelper.success(optLogService.selectUsernameByUsername(username));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed(BizCode.FAILED);
        }
    }

    @OptionLog("通过 用户名 查询log表中该用户的所有操作")
    @ApiOperation("通过 用户名 查询log表中该用户的所有操作")
    @PostMapping("/findByName")
    public ResponseEntity<?> selectOptLogByUsername(@Valid @RequestBody OptLogQueryVo optLogQueryVo){
        if(optLogQueryVo.getUsername() == null){
            optLogQueryVo.setUsername("");
        }
        return ResponseHelper.success(optLogService.selectOptLogByUsernamePage(optLogQueryVo));
    }


    @OptionLog("通过 id 删除log表数据")
    @ApiOperation("通过 id 删除log表数据")
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteOptLogById(@PathVariable(name = "id")Long id){
        try {
            optLogService.deleteOptLogById(id);
            return ResponseHelper.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed(BizCode.FAILED);
        }
    }

    @OptionLog("通过 log id 批量删除log表数据")
    @ApiOperation("通过 log id 批量删除log表数据")
    @PostMapping("/batchDelete")
    public ResponseEntity<?> batchDeleteOptLog(@RequestBody List<Long> optLogIds){
        try {
            optLogService.batchDeleteOptLog(optLogIds);
            return ResponseHelper.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed(BizCode.FAILED);
        }
    }













}
