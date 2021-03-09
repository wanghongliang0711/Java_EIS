package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.model.ProtPilotRunReportQueryRecord;
import com.mic.eis.domain.vo.ProtPilotRunReportVo;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.ProtPilotRunReportQueryRecordService;
import com.mic.eis.util.HttpContextUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author blake.wang
 * @date 2021-01-04 17:00
 */
@Slf4j
@RestController
@RequestMapping("/protPilotRunReportQueryRecord")
public class ProtPilotRunReportQueryRecordController {


    @Resource
    private ProtPilotRunReportQueryRecordService protPilotRunReportQueryRecordService;

    // 添加 或 更新 查询记录
    @OptionLog("添加 或 更新 PilotRunReportQueryRecord 查询记录")
    @ApiOperation("添加 或 更新 PilotRunReportQueryRecord 查询记录")
    @PostMapping("/saveOrUpdate")
    public ResponseEntity<?> saveOrUpdatePilotRunReportQueryRecord(@Valid @RequestBody ProtPilotRunReportVo protPilotRunReportVo) {
        try {
            protPilotRunReportQueryRecordService.saveOrUpdatePilotRunReportQueryRecord(protPilotRunReportVo);
            return ResponseHelper.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

    // 查询 用户 查询 记录
    @OptionLog("查询 用户 查询 记录")
    @ApiOperation("查询 用户 查询 记录")
    @PostMapping("/selectQueryRecord")
    public ResponseEntity<?> selectQueryRecord(){
        try {
            ProtPilotRunReportQueryRecord byId = protPilotRunReportQueryRecordService.getById(HttpContextUtil.getCurrentUser().getId());
            if (byId==null) {
                return ResponseHelper.success();
            }
            ProtPilotRunReportVo protPilotRunReportVo = new ProtPilotRunReportVo();
            if (byId.getSeverity() != null) {
                List<String> list = Arrays.asList(byId.getSeverity().split(","));
                protPilotRunReportVo.setSeverity(list);
            }
            if (byId.getStatus() != null) {
                List<String> list1 = Arrays.asList(byId.getStatus().split(","));
                protPilotRunReportVo.setStatus(list1);
            }
            return ResponseHelper.success(protPilotRunReportVo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }

}
