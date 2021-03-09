package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.model.ProtFileVer;
import com.mic.eis.domain.vo.ProtFileVerQueryVo;

import java.util.List;

public interface ProtFileVerService extends IService<ProtFileVer> {

    /**
     * 通过 子项目id 和 文件类别 和 文件版本 查询 ProtFileVer
     * @param protFileVerQueryVo 查询实体
     * 类别
     * A: Me Part List
     * @return  版本列表
     */
    List<ProtFileVer> selectByProtIdAndCategoryAndVer(ProtFileVerQueryVo protFileVerQueryVo);

    /**
     * 添加 子项目 文件 版本
     * @param protFileVer 项目文档版本 实体
     */
    void addProtFileVer(ProtFileVer protFileVer);


    /**
     * 通过 子项目id、 文件版本、 文件类别 删除
     * @param protFileVer 实体
     */
    void deleteProtFileVer(ProtFileVer protFileVer);

    /**
     * 编辑 ProtFileVer
     * @param protFileVer 实体
     * @return true false
     */
    boolean editProtFileVer(ProtFileVer protFileVer);

}
