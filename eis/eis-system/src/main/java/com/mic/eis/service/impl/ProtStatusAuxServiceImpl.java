package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.model.ProtStatusAux;
import com.mic.eis.mapper.ProtStatusAuxMapper;
import com.mic.eis.service.ProtStatusAuxService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-11-26 15:07
 */
@Service
public class ProtStatusAuxServiceImpl extends ServiceImpl<ProtStatusAuxMapper, ProtStatusAux> implements ProtStatusAuxService {


    @Resource
    private ProtStatusAuxMapper protStatusAuxMapper;


    @Override
    public List<ProtStatusAux> selectByStatusIdAndType(Long statusId, String dataType) {
        QueryWrapper<ProtStatusAux> wrapper = new QueryWrapper<>();
        wrapper.eq("prot_status_id", statusId)
                .eq("data_type", dataType)
                .isNotNull("data_num")
                .orderByAsc("data_num");
        return protStatusAuxMapper.selectList(wrapper);
    }

}
