package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.model.ProtPilotRunReportQueryRecord;
import com.mic.eis.domain.vo.ProtPilotRunReportVo;

public interface ProtPilotRunReportQueryRecordService extends IService<ProtPilotRunReportQueryRecord> {


    /**
     * 添加 或 更新 PilotRunReport 查询记录
     * @param protPilotRunReportVo ProtPilotRunReportVo
     */
    void saveOrUpdatePilotRunReportQueryRecord(ProtPilotRunReportVo protPilotRunReportVo);

}
