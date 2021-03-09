package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.model.MatelClass;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.MatelClassService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author blake.wang
 * @date 2020-08-19 12:59
 */
@Slf4j
@RestController
@RequestMapping("/matelClass")
public class MatelClassController {

    @Resource
    private MatelClassService matelClassService;

    // 这个 只查到 二级
    @OptionLog("通过 种类名 不分页查询材料分类")
    @ApiOperation("通过 种类名 不分页查询材料分类")
    @PostMapping("/queryByMaterial")
    public ResponseEntity<?> queryByMaterial(@Valid @RequestBody MatelClass matelClass) {
        if (matelClass.getMaterial() == null) {
            matelClass.setMaterial("");
        }
        return ResponseHelper.success(matelClassService.queryByMaterial(matelClass));
    }

    // 这个接口没用到
    @OptionLog("查询所有 材料分类 用于提示")
    @ApiOperation("查询所有 材料分类 用于提示")
    @PostMapping("/selectAll")
    public ResponseEntity<?> selectAllMaterialTips(){
        return ResponseHelper.success(matelClassService.selectAllMaterialTips());
    }

    @OptionLog("添加 材料分类")
    @ApiOperation("添加 材料分类")
    @PostMapping("/addMaterial")
    public ResponseEntity<?> addMaterial(@Valid @RequestBody MatelClass matelClass) {
        if(matelClass.getMaterial() != null && matelClass.getParentId() != null) {
            if (matelClass.getParentId() == 0) {
                if(matelClassService.selectByMaterial(matelClass.getMaterial()).size() > 0){
                    return ResponseHelper.failed(BizCode.MOLD_REPEAT);
                }
            }
            try {
                matelClassService.addMaterial(matelClass);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseHelper.failed();
            }
            return ResponseHelper.success();
        } else {
            return ResponseHelper.failed();
        }
    }

    @OptionLog("通过 id 删除 材料分类")
    @ApiOperation("通过 id 删除 材料分类")
    @GetMapping("/deleteMatelClass/{id}")
    public ResponseEntity<?> deleteMatelClass(@PathVariable Long id){
        try {
            matelClassService.deleteMatelClassById(id);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    @OptionLog("通过 id 编辑 材料分类")
    @ApiOperation("通过 id 编辑 材料分类")
    @PostMapping("/edit")
    public ResponseEntity<?> editMatelClass(@Valid @RequestBody MatelClass matelClass){
        if(matelClass.getId() != null && matelClass.getParentId() != null && matelClass.getMaterial() != null){
            try {
                boolean result = matelClassService.editMatelClass(matelClass);
                return result ? ResponseHelper.success(matelClass) : ResponseHelper.failed();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseHelper.failed();
            }
        }
        return ResponseHelper.failed();
    }

    // 接口1： 查询 一级下 所有数据 ， 然后后台处理，进行分级
    @OptionLog("通过 种类名 不分页查询材料分类 从一级往下查找")
    @ApiOperation("通过 种类名 不分页查询材料分类 从一级往下查找")
    @PostMapping("/queryByOneMaterial")
    public ResponseEntity<?> queryByOneMaterial(@Valid @RequestBody MatelClass matelClass) {
        if (matelClass.getMaterial() == null) {
            matelClass.setMaterial("");
        }
        return ResponseHelper.success(matelClassService.queryByOneMaterial(matelClass));
    }


    // 接口2： 添加品牌/型号
    @OptionLog("添加 品牌/型号")
    @ApiOperation("添加 品牌/型号")
    @PostMapping("/addBrand")
    public ResponseEntity<?> addBrand(@Valid @RequestBody MatelClass matelClass) {
        if(matelClass.getBrand() != null && matelClass.getParentId() != null) {
            try {
                matelClassService.addMaterial(matelClass);
                return ResponseHelper.success();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseHelper.failed();
            }
        } else {
            return ResponseHelper.failed();
        }
    }

    // 接口3： 修改品牌/型号
    @OptionLog("通过 id 编辑 材料分类 品牌/型号")
    @ApiOperation("通过 id 编辑 材料分类 品牌/型号")
    @PostMapping("/editBrand")
    public ResponseEntity<?> editBrand(@Valid @RequestBody MatelClass matelClass){
        if(matelClass.getId() != null && matelClass.getParentId() != null && matelClass.getBrand() != null){
            try {
                boolean result = matelClassService.editMatelClass(matelClass);
                return result ? ResponseHelper.success(matelClass) : ResponseHelper.failed();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseHelper.failed();
            }
        }
        return ResponseHelper.failed();
    }

}
