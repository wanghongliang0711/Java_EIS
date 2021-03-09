package com.mic.eis.service.impl;

import com.mic.eis.mapper.ProtMapper;
import com.mic.eis.service.DataPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author calisto
 * @date 2020-08-07 9:27 上午
 */
@Service
public class DatePermissionServiceImpl implements DataPermissionService {

    /**
     * 项目成员
     */
    private final static int MEMBER = 0;

    /**
     * 项目负责人
     */
    private final static int OWNER = 1;

    @Resource
    private ProtMapper protMapper;

    @Override
    public Boolean isOwner(Long userId, Long rootProtId) {
        List<Long> protIds = protMapper.selectProtByUserId(userId, OWNER);
        for (Long protId : protIds) {
            if (rootProtId.equals(protId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean isMember(Long userId, Long rootProtId) {
        List<Long> protIds = protMapper.selectProtByUserId(userId, MEMBER);
        for (Long protId : protIds) {
            if (rootProtId.equals(protId)) {
                return true;
            }
        }
        return false;
    }
}
