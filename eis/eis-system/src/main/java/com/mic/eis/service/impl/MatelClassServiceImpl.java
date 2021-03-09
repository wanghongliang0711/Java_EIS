package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.dto.MatelClassDto;
import com.mic.eis.domain.dto.MatelClassDto1;
import com.mic.eis.domain.model.MatelClass;
import com.mic.eis.mapper.MatelClassMapper;
import com.mic.eis.service.MatelClassService;
import com.mic.eis.utils.GeneratorCodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-08-19 11:29
 */
@Service
public class MatelClassServiceImpl extends ServiceImpl<MatelClassMapper, MatelClass> implements MatelClassService {

    @Resource
    private MatelClassMapper matelClassMapper;


    @Override
    public List<MatelClassDto> queryByMaterial(MatelClass matelClass) {
        QueryWrapper<MatelClass> wrapper = new QueryWrapper<>();
        wrapper.like("material", matelClass.getMaterial())
                .eq("parent_id", 0);
        List<MatelClass> matelClasses = matelClassMapper.selectList(wrapper);
        List<MatelClassDto> matelClassDtos = new ArrayList<>();
        for (MatelClass aClass : matelClasses) {
            MatelClassDto matelClassDto = new MatelClassDto();
            matelClassDto.setId(aClass.getId());
            matelClassDto.setMaterial(aClass.getMaterial());
            matelClassDto.setBrand(aClass.getBrand());
            matelClassDto.setCode(aClass.getCode());
            matelClassDto.setParentId(aClass.getParentId());
            matelClassDto.setChildren(selectByParentId(aClass.getId()));
            matelClassDtos.add(matelClassDto);
        }
        return matelClassDtos;
    }

    @Override
    public List<MatelClassDto1> queryByOneMaterial(MatelClass matelClass) {
        QueryWrapper<MatelClass> wrapper = new QueryWrapper<>();
        wrapper.like("material", matelClass.getMaterial())
                .eq("parent_id", 0);
        List<MatelClass> matelClasses = matelClassMapper.selectList(wrapper);
        List<MatelClassDto1> results = new ArrayList<>();
        // 一级
        for (MatelClass aClass : matelClasses) {
            MatelClassDto1 matelClassDto1 = new MatelClassDto1();
            matelClassDto1.setId(aClass.getId());
            matelClassDto1.setMaterial(aClass.getMaterial());
            matelClassDto1.setBrand(aClass.getBrand());
            matelClassDto1.setCode(aClass.getCode());
            matelClassDto1.setParentId(aClass.getParentId());
            // 二级
            QueryWrapper<MatelClass> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("is_delete", 0)
                    .eq("parent_id", aClass.getId());
            List<MatelClass> matelClasses2 = matelClassMapper.selectList(wrapper2);
            List<MatelClassDto1> results2 = new ArrayList<>();
            for (MatelClass matelClass2 : matelClasses2) {
                MatelClassDto1 matelClassDto2 = new MatelClassDto1();
                matelClassDto2.setId(matelClass2.getId());
                matelClassDto2.setMaterial(matelClass2.getMaterial());
                matelClassDto2.setBrand(matelClass2.getBrand());
                matelClassDto2.setParentId(matelClass2.getParentId());
                matelClassDto2.setCode(matelClass2.getCode());
                // 三级
                QueryWrapper<MatelClass> wrapper3 = new QueryWrapper<>();
                wrapper3.eq("is_delete", 0)
                        .eq("parent_id", matelClass2.getId());
                List<MatelClassDto1> results3 = new ArrayList<>();
                List<MatelClass> matelClasses3 = matelClassMapper.selectList(wrapper3);
                for (MatelClass matelClass3 : matelClasses3) {
                    MatelClassDto1 matelClassDto3 = new MatelClassDto1();
                    matelClassDto3.setId(matelClass3.getId());
                    matelClassDto3.setBrand(matelClass3.getBrand());
                    matelClassDto3.setCode(matelClass3.getCode());
                    matelClassDto3.setParentId(matelClass3.getParentId());
                    results3.add(matelClassDto3);
                }
                matelClassDto2.setChildren(results3);
                results2.add(matelClassDto2);
            }
            matelClassDto1.setChildren(results2);
            results.add(matelClassDto1);
        }
        return results;
    }

    @Override
    public List<HashMap<String, String>> selectAllMaterialTips() {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        List<MatelClass> matelClasses = selectAll();
        for (MatelClass matelClass : matelClasses) {
            HashMap<String, String> map = new HashMap<>();
            if (matelClass.getMaterial() != null) {
                map.put("value", matelClass.getMaterial());
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public List<MatelClass> selectByParentId(Long id) {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("parent_id", id);
        map.put("is_delete", 0);
        return matelClassMapper.selectByMap(map);
    }

    @Override
    public List<MatelClass> selectAll() {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("is_delete", 0);
        return matelClassMapper.selectByMap(map);
    }

    @Override
    public List<MatelClass> selectByMaterial(String material) {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("material", material);
        map.put("parent_id", 0);
        map.put("is_delete", 0);
        return matelClassMapper.selectByMap(map);
    }

    @Override
    public List<MatelClass> selectByAllMaterial(String material) {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("material", material);
        map.put("is_delete", 0);
        return matelClassMapper.selectByMap(map);
    }

    @Override
    public void addMaterial(MatelClass matelClass) {
        try {
            matelClass.setId(GeneratorCodeUtil.generateKey());
            matelClassMapper.insert(matelClass);
        } catch (Exception e) {
            log.error("添加材料分类异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteMatelClassById(Long id) {
        try {
            matelClassMapper.deleteById(id);
        } catch (Exception e) {
            log.error("删除材料分类异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean editMatelClass(MatelClass matelClass) {
        try {
            return matelClassMapper.updateById(matelClass) == 1;
        } catch (Exception e) {
            log.error("编辑模具种类异常");
            e.printStackTrace();
            throw e;
        }
    }


}
