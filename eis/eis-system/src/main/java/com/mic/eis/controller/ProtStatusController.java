package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.dto.ProtStatusInsertDto;
import com.mic.eis.domain.model.Prot;
import com.mic.eis.domain.model.ProtStatusAux;
import com.mic.eis.response.BizCode;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.DataPermissionService;
import com.mic.eis.service.ProtService;
import com.mic.eis.service.ProtStatusService;
import com.mic.eis.util.HttpContextUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author blake.wang
 * @date 2020-11-26 15:11
 */
@Slf4j
@RestController
@RequestMapping("/protStatus")
public class ProtStatusController {

    @Resource
    private DataPermissionService dataPermissionService;

    @Resource
    private ProtStatusService protStatusService;

    @Resource
    private ProtService protService;

    @OptionLog("获取所有 protStatus 数据")
    @ApiOperation("获取所有 protStatus 数据")
    @PostMapping("/find")
    public ResponseEntity<?> selectAllProtStatus() {
        return ResponseHelper.success(protStatusService.selectAllProtStatus());
    }

    @OptionLog("获取关注项目的 protStatus 数据为 Dashboard 显示")
    @ApiOperation("获取关注项目的 protStatus 数据为 Dashboard 显示")
    @PostMapping("/findForDashboard")
    public ResponseEntity<?> selectProtStatusForDashboard() {
        return ResponseHelper.success(protStatusService.selectProtStatusForDashboard());
    }

    @OptionLog("根据 mainProtId 获取 protStatus 数据")
    @ApiOperation("根据 mainProtId 获取 protStatus 数据")
    @PostMapping("/find/{mainProtId}")
    public ResponseEntity<?> selectByMainProtId(@PathVariable Long mainProtId) {
        return ResponseHelper.success(protStatusService.selectByMainProtId(mainProtId));
    }

    @OptionLog("根据prot Status 数据id, 获取所有 prot Status Aux表 最新 数据")
    @ApiOperation("根据prot Status 数据id, 获取所有 prot Status Aux表 最新 数据")
    @PostMapping("/findByProtStatusId/{protStatusId}")
    public ResponseEntity<?> findByProtStatusId(@PathVariable Long protStatusId) {
        return ResponseHelper.success(protStatusService.findByProtStatusId(protStatusId));
    }


    @OptionLog("根据prot Status 数据id, 获取所有 prot Status Aux表 EVT 类型 options")
    @ApiOperation("根据prot Status 数据id, 获取所有 prot Status Aux表 EVT 类型 options")
    @PostMapping("/getEVTOptionsByStatusId/{protStatusId}")
    public ResponseEntity<?> getEVTOptionsByStatusId(@PathVariable Long protStatusId) {
        return ResponseHelper.success(protStatusService.getEVTOptionsByStatusId(protStatusId));
    }

    @OptionLog("根据prot Status 数据id, 获取所有 prot Status Aux表 DVT 类型 options")
    @ApiOperation("根据prot Status 数据id, 获取所有 prot Status Aux表 DVT 类型 options")
    @PostMapping("/getDVTOptionsByStatusId/{protStatusId}")
    public ResponseEntity<?> getDVTOptionsByStatusId(@PathVariable Long protStatusId) {
        return ResponseHelper.success(protStatusService.getDVTOptionsByStatusId(protStatusId));
    }

    @OptionLog("根据prot Status 数据id, 获取所有 prot Status Aux表 PVT 类型 options")
    @ApiOperation("根据prot Status 数据id, 获取所有 prot Status Aux表 PVT 类型 options")
    @PostMapping("/getPVTOptionsByStatusId/{protStatusId}")
    public ResponseEntity<?> getPVTOptionsByStatusId(@PathVariable Long protStatusId) {
        return ResponseHelper.success(protStatusService.getPVTOptionsByStatusId(protStatusId));
    }

    // 根据 prot Status 数据id，dataType EVT DVT PVT 新增prot Status Aux表 数据
    @OptionLog("根据 prot Status 数据id，dataType EVT DVT PVT 新增prot Status Aux表 数据")
    @ApiOperation("根据 prot Status 数据id，dataType EVT DVT PVT 新增prot Status Aux表 数据")
    @PostMapping("/addProtStatusAux")
    public ResponseEntity<?> addProtStatusAux(@Valid @RequestBody ProtStatusAux protStatusAux) {
        try {
            if ((protStatusAux.getProtStatusId() == null) || (protStatusAux.getDataType() == null) ||
                    (protStatusAux.getId() == null)) {
                return ResponseHelper.failed();
            }
            // id 里 放的 是 main_prot_id， 用于 验证是否是项目成员
            // 目前支持 子项目也有 prot Status
            Prot prot = protService.selectProtByID(protStatusAux.getId());
            Long main_prot_id;
            if (prot.getParentId() == 0) {
                main_prot_id = prot.getId();
            } else {
                main_prot_id = prot.getParentId();
            }
            if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), main_prot_id) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), main_prot_id)){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            protStatusService.addProtStatusAux(protStatusAux);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }


    @PostMapping("/edit")
    public ResponseEntity<?> editProtStatus(@Valid @RequestBody ProtStatusInsertDto protStatusInsertDto) {
        try {
            Prot prot = protService.selectProtByID(protStatusInsertDto.getMainProtId());
            Long main_prot_id;
            if (prot.getParentId() == 0) {
                main_prot_id = prot.getId();
            } else {
                main_prot_id = prot.getParentId();
            }
            if (!dataPermissionService.isMember(HttpContextUtil.getCurrentUser().getId(), main_prot_id) &&
                    !dataPermissionService.isOwner(HttpContextUtil.getCurrentUser().getId(), main_prot_id)){
                return ResponseHelper.failed(BizCode.NO_DATA_PERMISSION);
            }
            protStatusService.editProtStatus(protStatusInsertDto);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }


    @OptionLog("根据 prot Status Aux id 查询 数据")
    @ApiOperation("根据 prot Status Aux id 查询 数据")
    @PostMapping("/selectProtStatusAuxById/{id}")
    public ResponseEntity<?> selectProtStatusAuxById(@PathVariable Long id) {
        return ResponseHelper.success(protStatusService.selectProtStatusAuxById(id));
    }


}
