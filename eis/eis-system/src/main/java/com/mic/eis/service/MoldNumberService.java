package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mic.eis.domain.model.MoldNumber;
import com.mic.eis.domain.vo.MoldNumberQueryVo;

import java.util.List;

public interface MoldNumberService extends IService<MoldNumber> {

    /**
     * 通过 mold num分页查询
     * @param moldNumberQueryVo 查询 实体
     * @return 分页后的结果
     */
    PageInfo<MoldNumber> queryByMoldNum(MoldNumberQueryVo moldNumberQueryVo);


    /**
     * 通过 moldNumber 查询， 用于判断 添加时是否有重复
     * @param moldNumber 字符串
     * @return  匹配的项目
     */
    List<MoldNumber> selectByMoldNum(String moldNumber);


    /**
     * 添加 数据
     * @param moldNumber  模穴数 实体
     */
    void addMoldNum(MoldNumber moldNumber);


    /**
     * 通过 id 删除 MoldNum
     * @param id 数据 id
     */
    void deleteMoldNumById(Long id);


    /**
     * 通过 id 排除当前的 的 moldNum
     * @param moldNumber  修改的 实体
     * @return  除了当前的实体， 是否有 其他 重复项
     */
    List<MoldNumber> selectByMoldNumExcludeMoldNum(MoldNumber moldNumber);

    /**
     * 编辑 moldNum
     * @param moldNumber  修改的 实体
     * @return  boolean
     */
    boolean editMoldNum(MoldNumber moldNumber);

}
