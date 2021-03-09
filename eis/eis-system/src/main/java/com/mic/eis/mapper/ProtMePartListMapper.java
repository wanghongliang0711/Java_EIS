package com.mic.eis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mic.eis.domain.model.ProtMePartList;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProtMePartListMapper extends BaseMapper<ProtMePartList> {

    /**
     * 通过 id 删除 -->  真实删除
     * @param id id
     * @return 1 / 0
     */
    void deleteProtMePartListById(@Param("id")Long id);

    /**
     * 通过 protId  fileVer 删除 -->  真实删除
     * @param protId  子项目id
     * @param fileVer  项目版本
     */
    void deleteProtMePartListByProtIdAndVer(@Param("protId")Long protId, @Param("fileVer")String fileVer);


    int addBatchProtMePartList(List<ProtMePartList> list);

}
