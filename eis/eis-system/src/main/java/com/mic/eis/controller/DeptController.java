package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.dto.DeptDto;
import com.mic.eis.domain.model.Dept;
import com.mic.eis.domain.vo.DeptQueryVo;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.DeptService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-07-07 17:09
 */
@Slf4j
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Resource
    private DeptService deptService;


    @OptionLog("通过id查询部门")
    @ApiOperation("通过id查询部门")
    @GetMapping("/find/{id}")
    public ResponseEntity<?> selectDeptById(@PathVariable(name = "id") Long id){
        return ResponseHelper.success(deptService.selectDeptById(id));
    }

    @OptionLog("通过ParentId查询部门")
    @ApiOperation("通过ParentId查询部门")
    @GetMapping("/findByPid/{id}")
    public ResponseEntity<?> selectDeptByParentId(@PathVariable(name = "id") Long id){
        return ResponseHelper.success(deptService.selectDeptByParentId(id));
    }


    @OptionLog("通过部门名 查询部门")
    @ApiOperation("通过部门名 查询部门")
    @PostMapping("/findByNamePage")
    public ResponseEntity<?> selectDeptByNamePage(@Valid @RequestBody DeptQueryVo deptQueryVo){
        if(deptQueryVo.getDeptName() == null){
            deptQueryVo.setDeptName("");
        }
        return ResponseHelper.success(deptService.findDeptListByPage(deptQueryVo));
    }

    @OptionLog("通过部门名 查询部门")
    @ApiOperation("通过部门名 查询部门")
    @GetMapping("/findByName")
    public ResponseEntity<?> selectDeptByName(@RequestParam(value="deptName", required = false, defaultValue = "") String deptName){
        return ResponseHelper.success(deptService.selectAllDept(deptName));
    }




    @OptionLog("添加部门")
    @ApiOperation("添加部门")
    @PostMapping("/add")
    public ResponseEntity<?> addDept(@Valid @RequestBody Dept deptVo){
        if(deptVo.getName()!=null && deptVo.getParentId()!=null){
            try {
                deptService.addDept(deptVo);
            }catch (Exception e){
                e.printStackTrace();
                return ResponseHelper.failed();
            }
            return ResponseHelper.success();
        }else {
            return ResponseHelper.failed();
        }

    }

    @OptionLog("通过部门id 删除 部门")
    @ApiOperation("通过部门id 删除 部门")
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteDeptById(@PathVariable Long id){
        try {
            deptService.deleteDeptById(id);
            return ResponseHelper.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }


    @OptionLog("通过部门id 批量删除 部门")
    @ApiOperation("通过部门id 批量删除 部门")
    @PostMapping("/batchDelete")
    public ResponseEntity<?> batchDeleteDept(@RequestBody List<Long> deptIds){
        try {
            deptService.batchDeleteDept(deptIds);
            return ResponseHelper.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }


    @OptionLog("通过部门id 编辑 部门")
    @ApiOperation("通过部门id 编辑 部门")
    @PostMapping("/edit")
    public ResponseEntity<?> editDept(@Valid @RequestBody Dept dept){
        if(dept.getId()!=null && dept.getName()!=null & dept.getParentId()!=null){
            try {
                boolean result = deptService.editDept(dept);
                return result ? ResponseHelper.success(dept) : ResponseHelper.failed();
            }catch (Exception e){
                e.printStackTrace();
                return ResponseHelper.failed();
            }
        }
        return ResponseHelper.failed();
    }


    @OptionLog("获取部门下拉树列表")
    @ApiOperation("获取部门下拉树列表")
    @GetMapping("/treeselect")
    public ResponseEntity<?> treeSelect(){
        List<Dept> depts = deptService.selectAllDept("");
        List<DeptDto> deptDtos = new ArrayList<DeptDto>();
        for (Dept dept : depts){
            DeptDto deptDto = new DeptDto();
            deptDto.setId(dept.getId());
            deptDto.setName(dept.getName());
            deptDto.setParentId(dept.getParentId());
            deptDtos.add(deptDto);
        }
        return ResponseHelper.success(deptService.buildDeptTreeSelect(deptDtos));
    }






}
