package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.model.ProtFileVer;
import com.mic.eis.domain.model.ProtMePartList;
import com.mic.eis.domain.vo.ProtMePartListQueryVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface ProtMePartListService extends IService<ProtMePartList> {


    /**
     * 通过 子项目id 和 文件版本 查询 Me Part List
     * @param protMePartListQueryVo  查询实体
     * @return  Me Part List 列表
     */
    List<ProtMePartList> selectByProtIdAndVersion(ProtMePartListQueryVo protMePartListQueryVo);


    /**
     * 通过 id 查询 ProtMePartList
     * @param id  id
     * @return ProtMePartList
     */
    ProtMePartList selectProtMePartListById(Long id);

    /**
     * 通过 id 删除 -->  真实删除
     * @param id id
     */
    void deleteProtMePartListById(Long id);

    /**
     * 通过 protId fileVer 删除  -->  真实删除
     * @param protId 子项目 id
     * @param fileVer 文件版本
     */
    void deleteProtMePartListByProtIdAndVer(Long protId, String fileVer);


    /**
     * 单个 添加 子项目 me part list
     * @param protMePartList 实体
     */
    HashMap<String, Object> addProtMePartList(ProtMePartList protMePartList);


    /**
     *  编辑 子项目 me part list
     * @param protMePartList 编辑实体
     * @return true false
     */
    HashMap<String, Object> editProtMePartList(ProtMePartList protMePartList);


    /**
     * 添加新版本 --  ProtMePartList 生成新版本数据， ProtFileVer 添加一条数据信息
     * @param protFileVer 实体
     */
    HashMap<String, Object> addNewVersion(ProtFileVer protFileVer);


    /**
     * 根据子项目id 、版本now 的 me part list，
     * 根据Part description 补齐 Part Number
     * @param sonProtId 子项目 id
     * @return
     */
    HashMap<String, Object> matchPartNum(Long sonProtId);


    /**
     * 根据 子项目id 、版本信息， 将 me part list 返回到之前的版本
     * @param protMePartListQueryVo 实体
     */
    void backBeforeVersion(ProtMePartListQueryVo protMePartListQueryVo);


    /**
     * 上传 me part list 文件， 处理数据
     * @param file Excel文件
     * @param sonProtId 子项目id
     * @return 各种结果
     */
    HashMap<String, Object> uploadMePartListFile(MultipartFile file, Long sonProtId);

    /**
     * 按预设项 覆盖description
     * @param protMePartLists ProtMePartList
     */
    void overDes(List<ProtMePartList> protMePartLists);
}
