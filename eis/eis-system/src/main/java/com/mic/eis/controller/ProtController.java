package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.model.Prot;
import com.mic.eis.domain.vo.ProtQueryVo;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.DataPermissionService;
import com.mic.eis.service.ProtService;
import com.mic.eis.util.HttpContextUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-07-08 16:51
 */
@Slf4j
@RestController
@RequestMapping("/prot")
public class ProtController {

    @Resource
    private ProtService protService;

    @Resource
    private DataPermissionService dataPermissionService;

    @OptionLog("通过id查询 项目")
    @ApiOperation("通过id查询 项目")
    @GetMapping("/find/{id}")
    public ResponseEntity<?> selectProtByID(@PathVariable Long id){
        return ResponseHelper.success(protService.selectProtByID(id));
    }

    // 2020 0824 不分页 查询 主项目下 所有子项目  // ok
    @OptionLog("通过ParentId查询 项目")
    @ApiOperation("通过ParentId查询 项目")
    @GetMapping("/findByPid/{id}")
    public ResponseEntity<?> selectProtByParentId(@PathVariable Long id){
        return ResponseHelper.success(protService.selectProtByParentId(id));
    }

    // 2020 0821 分页查询 通过 项目名和状态 要修改接口， 要结合 用户关注表 //ok
    @OptionLog("通过 项目名和状态 分页查询项目")
    @ApiOperation("通过 项目名和状态 分页查询项目")
    @PostMapping("/findMainProt")
    public ResponseEntity<?> selectMainProtByNameAndStatus(@Valid @RequestBody ProtQueryVo protQueryVo){
        if(protQueryVo.getProtName() == null){
            protQueryVo.setProtName("");
        }
        return ResponseHelper.success(protService.findMainProtByPage(protQueryVo));
    }


    // 2020 1118 查询所有 项目 用于 提示
    @OptionLog("查询所有 项目 用于 提示")
    @ApiOperation("查询所有 项目 用于 提示")
    @PostMapping("/selectAllProt")
    public ResponseEntity<?> selectAllProt() {
        return ResponseHelper.success(protService.selectAllProt());
    }

    // 2020 1118 查询 这个项目 对应的所有子项目  用于 提示
    @OptionLog("查询这个项目 对应的 所有子项目 用于 提示")
    @ApiOperation("查询这个项目 对应的 所有子项目 用于 提示")
    @PostMapping("/selectAllSonProt/{protId}")
    public ResponseEntity<?> selectAllSonProt(@PathVariable Long protId){
        return ResponseHelper.success(protService.selectAllSonProt(protId));
    }


    // 不分页查询主项目   主项目默认 parentId 是 0
    @OptionLog("通过 项目名和状态 不分页查询项目")
    @ApiOperation("通过 项目名和状态 不分页查询项目")
    @PostMapping("/queryByNameAndStatus")
    public ResponseEntity<?> queryByNameAndStatus(@Valid @RequestBody ProtQueryVo protQueryVo){
        if(protQueryVo.getProtName() == null){
            protQueryVo.setProtName("");
        }
        return ResponseHelper.success(protService.queryByNameAndStatus(protQueryVo));
    }

    // 2020 0821 添加主项目 //ok
    @OptionLog("添加父项目")
    @ApiOperation("添加父项目")
    @PostMapping("/addRootProt")
//    @RequiresPermissions("prot:add")
    public ResponseEntity<?> addRootProt(@Valid @RequestBody Prot prot){
        return addProject(prot);
    }

