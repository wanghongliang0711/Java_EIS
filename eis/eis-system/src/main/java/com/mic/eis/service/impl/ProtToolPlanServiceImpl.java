package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.dto.ProtToolPlanDto;
import com.mic.eis.domain.model.*;
import com.mic.eis.domain.vo.ProtFileVerQueryVo;
import com.mic.eis.domain.vo.ProtMePartListQueryVo;
import com.mic.eis.mapper.*;
import com.mic.eis.service.*;
import com.mic.eis.util.HttpContextUtil;
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
 * @date 2020-10-27 10:31
 */
@Service
public class ProtToolPlanServiceImpl extends ServiceImpl<ProtToolPlanMapper, ProtToolPlan> implements ProtToolPlanService {

    @Resource
    private ProtMePartListMapper protMePartListMapper;

    @Resource
    private ProtToolPlanAuxService protToolPlanAuxService;

    @Resource
    private ProtToolPlanAuxMapper protToolPlanAuxMapper;

    @Resource
    private ProtFileService protFileService;

    @Resource
    private ProtFileMapper protFileMapper;

    @Resource
    private ProtFileVerService protFileVerService;

    @Resource
    private ProtToolPlanMapper protToolPlanMapper;

    @Resource
    private PartNumberService partNumberService;

    @Resource
    private MatelClassMapper matelClassMapper;

    @Resource
    private ProtToolTrackListService protToolTrackListService;

