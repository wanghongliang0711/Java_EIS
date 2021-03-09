package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.model.ProtToolPlanAux;
import com.mic.eis.mapper.ProtToolPlanAuxMapper;
import com.mic.eis.service.ProtToolPlanAuxService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-10-27 10:36
 */
@Service
public class ProtToolPlanAuxServiceImpl extends ServiceImpl<ProtToolPlanAuxMapper, ProtToolPlanAux> implements ProtToolPlanAuxService {


    @Resource
    private ProtToolPlanAuxMapper protToolPlanAuxMapper;

    @Override
    public List<ProtToolPlanAux> selectByDataIdAndType(Long dataId, String dataType) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("data_id", dataId);
        map.put("data_type", dataType);
        return protToolPlanAuxMapper.selectByMap(map);
    }

    @Override
    public List<ProtToolPlanAux> selectByDataId(Long dataId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("data_id", dataId);
        return protToolPlanAuxMapper.selectByMap(map);
    }


}
