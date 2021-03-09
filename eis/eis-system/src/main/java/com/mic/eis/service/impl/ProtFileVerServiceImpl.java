package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.model.ProtFileVer;
import com.mic.eis.domain.vo.ProtFileVerQueryVo;
import com.mic.eis.mapper.ProtFileVerMapper;
import com.mic.eis.service.ProtFileVerService;
import com.mic.eis.utils.GeneratorCodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-09-07 11:08
 */
@Service
public class ProtFileVerServiceImpl extends ServiceImpl<ProtFileVerMapper, ProtFileVer> implements ProtFileVerService {

    @Resource
    private ProtFileVerMapper protFileVerMapper;

    @Override
    public List<ProtFileVer> selectByProtIdAndCategoryAndVer(ProtFileVerQueryVo protFileVerQueryVo) {
        QueryWrapper<ProtFileVer> wrapper = new QueryWrapper<>();
        if (protFileVerQueryVo.getFileVer() == null) {
            wrapper.eq("prot_id", protFileVerQueryVo.getProtId())
                    .eq("category", protFileVerQueryVo.getCategory())
                    .eq("is_delete", 0)
                    .ne("file_ver", "now")
                    .like("file_ver", "");
        } else {
            wrapper.eq("prot_id", protFileVerQueryVo.getProtId())
                    .eq("category", protFileVerQueryVo.getCategory())
                    .eq("is_delete", 0)
                    .eq("file_ver", protFileVerQueryVo.getFileVer());
        }
        return protFileVerMapper.selectList(wrapper);
    }

    @Override
    public void addProtFileVer(ProtFileVer protFileVer) {
        try {
            if (protFileVer.getFileVer().equals("now")) {
                deleteProtFileVer(protFileVer);
            }
            protFileVer.setId(GeneratorCodeUtil.generateKey());
            protFileVerMapper.insert(protFileVer);
        } catch (Exception e){
            log.error("添加 子项目 文件 版本异常");
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public void deleteProtFileVer(ProtFileVer protFileVer) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            // 自定义删除
            map.put("prot_id", protFileVer.getProtId());
            map.put("file_ver", protFileVer.getFileVer());
            map.put("category", protFileVer.getCategory());
            protFileVerMapper.deleteByMap(map);
        } catch (Exception e) {
            log.error("删除ProtFileVer异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean editProtFileVer(ProtFileVer protFileVer) {
        try {
            return protFileVerMapper.updateById(protFileVer) == 1;
        } catch (Exception e) {
            log.error("编辑ProtFileVer异常");
            e.printStackTrace();
            throw e;
        }
    }


}