    @Override
    public List<ProtToolPlanDto> selectByProtIdAndVersion(ProtMePartListQueryVo protMePartListQueryVo) {
        List<ProtToolPlanDto> results = new ArrayList<>();
        QueryWrapper<ProtToolPlan> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("is_delete", 0)
                .eq("prot_id", protMePartListQueryVo.getProtId())
                .eq("parent_id", 0)
                .eq("file_ver", protMePartListQueryVo.getFileVer())
                .orderByAsc("no");
        List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper1);
        for (ProtToolPlan protToolPlan : protToolPlans) {
            List<ProtToolPlanDto> children = new ArrayList<>();
            ProtToolPlanDto protToolPlanDto = setProtToolPlanToProtToolPlanDto(protToolPlan, true);
            List<ProtToolPlan> protToolPlans1 = selectByParentId(protToolPlanDto.getId());
            for (ProtToolPlan toolPlan : protToolPlans1) {
                ProtToolPlanDto protToolPlanDto1 = setProtToolPlanToProtToolPlanDto(toolPlan, false);
                children.add(protToolPlanDto1);
            }
            protToolPlanDto.setChildren(children);
            results.add(protToolPlanDto);
        }
        return results;
    }

    @Override
    public void backBeforeVersion(ProtMePartListQueryVo protMePartListQueryVo) {
        try {
            List<ProtToolPlan> addProtToolPlanDbList = new ArrayList<>();
            List<ProtFile> addProtFileDbList = new ArrayList<>();
            List<ProtToolPlanAux> addProtToolPlanAuxDbList = new ArrayList<>();
            QueryWrapper<ProtToolPlan> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("is_delete", 0)
                    .eq("prot_id", protMePartListQueryVo.getProtId())
                    .eq("parent_id", 0)
                    .eq("file_ver", protMePartListQueryVo.getFileVer())
                    .orderByAsc("no");
            List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper1);
            for (ProtToolPlan protToolPlan : protToolPlans) {
                protToolTrackListService.updateToolTrackListPartNumForOpen(protToolPlan.getPartNo(), protToolPlan.getProtId());
                ProtFile protFile1 = new ProtFile();
                protFile1.setProtId(protToolPlan.getId());
                protFile1.setCategory("P2");
                List<ProtFile> protFileList = protFileService.selectByProtIdAndCategory(protFile1);
                List<ProtToolPlanAux> protToolPlanAuxes = protToolPlanAuxService.selectByDataId(protToolPlan.getId());
                List<ProtToolPlan> protToolPlansSon = selectByParentId(protToolPlan.getId());
                protToolPlan.setId(GeneratorCodeUtil.generateKey());
                // ?????? ????????? ??????
                for (ProtToolPlanAux protToolPlanAux : protToolPlanAuxes) {
                    protToolPlanAux.setDataId(protToolPlan.getId());
                    protToolPlanAux.setId(GeneratorCodeUtil.generateKey());
                    addProtToolPlanAuxDbList.add(protToolPlanAux);
                }
                // ?????? ??????
                for (ProtFile protFile : protFileList) {
                    protFile.setId(GeneratorCodeUtil.generateKey());
                    protFile.setProtId(protToolPlan.getId());
                    addProtFileDbList.add(protFile);
                }
                // ??? ??????
                for (ProtToolPlan toolPlan : protToolPlansSon) {
                    ProtFile protFile = new ProtFile();
                    protFile.setCategory("P2");
                    protFile.setProtId(toolPlan.getId());
                    List<ProtFile> protFileListSon = protFileService.selectByProtIdAndCategory(protFile);
                    List<ProtToolPlanAux> protToolPlanAuxesSon = protToolPlanAuxService.selectByDataId(toolPlan.getId());
                    toolPlan.setId(GeneratorCodeUtil.generateKey());
                    // ?????? ??????
                    for (ProtFile fileSon : protFileListSon) {
                        fileSon.setId(GeneratorCodeUtil.generateKey());
                        fileSon.setProtId(toolPlan.getId());
                        addProtFileDbList.add(fileSon);
                    }
                    // ?????? ????????? ??????
                    for (ProtToolPlanAux protToolPlanAux : protToolPlanAuxesSon) {
                        protToolPlanAux.setDataId(toolPlan.getId());
                        protToolPlanAux.setId(GeneratorCodeUtil.generateKey());
                        addProtToolPlanAuxDbList.add(protToolPlanAux);
                    }
                    toolPlan.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
                    toolPlan.setUpdateTime(new Date());
                    toolPlan.setFileVer("now");
                    toolPlan.setParentId(protToolPlan.getId());
                    addProtToolPlanDbList.add(toolPlan);
                }
                protToolPlan.setFileVer("now");
                protToolPlan.setUpdateTime(new Date());
                protToolPlan.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
                addProtToolPlanDbList.add(protToolPlan);
            }
            // ?????? protId  fileVer ?????? ???????????????
            HashMap<String, Object> map = new HashMap<>();
            map.put("prot_id", protMePartListQueryVo.getProtId());
            map.put("file_ver", "now");
            List<ProtToolPlan> protToolPlansDelete = protToolPlanMapper.selectByMap(map);
            List<Long> deleteProtFile = new ArrayList<>();
            List<Long> deleteToolPlanAux = new ArrayList<>();
            for (ProtToolPlan protToolPlan : protToolPlansDelete) {
                deleteProtFile.add(protToolPlan.getId());
                deleteToolPlanAux.add(protToolPlan.getId());
            }
            if (deleteProtFile.size() > 0) {
                protFileMapper.deleteProtFileByProtIds(deleteProtFile);
            }
            if (deleteToolPlanAux.size() > 0) {
                protToolPlanAuxMapper.deleteProtToolPlanAuxByDataIds(deleteToolPlanAux);
            }
            // ProtToolPlan ?????? protId  fileVer ??????  ????????????
            protToolPlanMapper.deleteProtToolPlanByProtIdAndVer(protMePartListQueryVo.getProtId(), "now");
            // ????????? ??????????????? now  ??? ?????? ??? ?????????
            // ?????? ProtId ???????????? ????????????
//            protFileService.deleteProtFileByProtId(protToolPlan.getId());
            // ?????? ?????? dataId ??????   ????????????
//            protToolPlanAuxMapper.deleteProtToolPlanAuxByDataId(protToolPlan.getId());
            if (addProtToolPlanAuxDbList.size() > 0){
                protToolPlanAuxMapper.addBatchProtToolPlanAux(addProtToolPlanAuxDbList);
            }
            if (addProtFileDbList.size() > 0){
                protFileMapper.addBatchProtFile(addProtFileDbList);
            }
            if (addProtToolPlanDbList.size() > 0){
                protToolPlanMapper.addBatchProtToolPlan(addProtToolPlanDbList);
            }
        } catch (Exception e){
            log.error("??? tool plan ??????????????????????????????");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void updatePrNum(ProtToolPlan protToolPlan) {
        try {
            UpdateWrapper<ProtToolPlan> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("prot_id", protToolPlan.getProtId())
                    .eq("file_ver", "now");
            ProtToolPlan protToolPlan1 = new ProtToolPlan();
            protToolPlan1.setPrNumber(protToolPlan.getPrNumber());
            protToolPlan1.setUpdateTime(new Date());
            protToolPlan1.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
            protToolPlanMapper.update(protToolPlan1, updateWrapper);
        }catch (Exception e) {
            log.error("?????? update Pr Num??????");
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public HashMap<String, Object> addNewVersion(ProtFileVer protFileVer) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            // ?????? ??? protFileVer ?????????????????????
            ProtFileVerQueryVo protFileVerQueryVo = new ProtFileVerQueryVo();
            protFileVerQueryVo.setFileVer(protFileVer.getFileVer());
            protFileVerQueryVo.setProtId(protFileVer.getProtId());
            protFileVerQueryVo.setCategory(protFileVer.getCategory());
            List<ProtFileVer> protFileVers = protFileVerService.selectByProtIdAndCategoryAndVer(protFileVerQueryVo);
            if(protFileVers.size() > 0) {
                map.put("msgFail", "????????????????????????????????????????????? ???");
                return map;
            }
            List<ProtToolPlan> addProtToolPlanDbList = new ArrayList<>();
            List<ProtToolPlanAux> addProtToolPlanAuxDbList = new ArrayList<>();
            List<ProtFile> addProtFileDbList = new ArrayList<>();
            QueryWrapper<ProtToolPlan> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("is_delete", 0)
                    .eq("prot_id", protFileVer.getProtId())
                    .eq("parent_id", 0)
                    .eq("file_ver", "now")
                    .orderByAsc("no");
            List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper1);
            for (ProtToolPlan protToolPlan : protToolPlans) {
                ProtFile protFile1 = new ProtFile();
                protFile1.setCategory("P2");
                protFile1.setProtId(protToolPlan.getId());
                List<ProtFile> protFileList = protFileService.selectByProtIdAndCategory(protFile1);
                List<ProtToolPlanAux> protToolPlanAuxes = protToolPlanAuxService.selectByDataId(protToolPlan.getId());
                List<ProtToolPlan> protToolPlans1 = selectByParentId(protToolPlan.getId());
                protToolPlan.setId(GeneratorCodeUtil.generateKey());
                for (ProtToolPlan toolPlan : protToolPlans1) {
                    ProtFile protFile = new ProtFile();
                    protFile.setProtId(toolPlan.getId());
                    protFile.setCategory("P2");
                    List<ProtFile> protFileListSon = protFileService.selectByProtIdAndCategory(protFile);
                    List<ProtToolPlanAux> protToolPlanAuxesSon = protToolPlanAuxService.selectByDataId(toolPlan.getId());
                    toolPlan.setId(GeneratorCodeUtil.generateKey());
                    // ??????
                    for (ProtFile fileSon : protFileListSon) {
                        fileSon.setId(GeneratorCodeUtil.generateKey());
                        fileSon.setProtId(toolPlan.getId());
                        addProtFileDbList.add(fileSon);
                    }
                    // ????????? ??????
                    for (ProtToolPlanAux protToolPlanAux : protToolPlanAuxesSon) {
                        protToolPlanAux.setId(GeneratorCodeUtil.generateKey());
                        protToolPlanAux.setDataId(toolPlan.getId());
                        addProtToolPlanAuxDbList.add(protToolPlanAux);
                    }
                    toolPlan.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
                    toolPlan.setUpdateTime(new Date());
                    toolPlan.setFileVer(protFileVer.getFileVer());
                    toolPlan.setParentId(protToolPlan.getId());
                    addProtToolPlanDbList.add(toolPlan);
                }
                // ????????? ??????
                for (ProtToolPlanAux protToolPlanAux1 : protToolPlanAuxes) {
                    protToolPlanAux1.setId(GeneratorCodeUtil.generateKey());
                    protToolPlanAux1.setDataId(protToolPlan.getId());
                    addProtToolPlanAuxDbList.add(protToolPlanAux1);
                }
                // ??????
                for (ProtFile protFile : protFileList) {
                    protFile.setId(GeneratorCodeUtil.generateKey());
                    protFile.setProtId(protToolPlan.getId());
                    addProtFileDbList.add(protFile);
                }
                protToolPlan.setFileVer(protFileVer.getFileVer());
                protToolPlan.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
                protToolPlan.setUpdateTime(new Date());
                addProtToolPlanDbList.add(protToolPlan);
            }
            if (addProtToolPlanAuxDbList.size() > 0){
                protToolPlanAuxMapper.addBatchProtToolPlanAux(addProtToolPlanAuxDbList);
            }
            if (addProtFileDbList.size() > 0){
                protFileMapper.addBatchProtFile(addProtFileDbList);
            }
            if (addProtToolPlanDbList.size() > 0){
                protToolPlanMapper.addBatchProtToolPlan(addProtToolPlanDbList);
            }
            protFileVerService.addProtFileVer(protFileVer);
            return map;
        } catch (Exception e) {
            log.error("?????? tool plan ????????? ??????");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<ProtMePartList> selectByMePartList(Long sonProtId) {
        QueryWrapper<ProtMePartList> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("is_delete", 0)
                .eq("prot_id", sonProtId)
                .eq("file_ver", "now")
                .select("id", "no", "prot_id", "bom_level", "file_ver", "part_number", "part_description", "meterial")
                .isNotNull("part_number")
                .orderByAsc("no");
        List<ProtMePartList> protMePartLists = protMePartListMapper.selectList(wrapper1);
        List<ProtMePartList> results = new ArrayList<>();
        QueryWrapper<ProtToolPlan> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("is_delete", 0)
                .eq("prot_id", sonProtId)
                .eq("file_ver", "now")
                .eq("parent_id", 0)
                .select("id", "prot_id", "file_ver", "part_no", "parent_id");
        List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper2);
        for (ProtMePartList protMePartList : protMePartLists) {
            if (!isExistToolPlan(protMePartList, protToolPlans)){
                ProtMePartList mePartList = new ProtMePartList();
                mePartList.setId(protMePartList.getId());
                mePartList.setProtId(protMePartList.getProtId());
                mePartList.setFileVer(protMePartList.getFileVer());
                mePartList.setMeterial(protMePartList.getMeterial());
                mePartList.setPartNumber(protMePartList.getPartNumber());
                mePartList.setBomLevel(protMePartList.getBomLevel());
                mePartList.setNo(protMePartList.getNo());
                List<PartNumber> partNumbers = partNumberService.selectByNum(mePartList.getPartNumber());
                if (partNumbers.size() > 0){
                    mePartList.setPartDescription(partNumbers.get(0).getPartDescription());
                }
                results.add(mePartList);
            }
        }
        return results;
    }

    @Override
    public void batchAddByMePartList(Long sonProtId, List<String> mePartListPartNums) {
        try {
            List<ProtToolPlan> addDbList = new ArrayList<>();
            Integer lastNowNo = getLastNowNo(sonProtId);
            for (String mePartListPartNum : mePartListPartNums) {
                protToolTrackListService.updateToolTrackListPartNumForOpen(mePartListPartNum, sonProtId);
                ProtToolPlan protToolPlan = new ProtToolPlan();
                protToolPlan.setId(GeneratorCodeUtil.generateKey());
                protToolPlan.setParentId(0L);
                protToolPlan.setFileVer("now");
                protToolPlan.setProtId(sonProtId);
                protToolPlan.setPartNo(mePartListPartNum);
                protToolPlan.setNo(lastNowNo); // ???????????? now ?????? ?????????No
                protToolPlan.setUpdateTime(new Date());
                protToolPlan.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
                lastNowNo = lastNowNo + 1;
                addDbList.add(protToolPlan);
            }
            if (addDbList.size() > 0) {
                protToolPlanMapper.addBatchProtToolPlan(addDbList);
            }
        } catch (Exception e) {
            log.error("???????????? Tool plan ?????? ??????");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public HashMap<String, Object> addToolPlanSon(ProtToolPlanDto protToolPlanDto) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            if (checkMaterial(protToolPlanDto)) {
                ProtToolPlan protToolPlan = setProtToolPlanDtoToProtToolPlan(protToolPlanDto, true);
                protToolPlanMapper.insert(protToolPlan);
                List<ProtToolPlanAux> protToolPlanAux = getProtToolPlanAux(protToolPlanDto, protToolPlan.getId());
                if (protToolPlanAux.size() > 0){
                    protToolPlanAuxMapper.addBatchProtToolPlanAux(protToolPlanAux);
                }
                map.put("msgSuccess", "????????????");
            } else {
                map.put("msgFail", "Material ??????????????????");
            }
            return map;
        }catch (Exception e){
            log.error("?????? tool plan ??????");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public HashMap<String, Object> editProtToolPlan(ProtToolPlanDto protToolPlanDto) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            if (checkMaterial(protToolPlanDto)) {
                ProtToolPlan protToolPlan = setProtToolPlanDtoToProtToolPlan(protToolPlanDto, false);
                protToolPlanMapper.updateById(protToolPlan);
                List<ProtToolPlanAux> protToolPlanAux = getProtToolPlanAux(protToolPlanDto, protToolPlan.getId());
                protToolPlanAuxMapper.deleteProtToolPlanAuxByDataId(protToolPlan.getId());
                if (protToolPlanAux.size() > 0){
                    protToolPlanAuxMapper.addBatchProtToolPlanAux(protToolPlanAux);
                }
                map.put("msgSuccess", "????????????");
            } else {
                map.put("msgFail", "Material ??????????????????");
            }
            return map;
        } catch (Exception e){
            log.error("??????ProtToolPlan??????");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<HashMap<String, String>> selectAllVersion() {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        QueryWrapper<ProtToolPlan> wrapper = new QueryWrapper<>();
        wrapper.select("version")
                .eq("is_delete", 0)
                .isNotNull("version")
                .groupBy("version");
        List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper);
        for (ProtToolPlan protToolPlan : protToolPlans) {
            HashMap<String, String> map = new HashMap<>();
            if (protToolPlan.getVersion() != null) {
                map.put("value", protToolPlan.getVersion());
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public void deleteProtToolPlanById(Long id, Long parentId) {
        try {
            // ??????id ?????????????????????
            if (parentId == 0){
                ProtToolPlan protToolPlan = protToolPlanMapper.selectById(id);
                // ??? Tooling track list ???????????? part no ??????issue ???????????? closed??? ?????? remark
                if (protToolPlan.getPartNo() != null && protToolPlan.getProtId() != null) {
                    protToolTrackListService.updateToolTrackListPartNumForClosed(protToolPlan.getPartNo(), protToolPlan.getProtId());
                }
            }
            // ?????? deleteProtToolPlanById
            protToolPlanMapper.deleteProtToolPlanById(id);
            // ?????? picture
            protFileService.deleteProtFileByProtId(id);
            // ?????? ProtToolPlanAux
            protToolPlanAuxMapper.deleteProtToolPlanAuxByDataId(id);
        }catch (Exception e){
            log.error("?????? deleteProtToolPlanById ??????");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public HashMap<String, String> selectLastTimeUser(Long sonProtId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("lastTime", "");
        map.put("lastUser", "");
        QueryWrapper<ProtToolPlan> wrapper = new QueryWrapper<>();
        wrapper.select("update_user", "update_time")
                .eq("prot_id", sonProtId)
                .orderByDesc("update_time")
                .last("limit 2");
        List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper);
        if (protToolPlans.size() > 0) {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("lastTime", date.format(protToolPlans.get(0).getUpdateTime()));
            map.put("lastUser", protToolPlans.get(0).getUpdateUser());
        }
        return map;
    }

    @Override
    public HashMap<String, String> selectLastTimeUserByVer(Long sonProtId, String fileVer) {
        HashMap<String, String> map = new HashMap<>();
        map.put("lastTime", "");
        map.put("lastUser", "");
        QueryWrapper<ProtToolPlan> wrapper = new QueryWrapper<>();
        wrapper.select("update_user", "update_time")
                .eq("prot_id", sonProtId)
                .eq("file_ver", fileVer)
                .orderByDesc("update_time")
                .last("limit 2");
        List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper);
        if (protToolPlans.size() > 0) {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("lastUser", protToolPlans.get(0).getUpdateUser());
            map.put("lastTime", date.format(protToolPlans.get(0).getUpdateTime()));
        }
        return map;
    }

    @Override
    public List<HashMap<String, String>> selectAllMaterial() {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        QueryWrapper<MatelClass> wrapper = new QueryWrapper<>();
        wrapper.select("brand")
                .eq("is_delete", 0)
                .eq("code", "3")
                .isNotNull("brand")
                .groupBy("brand");
        List<MatelClass> matelClasses = matelClassMapper.selectList(wrapper);
        for (MatelClass matelClass : matelClasses) {
            HashMap<String, String> map = new HashMap<>();
            if (matelClass.getBrand() != null) {
                map.put("value", matelClass.getBrand());
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public List<HashMap<String, String>> selectAllColorNo() {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        QueryWrapper<ProtToolPlan> wrapper = new QueryWrapper<>();
        wrapper.select("color_no")
                .eq("is_delete", 0)
                .isNotNull("color_no")
                .groupBy("color_no");
        List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper);
        for (ProtToolPlan protToolPlan : protToolPlans) {
            HashMap<String, String> map = new HashMap<>();
            if (protToolPlan.getColorNo() != null){
                map.put("value", protToolPlan.getColorNo());
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public List<HashMap<String, String>> selectAllPaintingColorNo() {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        QueryWrapper<ProtToolPlan> wrapper = new QueryWrapper<>();
        wrapper.select("painting_color_no")
                .eq("is_delete", 0)
                .isNotNull("painting_color_no")
                .groupBy("painting_color_no");
        List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper);
        for (ProtToolPlan protToolPlan : protToolPlans) {
            HashMap<String, String> map = new HashMap<>();
            if (protToolPlan.getPaintingColorNo() != null) {
                map.put("value", protToolPlan.getPaintingColorNo());
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public List<HashMap<String, String>> selectAllPrintingColorNo() {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        QueryWrapper<ProtToolPlan> wrapper = new QueryWrapper<>();
        wrapper.select("printing_color_no")
                .eq("is_delete", 0)
                .isNotNull("printing_color_no")
                .groupBy("printing_color_no");
        List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper);
        for (ProtToolPlan protToolPlan : protToolPlans) {
            HashMap<String, String> map = new HashMap<>();
            if (protToolPlan.getPrintingColorNo() != null) {
                map.put("value", protToolPlan.getPrintingColorNo());
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public List<HashMap<String, String>> selectAllCoatingCategory() {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        QueryWrapper<ProtToolPlan> wrapper = new QueryWrapper<>();
        wrapper.select("coating_category")
                .eq("is_delete", 0)
                .isNotNull("coating_category")
                .groupBy("coating_category");
        List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper);
        for (ProtToolPlan protToolPlan : protToolPlans) {
            HashMap<String, String> map = new HashMap<>();
            if (protToolPlan.getCoatingCategory() != null) {
                map.put("value", protToolPlan.getCoatingCategory());
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public List<HashMap<String, String>> selectAllTextureCategory() {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        QueryWrapper<ProtToolPlanAux> wrapper = new QueryWrapper<>();
        wrapper.select("data_content")
                .eq("is_delete", 0)
                .eq("data_type", "A")
                .isNotNull("data_content")
                .groupBy("data_content");
        List<ProtToolPlanAux> protToolPlanAuxes = protToolPlanAuxMapper.selectList(wrapper);
        for (ProtToolPlanAux protToolPlanAux : protToolPlanAuxes) {
            HashMap<String, String> map = new HashMap<>();
            if (protToolPlanAux.getDataContent() != null) {
                map.put("value", protToolPlanAux.getDataContent());
                map.put("label", protToolPlanAux.getDataContent());
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public List<HashMap<String, String>> selectAllInsertNutSpec() {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        QueryWrapper<ProtToolPlanAux> wrapper = new QueryWrapper<>();
        wrapper.select("data_content")
                .eq("is_delete", 0)
                .eq("data_type", "B")
                .isNotNull("data_content")
                .groupBy("data_content");
        List<ProtToolPlanAux> protToolPlanAuxes = protToolPlanAuxMapper.selectList(wrapper);
        for (ProtToolPlanAux protToolPlanAux : protToolPlanAuxes) {
            HashMap<String, String> map = new HashMap<>();
            if (protToolPlanAux.getDataContent() != null) {
                map.put("label", protToolPlanAux.getDataContent());
                map.put("value", protToolPlanAux.getDataContent());
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public List<HashMap<String, String>> selectAllToolingVender() {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        QueryWrapper<ProtToolPlan> wrapper = new QueryWrapper<>();
        wrapper.select("tooling_vender")
                .eq("is_delete", 0)
                .isNotNull("tooling_vender")
                .groupBy("tooling_vender");
        List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper);
        for (ProtToolPlan protToolPlan : protToolPlans) {
            HashMap<String, String> map = new HashMap<>();
            if (protToolPlan.getToolingVender() != null) {
                map.put("value", protToolPlan.getToolingVender());
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public List<HashMap<String, String>> selectAllPrNumber() {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        QueryWrapper<ProtToolPlan> wrapper = new QueryWrapper<>();
        wrapper.select("pr_number")
                .eq("is_delete", 0)
                .isNotNull("pr_number")
                .groupBy("pr_number");
        List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper);
        for (ProtToolPlan protToolPlan : protToolPlans) {
            HashMap<String, String> map = new HashMap<>();
            if (protToolPlan.getPrNumber() != null) {
                map.put("value", protToolPlan.getPrNumber());
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public List<HashMap<String, String>> selectAllSuppliedVendor() {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        QueryWrapper<ProtToolPlan> wrapper = new QueryWrapper<>();
        wrapper.select("supplied_vendor")
                .eq("is_delete", 0)
                .isNotNull("supplied_vendor")
                .groupBy("supplied_vendor");
        List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper);
        for (ProtToolPlan protToolPlan : protToolPlans) {
            HashMap<String, String> map = new HashMap<>();
            if (protToolPlan.getSuppliedVendor() != null) {
                map.put("value", protToolPlan.getSuppliedVendor());
                resultList.add(map);
            }
        }
        return resultList;
    }


    /**
     * ?????? Material ?????? ??????
     * @param protToolPlanDto ProtToolPlanDto
     * @return true false
     */
    private boolean checkMaterial(ProtToolPlanDto protToolPlanDto){
        boolean result = true;
        if (protToolPlanDto.getMaterial() != null && !protToolPlanDto.getMaterial().equals("")){
            QueryWrapper<MatelClass> wrapper = new QueryWrapper<>();
            wrapper.select("brand")
                    .eq("is_delete", 0)
                    .eq("code", "3")
                    .eq("brand", protToolPlanDto.getMaterial());
            if (matelClassMapper.selectList(wrapper).size() == 0){
                result = false;
            }
        }
        return result;
    }

    /**
     * selectByParentId
     * @param parentId parentId
     * @return List<ProtToolPlan>
     */
    @Override
    public List<ProtToolPlan> selectByParentId(Long parentId) {
        QueryWrapper<ProtToolPlan> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId)
                .eq("is_delete", 0);
        return protToolPlanMapper.selectList(wrapper);
    }


    /**
     * ?????? ProtToolPlanDto ?????? ProtToolPlanAux ??????
     * @param protToolPlanDto ProtToolPlanDto
     * @param id ?????? id
     * @return List<ProtToolPlanAux>
     */
    private List<ProtToolPlanAux> getProtToolPlanAux(ProtToolPlanDto protToolPlanDto, Long id){
        List<ProtToolPlanAux> insertNutSpecDB = new ArrayList<>();
        List<String> insertNutSpec = protToolPlanDto.getInsertNutSpec();
        if (insertNutSpec.size() > 0) {
            for (String insertNutSpecStr : insertNutSpec) {
                ProtToolPlanAux protToolPlanAux = new ProtToolPlanAux();
                protToolPlanAux.setId(GeneratorCodeUtil.generateKey());
                protToolPlanAux.setDataContent(insertNutSpecStr);
                protToolPlanAux.setDataId(id);
                protToolPlanAux.setDataType("B");
                insertNutSpecDB.add(protToolPlanAux);
            }
        }
        List<String> textureCategory = protToolPlanDto.getTextureCategory();
        if (textureCategory.size() > 0) {
            for (String textureCategoryStr : textureCategory) {
                ProtToolPlanAux protToolPlanAux = new ProtToolPlanAux();
                protToolPlanAux.setId(GeneratorCodeUtil.generateKey());
                protToolPlanAux.setDataContent(textureCategoryStr);
                protToolPlanAux.setDataId(id);
                protToolPlanAux.setDataType("A");
                insertNutSpecDB.add(protToolPlanAux);
            }
        }
        return insertNutSpecDB;
    }

    /**
     * ProtToolPlanDto ???  ProtToolPlan
     * @param protToolPlanDto ProtToolPlanDto
     * @param isAdd add ?????? update
     * @return ProtToolPlan
     */
    private ProtToolPlan setProtToolPlanDtoToProtToolPlan(ProtToolPlanDto protToolPlanDto, boolean isAdd){
        ProtToolPlan protToolPlan = new ProtToolPlan();
        if (isAdd){
            protToolPlan.setId(GeneratorCodeUtil.generateKey());
        } else {
            protToolPlan.setId(protToolPlanDto.getId());
        }
        protToolPlan.setUpdateUser(HttpContextUtil.getCurrentUser().getUsername());
        protToolPlan.setUpdateTime(new Date());
        protToolPlan.setProtId(protToolPlanDto.getProtId());
        protToolPlan.setFileVer(protToolPlanDto.getFileVer());
        protToolPlan.setNo(protToolPlanDto.getNo());
        protToolPlan.setPartNo(protToolPlanDto.getPartNo());
        protToolPlan.setPartDes(protToolPlanDto.getPartDes());
        protToolPlan.setProEFileName(protToolPlanDto.getProEFileName());
        protToolPlan.setVersion(protToolPlanDto.getVersion());
        protToolPlan.setWeight(protToolPlanDto.getWeight());
        protToolPlan.setMaterial(protToolPlanDto.getMaterial());
        protToolPlan.setChineseName(protToolPlanDto.getChineseName());
        protToolPlan.setCav(protToolPlanDto.getCav());
        protToolPlan.setColorNo(protToolPlanDto.getColorNo());
        protToolPlan.setPaintingColorNo(protToolPlanDto.getPaintingColorNo());
        protToolPlan.setPrintingColorNo(protToolPlanDto.getPrintingColorNo());
        protToolPlan.setUnitPrice(protToolPlanDto.getUnitPrice());
        protToolPlan.setCoatingCategory(protToolPlanDto.getCoatingCategory());
        protToolPlan.setQty(protToolPlanDto.getQty());
        protToolPlan.setToolingVender(protToolPlanDto.getToolingVender());
        protToolPlan.setToolingPrCost(protToolPlanDto.getToolingPrCost());
        protToolPlan.setRemark(protToolPlanDto.getRemark());
        protToolPlan.setPrNumber(protToolPlanDto.getPrNumber());
        protToolPlan.setSuppliedVendor(protToolPlanDto.getSuppliedVendor());
        protToolPlan.setParentId(protToolPlanDto.getParentId());
        return protToolPlan;
    }


    /**
     * ProtToolPlan ???  ProtToolPlanDto
     * @param protToolPlan ProtToolPlan
     * @param isPartDes ?????? ?????? ?????????  PartDes
     * @return ProtToolPlanDto
     */
    private ProtToolPlanDto setProtToolPlanToProtToolPlanDto(ProtToolPlan protToolPlan, boolean isPartDes) {
        ProtToolPlanDto protToolPlanDto = new ProtToolPlanDto();
        protToolPlanDto.setId(protToolPlan.getId());
        protToolPlanDto.setProtId(protToolPlan.getProtId());
        protToolPlanDto.setFileVer(protToolPlan.getFileVer());
        protToolPlanDto.setNo(protToolPlan.getNo());
        if (isPartDes) {
            protToolPlanDto.setPartNo(protToolPlan.getPartNo());
            List<PartNumber> partNumbers = partNumberService.selectByNum(protToolPlan.getPartNo());
            if (partNumbers.size() > 0) { protToolPlanDto.setPartDes(partNumbers.get(0).getPartDescription()); }
        }
        protToolPlanDto.setProEFileName(protToolPlan.getProEFileName());
        protToolPlanDto.setVersion(protToolPlan.getVersion());
        protToolPlanDto.setWeight(protToolPlan.getWeight());
        protToolPlanDto.setChineseName(protToolPlan.getChineseName());
        protToolPlanDto.setCav(protToolPlan.getCav());
        protToolPlanDto.setMaterial(protToolPlan.getMaterial());
        protToolPlanDto.setColorNo(protToolPlan.getColorNo());
        protToolPlanDto.setPaintingColorNo(protToolPlan.getPaintingColorNo());
        protToolPlanDto.setPrintingColorNo(protToolPlan.getPrintingColorNo());
        protToolPlanDto.setCoatingCategory(protToolPlan.getCoatingCategory());
        protToolPlanDto.setUnitPrice(protToolPlan.getUnitPrice());
        protToolPlanDto.setQty(protToolPlan.getQty());
        protToolPlanDto.setToolingVender(protToolPlan.getToolingVender());
        protToolPlanDto.setToolingPrCost(protToolPlan.getToolingPrCost());
        protToolPlanDto.setPrNumber(protToolPlan.getPrNumber());
        protToolPlanDto.setSuppliedVendor(protToolPlan.getSuppliedVendor());
        protToolPlanDto.setRemark(protToolPlan.getRemark());
        protToolPlanDto.setParentId(protToolPlan.getParentId());
        // ?????? ?????? TEXTURE  CATEGORY  INSERT NUT SPEC.
        ProtFile protFile = new ProtFile();
        protFile.setProtId(protToolPlan.getId());
        protFile.setCategory("P2");
        protToolPlanDto.setPicture(protFileService.selectByProtIdAndCategory(protFile));
        List<ProtToolPlanAux> textureCategorys = protToolPlanAuxService.selectByDataIdAndType(protToolPlan.getId(), "A");
        List<String> textureCategoryString = new ArrayList<>();
        for (ProtToolPlanAux textureCategory : textureCategorys) {
            textureCategoryString.add(textureCategory.getDataContent());
        }
        protToolPlanDto.setTextureCategory(textureCategoryString);
        List<ProtToolPlanAux> insertNutSpecS = protToolPlanAuxService.selectByDataIdAndType(protToolPlan.getId(), "B");
        List<String> insertNutSpecString = new ArrayList<>();
        for (ProtToolPlanAux insertNutSpec : insertNutSpecS) {
            insertNutSpecString.add(insertNutSpec.getDataContent());
        }
        protToolPlanDto.setInsertNutSpec(insertNutSpecString);
        return protToolPlanDto;
    }

    /**
     * ??????me part list ??? ???????????? ????????? tool plan???
     * @param protMePartList ProtMePartList
     * @param protToolPlans List<ProtToolPlan>
     * @return ?????? true  ????????? false
     */
    private boolean isExistToolPlan(ProtMePartList protMePartList, List<ProtToolPlan> protToolPlans) {
        boolean repeat = false;
        if (protMePartList.getPartNumber() != null) {
            for (ProtToolPlan protToolPlan : protToolPlans) {
                if (protToolPlan.getPartNo().equals(protMePartList.getPartNumber())){
                    repeat = true;
                    break;
                }
            }
        }
        return repeat;
    }


    /**
     * ?????? sonProtId ??????????????? Now??????No
     * @param sonProtId sonProtId
     * @return  Last No + 1
     */
    private Integer getLastNowNo(Long sonProtId) {
        int item = 1;
        QueryWrapper<ProtToolPlan> wrapper = new QueryWrapper<>();
        wrapper.eq("prot_id", sonProtId)
                .eq("is_delete", 0)
                .eq("parent_id", 0)
                .eq("file_ver", "now")
                .select("no")
                .orderByDesc("no")  //  ??????
                .last("limit 2");
        List<ProtToolPlan> protToolPlans = protToolPlanMapper.selectList(wrapper);
        if (protToolPlans.size() > 0) {
            item = protToolPlans.get(0).getNo() + 1;
        }
        return item;
    }


}