    // 2020 0824 添加  // ok
    @OptionLog("添加子项目")
    @ApiOperation("添加子项目")
    @PostMapping("/addSonProt")
    public ResponseEntity<?> addSonProt(@Valid @RequestBody Prot prot){
        if (!dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), prot.getParentId())) {
            return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
        }
        return addProject(prot);
    }

    // 2020 0821 删除主项目  //ok
    @OptionLog("通过项目id 删除 主项目")
    @ApiOperation("通过项目id 删除 主项目")
    @GetMapping("/deleteRootProt/{id}")
    //    @RequiresPermissions("prot:delete")
    public ResponseEntity<?> deleteRootProtById(@PathVariable Long id){
        try {
            protService.deleteProtById(id);
            return ResponseHelper.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 2020 0824 删除  // ok
    @OptionLog("通过项目id 删除 子项目")
    @ApiOperation("通过项目id 删除 子项目")
    @GetMapping("/deleteSonProt/{id}/{parentId}")
    public ResponseEntity<?> deleteSonProtById(@PathVariable Long id, @PathVariable Long parentId){
        if (!dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), parentId)){
            return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
        }
        try {
            protService.deleteSubProtById(id);
            return ResponseHelper.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }




    @OptionLog("通过项目id 批量删除 项目")
    @ApiOperation("通过项目id 批量删除 项目")
    @PostMapping("/batchDelete")
    public ResponseEntity<?> batchDeleteProt(@RequestBody List<Long> deptIds){
        try {
            protService.batchDeleteProt(deptIds);
            return ResponseHelper.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 2020 0821 修改主项目 项目名 和 code 不能 重复 参考下方 addProject 方法 //ok
    // 2020 0824 修改子项目 先不做重复校验 // ok
    @OptionLog("通过项目id 编辑 项目")
    @ApiOperation("通过项目id 编辑 项目")
    @PostMapping("/edit")
    public ResponseEntity<?> editProt(@Valid @RequestBody Prot prot){
        Long currUserId = HttpContextUtil.getCurrentUser().getId();
        if(prot.getId() != null && prot.getName() != null && prot.getParentId() != null){
            try {
                // 主 项目名 和 code 不能重复
                if(prot.getParentId() == 0){
                    // 项目管理员 能编辑主项目 名字 code
                    if(!dataPermissionService.isOwner(currUserId, prot.getId())){
                        return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
                    }
                    if(protService.selectByNameExcludeProt(prot).size() >0
                    || protService.selectByCodeExcludeProt(prot).size() >0){
                        return ResponseHelper.failed(BizCode.PROJECT_REPEAT);
                    }
                } else{
                    // 项目管理员 能编辑子项目 名字
                    if(!dataPermissionService.isOwner(currUserId, prot.getParentId())){
                        return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
                    }
                }
                boolean result = protService.editProt(prot);
                return result ? ResponseHelper.success(prot) : ResponseHelper.failed();
            }catch (Exception e){
                e.printStackTrace();
                return ResponseHelper.failed();
            }
        }
        return ResponseHelper.failed();
    }


    /**
     * 查看 用户所有关注的项目
     * @return ResponseEntity
     */
    @OptionLog("查看 用户 Follow 项目 详细信息")
    @ApiOperation("查看 用户 Follow 项目 详细信息")
    @PostMapping("/selectFollowByUserId")
    public ResponseEntity<?> selectByUserId(){
        return ResponseHelper.success(protService.selectByUserIdReturnProt());
    }


    /**
     * 新增项目流程
     * @param prot 项目实体
     * @return  ResponseEntity
     */
    private ResponseEntity<?> addProject(@RequestBody @Valid Prot prot) {
        if(prot.getName() != null && prot.getParentId() != null){
            // 主 项目名 和 code 不能重复
            if(prot.getParentId() == 0){
                if(protService.selectProtByName(prot.getName()).size() > 0
                        || protService.selectProtByCode(prot.getCode()).size() > 0 ) {
                    return ResponseHelper.failed(BizCode.PROJECT_REPEAT);
                }
            }
            try {
                protService.addProt(prot);
            }catch (Exception e){
                e.printStackTrace();
                return ResponseHelper.failed();
            }
            return ResponseHelper.success();
        }else {
            return ResponseHelper.failed();
        }
    }
}
