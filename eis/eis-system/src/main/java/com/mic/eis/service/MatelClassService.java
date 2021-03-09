package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.dto.MatelClassDto;
import com.mic.eis.domain.dto.MatelClassDto1;
import com.mic.eis.domain.model.MatelClass;

import java.util.HashMap;
import java.util.List;

public interface MatelClassService extends IService<MatelClass> {


    /**
     * 通过 材料种类 查询材料 及其 子材料
     * @param matelClass 材料种类实体
     * @return  种类及其子类
     */
    List<MatelClassDto> queryByMaterial(MatelClass matelClass);

    /**
     * 通过 种类名 不分页查询材料分类 从一级往下查找 到 三级
     * @param matelClass  材料种类实体
     * @return   List MatelClassDto1
     */
    List<MatelClassDto1> queryByOneMaterial(MatelClass matelClass);

    /**
     * 查询出所有数据, 并变为所需要的数据格式
     * @return [{material}, ]
     */
    List<HashMap<String, String>> selectAllMaterialTips();

    /**
     * 通过ParentId查询 子类
     * @param id 父类 id
     * @return  这个父类的所有子类
     */
    List<MatelClass> selectByParentId(Long id);


    /**
     * 查找所有数据
     * @return all
     */
    List<MatelClass> selectAll();

    /**
     * 通过材料种类 查询 主种类 是否已经存在
     * @param material String
     * @return  List<MatelClass>
     */
    List<MatelClass> selectByMaterial(String material);


    /**
     *  通过 材料种类 查询 种类 是否存在
     * @param material String
     * @return List<MatelClass>
     */
    List<MatelClass> selectByAllMaterial(String material);

    /**
     * 添加材料种类
     * @param matelClass  matelClass 实体
     */
    void addMaterial(MatelClass matelClass);

    /**
     * 通过 id 删除模材料种类
     * @param id id
     */
    void deleteMatelClassById(Long id);


    /**
     * 编辑 材料种类
     * @param matelClass  实体
     * @return true false
     */
    boolean editMatelClass(MatelClass matelClass);

}
