package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mic.eis.domain.dto.ProtDto;
import com.mic.eis.domain.model.*;
import com.mic.eis.domain.vo.ProtQueryVo;
import com.mic.eis.mapper.ProtMapper;
import com.mic.eis.mapper.ProtStatusMapper;
import com.mic.eis.service.ProtService;
import com.mic.eis.service.UserFollowProtService;
import com.mic.eis.service.UserProtService;
import com.mic.eis.util.HttpContextUtil;
import com.mic.eis.utils.GeneratorCodeUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-07-09 9:34
 */
@Service
public class ProtServiceImpl extends ServiceImpl<ProtMapper, Prot> implements ProtService {

    @Resource
    private ProtMapper protMapper;

    @Resource
    private ProtStatusMapper protStatusMapper;

    @Resource
    private UserProtService userProtService;

    @Resource
    private UserFollowProtService userFollowProtService;

    /**
     * 通过 id 查询项目
     * @param id 项目id
     * @return 项目实体
     */
    @Override
    public Prot selectProtByID(Long id) {
        return protMapper.selectById(id);
    }

    /**
     * 过ParentId查询项目
     * @param id
     * @return
     */
    @Override
    public List<Prot> selectProtByParentId(Long id) {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("parent_id", id);
        map.put("is_delete", 0);
        return protMapper.selectByMap(map);
    }

    @Override
    public List<Prot> selectProtByName(String name) {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("name", name);
        map.put("parent_id", 0);
        map.put("is_delete", 0);
        return protMapper.selectByMap(map);
    }

    @Override
    public List<Prot> selectProtByCode(String code) {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("code", code);
        map.put("parent_id", 0);
        map.put("is_delete", 0);
        return protMapper.selectByMap(map);
    }

    @Override
    public PageInfo<ProtDto> findMainProtByPage(ProtQueryVo protQueryVo) {
        PageHelper.startPage(protQueryVo.getPageNum(), protQueryVo.getPageSize());
        QueryWrapper<Prot> wrapper = new QueryWrapper<>();
        if (protQueryVo.getStatus() == null) {
            wrapper.like("name", protQueryVo.getProtName())
                    .eq("parent_id", 0)
                    .eq("is_delete", 0);
        } else {
            wrapper.like("name", protQueryVo.getProtName())
                    .eq("parent_id", 0)
                    .eq("status", protQueryVo.getStatus())
                    .eq("is_delete", 0);
        }
        List<Prot> prots = protMapper.selectList(wrapper);
        List<ProtDto> protDtos = new ArrayList<>();
        List<UserFollowProt> userFollowProts = userFollowProtService.selectByUserId(HttpContextUtil.getCurrentUser().getId());
        for (Prot prot : prots) {
            ProtDto protDto = new ProtDto();
            protDto.setCode(prot.getCode());
            protDto.setName(prot.getName());
            protDto.setStatus(prot.getStatus());
            protDto.setId(prot.getId());
            protDto.setRemark(prot.getRemark());
            protDto.setParentId(prot.getParentId());
            if(isFollow(prot, userFollowProts)){
                protDto.setFollow("0");
            } else {
                protDto.setFollow("1");
            }
            protDtos.add(protDto);
        }
        return new PageInfo<>(protDtos);
    }

    @Override
    public List<HashMap<String, Object>> selectAllProt() {
        List<HashMap<String, Object>> result = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("parent_id", 0);
        map.put("is_delete", 0);
        List<Prot> prots = protMapper.selectByMap(map);
        for (Prot prot : prots) {
            if (prot.getName() != null) {
                HashMap<String, Object> mapResult = new HashMap<>();
                mapResult.put("value", prot.getId());
                mapResult.put("label", prot.getName());
                result.add(mapResult);
            }
        }
        return result;
    }

    @Override
    public List<HashMap<String, Object>> selectAllSonProt(Long protId) {
        List<HashMap<String, Object>> resultList = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("is_delete", 0);
        map.put("parent_id", protId);
        List<Prot> prots = protMapper.selectByMap(map);
        for (Prot prot : prots) {
            if (prot.getName() != null) {
                HashMap<String, Object> mapResult = new HashMap<>();
                mapResult.put("label", prot.getName());
                mapResult.put("value", prot.getId());
                resultList.add(mapResult);
            }
        }
        return resultList;
    }


    @Override
    public List<ProtDto> queryByNameAndStatus(ProtQueryVo protQueryVo) {
        QueryWrapper<Prot> wrapper = new QueryWrapper<>();
        if (protQueryVo.getStatus() == null){
            wrapper.like("name", protQueryVo.getProtName())
                    .eq("parent_id", 0);
        }else {
            wrapper.like("name", protQueryVo.getProtName())
                    .eq("status", protQueryVo.getStatus())
                    .eq("parent_id", 0);
        }
        List<Prot> prots = protMapper.selectList(wrapper);
        List<ProtDto> protDtos = new ArrayList<>();
        for (Prot prot : prots) {
            ProtDto protDto = new ProtDto();
            protDto.setId(prot.getId());
            protDto.setName(prot.getName());
            protDto.setCode(prot.getCode());
            protDto.setStatus(prot.getStatus());
            protDto.setRemark(prot.getRemark());
            protDto.setParentId(prot.getParentId());
            protDto.setChildren(selectProtByParentId(prot.getId()));
            protDtos.add(protDto);
        }
        return protDtos;
    }


