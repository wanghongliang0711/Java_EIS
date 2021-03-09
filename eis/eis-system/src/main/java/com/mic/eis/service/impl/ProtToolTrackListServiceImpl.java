package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.dto.*;
import com.mic.eis.domain.model.*;
import com.mic.eis.mapper.ProtFileMapper;
import com.mic.eis.mapper.ProtToolPlanMapper;
import com.mic.eis.mapper.ProtToolTrackListMapper;
import com.mic.eis.service.PartNumberService;
import com.mic.eis.service.ProtFileService;
import com.mic.eis.service.ProtService;
import com.mic.eis.service.ProtToolTrackListService;
import com.mic.eis.util.HttpContextUtil;
import com.mic.eis.utils.FastDfsUtils;
import com.mic.eis.utils.GeneratorCodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-10-15 15:04
 */
@Service
public class ProtToolTrackListServiceImpl extends ServiceImpl<ProtToolTrackListMapper, ProtToolTrackList> implements ProtToolTrackListService {

    @Resource
    private ProtToolTrackListMapper protToolTrackListMapper;

    @Resource
    private PartNumberService partNumberService;

    @Resource
    private ProtFileService protFileService;

    @Resource
    private ProtFileMapper protFileMapper;

    @Resource
    private FastDfsUtils fastDfsUtils;

    @Resource
    private ProtToolPlanMapper protToolPlanMapper;

    @Resource
    private ProtService protService;


    @Override
    public List<ProtToolTrackListDto> selectBySonProtId(Long sonProtId, String issuePriority) {
        // 1. 查询出 项目 part no 最新的 Tx
        List<ProtToolTrackList> lastPartNoAndTx = protToolTrackListMapper.getLastPartNoAndTx(sonProtId);
        List<ProtToolTrackListDto> protToolTrackListDtos = new ArrayList<>();
        // 2. 遍历查询所有最新的数据
        for (ProtToolTrackList partNoAndTx : lastPartNoAndTx) {
            QueryWrapper<ProtToolTrackList> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", 0)
                    .eq("prot_id", sonProtId)
                    .eq("part_no", partNoAndTx.getPartNo())
                    .eq("tx", partNoAndTx.getTx())
                    .eq("is_delete", 0);
            List<ProtToolTrackList> protToolTrackLists = protToolTrackListMapper.selectList(wrapper);
            if (protToolTrackLists.size() > 0) {
                ProtToolTrackListDto protToolTrackListDto = new ProtToolTrackListDto();
                protToolTrackListDto.setId(protToolTrackLists.get(0).getId());
                protToolTrackListDto.setProtId(protToolTrackLists.get(0).getProtId());
                protToolTrackListDto.setPartNo(protToolTrackLists.get(0).getPartNo());
                // PartName 从 part Num 数据库中获取
                List<PartNumber> partNumbers = partNumberService.selectByNum(protToolTrackLists.get(0).getPartNo());
                protToolTrackListDto.setPartName(getPartName(partNumbers));
                protToolTrackListDto.setParentId(protToolTrackLists.get(0).getParentId());
                protToolTrackListDto.setTx(protToolTrackLists.get(0).getTx());
                // children 设置 Part No 为 null , Tx 为 null, 添加 picture 字段
                List<ProtToolTrackList> protToolTrackLists1 = selectByParentId(protToolTrackLists.get(0).getId(), issuePriority);
                List<ProtToolTrackListIssueReport> children = new ArrayList<>();
                for (ProtToolTrackList protToolTrackList : protToolTrackLists1) {
                    ProtToolTrackListIssueReport protToolTrackListIssueReport = new ProtToolTrackListIssueReport();
                    protToolTrackListIssueReport.setId(protToolTrackList.getId());
                    protToolTrackListIssueReport.setParentId(protToolTrackList.getParentId());
                    protToolTrackListIssueReport.setProtId(protToolTrackList.getProtId());
                    protToolTrackListIssueReport.setItem(protToolTrackList.getItem());
                    protToolTrackListIssueReport.setIssueDescription(protToolTrackList.getIssueDescription());
                    protToolTrackListIssueReport.setRootCause(protToolTrackList.getRootCause());
                    protToolTrackListIssueReport.setAction(protToolTrackList.getAction());
                    protToolTrackListIssueReport.setIssuePriority(protToolTrackList.getIssuePriority());
                    protToolTrackListIssueReport.setStatus(protToolTrackList.getStatus());
                    protToolTrackListIssueReport.setRemark(protToolTrackList.getRemark());
                    ProtFile protFile = new ProtFile();
                    protFile.setProtId(protToolTrackList.getId());
                    protFile.setCategory("P1");
                    protToolTrackListIssueReport.setPicture(protFileService.selectByProtIdAndCategory(protFile));
                    children.add(protToolTrackListIssueReport);
                }
                protToolTrackListDto.setChildren(children);
                protToolTrackListDtos.add(protToolTrackListDto);
            }
        }
        return protToolTrackListDtos;
    }

