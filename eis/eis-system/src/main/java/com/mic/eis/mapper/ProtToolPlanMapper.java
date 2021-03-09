package com.mic.eis.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mic.eis.domain.model.ProtToolPlan;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProtToolPlanMapper extends BaseMapper<ProtToolPlan> {


    /**
     * 通过 id 删除 -->  真实删除
     * @param id id
     */
    void deleteProtToolPlanById(@Param("id")Long id);

    /**
     * 通过 parentId  删除 -->  真实删除
     * @param parentId 父  id
     */
    void deleteProtToolPlanByParentId(@Param("parentId")Long parentId);


    /**
     * 通过 protId  fileVer 删除 -->  真实删除
     * @param protId 子项目id
     * @param fileVer  项目文件版本
     */
    void deleteProtToolPlanByProtIdAndVer(@Param("protId")Long protId, @Param("fileVer")String fileVer);


    /**
     * 批量 添加 数据
     * @param list  List ProtToolPlan
     * @return 插入数量
     */
    int addBatchProtToolPlan(List<ProtToolPlan> list);

}
