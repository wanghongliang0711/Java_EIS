package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.dto.MoldTypeDto;
import com.mic.eis.domain.model.MoldType;

import java.util.List;

public interface MoldTypeService extends IService<MoldType> {

    /**
     * 通过模具种类 查询模具 及其 子磨具
     * @param moldType
     * @return
     */
    List<MoldTypeDto> queryByType(MoldType moldType);

    /**
     * 通过ParentId查询项目
      * @param id
     * @return
     */
    List<MoldType> selectByParentId(Long id);

    /**
     * 通过模具种类 查询 主模具 是否已经存在
     * @param type
     * @return
     */
    List<MoldType> selectByType(String type);

    /**
     * 添加模具种类
     * @param moldType
     */
    void addMoldType(MoldType moldType);

    /**
     * 通过 id 删除模具种类
     * @param id
     */
    void deleteMoldTypeById(Long id);


    /**
     * 编辑 模具种类
     * @param moldType
     * @return
     */
    boolean editMoldType(MoldType moldType);
}
