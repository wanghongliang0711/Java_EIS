package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.dto.ProtStatusDto;
import com.mic.eis.domain.dto.ProtStatusForDashboardDto;
import com.mic.eis.domain.dto.ProtStatusInsertDto;
import com.mic.eis.domain.model.Prot;
import com.mic.eis.domain.model.ProtStatus;
import com.mic.eis.domain.model.ProtStatusAux;
import com.mic.eis.domain.model.UserFollowProt;
import com.mic.eis.mapper.ProtStatusAuxMapper;
import com.mic.eis.mapper.ProtStatusMapper;
import com.mic.eis.service.ProtService;
import com.mic.eis.service.ProtStatusAuxService;
import com.mic.eis.service.ProtStatusService;
import com.mic.eis.service.UserFollowProtService;
import com.mic.eis.util.HttpContextUtil;
import com.mic.eis.utils.GeneratorCodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-11-26 15:03
 */
@Service
public class ProtStatusServiceImpl extends ServiceImpl<ProtStatusMapper, ProtStatus> implements ProtStatusService {

    @Resource
    private ProtStatusMapper protStatusMapper;

    @Resource
    private ProtStatusAuxMapper protStatusAuxMapper;

    @Resource
    private ProtStatusAuxService protStatusAuxService;

    @Resource
    private ProtService protService;

    @Resource
    private UserFollowProtService userFollowProtService;

    @Override
    public HashMap<String, Object> selectAllProtStatus() {
        HashMap<String, Object> map = new HashMap<>();
        // 表头
        List<String> tableHeaderEVTSys = new ArrayList<>();
        List<String> tableHeaderDVTSys = new ArrayList<>();
        List<String> tableHeaderPVTSys = new ArrayList<>();
        // 数据
        List<ProtStatusDto> resultData = new ArrayList<>();
        // 查找所有 用户 关注 的 项目
        Long currUserId = HttpContextUtil.getCurrentUser().getId();
        List<UserFollowProt> userFollowProts = userFollowProtService.selectByUserId(currUserId);
        List<Long> mainProtIdList = new ArrayList<>();
        for (UserFollowProt userFollowProt : userFollowProts) {
            mainProtIdList.add(userFollowProt.getProtId());
        }
        if (mainProtIdList.size() > 0) {
            QueryWrapper<ProtStatus> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", 0)
                    .eq("is_delete", 0)
                    .in("main_prot_id", mainProtIdList)
                    .orderByDesc("main_prot_id"); //  降序
            List<ProtStatus> protStatuses = protStatusMapper.selectList(wrapper);
//            List<ProtStatus> protStatuses = selectByParentId(0L);
            List<Long> protStatusIdList = new ArrayList<>();
            for (ProtStatus protStatus : protStatuses) {
                List<ProtStatus> mainSonProtStatus = new ArrayList<>();
                mainSonProtStatus.add(protStatus);
                // 根据 主项目id 查询对应的所有子项目
                List<Prot> prots = protService.selectProtByParentId(protStatus.getMainProtId());
                List<Long> sonProtIdList = new ArrayList<>();
                for (Prot prot : prots) {
                    sonProtIdList.add(prot.getId());
                }
                if (sonProtIdList.size() > 0) {
                    QueryWrapper<ProtStatus> wrapper1 = new QueryWrapper<>();
                    wrapper1.eq("parent_id", 0)
                            .eq("is_delete", 0)
                            .in("main_prot_id", sonProtIdList);
                    List<ProtStatus> protStatuses1 = protStatusMapper.selectList(wrapper1);
                    if (protStatuses1.size()>0){
                        mainSonProtStatus.addAll(protStatuses1);
                    }
                }
                for (ProtStatus sonProtStatus : mainSonProtStatus) {
                    protStatusIdList.add(sonProtStatus.getId());
                    List<ProtStatusDto> children = new ArrayList<>();
                    ProtStatusDto protStatusDtoSon = new ProtStatusDto();
                    ProtStatusDto protStatusDto = setProtStatusToProtStatusDto(sonProtStatus, true);
                    List<ProtStatus> protStatusSon = selectByParentId(sonProtStatus.getId());
                    if (protStatusSon.size() > 0) {
                        protStatusDtoSon = setProtStatusToProtStatusDto(protStatusSon.get(0), false);
                    }
                    setRevAndSysData(protStatusDto, protStatusDtoSon);
                    children.add(protStatusDtoSon);
                    protStatusDto.setChildren(children);
                    resultData.add(protStatusDto);
                }
            }
            if (protStatusIdList.size() > 0){
                setTableHeader(protStatusIdList, tableHeaderEVTSys, tableHeaderDVTSys, tableHeaderPVTSys);
            }
        }
        map.put("tableHeaderEVTSys", tableHeaderEVTSys);
        map.put("tableHeaderDVTSys", tableHeaderDVTSys);
        map.put("tableHeaderPVTSys", tableHeaderPVTSys);
        map.put("resultData", resultData);
        return map;
    }

