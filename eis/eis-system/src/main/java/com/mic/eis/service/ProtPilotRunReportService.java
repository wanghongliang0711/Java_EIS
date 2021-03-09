package com.mic.eis.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mic.eis.domain.dto.ProtPilotRunReportDto;
import com.mic.eis.domain.model.ProtFileVer;
import com.mic.eis.domain.model.ProtPilotRunReport;
import com.mic.eis.domain.vo.ProtMePartListQueryVo;
import com.mic.eis.domain.vo.ProtPilotRunReportVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface ProtPilotRunReportService extends IService<ProtPilotRunReport> {


    /**
     * 根据子项目id、文件版本、severity、status 查询数据
     * @param protPilotRunReportVo  项目id、文件版本、severity、status
     * @return List<ProtPilotRunReport>
     */
    List<ProtPilotRunReportDto> findProtPilotRunReport(ProtPilotRunReportVo protPilotRunReportVo);


    /**
     * 根据 项目id、文件版本  查询数据, 用于 下载Excel 时使用 不查询 图片
     * @param protId 项目 id
     * @param fileVer 文件 版本
     * @return List<ProtPilotRunReport>
     */
    List<ProtPilotRunReport> selectByProtIdAndVersion(Long protId, String fileVer);


    /**
     * 通过 id 删除 -->  真实删除
     * @param id id
     */
    void deleteProtPilotRunReportById(Long id);


    /**
     * 添加 ProtPilotRunReport 数据
     * @param protPilotRunReport  ProtPilotRunReport
     * @return {msg: }
     */
    HashMap<String, Object> addProtPilotRunReport(ProtPilotRunReport protPilotRunReport);


    /**
     * 修改 ProtPilotRunReport 数据
     * @param protPilotRunReport ProtPilotRunReport
     * @return {msg: }
     */
    HashMap<String, Object> editProtPilotRunReport(ProtPilotRunReport protPilotRunReport);


    /**
     * 获取 7 个 提示
     * @return {msg: }
     */
    HashMap<String, Object> selectAllTips();

    /**
     * 获取 Severity & Status 提示
     * @return {msg: }
     */
    HashMap<String, Object> selectQueryTips();

    /**
     * 添加新版本 --   PilotRunReport 生成新版本数据
     * @param protFileVer ProtFileVer 添加一条数据信息
     * @return 提示 信息
     */
    HashMap<String, Object> addNewVersionPilotRunReport(ProtFileVer protFileVer);


    /**
     * 根据 项目id 和 文件版本 将 PilotRunReport 恢复为之前的版本
     * @param protMePartListQueryVo 实体
     */
    void backBeforeVersion(ProtMePartListQueryVo protMePartListQueryVo);


    /**
     * 上传 PilotRunReport File
     * @param file MultipartFile
     * @param protId protId
     */
    void uploadPilotRunReportFile(MultipartFile file, Long protId);


}
