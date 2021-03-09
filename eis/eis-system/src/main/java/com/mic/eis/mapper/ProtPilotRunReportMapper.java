package com.mic.eis.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mic.eis.domain.model.ProtPilotRunReport;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProtPilotRunReportMapper extends BaseMapper<ProtPilotRunReport> {

    /**
     * 通过 id 删除 -->  真实删除
     */
    void deleteProtPilotRunReportById(@Param("id")Long id);


    /**
     * 通过 protId  fileVer 删除 -->  真实删除
     * @param protId 项目id
     * @param fileVer 项目文件版本
     */
    void deletePilotRunReportByProtIdAndVer(@Param("protId")Long protId, @Param("fileVer")String fileVer);

    /**
     * 批量 添加
     * @param list  List<ProtPilotRunReport>
     * @return 添加 数量
     */
    int addBatchProtPilotRunReport(List<ProtPilotRunReport> list);


    /**
     * 通过  List<Long> 批量 删除 By Ids
     * @param idList  List<Long>
     */
    void deleteBatchPilotRunReportByIds(@Param("idList") List<Long> idList);
}
