package com.mic.eis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mic.eis.domain.model.ProtFile;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProtFileMapper extends BaseMapper<ProtFile> {


    /**
     * 批量 添加 数据
     * @param list ProtFileList
     * @return int
     */
    int addBatchProtFile(List<ProtFile> list);

    /**
     * 通过  List<Long> protIds 批量 删除
     * @param protIds List<Long>
     */
    void deleteProtFileByProtIds(@Param("protIds") List<Long> protIds);

}
