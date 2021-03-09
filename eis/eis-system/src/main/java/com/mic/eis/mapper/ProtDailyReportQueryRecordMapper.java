package com.mic.eis.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mic.eis.domain.model.ProtDailyReportQueryRecord;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

@Mapper
public interface ProtDailyReportQueryRecordMapper extends BaseMapper<ProtDailyReportQueryRecord> {


    /**
     * 通过 id 删除 -->  真实删除
     * @param id id
     */
    void deleteProtDailyReportQueryRecord(@Param("id")Long id);

}
