package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.model.MatelClass;
import com.mic.eis.domain.model.PartNumber;
import com.mic.eis.domain.model.ProtFileVer;
import com.mic.eis.domain.model.ProtMePartList;
import com.mic.eis.domain.vo.ProtFileVerQueryVo;
import com.mic.eis.domain.vo.ProtMePartListQueryVo;
import com.mic.eis.mapper.ProtMePartListMapper;
import com.mic.eis.service.PartNumberService;
import com.mic.eis.service.ProtFileVerService;
import com.mic.eis.service.ProtMePartListService;
import com.mic.eis.util.ExcelReaderUtils;
import com.mic.eis.utils.GeneratorCodeUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-09-07 14:55
 */
@Service
public class ProtMePartListServiceImpl extends ServiceImpl<ProtMePartListMapper, ProtMePartList> implements ProtMePartListService {

    @Resource
    private ProtMePartListMapper protMePartListMapper;

    @Resource
    private PartNumberService partNumberService;

    @Resource
    private MatelClassServiceImpl matelClassService;

    @Resource
    private ProtFileVerService protFileVerService;

    @Override
    public List<ProtMePartList> selectByProtIdAndVersion(ProtMePartListQueryVo protMePartListQueryVo) {
        QueryWrapper<ProtMePartList> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("is_delete", 0)
                .eq("prot_id", protMePartListQueryVo.getProtId())
                .eq("file_ver", protMePartListQueryVo.getFileVer())
                .isNotNull("no")
                .orderByAsc("no");  // no 升序
        List<ProtMePartList> protMePartListsNotNull = protMePartListMapper.selectList(wrapper1);
        QueryWrapper<ProtMePartList> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("is_delete", 0)
                .eq("prot_id", protMePartListQueryVo.getProtId())
                .eq("file_ver", protMePartListQueryVo.getFileVer())
                .isNull("no")
                .orderByAsc("no");  // no 升序
        List<ProtMePartList> protMePartLists1Null = protMePartListMapper.selectList(wrapper2);
        protMePartListsNotNull.addAll(protMePartLists1Null);
        return protMePartListsNotNull;
    }

    @Override
    public ProtMePartList selectProtMePartListById(Long id) {
        return protMePartListMapper.selectById(id);
    }

