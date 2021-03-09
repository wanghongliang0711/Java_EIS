package com.mic.eis.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.model.ProtStatusAux;

import java.util.List;

public interface ProtStatusAuxService extends IService<ProtStatusAux> {


    /**
     * 通过 prot status主数据 id, 和 dataType 查询
     * data type: EVTSys  EVTRev  DVTSys  DVTRev  PVTSys PVTRev
     * @param statusId Long statusId
     * @param dataType String dataType
     * @return List<ProtStatusAux>
     */
    List<ProtStatusAux> selectByStatusIdAndType(Long statusId, String dataType);

}
