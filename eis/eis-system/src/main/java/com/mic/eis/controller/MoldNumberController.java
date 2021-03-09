package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.model.MoldNumber;
import com.mic.eis.domain.vo.MoldNumberQueryVo;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.MoldNumberService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author blake.wang
 * @date 2020-08-19 9:33
 */
@Slf4j
@RestController
@RequestMapping("/moldNum")
public class MoldNumberController {

    @Resource
    private MoldNumberService moldNumberService;


    @OptionLog("通过 模穴数 分页查询模具种类")
    @ApiOperation("通过 模穴数 分页查询模具种类")
    @PostMapping("/queryByMoldNum")
    public ResponseEntity<?> queryByMoldNum(@RequestBody MoldNumberQueryVo moldNumberQueryVo) {
        if (moldNumberQueryVo.getMoldNumber() == null) {
            moldNumberQueryVo.setMoldNumber("");
        }
        return ResponseHelper.success(moldNumberService.queryByMoldNum(moldNumberQueryVo));
    }


    @OptionLog("添加 模穴数")
    @ApiOperation("添加 模穴数")
    @PostMapping("/addMoldNum")
    public ResponseEntity<?> addMoldNum(@Valid @RequestBody MoldNumber moldNumber){
        try {
            if (moldNumberService.selectByMoldNum(moldNumber.getMoldNumber()).size() > 0) {
                return ResponseHelper.failed(BizCode.MOLD_REPEAT);
            }
            moldNumberService.addMoldNum(moldNumber);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }


    @OptionLog("通过 id 删除 MoldNum")
    @ApiOperation("通过 id 删除 MoldNum")
    @GetMapping("/deleteMoldNum/{id}")
    public ResponseEntity<?> deleteMoldNum(@PathVariable Long id){
        try {
            moldNumberService.deleteMoldNumById(id);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }


    @OptionLog("通过 id 编辑 MoldNum")
    @ApiOperation("通过 id 编辑 MoldNum")
    @PostMapping("/edit")
    public ResponseEntity<?> editMoldNum(@Valid @RequestBody MoldNumber moldNumber) {
        if(moldNumber.getId() != null){
            try {
                // 排除当前 项目， 查看是否有 重复的 MoldNum
                if (moldNumberService.selectByMoldNumExcludeMoldNum(moldNumber).size() > 0) {
                    return ResponseHelper.failed(BizCode.MOLD_REPEAT);
                }
                boolean result = moldNumberService.editMoldNum(moldNumber);
                return result ?  ResponseHelper.success(moldNumber) : ResponseHelper.failed();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseHelper.failed();
            }
        }
        return ResponseHelper.failed();
    }



}
