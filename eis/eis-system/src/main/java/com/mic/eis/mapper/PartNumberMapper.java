package com.mic.eis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mic.eis.domain.model.PartNumber;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface PartNumberMapper extends BaseMapper<PartNumber> {

    int addBatchPartNum(List<PartNumber> list);
}
