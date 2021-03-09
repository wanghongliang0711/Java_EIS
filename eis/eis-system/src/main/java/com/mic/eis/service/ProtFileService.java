package com.mic.eis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.dto.ProtFileDto;
import com.mic.eis.domain.model.ProtFile;
import com.mic.eis.domain.vo.ProtFileUploadVo;

import java.io.IOException;
import java.util.List;

public interface ProtFileService extends IService<ProtFile> {

    /**
     * 以前写的，似乎弃用了
     * 通过 子项目id、文件类别 查询 项目文件
     * @param protFile ProtFile
     * @return List<ProtFile>
     */
    List<ProtFile> selectByProtIdAndCategory(ProtFile protFile);

    /**
     *  通过 子项目id、文件类别 查询 项目文件
     * @param protFile ProtFile
     * @return  List<ProtFileDto>
     */
    List<ProtFileDto> selectByCategoryAndProtId(ProtFile protFile);

    /**
     * 通过文件类别查找  主要用于 查找  Template File
     * @param category 文件类别
     * @return ProtFile list
     */
    List<ProtFile>  selectByCategory(String category);


    /**
     * 通过 子项目id 逻辑删除
     * @param protId 子项目id
     */
    void deleteProtFileByProtId(Long protId);

    /**
     * 通过 子项目id 查询 项目 最新的 9种 文件
     * @param sonProtId 子项目id
     * @return List<ProtFileDto>
     */
    List<ProtFileDto> selectNewestFile(Long sonProtId);

    /**
     * 上传文件
     * @param protFileUploadVo
     */
    void uploadFile(ProtFileUploadVo protFileUploadVo) throws IOException;

    /**
     * 上传文件 Template File, 先删再插，确保唯一性
     * @param protFileUploadVo
     */
    void uploadTemplateFile(ProtFileUploadVo protFileUploadVo) throws IOException;

    /**
     * 主要用于 删除 Template File，只保留唯一的 Template File
     * @param category 文件类别
     */
    void deleteByCategory(String category);

    /**
     * 通过 id 下载文件
     * @param id
     */
    ProtFile downloadFileById(Long id);

}