    @Override
    public void deleteProtMePartListById(Long id) {
        try {
            // 查询当前版本 No > 当前No ，全部 -1
            ProtMePartList protMePartList = selectProtMePartListById(id);
            modifiedNo(protMePartList, "sub");
            protMePartListMapper.deleteProtMePartListById(id);
        } catch (Exception e) {
            log.error("删除 deleteProtMePartListById 异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteProtMePartListByProtIdAndVer(Long protId, String fileVer) {
        try {
            protMePartListMapper.deleteProtMePartListByProtIdAndVer(protId, fileVer);
        } catch (Exception e) {
            log.error("删除 deleteProtMePartListByProtIdAndVer 异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public HashMap<String, Object> addProtMePartList(ProtMePartList protMePartList) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            // 判断 插入的数据
            // 0: part Num 一致, part Des 不一致 询问用户是否 按预设项 覆盖description
            // 1: part Num 不一致 直接拎出来，告诉用户与预设项不符，无法导入
            // 3: part Num 一致, part Des 一致
            // 2: 没有part Num ， 不检查，直接插入
            int goodPartNumData = isGoodPartNumData(protMePartList);
            if (goodPartNumData == 3 || goodPartNumData == 2){
                if (isGoodMeterial(protMePartList)){
                    // part Num 没有重复  part des 不重复 ： 查询 现有的所有
                    QueryWrapper<ProtMePartList> wrapper1 = new QueryWrapper<>();
                    wrapper1.eq("is_delete", 0)
                            .eq("prot_id", protMePartList.getProtId())
                            .eq("file_ver", "now")
                            .orderByAsc("no");  // no 升序
                    List<ProtMePartList> protMePartLists = protMePartListMapper.selectList(wrapper1);
                    if (isRepeat(protMePartList, protMePartLists)) {
                        map.put("msgFail", "part number 或者 part description 已存在！");
                    } else {
                        // 查询当前 版本 No > 当前No ，全部 +1, 如果是第一个， 如果不是第一个。。
                        // 根据前台传来能直接插的数 No ， 前台传来 2， >=2（2,3,4,5..）的全部加1
                        modifiedNo(protMePartList, "add");
                        protMePartList.setId(GeneratorCodeUtil.generateKey());
                        protMePartListMapper.insert(protMePartList);
                        map.put("msgSuccess", "添加成功");
                    }
                } else {
                    map.put("msgFail", "预设项 Meterial 不存在！");
                }
            } else if (goodPartNumData == 0) {
                map.put("msgFail", "part Number 对应的 part Description 与预设项中的不一致！");
            } else if (goodPartNumData == 1) {
                map.put("msgFail", "预设项 part Number 不存在！");
            }
            // 需要 判断  No 和 Description、 Meterial
            return map;
        } catch (Exception e){
            log.error("添加 项目 Me Part List 异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public HashMap<String, Object> editProtMePartList(ProtMePartList protMePartList) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            int goodPartNumData = isGoodPartNumData(protMePartList);
            // 3: part Num 一致, part Des 一致
            // 2: 没有part Num ， 不检查，直接插入
            if (goodPartNumData == 3 || goodPartNumData == 2){
                if (isGoodMeterial(protMePartList)){
                    // part Num 没有重复  part des 不重复 ： 查询 现有的所有
                    QueryWrapper<ProtMePartList> wrapper1 = new QueryWrapper<>();
                    wrapper1.eq("is_delete", 0)
                            .eq("prot_id", protMePartList.getProtId())
                            .eq("file_ver", "now")
                            .ne("id", protMePartList.getId())
                            .orderByAsc("no");  // no 升序
                    List<ProtMePartList> protMePartLists = protMePartListMapper.selectList(wrapper1);
                    if (isRepeat(protMePartList, protMePartLists)) {
                        map.put("msgFail", "part number 或者 part description 已存在！");
                    } else {
                        protMePartListMapper.updateById(protMePartList);
                        map.put("msgSuccess", "修改成功");
                    }
                } else {
                    map.put("msgFail", "预设项 Meterial 不存在！");
                }
            } else if (goodPartNumData == 0) {
                // 0: part Num 一致, part Des 不一致 询问用户是否 按预设项 覆盖description
                map.put("msgFail", "part Number 对应的 part Description 与预设项中的不一致！");
            } else if (goodPartNumData == 1) {
                // 1: part Num 不一致 直接拎出来，告诉用户与预设项不符，无法导入
                map.put("msgFail", "预设项 part Number 不存在！");
            }
            // 需要 判断  No 和 Description、 Meterial
            return map;
        }catch (Exception e) {
            log.error("编辑子项目MePartList异常");
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public HashMap<String, Object> addNewVersion(ProtFileVer protFileVer) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            // 判断 表 protFileVer 中版本是否存在
            ProtFileVerQueryVo protFileVerQueryVo = new ProtFileVerQueryVo();
            protFileVerQueryVo.setFileVer(protFileVer.getFileVer());
            protFileVerQueryVo.setCategory(protFileVer.getCategory());
            protFileVerQueryVo.setProtId(protFileVer.getProtId());
            List<ProtFileVer> protFileVers = protFileVerService.selectByProtIdAndCategoryAndVer(protFileVerQueryVo);
            if(protFileVers.size() > 0) {
                map.put("msgFail", "该版本号已经存在，请修改版本号 ！");
                return map;
            }
            ProtMePartListQueryVo protMePartListQueryVo = new ProtMePartListQueryVo();
            protMePartListQueryVo.setFileVer("now");
            protMePartListQueryVo.setProtId(protFileVer.getProtId());
            List<ProtMePartList> protMePartLists = selectByProtIdAndVersion(protMePartListQueryVo);
            List<ProtMePartList> addDB = new ArrayList<>();
            for (ProtMePartList protMePartList : protMePartLists) {
                protMePartList.setId(GeneratorCodeUtil.generateKey());
                protMePartList.setFileVer(protFileVer.getFileVer());
                addDB.add(protMePartList);
            }
            if (addDB.size() > 0) {
                protMePartListMapper.addBatchProtMePartList(addDB);
            }
            protFileVerService.addProtFileVer(protFileVer);
            return map;
        } catch (Exception e) {
            log.error("添加 me part list 新版本 异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public HashMap<String, Object> matchPartNum(Long sonProtId) {
        HashMap<String, Object> map = new HashMap<>();
        // 查出所有的  PartNum
        List<PartNumber> partNumbers = partNumberService.selectAll();
        // 查出所有的 me part list  根据 子项目id 、版本now
        ProtMePartListQueryVo protMePartListQueryVo = new ProtMePartListQueryVo();
        protMePartListQueryVo.setProtId(sonProtId);
        protMePartListQueryVo.setFileVer("now");
        List<ProtMePartList> protMePartLists = selectByProtIdAndVersion(protMePartListQueryVo);
        // 1. 根据description将Part Number为空的进行补充
        // 2. 再根据Part Number修改description
        map.put("fillPartNumCount", fillPartNum(partNumbers, protMePartLists));
        map.put("modifyPartDesCount", modifyPartDes(partNumbers, protMePartLists));
        return map;
    }

    @Override
    public void backBeforeVersion(ProtMePartListQueryVo protMePartListQueryVo) {
        try {
            List<ProtMePartList> addDB = new ArrayList<>();
            List<ProtMePartList> protMePartLists = selectByProtIdAndVersion(protMePartListQueryVo);
            for (ProtMePartList protMePartList : protMePartLists) {
                protMePartList.setId(GeneratorCodeUtil.generateKey());
                protMePartList.setFileVer("now");
                addDB.add(protMePartList);
            }
            // 通过 子项目id 、 文件版本 删除 me part list
            deleteProtMePartListByProtIdAndVer(protMePartListQueryVo.getProtId(), "now");
            if (addDB.size() > 0) {
                protMePartListMapper.addBatchProtMePartList(addDB);
            }
        } catch (Exception e) {
            log.error("将me part list 返回到之前的版本异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public HashMap<String, Object> uploadMePartListFile(MultipartFile file, Long sonProtId) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            // 1. 获取所有的 Excel 数据
            List<ProtMePartList> protMePartListExcel = ExcelReaderUtils.readMePartList(file);
            if (protMePartListExcel!=null){
                // 2. 获取所有 part num
                List<PartNumber> partNumbersDB = partNumberService.selectAll();
                // 3. 获取所有 Meterial
                List<MatelClass> matelClassesDB = matelClassService.selectAll();
                List<ProtMePartList> partData  = new ArrayList<>();  // 剩余数据
                List<ProtMePartList> insertDBData  = new ArrayList<>();  // 插入数据库
                List<ProtMePartList> numMeteNotInDB  = new ArrayList<>();  // part num和mete不在数据库数据
                List<ProtMePartList> numDesDiffData  = new ArrayList<>();  // part num 和 des不匹配
                List<ProtMePartList> repeatData = new ArrayList<>();  // part num 或 part des 重复的数据
                boolean isPartData;
                for (ProtMePartList protMePartList : protMePartListExcel) {
                    isPartData = true;
                    protMePartList.setProtId(sonProtId);
                    protMePartList.setFileVer("now");
                    if (protMePartList.getPartNumber() != null){
                        // 判断 是否 是 合法 的 PartNumber
                        if (!judgeExcelPartNumData(protMePartList, partNumbersDB)){
                            numMeteNotInDB.add(protMePartList);
                            isPartData = false;
                        }
                    }
                    if (protMePartList.getMeterial() != null){
                        // 判断 是否 是 合法 的 Meterial
                        if (!judgeExcelMeterialData(protMePartList, matelClassesDB)){
                            if(isPartData){
                                numMeteNotInDB.add(protMePartList);
                                isPartData = false;
                            }
                        }
                    }
                    if(isPartData){
                        partData.add(protMePartList);
                    }
                }
                // 对剩下的数据 partData 判断 ， part num和part Des是否匹配
                for (ProtMePartList partDatum : partData) {
                    if (partDatum.getPartNumber()!=null){
                        if (partDatum.getPartDescription() != null){
                            // 判断part num 和 part des是否匹配
                            if(matchPartNumAndPartDes(partDatum, partNumbersDB)){
                                if (isRepeat(partDatum, insertDBData)) {
                                    repeatData.add(partDatum);
                                } else {
                                    partDatum.setId(GeneratorCodeUtil.generateKey());
                                    insertDBData.add(partDatum);
                                }
                            } else {
                                if(!isRepeat(partDatum, numDesDiffData)) {
                                    numDesDiffData.add(partDatum);
                                }
                                // numDesDiffData 也要判断是否有重复的数据，否则有两笔可能也会造成重复
                            }
                        } else {
                            if(!isRepeat(partDatum, numDesDiffData)) {
                                numDesDiffData.add(partDatum);
                            }
                        }
                    } else {
                        if (isRepeat(partDatum, insertDBData)) {
                            repeatData.add(partDatum);
                        } else {
                            partDatum.setId(GeneratorCodeUtil.generateKey());
                            insertDBData.add(partDatum);
                        }
                    }
                }
//                map.put("insertDBData", insertDBData);
                map.put("numMeteNotInDB", numMeteNotInDB);
                map.put("numDesDiffData", numDesDiffData);
                map.put("repeatData", repeatData);
                deleteProtMePartListByProtIdAndVer(sonProtId, "now");
                if (insertDBData.size() > 0) {
                    protMePartListMapper.addBatchProtMePartList(insertDBData);
                }
            }
            return map;
        } catch (Exception e){
            log.error("通过Excel 添加 项目 Me Part List 异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void overDes(List<ProtMePartList> protMePartLists) {
        try {
            List<ProtMePartList> insertDBData  = new ArrayList<>();  // 插入数据库
            List<ProtMePartList> dbData = new ArrayList<>();
            // 1. 查找所有的 part num 数据
            List<PartNumber> partNumbersDB = partNumberService.selectAll();
            boolean isFirst = true;
            for (ProtMePartList protMePartList : protMePartLists) {
                // 判断数据库中是否已经存在，不存在才能插入
                if (isFirst) {
                    QueryWrapper<ProtMePartList> wrapper1 = new QueryWrapper<>();
                    wrapper1.eq("is_delete", 0)
                            .eq("prot_id", protMePartList.getProtId())
                            .eq("file_ver", "now")
                            .orderByAsc("no");  // no 升序
                    dbData = protMePartListMapper.selectList(wrapper1);
                    isFirst = false;
                }
                if (!isRepeat(protMePartList, dbData)) {
                    if (protMePartList.getPartNumber() != null){
                        for (PartNumber partNumber : partNumbersDB) {
                            if (protMePartList.getPartNumber().equals(partNumber.getPartNumber())){
                                protMePartList.setPartDescription(partNumber.getPartDescription());
                                protMePartList.setId(GeneratorCodeUtil.generateKey());
                                insertDBData.add(protMePartList);
                                break;
                            }
                        }
                    }
                }
            }
            if (insertDBData.size() > 0) {
                protMePartListMapper.addBatchProtMePartList(insertDBData);
            }
        } catch (Exception e){
            log.error("按预设项 覆盖description异常");
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * 判断 要插入 或者 修改 的 protMePartList 是否已经存在
     * @param protMePartList ProtMePartList
     * @param protMePartLists List<ProtMePartList>
     * @return false true 重复 为 true、不重复为 false
     */
    private boolean isRepeat(ProtMePartList protMePartList, List<ProtMePartList> protMePartLists) {
        boolean isOk = false;
        // 判断 PartNumber 是否重复
        if (protMePartList.getPartNumber() !=null && (!protMePartList.getPartNumber().equals(""))) {
            for (ProtMePartList mePartList : protMePartLists) {
                if (protMePartList.getPartNumber().equals(mePartList.getPartNumber())) {
                    isOk = true;
                    break;
                }
            }
        }
        // 判断 Part Des 是否重复
        if (protMePartList.getPartDescription() !=null && (!protMePartList.getPartDescription().equals(""))) {
            for (ProtMePartList mePartList : protMePartLists) {
                if (protMePartList.getPartDescription().equals(mePartList.getPartDescription())) {
                    isOk = true;
                    break;
                }
            }
        }
        return isOk;
    }

    /**
     * 判断 part num 和 part Des 是否匹配
     * @param partDatum 实体
     * @param partNumbersDB 数据库所有part num 数据
     * @return boolean
     */
    private boolean matchPartNumAndPartDes(ProtMePartList partDatum, List<PartNumber> partNumbersDB){
        boolean isOk = false;
        for (PartNumber partNumber : partNumbersDB) {
            if (partNumber.getPartNumber() != null && partNumber.getPartDescription() != null){
                if (partNumber.getPartNumber().equals(partDatum.getPartNumber()) &&
                        partNumber.getPartDescription().equals(partDatum.getPartDescription())){
                    isOk = true;
                }
            }
        }
        return isOk;
    }

    /**
     * 根据 protMePartList、partNumbersDB判断 Excel 中 Meterial 是否存在在数据库
     * @param protMePartList 实体
     * @param matelClassesDB 数据库所有 MatelClass 数据
     * @return boolean
     */
    private boolean judgeExcelMeterialData(ProtMePartList protMePartList, List<MatelClass> matelClassesDB){
        boolean isOk = false;
        for (MatelClass matelClass : matelClassesDB) {
            if(matelClass.getMaterial() != null) {
                if(protMePartList.getMeterial().equals(matelClass.getMaterial())){
                    isOk = true;
                }
            }
        }
        return isOk;
    }


    /**
     * 根据 protMePartList、partNumbersDB判断 Excel 中 partNum 是否存在在数据库
     * @param protMePartList 实体
     * @param partNumbersDB 数据库所有part num 数据
     * @return boolean
     */
    private boolean judgeExcelPartNumData(ProtMePartList protMePartList, List<PartNumber> partNumbersDB){
        boolean isOk = false;
        for (PartNumber partNumber : partNumbersDB) {
            if (partNumber.getPartNumber() != null){
                if (partNumber.getPartNumber().equals(protMePartList.getPartNumber())){
                    isOk = true;
                }
            }
        }
        return isOk;
    }


    /**
     * 修改 partNumber
     * 根据 partNumber 修改  PartDes，与 partNumber表保持一致
     * @param partNumbers  List<PartNumber>
     * @param protMePartLists List<ProtMePartList>
     * @return int 修改的数量
     */
    private int modifyPartDes(List<PartNumber> partNumbers, List<ProtMePartList> protMePartLists) {
        int count = 0;
        for (ProtMePartList protMePartList : protMePartLists) { // 遍历 MePartLists
            if (protMePartList.getPartNumber() != null) {
                for (PartNumber partNumber : partNumbers) { // 遍历 partNumbers
                    if (protMePartList.getPartNumber().equals(partNumber.getPartNumber())) {
                        if (partNumber.getPartDescription() == null) {
                            if (protMePartList.getPartDescription() != null) {
                                protMePartList.setPartDescription(partNumber.getPartDescription());
                                protMePartListMapper.updateById(protMePartList);
                                count = count + 1;
                            }
                        } else {
                            if (!partNumber.getPartDescription().equals(protMePartList.getPartDescription())) {
                                protMePartList.setPartDescription(partNumber.getPartDescription());
                                protMePartListMapper.updateById(protMePartList);
                                count = count + 1;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }
    /**
     * 填充 partNumber
     * 只有description没有part no ，点击按钮后，匹配预设项中的description
     * ，如果唯一，就自动填充part no，不匹配或者不唯一匹配就不填充
     * @param partNumbers (List<PartNumber>
     * @param protMePartLists List<ProtMePartList>
     * @return int 修改的数量
     */
    private int fillPartNum(List<PartNumber> partNumbers, List<ProtMePartList> protMePartLists){
        int count = 0;
        int desCount = 0;
        String partNum = "";
        for (ProtMePartList protMePartList : protMePartLists) {  // 遍历 MePartLists
            // 只有description没有part no
            if (protMePartList.getPartDescription() != null && (protMePartList.getPartNumber() == null || protMePartList.getPartNumber().equals(""))){
                desCount = 0;
                for (PartNumber partNumber : partNumbers) {  // 遍历 partNumbers
                    if (partNumber.getPartNumber() != null && partNumber.getPartDescription() != null) {
                        if(partNumber.getPartDescription().equals(protMePartList.getPartDescription())){
                            desCount = desCount + 1;
                            partNum = partNumber.getPartNumber();
                        }
                    }
                }
                if (desCount == 1) {
                    protMePartList.setPartNumber(partNum);
                    // 更新 数据
                    protMePartListMapper.updateById(protMePartList);
                    count = count + 1;
                }
            }
        }
        return count;
    }

    /**
     * 判断 PartNum 是否符合规则
     * @param protMePartList 实体
     * @return int
     */
    private int isGoodPartNumData(ProtMePartList protMePartList){
        int isOk;
        // 有 part Num
        // 0: part Num 一致, part Des 不一致 询问用户是否 按预设项 覆盖description
        // 1: part Num 不一致 直接拎出来，告诉用户与预设项不符，无法导入
        // 3: part Num 一致, part Des 一致
        // 2: 没有part Num ， 不检查，直接插入

        // 没有 part Num 无论是否有 description
        // 直接插入  插入数据不check 预设项

        // 1. 有 part Num
        if (protMePartList.getPartNumber() != null && !protMePartList.getPartNumber().equals("")) {
            List<PartNumber> partNumbers = partNumberService.selectByNum(protMePartList.getPartNumber());
            if (partNumbers.size() > 0){
                PartNumber partNumber = partNumbers.get(0);
                if (protMePartList.getPartDescription() != null) {
                    if (protMePartList.getPartDescription().equals(partNumber.getPartDescription())){
                        isOk = 3;  // 3: part Num 一致, part Des 一致
                    } else {
                        isOk = 0;  // 0: part Num 一致, part Des 不一致
                    }
                } else {
                    isOk = 0;
                }

            } else {
                isOk = 1;  // 1: part Num 不一致 直接拎出来，告诉用户与预设项不符，无法导入
            }
        } else { // 没有part Num
            isOk = 2;  // 2: 没有part Num ， 不检查，直接插入
        }
        return isOk;
    }

    /**
     * 判断 Meterial 是否符合规则
     * @param protMePartList 实体
     * @return true false
     */
    private boolean isGoodMeterial(ProtMePartList protMePartList){
        boolean isOk;
        if (protMePartList.getMeterial() != null && !protMePartList.getMeterial().equals("")){
            List<MatelClass> matelClasses = matelClassService.selectByAllMaterial(protMePartList.getMeterial());
            if (matelClasses.size() > 0){
                isOk = true;
            } else {
                isOk = false;
            }
        } else {
            isOk = true;
        }
        return isOk;
    }


    /**
     * 添加或删除时，修改序号
     * @param protMePartList 实体
     * @param addOrSub 加 还是 减  add / sub
     */
    private void modifiedNo(ProtMePartList protMePartList, String addOrSub){
        if(protMePartList.getNo() != null){
            QueryWrapper<ProtMePartList> wrapper = new QueryWrapper<>();
            wrapper.eq("is_delete", 0)
                    .eq("prot_id", protMePartList.getProtId())
                    .eq("file_ver", "now")
                    .ge("no", protMePartList.getNo());  //大于等于>=
            List<ProtMePartList> protMePartLists = protMePartListMapper.selectList(wrapper);
            if (addOrSub.equals("add")) {
                for (ProtMePartList mePartList : protMePartLists) {
                    mePartList.setNo(mePartList.getNo()+1);
                    protMePartListMapper.updateById(mePartList);
                }
            } else {
                for (ProtMePartList mePartList : protMePartLists) {
                    mePartList.setNo(mePartList.getNo()-1);
                    protMePartListMapper.updateById(mePartList);
                }
            }
        }

    }



}
