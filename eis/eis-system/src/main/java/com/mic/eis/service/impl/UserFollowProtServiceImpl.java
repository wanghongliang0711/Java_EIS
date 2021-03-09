package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.model.UserFollowProt;
import com.mic.eis.mapper.UserFollowProtMapper;
import com.mic.eis.service.UserFollowProtService;
import com.mic.eis.utils.GeneratorCodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author blake.wang
 * @date 2020-08-24 9:11
 */
@Service
public class UserFollowProtServiceImpl extends ServiceImpl<UserFollowProtMapper, UserFollowProt> implements UserFollowProtService {

    @Resource
    private UserFollowProtMapper userFollowProtMapper;

//    @Resource  // 不允许 protService 与 userFollowProtMapper 相互调用
//    private ProtService protService;


    @Override
    public List<UserFollowProt> selectByUserId(Long id) {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("user_id", id);
        map.put("is_delete", 0);
        return userFollowProtMapper.selectByMap(map);
    }

//    @Override
//    public List<ProtDto> selectByUserIdReturnProt() {
//        Long currUserId = HttpContextUtil.getCurrentUser().getId();
//        List<UserFollowProt> userFollowProts = selectByUserId(currUserId);
//        List<ProtDto> protDtos = new ArrayList<>();
//        for (UserFollowProt userFollowProt : userFollowProts) {
//            Prot prot = protService.selectProtByID(userFollowProt.getProtId());
//            ProtDto protDto = new ProtDto();
//            protDto.setName(prot.getName());
//            protDto.setCode(prot.getCode());
//            protDto.setId(prot.getId());
//            protDto.setStatus(prot.getStatus());
//            protDto.setRemark(prot.getRemark());
//            protDto.setParentId(prot.getParentId());
//            protDto.setFollow("0");  // 默认就是0:关注 因为是从关注表中获取的
//            protDtos.add(protDto);
//        }
//        return protDtos;
//    }

    @Override
    public void deleteByUserIdAndProtId(UserFollowProt userFollowProt) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            // 自定义删除
            map.put("user_id", userFollowProt.getUserId());
            map.put("prot_id", userFollowProt.getProtId());
            userFollowProtMapper.deleteByMap(map);
        } catch (Exception e) {
            log.error("删除用户关注项目表异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteByProtId(Long id) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            // 自定义删除
            map.put("prot_id", id);
            map.put("is_delete", 0);
            userFollowProtMapper.deleteByMap(map);
        } catch (Exception e) {
            log.error("删除用户关注项目表异常");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addUserFollowProt(UserFollowProt userFollowProt) {
        try{
            deleteByUserIdAndProtId(userFollowProt);
            userFollowProt.setId(GeneratorCodeUtil.generateKey());
            userFollowProtMapper.insert(userFollowProt);
        } catch (Exception e){
            log.error("添加用户关注项目表异常");
            e.printStackTrace();
            throw e;
        }
    }


}
