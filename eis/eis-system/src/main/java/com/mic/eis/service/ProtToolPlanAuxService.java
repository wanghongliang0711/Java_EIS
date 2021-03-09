package com.mic.eis.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.model.ProtToolPlanAux;

import java.util.List;

public interface ProtToolPlanAuxService extends IService<ProtToolPlanAux> {


    /**
     * 通过  所属数据id 数据类型 获取数据
     * @param dataId  所属数据id
     * @param dataType 数据类型
     * @return  List<ProtToolPlanAux>
     *     A: TEXTURE  CATEGORY
     *     B: INSERT NUT SPEC.
     */
    List<ProtToolPlanAux> selectByDataIdAndType(Long dataId, String dataType);


    /**
     * 通过  所属数据id 查询
     * @param dataId 所属数据id
     * @return List<ProtToolPlanAux>
     */
    List<ProtToolPlanAux> selectByDataId(Long dataId);

}
