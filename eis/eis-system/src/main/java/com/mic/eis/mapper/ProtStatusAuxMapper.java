package com.mic.eis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mic.eis.domain.model.ProtStatusAux;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper
public interface ProtStatusAuxMapper extends BaseMapper<ProtStatusAux> {



    /**
     * 通过dataType获取最大的DataNum，用于制作表头
     * EVTSys  EVTRev  DVTSys  DVTRev  PVTSys PVTRev
     * @param protStatusIdList  List<Long>
     * @return List<ProtStatusAux>
     */
    List<ProtStatusAux> getMaxDataNumAndDataType(@Param("protStatusIdList") List<Long> protStatusIdList);

}
