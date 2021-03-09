package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mic.eis.domain.model.PartNumber;
import com.mic.eis.domain.vo.PartNumberQueryVo;
import com.mic.eis.mapper.PartNumberMapper;
import com.mic.eis.service.PartNumberService;
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
 * @date 2020-08-14 12:53
 */
@Service
public class PartNumberServiceImpl extends ServiceImpl<PartNumberMapper, PartNumber> implements PartNumberService {

    @Resource
    private PartNumberMapper partNumberMapper;

    @Override
    public PageInfo<PartNumber> findByNumAndDes(PartNumberQueryVo partNumberQueryVo) {
        PageHelper.startPage(partNumberQueryVo.getPageNum(), partNumberQueryVo.getPageSize());
        QueryWrapper<PartNumber> wrapper = new QueryWrapper<>();
        wrapper.like("part_number", partNumberQueryVo.getPartNumber())
                .like("part_description", partNumberQueryVo.getPartDescription())
                .eq("is_delete", 0);
        List<PartNumber> partNumbers = partNumberMapper.selectList(wrapper);
        return new PageInfo<>(partNumbers);
    }

    @Override
    public List<PartNumber> selectByNum(String partNumber) {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("part_number", partNumber);
        map.put("is_delete", 0);
        return partNumberMapper.selectByMap(map);
    }

    @Override
    public List<PartNumber> selectAllPartNum() {
        QueryWrapper<PartNumber> wrapper = new QueryWrapper<>();
        wrapper.select("part_number")
                .eq("is_delete", 0);
        return partNumberMapper.selectList(wrapper);
    }

    @Override
    public List<PartNumber> selectAll() {
        QueryWrapper<PartNumber> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        return partNumberMapper.selectList(wrapper);
    }

    @Override
    public List<HashMap<String, String>> selectAllPartNumberTips() {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        QueryWrapper<PartNumber> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        List<PartNumber> partNumbers = partNumberMapper.selectList(wrapper);
        for (PartNumber partNumber : partNumbers) {
            HashMap<String, String> map = new HashMap<>();
            if (partNumber.getPartNumber() != null){
                map.put("value", partNumber.getPartNumber());
                if (partNumber.getPartDescription() != null) {
                    map.put("partDescription", partNumber.getPartDescription());
                } else {
                    map.put("partDescription", "");
                }
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public List<PartNumber> uploadFile(MultipartFile file) {
        try {
            List<PartNumber> excelPartNum = ExcelReaderUtils.readPartNumExcel(file);
            List<PartNumber> repeatPartNum = new ArrayList<>();
            if (excelPartNum != null){
                List<PartNumber> dbPartNum = selectAllPartNum();
                List<PartNumber> insertDB = new ArrayList<>();
                // 把重复返回，不重复的插入数据库
                for (PartNumber partNumber : excelPartNum) {
                    if (isRepeat(partNumber, dbPartNum)){
                        repeatPartNum.add(partNumber);
                    } else {
                        partNumber.setId(GeneratorCodeUtil.generateKey());
                        insertDB.add(partNumber);
                    }
                }
                if (insertDB.size() > 0){
                    partNumberMapper.addBatchPartNum(insertDB);
                }
            }
            return repeatPartNum;
        } catch (Exception e){
            log.error("从 excel 添加 Part Number 异常");
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public List<PartNumber> selectByDes(String partDescription) {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("part_description", partDescription);
        map.put("is_delete", 0);
        return partNumberMapper.selectByMap(map);
    }

    @Override
    public List<PartNumber> selectByPartNumExcludePartNum(PartNumber partNumber) {
        QueryWrapper<PartNumber> wrapper = new QueryWrapper<>();
        wrapper.eq("part_number", partNumber.getPartNumber())
                .eq("is_delete", 0)
                .ne("id", partNumber.getId());
        return partNumberMapper.selectList(wrapper);
    }

    @Override
    public List<PartNumber> selectByPartNumExcludePartDes(PartNumber partNumber) {
        QueryWrapper<PartNumber> wrapper = new QueryWrapper<>();
        wrapper.eq("part_description", partNumber.getPartDescription())
                .eq("is_delete", 0)
                .ne("id", partNumber.getId());
        return partNumberMapper.selectList(wrapper);
    }

    @Override
    public void addPartNum(PartNumber partNumber) {
        try {
            partNumber.setPartNumber(partNumber.getPartNumber().replaceAll("\r|\n", "").trim());
            partNumber.setPartDescription(partNumber.getPartDescription().replaceAll("\r|\n", "").trim());
            partNumber.setId(GeneratorCodeUtil.generateKey());
            partNumberMapper.insert(partNumber);
        }catch (Exception e){
            log.error("添加 Part Number 异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deletePartNumById(Long id) {
        try {
            partNumberMapper.deleteById(id);
        }catch (Exception e){
            log.error("删除PartNum异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean editPartNum(PartNumber partNumber) {
        try {
            partNumber.setPartNumber(partNumber.getPartNumber().replaceAll("\r|\n", "").trim());
            partNumber.setPartDescription(partNumber.getPartDescription().replaceAll("\r|\n", "").trim());
            return partNumberMapper.updateById(partNumber) == 1;
        } catch (Exception e){
            log.error("编辑PartNum异常");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     *
     * @param partNumber partNumber实体
     * @param dbPartNumList 数据库中 partNumber 列表
     * @return
     */
    private boolean isRepeat(PartNumber partNumber, List<PartNumber> dbPartNumList){
        boolean repeat = false;
        for (PartNumber number : dbPartNumList) {
            if (number.getPartNumber().equals(partNumber.getPartNumber())){
                repeat = true;
            }
        }
        return repeat;
    }
}