    /**
     * 添加项目
     * @param prot
     */
    @Override
    public void addProt(Prot prot) {
        try {
            prot.setId(GeneratorCodeUtil.generateKey());
            protMapper.insert(prot);
            if(prot.getParentId() == 0){  // 如果是主项目
                // 添加到 project summary/status
                ProtStatus protStatus = new ProtStatus();
                protStatus.setId(GeneratorCodeUtil.generateKey());
                protStatus.setMainProtId(prot.getId());
                protStatus.setParentId(0L);
                protStatus.setPlanOrFact("Plan");
                ProtStatus protStatusSon = new ProtStatus();
                protStatusSon.setId(GeneratorCodeUtil.generateKey());
                protStatusSon.setMainProtId(prot.getId());
                protStatusSon.setParentId(protStatus.getId());
                protStatusSon.setPlanOrFact("Fact");
                protStatusMapper.insert(protStatus);
                protStatusMapper.insert(protStatusSon);
                // 获取当前用户的id
                User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
                // 先根据  user_id prot_id 删除 用户项目表 的数据
                if (loginUser.getId() == 0L) {  // 如果是超级管理员
                    UserProt userProt = new UserProt();
                    userProt.setUserId(0L);
                    userProt.setProtId(prot.getId());
                    userProt.setMember(1);  // 0:普通用户  1:项目管理员
                    userProtService.addUserProt(userProt);
                }else {  // 如果是 管理员
                    UserProt userProt = new UserProt();
                    userProt.setUserId(0L);
                    userProt.setProtId(prot.getId());
                    userProt.setMember(1);  // 0:普通用户  1:项目管理员
                    userProtService.addUserProt(userProt);
                    userProt.setUserId(loginUser.getId());
                    userProtService.addUserProt(userProt);
                }
            } else { // 如果是 子项目
                // 添加到 project summary/status
                ProtStatus protStatus = new ProtStatus();
                protStatus.setId(GeneratorCodeUtil.generateKey());
                protStatus.setMainProtId(prot.getId());
                protStatus.setPlanOrFact("Plan");
                protStatus.setParentId(0L);
                ProtStatus protStatusSon = new ProtStatus();
                protStatusSon.setId(GeneratorCodeUtil.generateKey());
                protStatusSon.setParentId(protStatus.getId());
                protStatusSon.setMainProtId(prot.getId());
                protStatusSon.setPlanOrFact("Fact");
                protStatusMapper.insert(protStatus);
                protStatusMapper.insert(protStatusSon);
            }
        }catch (Exception e){
            log.error("添加项目异常");
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * 通过 id 删除项目
     * @param id
     */
    @Override
    public void deleteProtById(Long id) {
        try {
            protMapper.deleteById(id);
            // 删除对应的 子项目
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("parent_id", id);
            map1.put("is_delete", 0);
            List<Prot> prots = protMapper.selectByMap(map1);
            for (Prot prot : prots) {
                deleteSubProtById(prot.getId());
            }
            userProtService.deleteUserProtByProtId(id);
            userFollowProtService.deleteByProtId(id);
            HashMap<String, Object> map = new HashMap<>();
            map.put("main_prot_id", id);
            map.put("is_delete", 0);
            protStatusMapper.deleteByMap(map);
        }catch (Exception e){
            log.error("删除项目异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteSubProtById(Long id) {
        try {
            protMapper.deleteById(id);
            HashMap<String, Object> map = new HashMap<>();
            map.put("main_prot_id", id);
            map.put("is_delete", 0);
            protStatusMapper.deleteByMap(map);
        }catch (Exception e){
            log.error("删除子项目异常");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     *  批量删除项目
     * @param deptIds
     */
    @Override
    public void batchDeleteProt(List<Long> deptIds) {
        try {
            protMapper.deleteBatchIds(deptIds);
        }catch (Exception e){
            log.error("批量删除项目异常");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 编辑 项目
     * @param prot
     */
    @Override
    public boolean editProt(Prot prot) {
        try {
            return protMapper.updateById(prot) == 1;
        }catch (Exception e){
            log.error("编辑项目异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Prot> selectByNameExcludeProt(Prot prot) {
        QueryWrapper<Prot> wrapper = new QueryWrapper<>();
        wrapper.eq("name", prot.getName())
                .eq("is_delete", 0)
                .eq("parent_id", 0)
                .ne("id", prot.getId());
        return protMapper.selectList(wrapper);
    }

    @Override
    public List<Prot> selectByCodeExcludeProt(Prot prot) {
        QueryWrapper<Prot> wrapper = new QueryWrapper<>();
        wrapper.eq("code", prot.getCode())
                .eq("is_delete", 0)
                .eq("parent_id", 0)
                .ne("id", prot.getId());
        return protMapper.selectList(wrapper);
    }

    @Override
    public List<ProtDto> selectByUserIdReturnProt() {
        Long currUserId = HttpContextUtil.getCurrentUser().getId();
        List<UserFollowProt> userFollowProts = userFollowProtService.selectByUserId(currUserId);
        List<ProtDto> protDtos = new ArrayList<>();
        for (UserFollowProt userFollowProt : userFollowProts) {
            Prot prot = selectProtByID(userFollowProt.getProtId());
            ProtDto protDto = new ProtDto();
            protDto.setName(prot.getName());
            protDto.setCode(prot.getCode());
            protDto.setId(prot.getId());
            protDto.setStatus(prot.getStatus());
            protDto.setRemark(prot.getRemark());
            protDto.setParentId(prot.getParentId());
            protDto.setFollow("0");  // 默认就是0:关注 因为是从关注表中获取的
            protDtos.add(protDto);

        }
        return protDtos;
    }

    /**
     * 判断这条数据，是否是用户 关注的
     * @param prot 项目实体
     * @param userFollowProts  用户关注的所有项目
     * @return true or false
     */
    private boolean isFollow(Prot prot, List<UserFollowProt> userFollowProts){
        boolean repeat = false;
        for (UserFollowProt userFollowProt : userFollowProts) {
            if (userFollowProt.getProtId().equals(prot.getId())){
                repeat = true;
            }
        }
        return repeat;
    }

}
