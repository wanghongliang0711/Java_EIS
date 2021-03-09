package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.model.MoldType;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.MoldTypeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author blake.wang
 * @date 2020-08-13 15:55
 */
@Slf4j
@RestController
@RequestMapping("/moldType")
public class MoldTypeController {

    @Resource
    private MoldTypeService moldTypeService;


    @OptionLog("通过 种类名 不分页查询模具种类")
    @ApiOperation("通过 种类名 不分页查询模具种类")
    @PostMapping("/queryByType")
    public ResponseEntity<?> queryByType(@Valid @RequestBody MoldType moldType){
        if(moldType.getMoldType() == null){
            moldType.setMoldType("");
        }
        return ResponseHelper.success(moldTypeService.queryByType(moldType));
    }

    @OptionLog("添加模具种类")
    @ApiOperation("添加模具种类")
    @PostMapping("/addMoldType")
    public ResponseEntity<?> addMoldType(@Valid @RequestBody MoldType moldType){
        if (moldType.getMoldType() != null && moldType.getParentId() != null){
            if(moldType.getParentId() == 0){
                if(moldTypeService.selectByType(moldType.getMoldType()).size() > 0) {
                    return ResponseHelper.failed(BizCode.MOLD_REPEAT);
                }
            }
            try {
                moldTypeService.addMoldType(moldType);
            }catch (Exception e){
                e.printStackTrace();
                return ResponseHelper.failed();
            }
            return ResponseHelper.success();
        } else {
            return ResponseHelper.failed();
        }
    }


    @OptionLog("通过 id 删除 模具种类")
    @ApiOperation("通过 id 删除 模具种类")
    @GetMapping("/deleteMoldType/{id}")
    public ResponseEntity<?> deleteMoldType(@PathVariable Long id){
        try {
            moldTypeService.deleteMoldTypeById(id);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    @OptionLog("通过 id 编辑 模具种类")
    @ApiOperation("通过 id 编辑 模具种类")
    @PostMapping("/edit")
    public ResponseEntity<?> editMoldType(@Valid @RequestBody MoldType moldType){
        if (moldType.getId() != null && moldType.getParentId() != null && moldType.getMoldType() != null){
            try {
                boolean result = moldTypeService.editMoldType(moldType);
                return  result ? ResponseHelper.success(moldType) : ResponseHelper.failed();
            }catch (Exception e){
                e.printStackTrace();
                return ResponseHelper.failed();
            }
        }
        return ResponseHelper.failed();
    }

}