    @Override
    public List<ProtToolTrackListForDailyReportDto> findForDailyReport(ProtDailyReportQueryRecordDto protDailyReportQueryRecordDto) {
        List<ProtToolTrackListForDailyReportDto> resultList = new ArrayList<>();
        // SubProtId 存在
        if (protDailyReportQueryRecordDto.getSubProtId() != null) {
            Prot prot = protService.selectProtByID(protDailyReportQueryRecordDto.getSubProtId());
            findForDailyReportAssist(resultList, prot, protDailyReportQueryRecordDto);
        } else {
            List<Prot> prots = protService.selectProtByParentId(protDailyReportQueryRecordDto.getProtId());
            for (Prot prot : prots) {
                findForDailyReportAssist(resultList, prot, protDailyReportQueryRecordDto);
            }
        }
        return resultList;
    }


    public void findForDailyReportAssist(List<ProtToolTrackListForDailyReportDto> resultList, Prot prot,
                                         ProtDailyReportQueryRecordDto protDailyReportQueryRecordDto){
        ProtToolTrackListForDailyReportDto protToolTrackListForDailyReportDto = new ProtToolTrackListForDailyReportDto();
        protToolTrackListForDailyReportDto.setId(prot.getId());
        protToolTrackListForDailyReportDto.setSonProtName(prot.getName());
        List<ProtToolTrackListDto> childrenOne = new ArrayList<>();
        List<ProtToolTrackList> lastPartNoAndTx = protToolTrackListMapper.getLastPartNoAndTx(prot.getId());
        for (ProtToolTrackList partNoAndTx : lastPartNoAndTx) {
            QueryWrapper<ProtToolTrackList> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", 0)
                    .eq("prot_id", prot.getId())
                    .eq("part_no", partNoAndTx.getPartNo())
                    .eq("tx", partNoAndTx.getTx())
                    .eq("is_delete", 0);
            List<ProtToolTrackList> protToolTrackLists = protToolTrackListMapper.selectList(wrapper);
            if (protToolTrackLists.size() > 0) {
                ProtToolTrackListDto protToolTrackListDto = new ProtToolTrackListDto();
                protToolTrackListDto.setId(protToolTrackLists.get(0).getId());
                protToolTrackListDto.setPartNo(protToolTrackLists.get(0).getPartNo());
                // PartName 从 part Num 数据库中获取
                List<PartNumber> partNumbers = partNumberService.selectByNum(protToolTrackLists.get(0).getPartNo());
                if (partNumbers.size() > 0) {
                    protToolTrackListDto.setPartName(partNumbers.get(0).getPartDescription());
                }
                protToolTrackListDto.setParentId(protToolTrackLists.get(0).getParentId());
                protToolTrackListDto.setTx(protToolTrackLists.get(0).getTx());
                protToolTrackListDto.setDateAct(protToolTrackLists.get(0).getDateAct());
                List<ProtToolTrackList> protToolTrackLists1 = selectByParentIdAndIssuePriority(protToolTrackLists.get(0).getId(), protDailyReportQueryRecordDto);
                List<ProtToolTrackListIssueReport> childrenTwo = new ArrayList<>();
                for (ProtToolTrackList protToolTrackList : protToolTrackLists1) {
                    ProtToolTrackListIssueReport protToolTrackListIssueReport = new ProtToolTrackListIssueReport();
                    protToolTrackListIssueReport.setId(protToolTrackList.getId());
                    protToolTrackListIssueReport.setParentId(protToolTrackList.getParentId());
                    protToolTrackListIssueReport.setProtId(protToolTrackList.getProtId());
                    protToolTrackListIssueReport.setItem(protToolTrackList.getItem());
                    protToolTrackListIssueReport.setIssueDescription(protToolTrackList.getIssueDescription());
                    protToolTrackListIssueReport.setAction(protToolTrackList.getAction());
                    childrenTwo.add(protToolTrackListIssueReport);
                }
                protToolTrackListDto.setChildren(childrenTwo);
                childrenOne.add(protToolTrackListDto);
            }
        }
        protToolTrackListForDailyReportDto.setChildren(childrenOne);
        resultList.add(protToolTrackListForDailyReportDto);
    }