    @Override
    public List<ProtStatusForDashboardDto> selectProtStatusForDashboard() {
        List<ProtStatusForDashboardDto> resultList = new ArrayList<>();
        // 查找所有 用户 关注 的 项目
        List<Long> mainProtIdList = new ArrayList<>();
        List<UserFollowProt> userFollowProts = userFollowProtService.selectByUserId(HttpContextUtil.getCurrentUser().getId());
        for (UserFollowProt userFollowProt : userFollowProts) {
            mainProtIdList.add(userFollowProt.getProtId());
        }
        if (mainProtIdList.size() > 0) {
            QueryWrapper<ProtStatus> wrapper = new QueryWrapper<>();
            wrapper.eq("is_delete", 0)
                    .eq("parent_id", 0)
                    .in("main_prot_id", mainProtIdList)
                    .orderByDesc("main_prot_id"); //  降序
            List<ProtStatus> protStatuses = protStatusMapper.selectList(wrapper);
            for (ProtStatus protStatus : protStatuses) {
                List<Long> sonProtIdList = new ArrayList<>();
                List<ProtStatus> mainSonProtStatus = new ArrayList<>();
                mainSonProtStatus.add(protStatus);
                List<Prot> prots = protService.selectProtByParentId(protStatus.getMainProtId());
                for (Prot prot : prots) {
                    sonProtIdList.add(prot.getId());
                }
                if (sonProtIdList.size() > 0) {
                    QueryWrapper<ProtStatus> wrapper1 = new QueryWrapper<>();
                    wrapper1.eq("is_delete", 0)
                            .eq("parent_id", 0)
                            .in("main_prot_id", sonProtIdList);
                    List<ProtStatus> protStatuses1 = protStatusMapper.selectList(wrapper1);
                    if (protStatuses1.size()>0){
                        mainSonProtStatus.addAll(protStatuses1);
                    }
                }
                for (ProtStatus sonProtStatus : mainSonProtStatus) {
                    resultList.add(setProtStatusToProtStatusForDashboardDto(sonProtStatus));
                }
            }
        }
        return resultList;
    }


