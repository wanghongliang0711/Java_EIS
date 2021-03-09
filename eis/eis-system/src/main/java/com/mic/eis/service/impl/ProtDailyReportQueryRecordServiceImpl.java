package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.dto.ProtDailyReportQueryRecordDto;
import com.mic.eis.domain.model.ProtDailyReportQueryRecord;
import com.mic.eis.mapper.ProtDailyReportQueryRecordMapper;
import com.mic.eis.service.ProtDailyReportQueryRecordService;
import com.mic.eis.util.HttpContextUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author blake.wang
 * @date 2020-11-18 10:06
 */
@Service
public class ProtDailyReportQueryRecordServiceImpl extends ServiceImpl<ProtDailyReportQueryRecordMapper, ProtDailyReportQueryRecord> implements ProtDailyReportQueryRecordService {

    @Resource
    private ProtDailyReportQueryRecordMapper protDailyReportQueryRecordMapper;

    @Override
    public void saveOrUpdateDailyReportQueryRecord(ProtDailyReportQueryRecordDto protDailyReportQueryRecordDto) {
        try {
            protDailyReportQueryRecordMapper.deleteProtDailyReportQueryRecord(HttpContextUtil.getCurrentUser().getId());
            ProtDailyReportQueryRecord protDailyReportQueryRecord = new ProtDailyReportQueryRecord();
            protDailyReportQueryRecord.setId(HttpContextUtil.getCurrentUser().getId());
            protDailyReportQueryRecord.setProtId(protDailyReportQueryRecordDto.getProtId());
            protDailyReportQueryRecord.setSubProtId(protDailyReportQueryRecordDto.getSubProtId());
            if (protDailyReportQueryRecordDto.getIssuePriority() != null) {
                if (protDailyReportQueryRecordDto.getIssuePriority().size() > 0) {
                    protDailyReportQueryRecord.setIssuePriority(String.join(",", protDailyReportQueryRecordDto.getIssuePriority()));
                }
            }
            protDailyReportQueryRecordMapper.insert(protDailyReportQueryRecord);
        } catch (Exception e) {
            log.error("更新 saveOrUpdateDailyReportQueryRecord 异常");
            e.printStackTrace();
            throw e;
        }
    }

}
