package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.model.ProtPilotRunReportQueryRecord;
import com.mic.eis.domain.vo.ProtPilotRunReportVo;
import com.mic.eis.mapper.ProtPilotRunReportQueryRecordMapper;
import com.mic.eis.service.ProtPilotRunReportQueryRecordService;
import com.mic.eis.util.HttpContextUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author blake.wang
 * @date 2021-01-04 16:57
 */
@Service
public class ProtPilotRunReportQueryRecordServiceImpl extends ServiceImpl<ProtPilotRunReportQueryRecordMapper, ProtPilotRunReportQueryRecord> implements ProtPilotRunReportQueryRecordService {

    @Resource
    private ProtPilotRunReportQueryRecordMapper protPilotRunReportQueryRecordMapper;

    @Override
    public void saveOrUpdatePilotRunReportQueryRecord(ProtPilotRunReportVo protPilotRunReportVo) {
        try {
            // 真实删除
            protPilotRunReportQueryRecordMapper.deletePilotRunReportQueryRecord(HttpContextUtil.getCurrentUser().getId());
            ProtPilotRunReportQueryRecord protPilotRunReportQueryRecord = new ProtPilotRunReportQueryRecord();
            protPilotRunReportQueryRecord.setId(HttpContextUtil.getCurrentUser().getId());
            if (protPilotRunReportVo.getSeverity() != null) {
                if (protPilotRunReportVo.getSeverity().size() > 0) {
                    protPilotRunReportQueryRecord.setSeverity(String.join(",", protPilotRunReportVo.getSeverity()));
                }
            }
            if (protPilotRunReportVo.getStatus() != null) {
                if (protPilotRunReportVo.getStatus().size() > 0) {
                    protPilotRunReportQueryRecord.setStatus(String.join(",", protPilotRunReportVo.getStatus()));
                }
            }
            protPilotRunReportQueryRecordMapper.insert(protPilotRunReportQueryRecord);
        } catch (Exception e) {
            log.error("更新 saveOrUpdatePilotRunReportQueryRecord 异常");
            e.printStackTrace();
            throw e;
        }
    }
}
