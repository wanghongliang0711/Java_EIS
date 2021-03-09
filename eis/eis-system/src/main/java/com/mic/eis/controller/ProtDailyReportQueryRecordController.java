package com.mic.eis.controller;

import com.mic.eis.annotation.OptionLog;
import com.mic.eis.domain.dto.ProtDailyReportQueryRecordDto;
import com.mic.eis.domain.model.ProtDailyReportQueryRecord;
import com.mic.eis.response.ResponseEntity;
import com.mic.eis.response.ResponseHelper;
import com.mic.eis.service.ProtDailyReportQueryRecordService;
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
 * @date 2020-11-18 10:08
 */
@Slf4j
@RestController
@RequestMapping("/protDailyReportQueryRecord")
public class ProtDailyReportQueryRecordController {

    @Resource
    private ProtDailyReportQueryRecordService protDailyReportQueryRecordService;

    // 添加 或 更新 查询记录
    @OptionLog("添加 或 更新 DailyReport 查询记录")
    @ApiOperation("添加 或 更新 DailyReport 查询记录")
    @PostMapping("/saveOrUpdate")
    public ResponseEntity<?> saveOrUpdateDailyReportQueryRecord(@Valid @RequestBody ProtDailyReportQueryRecordDto protDailyReportQueryRecordDto){
        try {
            protDailyReportQueryRecordService.saveOrUpdateDailyReportQueryRecord(protDailyReportQueryRecordDto);
            return ResponseHelper.success();
        } catch (Exception e){
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
            ProtDailyReportQueryRecord byId = protDailyReportQueryRecordService.getById(HttpContextUtil.getCurrentUser().getId());
            if (byId==null) {
                return ResponseHelper.success();
            }
            ProtDailyReportQueryRecordDto protDailyReportQueryRecordDto = new ProtDailyReportQueryRecordDto();
            protDailyReportQueryRecordDto.setId(byId.getId());
            protDailyReportQueryRecordDto.setProtId(byId.getProtId());
            protDailyReportQueryRecordDto.setSubProtId(byId.getSubProtId());
            if (byId.getIssuePriority() != null) {
                List<String> list = Arrays.asList(byId.getIssuePriority().split(","));
                protDailyReportQueryRecordDto.setIssuePriority(list);
            }
            return ResponseHelper.success(protDailyReportQueryRecordDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.failed();
        }
    }





}