    @Override
    public ProtStatusDto selectByMainProtId(Long mainProtId) {
        ProtStatusDto protStatusDto = new ProtStatusDto();
        QueryWrapper<ProtStatus> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0)
                .eq("is_delete", 0)
                .eq("main_prot_id", mainProtId); //  降序
        List<ProtStatus> protStatuses = protStatusMapper.selectList(wrapper);
        if (protStatuses.size() > 0) {
            ProtStatus protStatus = protStatuses.get(0);
            ProtStatusDto protStatusDtoSon = new ProtStatusDto();
            protStatusDto = setProtStatusToProtStatusDto(protStatus, true);
            List<ProtStatus> protStatusSon = selectByParentId(protStatus.getId());
            List<ProtStatusDto> children = new ArrayList<>();
            if (protStatusSon.size() > 0) {
                protStatusDtoSon = setProtStatusToProtStatusDto(protStatusSon.get(0), false);
            }
//            setRevAndSysData(protStatusDto, protStatusDtoSon);
            children.add(protStatusDtoSon);
            protStatusDto.setChildren(children);
        }
        return protStatusDto;
    }


    @Override
    public List<ProtStatus> selectByParentId(Long parentId) {
        QueryWrapper<ProtStatus> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId)
                .eq("is_delete", 0);
        return protStatusMapper.selectList(wrapper);
    }

    // 没用到这个接口，不用修改
    @Override
    public ProtStatusInsertDto findByProtStatusId(Long protStatusId) {
        ProtStatusInsertDto protStatusInsertDto = new ProtStatusInsertDto();
        // EVTSys
        List<ProtStatusAux> evtSys = protStatusAuxService.selectByStatusIdAndType(protStatusId, "EVTSys");
        if (evtSys.size() > 0) {
            ProtStatusAux protStatusAux = evtSys.get(evtSys.size() - 1);
            protStatusInsertDto.setEvtSysId(protStatusAux.getId());
            protStatusInsertDto.setEvtSysPlanStart(protStatusAux.getPlanStart());
            protStatusInsertDto.setEvtSysPlanEnd(protStatusAux.getPlanEnd());
            protStatusInsertDto.setEvtSysFactStart(protStatusAux.getFactStart());
            protStatusInsertDto.setEvtSysFactEnd(protStatusAux.getFactEnd());
        }
        // EVTRev
        List<ProtStatusAux> evtRev = protStatusAuxService.selectByStatusIdAndType(protStatusId, "EVTRev");
        if (evtRev.size() > 0) {
            ProtStatusAux protStatusAux = evtRev.get(evtRev.size() - 1);
            protStatusInsertDto.setEvtRevId(protStatusAux.getId());
            protStatusInsertDto.setEvtRevPlanStart(protStatusAux.getPlanStart());
            protStatusInsertDto.setEvtRevFactStart(protStatusAux.getFactStart());
        }
        // DVTSys
        List<ProtStatusAux> dvtSys = protStatusAuxService.selectByStatusIdAndType(protStatusId, "DVTSys");
        if (dvtSys.size() > 0) {
            ProtStatusAux protStatusAux = dvtSys.get(dvtSys.size() - 1);
            protStatusInsertDto.setDvtSysId(protStatusAux.getId());
            protStatusInsertDto.setDvtSysPlanStart(protStatusAux.getPlanStart());
            protStatusInsertDto.setDvtSysPlanEnd(protStatusAux.getPlanEnd());
            protStatusInsertDto.setDvtSysFactStart(protStatusAux.getFactStart());
            protStatusInsertDto.setDvtSysFactEnd(protStatusAux.getFactEnd());
        }
        // DVTRev
        List<ProtStatusAux> dvtRev = protStatusAuxService.selectByStatusIdAndType(protStatusId, "DVTRev");
        if (dvtRev.size() > 0){
            ProtStatusAux protStatusAux = dvtRev.get(dvtRev.size() - 1);
            protStatusInsertDto.setDvtRevId(protStatusAux.getId());
            protStatusInsertDto.setDvtRevPlanStart(protStatusAux.getPlanStart());
            protStatusInsertDto.setDvtRevFactStart(protStatusAux.getFactStart());
        }
        // PVTSys
        List<ProtStatusAux> pvtSys = protStatusAuxService.selectByStatusIdAndType(protStatusId, "PVTSys");
        if (pvtSys.size() > 0){
            ProtStatusAux protStatusAux = pvtSys.get(pvtSys.size() - 1);
            protStatusInsertDto.setPvtSysId(protStatusAux.getId());
            protStatusInsertDto.setPvtSysPlanStart(protStatusAux.getPlanStart());
            protStatusInsertDto.setPvtSysPlanEnd(protStatusAux.getPlanEnd());
            protStatusInsertDto.setPvtSysFactStart(protStatusAux.getFactStart());
            protStatusInsertDto.setPvtSysFactEnd(protStatusAux.getFactEnd());
        }
        // PVTRev
        List<ProtStatusAux> pvtRev = protStatusAuxService.selectByStatusIdAndType(protStatusId, "PVTRev");
        if (pvtRev.size() > 0) {
            ProtStatusAux protStatusAux = pvtRev.get(pvtRev.size() - 1);
            protStatusInsertDto.setPvtRevId(protStatusAux.getId());
            protStatusInsertDto.setPvtRevPlanStart(protStatusAux.getPlanStart());
            protStatusInsertDto.setPvtRevFactStart(protStatusAux.getFactStart());
        }
        return protStatusInsertDto;
    }

    @Override
    public HashMap<String, Object> getEVTOptionsByStatusId(Long protStatusId) {
        HashMap<String, Object> mapResult = new HashMap<>();
        // EVTSys
        List<HashMap<String, Object>> evtSysOptions = new ArrayList<>();
        List<ProtStatusAux> evtSys = protStatusAuxService.selectByStatusIdAndType(protStatusId, "EVTSys");
        for (ProtStatusAux evtSy : evtSys) {
            HashMap<String, Object> evtSysMap = new HashMap<>();
            evtSysMap.put("label", "EVT"+ evtSy.getDataNum() +" System Assembly");
            evtSysMap.put("value", evtSy.getId());
            evtSysOptions.add(evtSysMap);
        }
        mapResult.put("optionsEVTSys", evtSysOptions);
        /// 修改 最好 写6个 接口， 分别获取 提示，
        // 或者 写 3 个接口， 分类 获取 提示， 因为 后面 add 时，需要再次获取 提示
        return mapResult;
    }

    @Override
    public HashMap<String, Object> getDVTOptionsByStatusId(Long protStatusId) {
        HashMap<String, Object> mapResult = new HashMap<>();
        // DVTSys
        List<HashMap<String, Object>> evtSysOptions = new ArrayList<>();
        List<ProtStatusAux> evtSys = protStatusAuxService.selectByStatusIdAndType(protStatusId, "DVTSys");
        for (ProtStatusAux evtSy : evtSys) {
            HashMap<String, Object> evtSysMap = new HashMap<>();
            evtSysMap.put("label", "DVT"+ evtSy.getDataNum() +" System Assembly");
            evtSysMap.put("value", evtSy.getId());
            evtSysOptions.add(evtSysMap);
        }
        mapResult.put("optionsDVTSys", evtSysOptions);
        return mapResult;
    }

    @Override
    public HashMap<String, Object> getPVTOptionsByStatusId(Long protStatusId) {
        HashMap<String, Object> mapResult = new HashMap<>();
        // PVTSys
        List<HashMap<String, Object>> evtSysOptions = new ArrayList<>();
        List<ProtStatusAux> evtSys = protStatusAuxService.selectByStatusIdAndType(protStatusId, "PVTSys");
        for (ProtStatusAux evtSy : evtSys) {
            HashMap<String, Object> evtSysMap = new HashMap<>();
            evtSysMap.put("label", "PVT"+ evtSy.getDataNum() +" System Assembly");
            evtSysMap.put("value", evtSy.getId());
            evtSysOptions.add(evtSysMap);
        }
        mapResult.put("optionsPVTSys", evtSysOptions);
        return mapResult;
    }

    @Override
    public void addProtStatusAux(ProtStatusAux protStatusAux) {
        try {
            ProtStatusAux protStatusAuxInsertSys = new ProtStatusAux();
            protStatusAuxInsertSys.setId(GeneratorCodeUtil.generateKey());
            protStatusAuxInsertSys.setProtStatusId(protStatusAux.getProtStatusId());
            if (protStatusAux.getDataType().equals("EVT")){
                Integer lastDataNum = getLastDataNum(protStatusAux);
                protStatusAuxInsertSys.setDataNum(lastDataNum);
                protStatusAuxInsertSys.setDataType("EVTSys");
                protStatusAuxMapper.insert(protStatusAuxInsertSys);
            } else if (protStatusAux.getDataType().equals("DVT")) {
                Integer lastDataNum = getLastDataNum(protStatusAux);
                protStatusAuxInsertSys.setDataNum(lastDataNum);
                protStatusAuxInsertSys.setDataType("DVTSys");
                protStatusAuxMapper.insert(protStatusAuxInsertSys);
            } else if (protStatusAux.getDataType().equals("PVT")) {
                Integer lastDataNum = getLastDataNum(protStatusAux);
                protStatusAuxInsertSys.setDataNum(lastDataNum);
                protStatusAuxInsertSys.setDataType("PVTSys");
                protStatusAuxMapper.insert(protStatusAuxInsertSys);
            }
        } catch (Exception e) {
            log.error("添加 ProtStatusAux 数据 异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void editProtStatus(ProtStatusInsertDto protStatusInsertDto) {
        try {
            ProtStatus protStatus = new ProtStatus();
            ProtStatus protStatusSon = new ProtStatus();
            // 生成 主 / 子 数据
            protStatus.setId(protStatusInsertDto.getId());
            protStatusSon.setId(protStatusInsertDto.getSonId());
            protStatus.setPhase(protStatusInsertDto.getPhase());
            protStatus.setProtType(protStatusInsertDto.getProtType());
            protStatus.setOwnedBy(protStatusInsertDto.getOwnedBy());
            protStatus.setPeriod(protStatusInsertDto.getPeriod());
            protStatus.setVender(protStatusInsertDto.getVender());
            protStatus.setAchievedMeCost(protStatusInsertDto.getAchievedMeCost());
            protStatus.setMeCostTarget(protStatusInsertDto.getMeCostTarget());
            protStatus.setSection(protStatusInsertDto.getSection());
            protStatus.setTeam(protStatusInsertDto.getTeam());
            protStatus.setTaskLeader(protStatusInsertDto.getTaskLeader());
            protStatus.setEngineers(protStatusInsertDto.getEngineers());
            protStatus.setProtKickoffMeet(protStatusInsertDto.getProtKickoffMeetPlan());
            protStatusSon.setProtKickoffMeet(protStatusInsertDto.getProtKickoffMeetFact());
            protStatus.setIndustrialDesignStart(protStatusInsertDto.getIndustrialDesignPlanStart());
            protStatus.setIndustrialDesignEnd(protStatusInsertDto.getIndustrialDesignPlanEnd());
            protStatusSon.setIndustrialDesignStart(protStatusInsertDto.getIndustrialDesignFactStart());
            protStatusSon.setIndustrialDesignEnd(protStatusInsertDto.getIndustrialDesignFactEnd());
            protStatus.setIdDummySampleStart(protStatusInsertDto.getIdDummySamplePlanStart());
            protStatus.setIdDummySampleEnd(protStatusInsertDto.getIdDummySamplePlanEnd());
            protStatusSon.setIdDummySampleStart(protStatusInsertDto.getIdDummySampleFactStart());
            protStatusSon.setIdDummySampleEnd(protStatusInsertDto.getIdDummySampleFactEnd());
            protStatus.setMechanicalDesignStart(protStatusInsertDto.getMechanicalDesignPlanStart());
            protStatus.setMechanicalDesignEnd(protStatusInsertDto.getMechanicalDesignPlanEnd());
            protStatusSon.setMechanicalDesignStart(protStatusInsertDto.getMechanicalDesignFactStart());
            protStatusSon.setMechanicalDesignEnd(protStatusInsertDto.getMechanicalDesignFactEnd());
            protStatus.setCncSampleStart(protStatusInsertDto.getCncSamplePlanStart());
            protStatus.setCncSampleEnd(protStatusInsertDto.getCncSamplePlanEnd());
            protStatusSon.setCncSampleStart(protStatusInsertDto.getCncSampleFactStart());
            protStatusSon.setCncSampleEnd(protStatusInsertDto.getCncSampleFactEnd());
            protStatus.setSoftToolPartStart(protStatusInsertDto.getSoftToolPartPlanStart());
            protStatus.setSoftToolPartEnd(protStatusInsertDto.getSoftToolPartPlanEnd());
            protStatusSon.setSoftToolPartStart(protStatusInsertDto.getSoftToolPartFactStart());
            protStatusSon.setSoftToolPartEnd(protStatusInsertDto.getSoftToolPartFactEnd());
            protStatus.setTooling(protStatusInsertDto.getToolingPlan());
            protStatusSon.setTooling(protStatusInsertDto.getToolingFact());
            protStatusMapper.updateById(protStatus);
            protStatusMapper.updateById(protStatusSon);
            // prot Status Aux 6 个
            if (protStatusInsertDto.getEvtSysId()!=null) {
                ProtStatusAux protStatusAuxEVTSys = new ProtStatusAux();
                protStatusAuxEVTSys.setId(protStatusInsertDto.getEvtSysId());
                protStatusAuxEVTSys.setPlanStart(protStatusInsertDto.getEvtSysPlanStart());
                protStatusAuxEVTSys.setPlanEnd(protStatusInsertDto.getEvtSysPlanEnd());
                protStatusAuxEVTSys.setFactStart(protStatusInsertDto.getEvtSysFactStart());
                protStatusAuxEVTSys.setFactEnd(protStatusInsertDto.getEvtSysFactEnd());
                protStatusAuxMapper.updateById(protStatusAuxEVTSys);
            }
            if (protStatusInsertDto.getDvtSysId()!=null) {
                ProtStatusAux protStatusAuxDVTSys = new ProtStatusAux();
                protStatusAuxDVTSys.setId(protStatusInsertDto.getDvtSysId());
                protStatusAuxDVTSys.setPlanStart(protStatusInsertDto.getDvtSysPlanStart());
                protStatusAuxDVTSys.setPlanEnd(protStatusInsertDto.getDvtSysPlanEnd());
                protStatusAuxDVTSys.setFactStart(protStatusInsertDto.getDvtSysFactStart());
                protStatusAuxDVTSys.setFactEnd(protStatusInsertDto.getDvtSysFactEnd());
                protStatusAuxMapper.updateById(protStatusAuxDVTSys);
            }
            if (protStatusInsertDto.getPvtSysId()!=null) {
                ProtStatusAux protStatusAuxPVTSys = new ProtStatusAux();
                protStatusAuxPVTSys.setId(protStatusInsertDto.getPvtSysId());
                protStatusAuxPVTSys.setPlanStart(protStatusInsertDto.getPvtSysPlanStart());
                protStatusAuxPVTSys.setPlanEnd(protStatusInsertDto.getPvtSysPlanEnd());
                protStatusAuxPVTSys.setFactStart(protStatusInsertDto.getPvtSysFactStart());
                protStatusAuxPVTSys.setFactEnd(protStatusInsertDto.getPvtSysFactEnd());
                protStatusAuxMapper.updateById(protStatusAuxPVTSys);
            }
        } catch (Exception e) {
            log.error("修改 ProtStatus 和 ProtStatusAux 数据 异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public ProtStatusAux selectProtStatusAuxById(Long id) {
        return protStatusAuxMapper.selectById(id);
    }

    /**
     * EVT DVT PVT 获取某个类型的 data_num 数
     * @param protStatusAux ProtStatusAux
     * @return Integer
     */
    private Integer getLastDataNum(ProtStatusAux protStatusAux) {
        int item = 1;
        QueryWrapper<ProtStatusAux> wrapper = new QueryWrapper<>();
        wrapper.eq("prot_status_id", protStatusAux.getProtStatusId())
                .like("data_type", protStatusAux.getDataType())
                .eq("is_delete", 0)
                .orderByDesc("data_num"); //  降序
        List<ProtStatusAux> protStatusAuxes = protStatusAuxMapper.selectList(wrapper);
        if (protStatusAuxes.size() > 0) {
            item = protStatusAuxes.get(0).getDataNum() + 1;
        }
        return item;
    }


    /**
     * 设置 对象 的  RevAndSys 数据
     * @param protStatusDto ProtStatusDto
     * @param protStatusDtoSon ProtStatusDto
     */
    private void setRevAndSysData(ProtStatusDto protStatusDto, ProtStatusDto protStatusDtoSon){
        // EVTSys
        List<String> evtSysDataPlan = new ArrayList<>();
        List<String> evtSysDataFact = new ArrayList<>();
        List<ProtStatusAux> evtSys = protStatusAuxService.selectByStatusIdAndType(protStatusDto.getId(), "EVTSys");
        setSysData(evtSysDataPlan, evtSysDataFact, evtSys);
        protStatusDto.setEvtSysData(evtSysDataPlan);
        protStatusDtoSon.setEvtSysData(evtSysDataFact);
        // EVTRev
//        List<String> evtRevDataPlan = new ArrayList<>();
//        List<String> evtRevDataFact = new ArrayList<>();
//        List<ProtStatusAux> evtRev = protStatusAuxService.selectByStatusIdAndType(protStatusDto.getId(), "EVTRev");
//        setRevData(evtRevDataPlan, evtRevDataFact, evtRev);
//        protStatusDto.setEvtRevData(evtRevDataPlan);
//        protStatusDtoSon.setEvtRevData(evtRevDataFact);
        // DVTSys
        List<String> dvtSysDataPlan = new ArrayList<>();
        List<String> dvtSysDataFact = new ArrayList<>();
        List<ProtStatusAux> dvtSys = protStatusAuxService.selectByStatusIdAndType(protStatusDto.getId(), "DVTSys");
        setSysData(dvtSysDataPlan, dvtSysDataFact, dvtSys);
        protStatusDto.setDvtSysData(dvtSysDataPlan);
        protStatusDtoSon.setDvtSysData(dvtSysDataFact);
        // DVTRev
//        List<String> dvtRevDataPlan = new ArrayList<>();
//        List<String> dvtRevDataFact = new ArrayList<>();
//        List<ProtStatusAux> dvtRev = protStatusAuxService.selectByStatusIdAndType(protStatusDto.getId(), "DVTRev");
//        setRevData(dvtRevDataPlan, dvtRevDataFact, dvtRev);
//        protStatusDto.setDvtRevData(dvtRevDataPlan);
//        protStatusDtoSon.setDvtRevData(dvtRevDataFact);
        // PVTSys
        List<String> pvtSysDataPlan = new ArrayList<>();
        List<String> pvtSysDataFact = new ArrayList<>();
        List<ProtStatusAux> pvtSys = protStatusAuxService.selectByStatusIdAndType(protStatusDto.getId(), "PVTSys");
        setSysData(pvtSysDataPlan, pvtSysDataFact, pvtSys);
        protStatusDto.setPvtSysData(pvtSysDataPlan);
        protStatusDtoSon.setPvtSysData(pvtSysDataFact);

        // PVTRev
//        List<String> pvtRevDataPlan = new ArrayList<>();
//        List<String> pvtRevDataFact = new ArrayList<>();
//        List<ProtStatusAux> pvtRev = protStatusAuxService.selectByStatusIdAndType(protStatusDto.getId(), "PVTRev");
//        setRevData(pvtRevDataPlan, pvtRevDataFact, pvtRev);
//        protStatusDto.setPvtRevData(pvtRevDataPlan);
//        protStatusDtoSon.setPvtRevData(pvtRevDataFact);
    }


    /**
     * 制作  Rev 类型 数据
     * @param revDataPlan List<String> revDataPlan
     * @param revDataFact List<String> revDataFact
     * @param revDatas List<ProtStatusAux>
     */
    private void setRevData(List<String> revDataPlan, List<String> revDataFact, List<ProtStatusAux> revDatas){
        for (ProtStatusAux revData : revDatas) {
            if (revData.getPlanStart()!=null) {
                revDataPlan.add(revData.getPlanStart());
            } else {
                revDataPlan.add("");
            }
            if (revData.getFactStart()!=null){
                revDataFact.add(revData.getFactStart());
            } else {
                revDataFact.add("");
            }
        }

    }


    /**
     * 制作  Sys 类型 数据
     * @param sysDataPlan List<String>
     * @param sysDataFact List<String>
     * @param sysDatas List<ProtStatusAux>
     */
    private void setSysData(List<String> sysDataPlan, List<String> sysDataFact, List<ProtStatusAux> sysDatas) {
        for (ProtStatusAux sysData : sysDatas) {
            if (sysData.getPlanStart()!=null){
                sysDataPlan.add(sysData.getPlanStart());
            } else {
                sysDataPlan.add("");
            }
            if (sysData.getPlanEnd()!=null){
                sysDataPlan.add(sysData.getPlanEnd());
            } else {
                sysDataPlan.add("");
            }
            if (sysData.getFactStart()!=null){
                sysDataFact.add(sysData.getFactStart());
            } else {
                sysDataFact.add("");
            }
            if (sysData.getFactEnd()!=null){
                sysDataFact.add(sysData.getFactEnd());
            } else {
                sysDataFact.add("");
            }
        }
    }

    /**
     * 生成表头
     * @param protStatusIdList List<Long> protStatusIdList
     * @param tableHeaderEVTSys List<String> tableHeaderEVTSys
     * @param tableHeaderDVTSys List<String> tableHeaderDVTSys
     * @param tableHeaderPVTSys List<String> tableHeaderPVTSys
     */
    private void setTableHeader(List<Long> protStatusIdList, List<String> tableHeaderEVTSys,
                                       List<String> tableHeaderDVTSys,
                                       List<String> tableHeaderPVTSys){
        List<ProtStatusAux> maxDataNumAndDataType = protStatusAuxMapper.getMaxDataNumAndDataType(protStatusIdList);
        for (ProtStatusAux protStatusAux : maxDataNumAndDataType) {
            // 1. EVT System Assembly 找最大值
            if (protStatusAux.getDataType().equals("EVTSys")) {
                if (protStatusAux.getDataNum() != null) {
                    for (int i1=0;i1<protStatusAux.getDataNum();i1++){
                        tableHeaderEVTSys.add("EVT" + (i1 + 1) + " Start");
                        tableHeaderEVTSys.add("EVT" + (i1 + 1) + " End");
                    }
                }
            }
            // 3. DVT System Assembly 找最大值
            else if (protStatusAux.getDataType().equals("DVTSys")) {
                if (protStatusAux.getDataNum() != null) {
                    for (int i1=0;i1<protStatusAux.getDataNum();i1++){
                        tableHeaderDVTSys.add("DVT"+ (i1 + 1) +" Start");
                        tableHeaderDVTSys.add("DVT"+ (i1 + 1) +" End");
                    }
                }
            }
            // 5. PVT System Assembly  找最大值
            else if (protStatusAux.getDataType().equals("PVTSys")) {
                if (protStatusAux.getDataNum() != null) {
                    for (int i1=0;i1<protStatusAux.getDataNum();i1++){
                        tableHeaderPVTSys.add("PVT"+ (i1 + 1) +" Start");
                        tableHeaderPVTSys.add("PVT"+ (i1 + 1) +" End");
                    }
                }
            }
        }
    }


    /**
     * ProtStatus 转 ProtStatusDto
     * @param protStatus ProtStatus
     * @param isSetProt ProtStatusDto
     * @return ProtStatusDto
     */
    private ProtStatusDto setProtStatusToProtStatusDto(ProtStatus protStatus, boolean isSetProt){
        ProtStatusDto protStatusDto = new ProtStatusDto();
        protStatusDto.setId(protStatus.getId());
        protStatusDto.setMainProtId(protStatus.getMainProtId());
        protStatusDto.setParentId(protStatus.getParentId());
        protStatusDto.setPhase(protStatus.getPhase());
        protStatusDto.setOwnedBy(protStatus.getOwnedBy());
        protStatusDto.setProtType(protStatus.getProtType());
        protStatusDto.setPeriod(protStatus.getPeriod());
        protStatusDto.setAchievedMeCost(protStatus.getAchievedMeCost());
        protStatusDto.setMeCostTarget(protStatus.getMeCostTarget());
        protStatusDto.setVender(protStatus.getVender());
        protStatusDto.setSection(protStatus.getSection());
        protStatusDto.setTeam(protStatus.getTeam());
        protStatusDto.setTaskLeader(protStatus.getTaskLeader());
        protStatusDto.setEngineers(protStatus.getEngineers());
        protStatusDto.setPlanOrFact(protStatus.getPlanOrFact());
        protStatusDto.setProtKickoffMeet(protStatus.getProtKickoffMeet());
        protStatusDto.setIndustrialDesignStart(protStatus.getIndustrialDesignStart());
        protStatusDto.setIndustrialDesignEnd(protStatus.getIndustrialDesignEnd());
        protStatusDto.setIdDummySampleStart(protStatus.getIdDummySampleStart());
        protStatusDto.setIdDummySampleEnd(protStatus.getIdDummySampleEnd());
        protStatusDto.setMechanicalDesignStart(protStatus.getMechanicalDesignStart());
        protStatusDto.setMechanicalDesignEnd(protStatus.getMechanicalDesignEnd());
        protStatusDto.setCncSampleStart(protStatus.getCncSampleStart());
        protStatusDto.setCncSampleEnd(protStatus.getCncSampleEnd());
        protStatusDto.setSoftToolPartStart(protStatus.getSoftToolPartStart());
        protStatusDto.setSoftToolPartEnd(protStatus.getSoftToolPartEnd());
        protStatusDto.setTooling(protStatus.getTooling());
        if (isSetProt) {
            Prot prot = protService.selectProtByID(protStatus.getMainProtId());
            if (prot.getParentId()==0) {
                protStatusDto.setProtCode(prot.getCode());
                protStatusDto.setProtDes(prot.getName());
            } else {
                protStatusDto.setSonProtDes(prot.getName());
                Prot prot1 = protService.selectProtByID(prot.getParentId());
                protStatusDto.setProtCode(prot1.getCode());
                protStatusDto.setProtDes(prot1.getName());
            }

        }
        return protStatusDto;
    }


    /**
     * ProtStatus 转 ProtStatusForDashboardDto
     * @param sonProtStatus ProtStatus
     * @return ProtStatusForDashboardDto
     */
    private ProtStatusForDashboardDto setProtStatusToProtStatusForDashboardDto(ProtStatus sonProtStatus) {
        ProtStatusForDashboardDto protStatusForDashboardDto = new ProtStatusForDashboardDto();
        protStatusForDashboardDto.setId(sonProtStatus.getId());
        protStatusForDashboardDto.setMainProtId(sonProtStatus.getMainProtId());
        List<ProtStatus> protStatusSonList = selectByParentId(sonProtStatus.getId());
        if (protStatusSonList.size() > 0) {
            ProtStatus protStatusSon = protStatusSonList.get(0);
            protStatusForDashboardDto.setToolStart(protStatusSon.getTooling());
        }
        Prot prot = protService.selectProtByID(sonProtStatus.getMainProtId());
        if (prot.getParentId()==0) {
            protStatusForDashboardDto.setProtCode(prot.getCode());
            protStatusForDashboardDto.setProtDes(prot.getName());
        } else {
            protStatusForDashboardDto.setSonProtDes(prot.getName());
            Prot prot1 = protService.selectProtByID(prot.getParentId());
            protStatusForDashboardDto.setProtDes(prot1.getName());
            protStatusForDashboardDto.setProtCode(prot1.getCode());
        }
        String maxDVT = "";
        // DVT 实际的结束时间 最大值
        QueryWrapper<ProtStatusAux> wrapper = new QueryWrapper<>();
        wrapper.eq("prot_status_id", sonProtStatus.getId())
                .eq("data_type", "DVTSys")
                .isNotNull("fact_end");
        List<ProtStatusAux> protStatusAuxes = protStatusAuxMapper.selectList(wrapper);
        for (ProtStatusAux protStatusAux : protStatusAuxes) {
            if (maxDVT.compareTo(protStatusAux.getFactEnd())<0) {
                maxDVT = protStatusAux.getFactEnd();
            }
        }
        protStatusForDashboardDto.setDvt(maxDVT);
        // PVT 实际的结束时间 最大值
        QueryWrapper<ProtStatusAux> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("data_type", "PVTSys")
                .eq("prot_status_id", sonProtStatus.getId())
                .isNotNull("fact_end");
        List<ProtStatusAux> protStatusAuxesPVT = protStatusAuxMapper.selectList(wrapper1);
        String maxPVT = "";
        for (ProtStatusAux protStatusAux : protStatusAuxesPVT) {
            if (maxPVT.compareTo(protStatusAux.getFactEnd())<0) {
                maxPVT = protStatusAux.getFactEnd();
            }
        }
        protStatusForDashboardDto.setPvt(maxPVT);
        return protStatusForDashboardDto;
    }
}
