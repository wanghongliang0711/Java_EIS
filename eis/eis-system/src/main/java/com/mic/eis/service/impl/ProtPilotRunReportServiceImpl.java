package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.dto.ProtPilotRunReportDto;
import com.mic.eis.domain.model.ProtFile;
import com.mic.eis.domain.model.ProtFileVer;
import com.mic.eis.domain.model.ProtPilotRunReport;
import com.mic.eis.domain.vo.ProtFileVerQueryVo;
import com.mic.eis.domain.vo.ProtMePartListQueryVo;
import com.mic.eis.domain.vo.ProtPilotRunReportVo;
import com.mic.eis.mapper.ProtFileMapper;
import com.mic.eis.mapper.ProtPilotRunReportMapper;
import com.mic.eis.service.ProtFileService;
import com.mic.eis.service.ProtFileVerService;
import com.mic.eis.service.ProtPilotRunReportService;
import com.mic.eis.util.ExcelReaderUtils;
import com.mic.eis.utils.FastDfsUtils;
import com.mic.eis.utils.GeneratorCodeUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author blake.wang
 * @date 2021-01-04 16:53
 */
@Service
public class ProtPilotRunReportServiceImpl extends ServiceImpl<ProtPilotRunReportMapper, ProtPilotRunReport> implements ProtPilotRunReportService {


    @Resource
    private ProtPilotRunReportMapper protPilotRunReportMapper;

    @Resource
    private ProtFileService protFileService;

    @Resource
    private ProtFileVerService protFileVerService;

    @Resource
    private ProtFileMapper protFileMapper;

    @Resource
    private FastDfsUtils fastDfsUtils;


