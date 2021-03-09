package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mic.eis.domain.model.MoldNumber;
import com.mic.eis.domain.vo.MoldNumberQueryVo;
import com.mic.eis.mapper.MoldNumberMapper;
import com.mic.eis.service.MoldNumberService;
import com.mic.eis.utils.GeneratorCodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-08-19 9:30
 */
@Service
public class MoldNumberServiceImpl extends ServiceImpl<MoldNumberMapper, MoldNumber> implements MoldNumberService {


    @Resource
    private MoldNumberMapper moldNumberMapper;

    @Override
    public PageInfo<MoldNumber> queryByMoldNum(MoldNumberQueryVo moldNumberQueryVo) {
        PageHelper.startPage(moldNumberQueryVo.getPageNum(), moldNumberQueryVo.getPageSize());
        QueryWrapper<MoldNumber> wrapper = new QueryWrapper<>();
        wrapper.like("mold_number", moldNumberQueryVo.getMoldNumber())
                .eq("is_delete", 0);
        List<MoldNumber> moldNumbers = moldNumberMapper.selectList(wrapper);
        return new PageInfo<>(moldNumbers);
    }

    @Override
    public List<MoldNumber> selectByMoldNum(String moldNumber) {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("mold_number", moldNumber);
        map.put("is_delete", 0);
        return moldNumberMapper.selectByMap(map);
    }

    @Override
    public void addMoldNum(MoldNumber moldNumber) {
        try {
            moldNumber.setId(GeneratorCodeUtil.generateKey());
            moldNumberMapper.insert(moldNumber);
        } catch (Exception e) {
            log.error("添加 MoldNumber 异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteMoldNumById(Long id) {
        try {
            moldNumberMapper.deleteById(id);
        } catch (Exception e) {
            log.error("删除MoldNum异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<MoldNumber> selectByMoldNumExcludeMoldNum(MoldNumber moldNumber) {
        QueryWrapper<MoldNumber> wrapper = new QueryWrapper<>();
        wrapper.eq("mold_number", moldNumber.getMoldNumber())
                .eq("is_delete", 0)
                .ne("id", moldNumber.getId());
        return moldNumberMapper.selectList(wrapper);
    }

    @Override
    public boolean editMoldNum(MoldNumber moldNumber) {
        try {
            return moldNumberMapper.updateById(moldNumber) == 1;
        } catch (Exception e) {
            log.error("编辑 MoldNumber 异常");
            e.printStackTrace();
            throw e;
        }
    }


}
