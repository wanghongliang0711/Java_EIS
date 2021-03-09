package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mic.eis.domain.model.PartNumber;
import com.mic.eis.domain.vo.PartNumberQueryVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface PartNumberService extends IService<PartNumber> {


    /**
     * 根据 partNumber partDescription 分页查询
     * @param partNumberQueryVo
     * @return
     */
    PageInfo<PartNumber> findByNumAndDes(PartNumberQueryVo partNumberQueryVo);

    /**
     * 通过 partNumber 查询
     * @param partNumber
     * @return
     */
    List<PartNumber> selectByNum(String partNumber);

    /**
     * 查询所有的 partNumber
     * @return partNumber 的 list
     */
    List<PartNumber> selectAllPartNum();

    /**
     * 查询出所有数据
     * @return partNumber 的 list
     */
    List<PartNumber> selectAll();

    /**
     * 查询出所有数据, 并变为所需要的数据格式
     * @return [{partNum, partDes}, ]
     */
    List<HashMap<String, String>> selectAllPartNumberTips();

    /**
     * 上传 的 文件处理 根据Part Number
     * 与数据库的Part Number对比，重复的返回前台，新的插入
     * @param file 上传的文件
     * @return
     */
    List<PartNumber> uploadFile(MultipartFile file);


    /**
     * 通过 part Description 查询
     * @param partDescription
     * @return
     */
    List<PartNumber> selectByDes(String partDescription);

    /**
     * 排除 当前的  PartNum  =part_number   ！=id
     * @param partNumber 修改的实体
     * @return  除了当前的实体，是否有其他 重复
     */
    List<PartNumber> selectByPartNumExcludePartNum(PartNumber partNumber);

    /**
     * 排除 当前的  PartNum   =part_description ！=id
     * @param partNumber 修改的实体
     * @return  除了当前的实体，是否有其他 重复
     */
    List<PartNumber> selectByPartNumExcludePartDes(PartNumber partNumber);

    /**
     * 添加 数据
     * @param partNumber
     */
    void addPartNum(PartNumber partNumber);

    /**
     * 通过 id 删除 PartNum
     * @param id
     */
    void deletePartNumById(Long id);

    /**
     * 编辑 PartNum
     * @param partNumber
     * @return
     */
    boolean editPartNum(PartNumber partNumber);
}