    @Override
    public List<ProtPilotRunReportDto> findProtPilotRunReport(ProtPilotRunReportVo protPilotRunReportVo) {
        try {
            List<ProtPilotRunReportDto> resultList = new ArrayList<>();
            QueryWrapper<ProtPilotRunReport> wrapper = new QueryWrapper<>();
            wrapper.eq("prot_id", protPilotRunReportVo.getProtId())
                    .eq("file_ver", protPilotRunReportVo.getFileVer())
                    .eq("is_delete", 0)
                    .orderByAsc("item");  // ??????
            if (protPilotRunReportVo.getSeverity() != null && protPilotRunReportVo.getSeverity().size() > 0) {
                wrapper.in("severity", protPilotRunReportVo.getSeverity());
            }
            if (protPilotRunReportVo.getStatus() != null && protPilotRunReportVo.getStatus().size() > 0) {
                wrapper.in("status", protPilotRunReportVo.getStatus());
            }
            List<ProtPilotRunReport> protPilotRunReports = protPilotRunReportMapper.selectList(wrapper);
            for (ProtPilotRunReport protPilotRunReport : protPilotRunReports) {
                ProtPilotRunReportDto protPilotRunReportDto = new ProtPilotRunReportDto();
                protPilotRunReportDto.setId(protPilotRunReport.getId());
                protPilotRunReportDto.setProtId(protPilotRunReport.getProtId());
                protPilotRunReportDto.setFileVer(protPilotRunReport.getFileVer());
                protPilotRunReportDto.setItem(protPilotRunReport.getItem());
                protPilotRunReportDto.setProblemDes(protPilotRunReport.getProblemDes());
                protPilotRunReportDto.setDateTime(protPilotRunReport.getDateTime());
                protPilotRunReportDto.setFailRate(protPilotRunReport.getFailRate());
                protPilotRunReportDto.setSeverity(protPilotRunReport.getSeverity());
                protPilotRunReportDto.setIssuer(protPilotRunReport.getIssuer());
                protPilotRunReportDto.setOwner(protPilotRunReport.getOwner());
                protPilotRunReportDto.setDesign(protPilotRunReport.getDesign());
                protPilotRunReportDto.setWork(protPilotRunReport.getWork());
                protPilotRunReportDto.setMaterial(protPilotRunReport.getMaterial());
                protPilotRunReportDto.setRootCause(protPilotRunReport.getRootCause());
                protPilotRunReportDto.setAction(protPilotRunReport.getAction());
                protPilotRunReportDto.setDueDate(protPilotRunReport.getDueDate());
                protPilotRunReportDto.setStatus(protPilotRunReport.getStatus());
                ProtFile protFile = new ProtFile();
                protFile.setProtId(protPilotRunReport.getId());
                protFile.setCategory("P3");
                protPilotRunReportDto.setPicture(protFileService.selectByProtIdAndCategory(protFile));
                resultList.add(protPilotRunReportDto);
            }
            return resultList;
        } catch (Exception e) {
            log.error("?????? findProtPilotRunReport ??????");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<ProtPilotRunReport> selectByProtIdAndVersion(Long protId, String fileVer) {
        QueryWrapper<ProtPilotRunReport> wrapper = new QueryWrapper<>();
        wrapper.eq("prot_id", protId)
                .eq("file_ver", fileVer)
                .orderByAsc("item");  // ??????
        return protPilotRunReportMapper.selectList(wrapper);
    }

    @Override
    public void deleteProtPilotRunReportById(Long id) {
        try {
            protPilotRunReportMapper.deleteProtPilotRunReportById(id);
            // ?????????????????? ??????????????? ????????????
            // ???????????????????????????????????????????????????
            // ToolPlan ??????????????????
            protFileService.deleteProtFileByProtId(id);
        } catch (Exception e) {
            log.error("?????? deleteProtPilotRunReportById ??????");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public HashMap<String, Object> addProtPilotRunReport(ProtPilotRunReport protPilotRunReport) {
        HashMap<String, Object> resultMap = new HashMap<>();
        try {
            // ?????????????????????????????????
            QueryWrapper<ProtPilotRunReport> wrapper = new QueryWrapper<>();
            wrapper.eq("prot_id", protPilotRunReport.getProtId())
                    .eq("file_ver", protPilotRunReport.getFileVer())
                    .eq("is_delete", 0);
            List<ProtPilotRunReport> protPilotRunReports = protPilotRunReportMapper.selectList(wrapper);
            if (isExist(protPilotRunReport, protPilotRunReports)) { // item ??????
                resultMap.put("msgFail", "item ????????????, ???????????????????????????");
            } else {  // item ?????????
                protPilotRunReport.setId(GeneratorCodeUtil.generateKey());
                protPilotRunReport.setUpdateTime(new Date());
                protPilotRunReportMapper.insert(protPilotRunReport);
            }
            return resultMap;
        } catch (Exception e) {
            log.error("?????? addProtPilotRunReport ??????");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public HashMap<String, Object> editProtPilotRunReport(ProtPilotRunReport protPilotRunReport) {
        try {
            HashMap<String, Object> resultMap = new HashMap<>();
            QueryWrapper<ProtPilotRunReport> wrapper = new QueryWrapper<>();
            wrapper.eq("prot_id", protPilotRunReport.getProtId())
                    .eq("file_ver", protPilotRunReport.getFileVer())
                    .eq("is_delete", 0)
                    .ne("id", protPilotRunReport.getId());
            List<ProtPilotRunReport> protPilotRunReports = protPilotRunReportMapper.selectList(wrapper);
            if (isExist(protPilotRunReport, protPilotRunReports)) {  // item ??????
                resultMap.put("msgFail", "item ????????????, ???????????????????????????");
            } else {  // item ?????????
                protPilotRunReport.setUpdateTime(new Date());
                protPilotRunReportMapper.updateById(protPilotRunReport);
            }
            return resultMap;
        }catch (Exception e) {
            log.error("?????? editProtPilotRunReport ??????");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public HashMap<String, Object> selectAllTips() {
        HashMap<String, Object> resultMap = new HashMap<>();
        // Problem Description
        List<HashMap<String, String>> problemDes = new ArrayList<>();
        QueryWrapper<ProtPilotRunReport> wrapper = new QueryWrapper<>();
        wrapper.select("problem_des")
                .isNotNull("problem_des")
                .groupBy("problem_des");
        List<ProtPilotRunReport> protPilotRunReports = protPilotRunReportMapper.selectList(wrapper);
        for (ProtPilotRunReport protPilotRunReport : protPilotRunReports) {
            HashMap<String, String> map = new HashMap<>();
            map.put("value", protPilotRunReport.getProblemDes());
            problemDes.add(map);
        }
        resultMap.put("problemDes", problemDes);
        // Severity
        List<HashMap<String, String>> severity = new ArrayList<>();
        QueryWrapper<ProtPilotRunReport> wrapperSeverity = new QueryWrapper<>();
        wrapperSeverity.select("severity")
                        .isNotNull("severity")
                        .groupBy("severity");
        List<ProtPilotRunReport> protPilotRunReports1 = protPilotRunReportMapper.selectList(wrapperSeverity);
        for (ProtPilotRunReport protPilotRunReport : protPilotRunReports1) {
            HashMap<String, String> map = new HashMap<>();
            map.put("value", protPilotRunReport.getSeverity());
            severity.add(map);
        }
        resultMap.put("severity", severity);
        // Issuer
        List<HashMap<String, String>> issuer = new ArrayList<>();
        QueryWrapper<ProtPilotRunReport> wrapperIssuer = new QueryWrapper<>();
        wrapperIssuer.select("issuer")
                    .isNotNull("issuer")
                    .groupBy("issuer");
        List<ProtPilotRunReport> protPilotRunReports2 = protPilotRunReportMapper.selectList(wrapperIssuer);
        for (ProtPilotRunReport protPilotRunReport : protPilotRunReports2) {
            HashMap<String, String> map = new HashMap<>();
            map.put("value", protPilotRunReport.getIssuer());
            issuer.add(map);
        }
        resultMap.put("issuer", issuer);
        // Owner
        List<HashMap<String, String>> owner = new ArrayList<>();
        QueryWrapper<ProtPilotRunReport> wrapperOwner = new QueryWrapper<>();
        wrapperOwner.select("owner")
                    .isNotNull("owner")
                    .groupBy("owner");
        List<ProtPilotRunReport> protPilotRunReports3 = protPilotRunReportMapper.selectList(wrapperOwner);
        for (ProtPilotRunReport protPilotRunReport : protPilotRunReports3) {
            HashMap<String, String> map = new HashMap<>();
            map.put("value", protPilotRunReport.getOwner());
            owner.add(map);
        }
        resultMap.put("owner", owner);
        // Root Cause
        List<HashMap<String, String>> rootCause = new ArrayList<>();
        QueryWrapper<ProtPilotRunReport> wrapperRootCause = new QueryWrapper<>();
        wrapperRootCause.select("root_cause")
                        .isNotNull("root_cause")
                        .groupBy("root_cause");
        List<ProtPilotRunReport> protPilotRunReports4 = protPilotRunReportMapper.selectList(wrapperRootCause);
        for (ProtPilotRunReport protPilotRunReport : protPilotRunReports4) {
            HashMap<String, String> map = new HashMap<>();
            map.put("value", protPilotRunReport.getRootCause());
            rootCause.add(map);
        }
        resultMap.put("rootCause", rootCause);
        // Action
        List<HashMap<String, String>> action = new ArrayList<>();
        QueryWrapper<ProtPilotRunReport> wrapperAction = new QueryWrapper<>();
        wrapperAction.select("action")
                    .isNotNull("action")
                    .groupBy("action");
        List<ProtPilotRunReport> protPilotRunReports5 = protPilotRunReportMapper.selectList(wrapperAction);
        for (ProtPilotRunReport protPilotRunReport : protPilotRunReports5) {
            HashMap<String, String> map = new HashMap<>();
            map.put("value", protPilotRunReport.getAction());
            action.add(map);
        }
        resultMap.put("action", action);
        // Status
        List<HashMap<String, String>> status = new ArrayList<>();
        QueryWrapper<ProtPilotRunReport> wrapperStatus = new QueryWrapper<>();
        wrapperStatus.select("status")
                    .isNotNull("status")
                    .groupBy("status");
        List<ProtPilotRunReport> protPilotRunReports6 = protPilotRunReportMapper.selectList(wrapperStatus);
        for (ProtPilotRunReport protPilotRunReport : protPilotRunReports6) {
            HashMap<String, String> map = new HashMap<>();
            map.put("value", protPilotRunReport.getStatus());
            status.add(map);
        }
        resultMap.put("status", status);
        return resultMap;
    }

    @Override
    public HashMap<String, Object> selectQueryTips() {
        HashMap<String, Object> resultMap = new HashMap<>();
        // Severity
        List<HashMap<String, String>> severity = new ArrayList<>();
        QueryWrapper<ProtPilotRunReport> wrapperSeverity = new QueryWrapper<>();
        wrapperSeverity.isNotNull("severity")
                        .select("severity")
                        .groupBy("severity");
        List<ProtPilotRunReport> protPilotRunReports1 = protPilotRunReportMapper.selectList(wrapperSeverity);
        for (ProtPilotRunReport protPilotRunReport : protPilotRunReports1) {
            HashMap<String, String> map = new HashMap<>();
            map.put("label", protPilotRunReport.getSeverity());
            map.put("value", protPilotRunReport.getSeverity());
            severity.add(map);
        }
        resultMap.put("severity", severity);
        // Status
        List<HashMap<String, String>> status = new ArrayList<>();
        QueryWrapper<ProtPilotRunReport> wrapperStatus = new QueryWrapper<>();
        wrapperStatus.isNotNull("status")
                        .select("status")
                        .groupBy("status");
        List<ProtPilotRunReport> protPilotRunReports6 = protPilotRunReportMapper.selectList(wrapperStatus);
        for (ProtPilotRunReport protPilotRunReport : protPilotRunReports6) {
            HashMap<String, String> map = new HashMap<>();
            map.put("label", protPilotRunReport.getStatus());
            map.put("value", protPilotRunReport.getStatus());
            status.add(map);
        }
        resultMap.put("status", status);
        return resultMap;
    }

    @Override
    public HashMap<String, Object> addNewVersionPilotRunReport(ProtFileVer protFileVer) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            // ?????? ??? protFileVer ?????????????????????
            ProtFileVerQueryVo protFileVerQueryVo = new ProtFileVerQueryVo();
            protFileVerQueryVo.setFileVer(protFileVer.getFileVer());
            protFileVerQueryVo.setProtId(protFileVer.getProtId());
            protFileVerQueryVo.setCategory(protFileVer.getCategory());
            List<ProtFileVer> protFileVers = protFileVerService.selectByProtIdAndCategoryAndVer(protFileVerQueryVo);
            if (protFileVers.size() > 0) {
                map.put("msgFail", "????????????????????????????????????????????? ???");
                return map;
            }
            List<ProtPilotRunReport> addProtPRRDbList = new ArrayList<>();
            List<ProtFile> addProtFileDbList = new ArrayList<>();
            QueryWrapper<ProtPilotRunReport> wrapper = new QueryWrapper<>();
            wrapper.eq("prot_id", protFileVer.getProtId())
                    .eq("file_ver", "now")
                    .eq("is_delete", 0)
                    .orderByAsc("item");  // ??????
            List<ProtPilotRunReport> protPilotRunReports = protPilotRunReportMapper.selectList(wrapper);
            for (ProtPilotRunReport protPilotRunReport : protPilotRunReports) {
                ProtFile protFile1 = new ProtFile();
                protFile1.setCategory("P3");
                protFile1.setProtId(protPilotRunReport.getId());
                List<ProtFile> protFileList = protFileService.selectByProtIdAndCategory(protFile1);
                // ??????
                protPilotRunReport.setId(GeneratorCodeUtil.generateKey());
                for (ProtFile protFile : protFileList) {
                    protFile.setId(GeneratorCodeUtil.generateKey());
                    protFile.setProtId(protPilotRunReport.getId());
                    addProtFileDbList.add(protFile);
                }
                // ??????
                protPilotRunReport.setFileVer(protFileVer.getFileVer());
                protPilotRunReport.setUpdateTime(new Date());
                addProtPRRDbList.add(protPilotRunReport);
            }
            if (addProtPRRDbList.size() > 0) {
                protPilotRunReportMapper.addBatchProtPilotRunReport(addProtPRRDbList);
            }
            if (addProtFileDbList.size() > 0) {
                protFileMapper.addBatchProtFile(addProtFileDbList);
            }
            protFileVerService.addProtFileVer(protFileVer);
            return map;
        } catch (Exception e) {
            log.error("?????? addNewVersionPilotRunReport ????????? ??????");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void backBeforeVersion(ProtMePartListQueryVo protMePartListQueryVo) {
        try {
            List<ProtPilotRunReport> addProtPRRDbList = new ArrayList<>();
            List<ProtFile> addProtFileDbList = new ArrayList<>();
            QueryWrapper<ProtPilotRunReport> wrapper = new QueryWrapper<>();
            wrapper.eq("prot_id", protMePartListQueryVo.getProtId())
                    .eq("file_ver", protMePartListQueryVo.getFileVer())
                    .eq("is_delete", 0)
                    .orderByAsc("item");  // ??????
            List<ProtPilotRunReport> protPilotRunReports = protPilotRunReportMapper.selectList(wrapper);
            for (ProtPilotRunReport protPilotRunReport : protPilotRunReports) {
                ProtFile protFile1 = new ProtFile();
                protFile1.setProtId(protPilotRunReport.getId());
                protFile1.setCategory("P3");
                List<ProtFile> protFileList = protFileService.selectByProtIdAndCategory(protFile1);
                // ??????
                protPilotRunReport.setId(GeneratorCodeUtil.generateKey());
                for (ProtFile protFile : protFileList) {
                    protFile.setProtId(protPilotRunReport.getId());
                    protFile.setId(GeneratorCodeUtil.generateKey());
                    addProtFileDbList.add(protFile);
                }
                // ??????
                protPilotRunReport.setFileVer("now");
                protPilotRunReport.setUpdateTime(new Date());
                addProtPRRDbList.add(protPilotRunReport);
            }
            // ??????  protId  fileVer ?????? ???????????????
            HashMap<String, Object> map = new HashMap<>();
            map.put("prot_id", protMePartListQueryVo.getProtId());
            map.put("file_ver", "now");
            List<ProtPilotRunReport> protPilotRunReportsDelete = protPilotRunReportMapper.selectByMap(map);
            List<Long> deleteProtFile = new ArrayList<>();
            for (ProtPilotRunReport protPilotRunReport : protPilotRunReportsDelete) {
                deleteProtFile.add(protPilotRunReport.getId());
            }
            if (deleteProtFile.size() > 0) {
                protFileMapper.deleteProtFileByProtIds(deleteProtFile);
            }
            // Pilot Run Report ?????? protId  fileVer ??????  ????????????
            protPilotRunReportMapper.deletePilotRunReportByProtIdAndVer(protMePartListQueryVo.getProtId(), "now");
            if (addProtPRRDbList.size() > 0) {
                protPilotRunReportMapper.addBatchProtPilotRunReport(addProtPRRDbList);
            }
            if (addProtFileDbList.size() > 0) {
                protFileMapper.addBatchProtFile(addProtFileDbList);
            }
        } catch (Exception e) {
            log.error("??? Prot Pilot Run Report ??????????????????????????????");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void uploadPilotRunReportFile(MultipartFile file, Long protId) {
        try {
            // ?????? Excel ??? item ?????????????????? ????????????item?????????
            List<ProtPilotRunReport> protPilotRunReports = ExcelReaderUtils.readPilotRunReport(file);
            if (protPilotRunReports != null) {
                List<ProtPilotRunReport> duplicateRemoval = new ArrayList<>();
                // ??? Excel ???????????? ?????? ????????? ???????????? item????????? ??????
                Collections.reverse(protPilotRunReports);
                for (ProtPilotRunReport protPilotRunReport : protPilotRunReports) {
                    if (!isExist(protPilotRunReport, duplicateRemoval)) {
                        duplicateRemoval.add(protPilotRunReport);
                    }
                }
                List<ProtPilotRunReport> nowDb = selectByProtIdAndVersion(protId, "now");
                List<ProtPilotRunReport> insertDb = new ArrayList<>();
                List<ProtPilotRunReport> updateDb = new ArrayList<>();
                // ??????????????????????????????
                for (ProtPilotRunReport protPilotRunReport : duplicateRemoval) {
                    if (isExist(protPilotRunReport, nowDb)) {
                        protPilotRunReport.setProtId(protId);
                        protPilotRunReport.setFileVer("now");
                        protPilotRunReport.setUpdateTime(new Date());
                        updateDb.add(protPilotRunReport);
                    } else {
                        protPilotRunReport.setId(GeneratorCodeUtil.generateKey());
                        protPilotRunReport.setProtId(protId);
                        protPilotRunReport.setFileVer("now");
                        protPilotRunReport.setUpdateTime(new Date());
                        insertDb.add(protPilotRunReport);
                    }
                }
                List<Long> idList = new ArrayList<>();  // ????????????????????? id ??????
                if (updateDb.size() > 0) {
                    for (ProtPilotRunReport protPilotRunReport : updateDb) {
                        for (ProtPilotRunReport pilotRunReport : nowDb) {
                            if (protPilotRunReport.getItem().equals(pilotRunReport.getItem())) {
                                idList.add(pilotRunReport.getId());
                                protPilotRunReport.setId(pilotRunReport.getId());
                            }
                        }
                    }
                    protPilotRunReportMapper.deleteBatchPilotRunReportByIds(idList);
//                    protPilotRunReportMapper.deleteBatchIds(idList);
                }
                insertDb.addAll(updateDb);
                if (insertDb.size() > 0) {
                    protPilotRunReportMapper.addBatchProtPilotRunReport(insertDb);
                }
//                if (updateDb.size() > 0) {
//                    for (ProtPilotRunReport protPilotRunReport : updateDb) {
//                        UpdateWrapper<ProtPilotRunReport> updateWrapper = new UpdateWrapper<>();
//                        updateWrapper.eq("prot_id", protPilotRunReport.getProtId())
//                                .eq("file_ver", protPilotRunReport.getFileVer())
//                                .eq("item", protPilotRunReport.getItem());
//                        protPilotRunReportMapper.update(protPilotRunReport, updateWrapper);
//                    }
//                }
            }
        } catch (Exception e) {
            log.error("??????Excel ?????? ?????? Pilot Run Report ??????");
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * ?????? ?????? ??? item ?????? ????????????
     * @param protPilotRunReport  ProtPilotRunReport
     * @param protPilotRunReports List<ProtPilotRunReport>
     * @return ??????false ???true   ??????false
     */
    public boolean isExist(ProtPilotRunReport protPilotRunReport, List<ProtPilotRunReport> protPilotRunReports) {
        boolean result = false;
        for (ProtPilotRunReport pilotRunReport : protPilotRunReports) {
            if (protPilotRunReport.getItem().equals(pilotRunReport.getItem())) {
                result = true;
                break;
            }
        }
        return result;
    }

}
