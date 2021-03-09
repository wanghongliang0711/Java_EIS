package com.mic.eis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mic.eis.domain.model.ProtPilotRunReportQueryRecord;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

@Mapper
public interface ProtPilotRunReportQueryRecordMapper extends BaseMapper<ProtPilotRunReportQueryRecord> {


    /**
     * 通过 id 删除 -->  真实删除
     * @param id id
     */
    void deletePilotRunReportQueryRecord(@Param("id")Long id);

}