    @Override
    public List<ProtToolTrackList> selectByParentId(Long parentId, String issuePriority) {
        QueryWrapper<ProtToolTrackList> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId)
                .eq("is_delete", 0)
                .like("issue_priority", issuePriority)
                .orderByAsc("item");
        return protToolTrackListMapper.selectList(wrapper);
    }

    @Override
    public ProtToolTrackList selectToolTrackListById(Long id) {
        return protToolTrackListMapper.selectById(id);
    }

    @Override
    public HashMap<String, Object> addProtToolTrackListIssue(ProtToolTrackList protToolTrackList) {
        HashMap<String, Object> resultMap = new HashMap<>();
        try {
            protToolTrackList.setId(GeneratorCodeUtil.generateKey());
            protToolTrackList.setUpdateTime(new Date());
            protToolTrackList.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
            if (protToolTrackList.getParentId() != 0) { // 不等于0 是issue-- 等于0 是主类不需要item
                protToolTrackList.setItem(getLastItem(protToolTrackList));
                protToolTrackListMapper.insert(protToolTrackList);
                resultMap.put("msgSuccess", "新增 issue 成功!!!");
            } else { // 等于0 是主类不需要item
                // 添加 Create new Tx 对应的所有OPEN的item都会copy
                List<ProtToolTrackList> addDb = new ArrayList<>();
                List<ProtFile> addProtFile = new ArrayList<>();
                addDb.add(protToolTrackList);
                HashMap<String, Object> map = new HashMap<>();
                map.put("prot_id", protToolTrackList.getProtId());
                map.put("part_no", protToolTrackList.getPartNo());
                map.put("is_delete", 0);
                map.put("tx", protToolTrackList.getTx() - 1);
                map.put("status", "OPEN");
                List<ProtToolTrackList> protToolTrackLists = protToolTrackListMapper.selectByMap(map);
                if (protToolTrackLists.size() == 0) {
                    resultMap.put("msgFail", "没有OPEN Issue，不能再创建new Tx  !!!");
                    return resultMap;
                }
                for (ProtToolTrackList toolTrackList : protToolTrackLists) {
                    ProtFile protFile = new ProtFile();
                    protFile.setProtId(toolTrackList.getId());
                    protFile.setCategory("P1");
                    List<ProtFile> protFileList = protFileService.selectByProtIdAndCategory(protFile);
                    toolTrackList.setId(GeneratorCodeUtil.generateKey());
                    for (ProtFile file : protFileList) {
                        file.setId(GeneratorCodeUtil.generateKey());
                        file.setProtId(toolTrackList.getId());
                        addProtFile.add(file);
                    }
                    toolTrackList.setUpdateTime(new Date());
                    toolTrackList.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
                    toolTrackList.setParentId(protToolTrackList.getId());
                    toolTrackList.setTx(protToolTrackList.getTx());
                    toolTrackList.setRemark(null);
                    toolTrackList.setIsFirst("false");
                    addDb.add(toolTrackList);
                }
                if (addDb.size() > 0) {
                    protToolTrackListMapper.addBatchProtToolTrackList(addDb);
                }
                // 批量 添加 Prot file 表 数据
                if (addProtFile.size() > 0) {
                    protFileMapper.addBatchProtFile(addProtFile);
                }
                resultMap.put("msgSuccess", "创建 new Tx 成功!!!");
            }
            return resultMap;
        } catch (Exception e) {
            log.error("添加 ProtToolTrackList issue异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean editProtToolTrackListIssue(ProtToolTrackList protToolTrackList) {
        try {
            protToolTrackList.setUpdateTime(new Date());
            protToolTrackList.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
            return protToolTrackListMapper.updateById(protToolTrackList) == 1;
        } catch (Exception e) {
            log.error("编辑ProtToolTrackListIssue异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteProtToolTrackListById(Long id) {
        try {
            protToolTrackListMapper.deleteProtToolTrackListById(id);
            // 删除文件数据 且删除对应的图片
            HashMap<String, Object> map = new HashMap<>();
            map.put("prot_id", id);
            map.put("is_delete", 0);
            List<ProtFile> protFileList = protFileMapper.selectByMap(map);
            protFileMapper.deleteByMap(map);
            for (ProtFile protFile : protFileList) {
                fastDfsUtils.deleteFile(protFile.getResource());
            }
        } catch (Exception e) {
            log.error("删除 ProtToolTrackList 异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void updateToolTrackListPartNumForClosed(String partNo, Long sonProtId) {
        try {
            // 查找所有最新的part no Tx
            List<ProtToolTrackList> lastPartNoAndTx = protToolTrackListMapper.getLastPartNoAndTx(sonProtId);
            for (ProtToolTrackList partNoAndTx : lastPartNoAndTx) {
                if (partNoAndTx.getPartNo().equals(partNo)) {
                    QueryWrapper<ProtToolTrackList> wrapper = new QueryWrapper<>();
                    wrapper.ne("parent_id", 0)
                            .eq("prot_id", sonProtId)
                            .eq("part_no", partNo)
                            .eq("tx", partNoAndTx.getTx())
                            .eq("is_delete", 0)
                            .eq("status", "OPEN");
                    List<ProtToolTrackList> protToolTrackLists = protToolTrackListMapper.selectList(wrapper);
                    if (protToolTrackLists.size() > 0){
                        for (ProtToolTrackList protToolTrackList : protToolTrackLists) {
                            protToolTrackList.setStatus("CLOSE");
                            if (protToolTrackList.getRemark() != null) {
                                protToolTrackList.setRemark(protToolTrackList.getRemark() + " ; close because the the tooling is delete from tooling plan");
                            } else {
                                protToolTrackList.setRemark("close because the the tooling is delete from tooling plan");
                            }
                            protToolTrackList.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
                            protToolTrackList.setUpdateTime(new Date());
                            protToolTrackListMapper.updateById(protToolTrackList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("删除 updateToolTrackListPartNumForClosed 异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void updateToolTrackListPartNumForOpen(String partNo, Long sonProtId) {
        try {
            // 查找所有最新的part no Tx
            List<ProtToolTrackList> lastPartNoAndTx = protToolTrackListMapper.getLastPartNoAndTx(sonProtId);
            boolean updateIsShow = true;
            for (ProtToolTrackList partNoAndTx : lastPartNoAndTx) {
                if (partNo.equals(partNoAndTx.getPartNo())) {
                    QueryWrapper<ProtToolTrackList> wrapper = new QueryWrapper<>();
                    wrapper.ne("parent_id", 0)
                            .eq("prot_id", sonProtId)
                            .eq("part_no", partNo)
                            .eq("is_delete", 0)
                            .eq("tx", partNoAndTx.getTx())
                            .eq("status", "CLOSE");
                    List<ProtToolTrackList> protToolTrackLists = protToolTrackListMapper.selectList(wrapper);
                    if (protToolTrackLists.size() > 0){
                        for (ProtToolTrackList protToolTrackList : protToolTrackLists) {
                            if (protToolTrackList.getRemark() != null) {
                                if (protToolTrackList.getRemark().contains("close because the the tooling is delete from tooling plan")){
                                    protToolTrackList.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
                                    protToolTrackList.setUpdateTime(new Date());
                                    protToolTrackList.setStatus("OPEN");
                                    protToolTrackListMapper.updateById(protToolTrackList);
                                    // re-open同时，关于这个issue所属Part的呈现属性设为show
                                    if (updateIsShow) {
                                        ProtToolTrackList protToolTrackList1 = protToolTrackListMapper.selectById(protToolTrackList.getParentId());
                                        if (protToolTrackList1.getIsShow() == null || protToolTrackList1.getIsShow().equals("false")){
                                            protToolTrackList1.setUpdateTime(new Date());
                                            protToolTrackList1.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
                                            protToolTrackList1.setIsShow("true");
                                            protToolTrackListMapper.updateById(protToolTrackList1);
                                        }
                                        updateIsShow = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            log.error("删除 updateToolTrackListPartNumForOpen 异常");
            e.printStackTrace();
            throw e;
        }

    }


    @Override
    public List<HashMap<String, String>> selectAllRootCauseTips() {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        QueryWrapper<ProtToolTrackList> wrapper = new QueryWrapper<>();
        wrapper.select("root_cause")
                .ne("parent_id", 0)
                .eq("is_delete", 0)
                .isNotNull("root_cause")
                .groupBy("root_cause");
        List<ProtToolTrackList> protToolTrackLists = protToolTrackListMapper.selectList(wrapper);
        for (ProtToolTrackList protToolTrackList : protToolTrackLists) {
            HashMap<String, String> map = new HashMap<>();
            if (protToolTrackList.getRootCause() != null) {
                map.put("value", protToolTrackList.getRootCause());
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public HashMap<String, String> selectLastTimeUser(Long sonProtId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("lastTime", "");
        map.put("lastUser", "");
        QueryWrapper<ProtToolTrackList> wrapper = new QueryWrapper<>();
        wrapper.eq("prot_id", sonProtId)
                .orderByDesc("update_time")
                .last("limit 2");
        List<ProtToolTrackList> protToolTrackLists = protToolTrackListMapper.selectList(wrapper);
        if (protToolTrackLists.size() > 0) {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("lastTime", date.format(protToolTrackLists.get(0).getUpdateTime()));
            map.put("lastUser", protToolTrackLists.get(0).getUpdateUser());
        }
        return map;
    }

    @Override
    public List<ProtToolTrackList> selectLatestTxBySonProtId(Long sonProtId) {
        // 1. 查询出 项目 part no 最新的 Tx
        List<ProtToolTrackList> lastPartNoAndTx = protToolTrackListMapper.getLastPartNoAndTx(sonProtId);
        List<ProtToolTrackList> results = new ArrayList<>();
        for (ProtToolTrackList partNoAndTx : lastPartNoAndTx) {
            QueryWrapper<ProtToolTrackList> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", 0)
                    .eq("prot_id", sonProtId)
                    .eq("part_no", partNoAndTx.getPartNo())
                    .eq("tx", partNoAndTx.getTx())
                    .eq("is_delete", 0);
            List<ProtToolTrackList> protToolTrackLists = protToolTrackListMapper.selectList(wrapper);
            if (protToolTrackLists.size() > 0) {
                ProtToolTrackList protToolTrackList = new ProtToolTrackList();
                protToolTrackList.setId(protToolTrackLists.get(0).getId());
                protToolTrackList.setPartNo(protToolTrackLists.get(0).getPartNo());
                protToolTrackList.setProtId(protToolTrackLists.get(0).getProtId());
                protToolTrackList.setTx(protToolTrackLists.get(0).getTx());
                protToolTrackList.setDatePlan(protToolTrackLists.get(0).getDatePlan());
                protToolTrackList.setDateAct(protToolTrackLists.get(0).getDateAct());
                protToolTrackList.setParentId(protToolTrackLists.get(0).getParentId());
                protToolTrackList.setIsShow(protToolTrackLists.get(0).getIsShow());
                // PartName 从 part Num 数据库中获取
                List<PartNumber> partNumbers = partNumberService.selectByNum(protToolTrackLists.get(0).getPartNo());
                protToolTrackList.setPartName(getPartName(partNumbers));
                results.add(protToolTrackList);
            }
        }
        return results;
    }

    @Override
    public List<ProtToolTrackList> selectTxHistoryBySonProtId(Long sonProtId) {
        QueryWrapper<ProtToolTrackList> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0)
                .eq("prot_id", sonProtId)
                .eq("is_delete", 0)
                .orderByAsc("part_no", "tx");
        List<ProtToolTrackList> protToolTrackLists = protToolTrackListMapper.selectList(wrapper);
        for (ProtToolTrackList protToolTrackList : protToolTrackLists) {
            List<PartNumber> partNumbers = partNumberService.selectByNum(protToolTrackList.getPartNo());
            protToolTrackList.setPartName(getPartName(partNumbers));
        }
        return protToolTrackLists;
    }

    @Override
    public void syncToolPlanData(Long sonProtId) {
        try {
            // 1.获取 tool plan 中所有 父类数据
            QueryWrapper<ProtToolPlan> wrapper1 = new QueryWrapper<>();
            wrapper1.select("prot_id", "file_ver", "part_no")
                    .eq("is_delete", 0)
                    .eq("prot_id", sonProtId)
                    .eq("parent_id", 0)
                    .eq("file_ver", "now");
            List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper1);
            // 2. 获取 tool tracking list 中所有 的 part num 种类  --- 最新 T版本的 Part No
            List<ProtToolTrackList> lastPartNoAndTx = protToolTrackListMapper.getLastPartNoAndTx(sonProtId);
            // 3. 判断 是否在 tool tracking list 中，没有的就添加
            List<ProtToolTrackList> addDb = new ArrayList<>();
            for (ProtToolPlan protToolPlan : protToolPlans) {
                if (!checkWhetherToolPlanInToolTrackList(protToolPlan, lastPartNoAndTx)) {
                    ProtToolTrackList toolTrackList = new ProtToolTrackList();
                    toolTrackList.setId(GeneratorCodeUtil.generateKey());
                    toolTrackList.setUpdateTime(new Date());
                    toolTrackList.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
                    toolTrackList.setProtId(sonProtId);
                    toolTrackList.setPartNo(protToolPlan.getPartNo());
                    toolTrackList.setTx(1);
                    toolTrackList.setParentId(0L);
                    toolTrackList.setIsShow("true");
                    addDb.add(toolTrackList);
                }
            }
            if (addDb.size() > 0) {
                protToolTrackListMapper.addBatchProtToolTrackList(addDb);
            }
        }catch (Exception e) {
            log.error("同步 tooling plan 数据 异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public  HashMap<String, Object> selectIssueReportBySonProtId(Long sonProtId) {
        /**
         *  1. 根据 sonProtId ， 获取最大的 tx --》select MAX(tx) as tx from sys_prot_ToolingTrackingList where prot_id =443318032593633280;
         *  2. 根据 sonProtId获取 所有数据
         *  3. 根据 getLastPartNoAndTx 获取最新的 数据
         */
        HashMap<String, Object> map = new HashMap<>();
        // 表头
        List<String> tableHeader = new ArrayList<>();
        // 数据
        List<ProtToolTrackListIssueReportDto> resultData = new ArrayList<>();
        // 1. 根据 sonProtId , 获取最大的tx , 生成表头
        List<ProtToolTrackList> lastTxBySonProtId = protToolTrackListMapper.getLastTxBySonProtId(sonProtId);
        if (lastTxBySonProtId.get(0) !=null && lastTxBySonProtId.get(0).getTx() != null) {
            for (int i = 0; i < lastTxBySonProtId.get(0).getTx(); i++) {
                tableHeader.add("T"+(i+1));
            }
        }
        // 查询出 子项目所有 part_no
        QueryWrapper<ProtToolTrackList> wrapper1 = new QueryWrapper<>();
        wrapper1.select("part_no")
                .eq("is_delete", 0)
                .eq("prot_id", sonProtId)
                .isNotNull("part_no")
                .groupBy("part_no");
        List<ProtToolTrackList> protToolTrackLists = protToolTrackListMapper.selectList(wrapper1);
        long falseId = 100L;
        for (ProtToolTrackList protToolTrackList : protToolTrackLists) {
            ProtToolTrackListIssueReportDto protToolTrackListIssueReportDto = new ProtToolTrackListIssueReportDto();
            protToolTrackListIssueReportDto.setPartNo(protToolTrackList.getPartNo());
            List<PartNumber> partNumbers = partNumberService.selectByNum(protToolTrackList.getPartNo());
            protToolTrackListIssueReportDto.setPartName(getPartName(partNumbers));
            // 这是 假的 id
            protToolTrackListIssueReportDto.setId(falseId);
            falseId = falseId + 1L;
            List<ProtToolTrackListIssueReport> children = new ArrayList<>();
            // 查出这个 part_no 所有的 item
            QueryWrapper<ProtToolTrackList> wrapper2 = new QueryWrapper<>();
            wrapper2.select("item")
                    .eq("is_delete", 0)
                    .eq("prot_id", sonProtId)
                    .eq("part_no", protToolTrackList.getPartNo())
                    .isNotNull("item")
                    .groupBy("item")
                    .orderByAsc("item");
            List<ProtToolTrackList> protToolTrackLists1 = protToolTrackListMapper.selectList(wrapper2);
            for (ProtToolTrackList toolTrackList : protToolTrackLists1) {
                ProtToolTrackListIssueReport protToolTrackListIssueReport = setIssueReportChildrenData(toolTrackList, tableHeader, sonProtId, protToolTrackList.getPartNo());
                children.add(protToolTrackListIssueReport);
            }
            protToolTrackListIssueReportDto.setChildren(children);
            List<String> txList = new ArrayList<>();
            for (int i = 0; i < tableHeader.size(); i++) {
                txList.add("");
            }
            protToolTrackListIssueReportDto.setTxList(txList);
            resultData.add(protToolTrackListIssueReportDto);
        }
        map.put("tableHeader", tableHeader);
        map.put("tableData", resultData);
        return map;
    }

    @Override
    public List<ProtToolTrackList> selectIssueTracking(Long sonProtId, Long issueId) {
        ProtToolTrackList protToolTrackList = protToolTrackListMapper.selectById(issueId);
        QueryWrapper<ProtToolTrackList> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("is_delete", 0)
                .eq("prot_id", sonProtId)
                .eq("part_no", protToolTrackList.getPartNo())
                .eq("item", protToolTrackList.getItem())
                .isNotNull("tx")
                .orderByAsc("tx");
        List<PartNumber> partNumbers = partNumberService.selectByNum(protToolTrackList.getPartNo());
        String partName = getPartName(partNumbers);
        List<ProtToolTrackList> protToolTrackLists = protToolTrackListMapper.selectList(wrapper2);
        for (ProtToolTrackList toolTrackList : protToolTrackLists) {
            toolTrackList.setPartName(partName);
        }
        return protToolTrackLists;
    }

    @Override
    public void reOpenIssue(Long issueId) {
        try {
            ProtToolTrackList protToolTrackList = protToolTrackListMapper.selectById(issueId);
            if (protToolTrackList.getTx() != null && protToolTrackList.getPartNo() != null && protToolTrackList.getProtId() != null) {
                // 根据 项目id，partNo，parentId =0, 获取所有Tx，取Tx最大的那条数据
                QueryWrapper<ProtToolTrackList> wrapper2 = new QueryWrapper<>();
                wrapper2.eq("is_delete", 0)
                        .eq("prot_id", protToolTrackList.getProtId())
                        .eq("part_no", protToolTrackList.getPartNo())
                        .eq("parent_id", 0)
                        .isNotNull("tx")
                        .orderByDesc("tx")
                        .last("limit 2"); // 降序
                List<ProtToolTrackList> protToolTrackLists = protToolTrackListMapper.selectList(wrapper2);
                if (protToolTrackLists.size() > 0) {
                    ProtToolTrackList protToolTrackList1 = protToolTrackLists.get(0);
                    if (protToolTrackList1.getTx().equals(protToolTrackList.getTx())) {
                        protToolTrackList.setStatus("OPEN");
                        protToolTrackList.setUpdateTime(new Date());
                        protToolTrackList.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
                        protToolTrackListMapper.updateById(protToolTrackList);
                    } else {
                        protToolTrackList.setId(GeneratorCodeUtil.generateKey());
                        protToolTrackList.setStatus("OPEN");
                        protToolTrackList.setIsFirst("false");
                        protToolTrackList.setParentId(protToolTrackList1.getId());
                        protToolTrackList.setTx(protToolTrackList1.getTx());
                        protToolTrackList.setUpdateTime(new Date());
                        protToolTrackList.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
                        protToolTrackListMapper.insert(protToolTrackList);
                        // 图片 处理
                        ProtFile protFile = new ProtFile();
                        protFile.setProtId(issueId);
                        protFile.setCategory("P1");
                        List<ProtFile> protFileList = protFileService.selectByProtIdAndCategory(protFile);
                        List<ProtFile> addProtFile = new ArrayList<>();
                        for (ProtFile file : protFileList) {
                            file.setId(GeneratorCodeUtil.generateKey());
                            file.setProtId(protToolTrackList.getId());
                            addProtFile.add(file);
                        }
                        // 批量 添加 Prot file 表 数据
                        if (addProtFile.size() > 0) {
                            protFileMapper.addBatchProtFile(addProtFile);
                        }
                    }
                }
            }
        }catch (Exception e) {
            log.error("re OpenIssue 数据 异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<ProtToolTrackList> selectExcelData(Long sonProtId) {
        // 1. 查询出 项目 part no 最新的 Tx
        List<ProtToolTrackList> lastPartNoAndTx = protToolTrackListMapper.getLastPartNoAndTx(sonProtId);
        List<ProtToolTrackList> results = new ArrayList<>();
        // 2. 遍历查询所有最新的数据
        for (ProtToolTrackList partNoAndTx : lastPartNoAndTx) {
            QueryWrapper<ProtToolTrackList> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", 0)
                    .eq("prot_id", sonProtId)
                    .eq("part_no", partNoAndTx.getPartNo())
                    .eq("tx", partNoAndTx.getTx())
                    .eq("is_delete", 0);
            List<ProtToolTrackList> protToolTrackLists1 = protToolTrackListMapper.selectList(wrapper);
            if (protToolTrackLists1.size() > 0) {
                // PartName 从 part Num 数据库中获取
                List<PartNumber> partNumbers = partNumberService.selectByNum(protToolTrackLists1.get(0).getPartNo());
                protToolTrackLists1.get(0).setPartName(getPartName(partNumbers));
                results.add(protToolTrackLists1.get(0));
                List<ProtToolTrackList> protToolTrackLists = selectByParentId(protToolTrackLists1.get(0).getId(), "");
                results.addAll(protToolTrackLists);
            }
        }
        return results;
    }


    /**
     * setIssueReportChildrenData 设置 IssueReport 的子数据
     * @param toolTrackList ProtToolTrackList
     * @param tableHeader tableHeader
     * @param sonProtId Long
     * @param partNo String
     * @return ProtToolTrackListIssueReport
     */
    public ProtToolTrackListIssueReport setIssueReportChildrenData(ProtToolTrackList toolTrackList, List<String> tableHeader,
                                                                   Long sonProtId, String partNo){
        ProtToolTrackListIssueReport reportIssue = new ProtToolTrackListIssueReport();
        reportIssue.setItem(toolTrackList.getItem());
        List<String> txList = new ArrayList<>();
        for (int i = 0; i < tableHeader.size(); i++) {
            txList.add("-");
        }
        QueryWrapper<ProtToolTrackList> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("is_delete", 0)
                .eq("prot_id", sonProtId)
                .eq("part_no", partNo)
                .eq("item", toolTrackList.getItem())
                .isNotNull("tx")
                .orderByAsc("tx");
        List<ProtToolTrackList> protToolTrackLists = protToolTrackListMapper.selectList(wrapper2);
        for (ProtToolTrackList protToolTrackList : protToolTrackLists) {
            if (protToolTrackList.getTx()<=tableHeader.size()) {
                if (protToolTrackList.getStatus() != null) {
                    if (protToolTrackList.getStatus().equals("OPEN")) {
                        txList.set(protToolTrackList.getTx()-1, "X");
                    }else if (protToolTrackList.getStatus().equals("CLOSE")) {
                        txList.set(protToolTrackList.getTx()-1, "O");
                    }else if (protToolTrackList.getStatus().equals("WAIVE")) {
                        txList.set(protToolTrackList.getTx()-1, "O");
                    }
                }
            }
        }
        reportIssue.setTxList(txList);
        if (protToolTrackLists.size() > 0) {
            ProtToolTrackList protToolTrackList = protToolTrackLists.get(protToolTrackLists.size() - 1);
            reportIssue.setId(protToolTrackList.getId());
            reportIssue.setStatus(protToolTrackList.getStatus());
            reportIssue.setIssueDescription(protToolTrackList.getIssueDescription());
            reportIssue.setRootCause(protToolTrackList.getRootCause());
        }
        return reportIssue;
    }


    /**
     * 通过 ParentId 和 List<String> issuePriority， 如果issuePriority为空则查找所有
     * @param parentId parentId
     * @param protDailyReportQueryRecordDto ProtDailyReportQueryRecordDto
     * @return List<ProtToolTrackList>
     */
    public List<ProtToolTrackList> selectByParentIdAndIssuePriority(Long parentId, ProtDailyReportQueryRecordDto protDailyReportQueryRecordDto) {
        QueryWrapper<ProtToolTrackList> wrapper = new QueryWrapper<>();
        if (protDailyReportQueryRecordDto.getIssuePriority() == null) {
            wrapper.eq("parent_id", parentId)
                    .eq("is_delete", 0)
                    .like("issue_priority", "")
                    .orderByAsc("item");
        } else {
            if (protDailyReportQueryRecordDto.getIssuePriority().size() > 0) {
                wrapper.eq("parent_id", parentId)
                        .eq("is_delete", 0)
                        .in("issue_priority", protDailyReportQueryRecordDto.getIssuePriority())
                        .orderByAsc("item");
            } else{
                wrapper.eq("parent_id", parentId)
                        .eq("is_delete", 0)
                        .like("issue_priority", "")
                        .orderByAsc("item");
            }
        }

        return protToolTrackListMapper.selectList(wrapper);
    }


    /**
     * 获取 这个 类型最新的 item
     * @return 填入的 item
     */
    private Integer getLastItem(ProtToolTrackList protToolTrackList) {
        int item = 1;
        QueryWrapper<ProtToolTrackList> wrapper = new QueryWrapper<>();
        wrapper.ne("parent_id", 0)
                .eq("prot_id", protToolTrackList.getProtId())
                .eq("part_no", protToolTrackList.getPartNo())
                .eq("is_delete", 0)
                .orderByDesc("item"); //  降序
        List<ProtToolTrackList> protToolTrackLists = protToolTrackListMapper.selectList(wrapper);
        if (protToolTrackLists.size() > 0){
            item = protToolTrackLists.get(0).getItem() + 1;
        }
        return item;
    }


    /**
     * 获取 partNum 对应的 partDes TF-REAR COVER ASSY; PND 6.2 WITH TMC MIO HANNA
     * @param partNumbers
     * @return null  //  partDes substring
     */
    private String getPartName(List<PartNumber> partNumbers) {
        if (partNumbers.size() > 0) {
            if (partNumbers.get(0).getPartDescription() != null) {
                return partNumbers.get(0).getPartDescription().split(";")[0].substring(3);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    /**
     * 检查 ToolPlan 是否 已经添加到 ToolTrackList
     * @param protToolPlan  ProtToolPlan
     * @param lastPartNoAndTx  List<ProtToolTrackList>
     * @return  有 true  没有 false
     */
    private boolean checkWhetherToolPlanInToolTrackList(ProtToolPlan protToolPlan, List<ProtToolTrackList> lastPartNoAndTx) {
        boolean result = false;
        for (ProtToolTrackList partNoAndTx : lastPartNoAndTx) {
            if (protToolPlan.getPartNo() != null && partNoAndTx.getPartNo() != null) {
                if (protToolPlan.getPartNo().equals(partNoAndTx.getPartNo())){
                    result = true;
                }
            } else {
                result = true;
            }
        }
        return result;
    }

}
