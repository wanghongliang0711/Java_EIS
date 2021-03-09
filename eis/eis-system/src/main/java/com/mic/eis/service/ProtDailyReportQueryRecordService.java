package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.dto.ProtDailyReportQueryRecordDto;
import com.mic.eis.domain.model.ProtDailyReportQueryRecord;

public interface ProtDailyReportQueryRecordService extends IService<ProtDailyReportQueryRecord> {

    /**
     * 添加 或 更新 DailyReport 查询记录
     * @param protDailyReportQueryRecordDto ProtDailyReportQueryRecordDto
     */
    void saveOrUpdateDailyReportQueryRecord(ProtDailyReportQueryRecordDto protDailyReportQueryRecordDto);

}
