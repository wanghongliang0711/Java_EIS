package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.model.PartNumber;
import com.mic.eis.domain.vo.PartNumberQueryVo;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.PartNumberService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author blake.wang
 * @date 2020-08-14 13:09
 */
@Slf4j
@RestController
@RequestMapping("/partNum")
public class PartNumberController {

    @Resource
    private PartNumberService partNumberService;


    @OptionLog("通过 零件编号和零件描述 分页查询模具种类")
    @ApiOperation("通过 零件编号和零件描述 分页查询模具种类")
    @PostMapping("/queryByNumAndDes")
    public ResponseEntity<?> queryByNumAndDes(@Valid @RequestBody PartNumberQueryVo partNumberQueryVo){
        if (partNumberQueryVo.getPartNumber() == null){
            partNumberQueryVo.setPartNumber("");
        }
        if (partNumberQueryVo.getPartDescription() == null){
            partNumberQueryVo.setPartDescription("");
        }
        return ResponseHelper.success(partNumberService.findByNumAndDes(partNumberQueryVo));
    }

    @OptionLog("查询所有 partNum")
    @ApiOperation("查询所有 partNum")
    @PostMapping("/selectAll")
    public ResponseEntity<?> selectAllPartNumberTips(){
        return ResponseHelper.success(partNumberService.selectAllPartNumberTips());
    }

    @OptionLog("添加Part Number")
    @ApiOperation("添加Part Number")
    @PostMapping("/addPartNum")
    public ResponseEntity<?> addPartNum(@Valid @RequestBody PartNumber partNumber){
        try {
            if (partNumberService.selectByNum(partNumber.getPartNumber()).size() >0){
                return ResponseHelper.failed(BizCode.MOLD_REPEAT);
            }
            partNumberService.addPartNum(partNumber);
            return ResponseHelper.success();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    @OptionLog("通过 id 删除 PartNum")
    @ApiOperation("通过 id 删除 PartNum")
    @GetMapping("/deletePartNum/{id}")
    public ResponseEntity<?> deletePartNum(@PathVariable Long id){
        try {
            partNumberService.deletePartNumById(id);
            return ResponseHelper.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    @OptionLog("通过 id 编辑 PartNum")
    @ApiOperation("通过 id 编辑 PartNum")
    @PostMapping("/edit")
    public ResponseEntity<?> editPartNum(@Valid @RequestBody PartNumber partNumber){
        if(partNumber.getId() != null){
            try {
                // 如果只修改了 描述会报重复的错，还没想好如何解决
                // 解决方法，排除当前id项，查找除了当前id 以外的其他项是否有重复
                if (partNumberService.selectByPartNumExcludePartNum(partNumber).size() > 0){
                    return ResponseHelper.failed(BizCode.MOLD_REPEAT);
                }
                boolean result = partNumberService.editPartNum(partNumber);
                return result ? ResponseHelper.success(partNumber) : ResponseHelper.failed();
            }catch (Exception e){
                e.printStackTrace();
                return ResponseHelper.failed();
            }
        }
        return ResponseHelper.failed();
    }


    @PostMapping("/uploadPartNum")
    public ResponseEntity<?> uploadFile(MultipartFile file){
        try {
            return ResponseHelper.success(partNumberService.uploadFile(file));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }



}
