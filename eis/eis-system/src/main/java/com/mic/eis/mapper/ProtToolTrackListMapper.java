package com.mic.eis.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mic.eis.domain.model.ProtToolTrackList;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProtToolTrackListMapper extends BaseMapper<ProtToolTrackList> {


    /**
     * 通过 id 删除 -->  真实删除
     */
    void deleteProtToolTrackListById(@Param("id")Long id);


    /**
     * 批量添加
     */
    int addBatchProtToolTrackList(List<ProtToolTrackList> list);


    /**
     * 通过 sonProtId 获取 最新 T版本的 Part No
     */
    List<ProtToolTrackList> getLastPartNoAndTx(@Param("sonProtId") Long sonProtId);


    /**
     * 通过 sonProtId 获取 最新 T, 用于生成 表头
     */
    List<ProtToolTrackList> getLastTxBySonProtId(@Param("sonProtId") Long sonProtId);

}
