package com.mic.eis.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mic.eis.domain.model.ProtToolPlanAux;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProtToolPlanAuxMapper extends BaseMapper<ProtToolPlanAux> {

    /**
     * 通过 dataId 删除 -->  真实删除
     * @param dataId  dataId 数据 id
     */
    void deleteProtToolPlanAuxByDataId(@Param("dataId")Long dataId);


    /**
     * 批量 添加 数据
     * @param list List  ProtToolPlanAux
     * @return 插入数量
     */
    int addBatchProtToolPlanAux(List<ProtToolPlanAux> list);


    /**
     * 通过  List<Long> dataIds 批量 删除
     * @param dataIds List<Long>
     */
    void deleteProtToolPlanAuxByDataIds(@Param("dataIds") List<Long> dataIds);

}
