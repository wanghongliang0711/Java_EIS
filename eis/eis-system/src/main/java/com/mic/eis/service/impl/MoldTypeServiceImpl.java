package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.dto.MoldTypeDto;
import com.mic.eis.domain.model.MoldType;
import com.mic.eis.mapper.MoldTypeMapper;
import com.mic.eis.service.MoldTypeService;
import com.mic.eis.utils.GeneratorCodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-08-13 15:53
 */
@Service
public class MoldTypeServiceImpl extends ServiceImpl<MoldTypeMapper, MoldType> implements MoldTypeService {

    @Resource
    private MoldTypeMapper moldTypeMapper;

    @Override
    public List<MoldTypeDto> queryByType(MoldType moldType) {
        QueryWrapper<MoldType> wrapper = new QueryWrapper<>();
        wrapper.like("mold_type", moldType.getMoldType())
                .eq("parent_id", 0);
        List<MoldType> moldTypes = moldTypeMapper.selectList(wrapper);
        List<MoldTypeDto> moldTypeDtos = new ArrayList<>();
        for (MoldType type : moldTypes) {
            MoldTypeDto moldTypeDto = new MoldTypeDto();
            moldTypeDto.setId(type.getId());
            moldTypeDto.setMoldType(type.getMoldType());
            moldTypeDto.setParentId(type.getParentId());
            moldTypeDto.setChildren(selectByParentId(type.getId()));
            moldTypeDtos.add(moldTypeDto);
        }
        return moldTypeDtos;
    }

    @Override
    public List<MoldType> selectByParentId(Long id) {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("parent_id", id);
        map.put("is_delete", 0);
        return moldTypeMapper.selectByMap(map);
    }

    @Override
    public List<MoldType> selectByType(String type) {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("mold_type", type);
        map.put("parent_id", 0);
        map.put("is_delete", 0);
        return moldTypeMapper.selectByMap(map);
    }

    @Override
    public void addMoldType(MoldType moldType) {
        try {
            moldType.setId(GeneratorCodeUtil.generateKey());
            moldTypeMapper.insert(moldType);
        }catch (Exception e){
            log.error("添加模具种类异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteMoldTypeById(Long id) {
        try {
            moldTypeMapper.deleteById(id);
        } catch (Exception e) {
            log.error("删除模具种类异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean editMoldType(MoldType moldType) {
        try {
            return moldTypeMapper.updateById(moldType) == 1;
        } catch (Exception e) {
            log.error("编辑模具种类异常");
            e.printStackTrace();
            throw e;
        }
    }
}
