package com.mic.eis.service;

/**
 * @author calisto
 * @date 2020-08-07 9:26 上午
 */
public interface DataPermissionService {

    /**
     * 是否是项目负责人
     * @param userId
     * @param rootProtId
     * @return
     */
    Boolean isOwner(Long userId, Long rootProtId);

    /**
     * 是否是项目成员
     * @param userId
     * @param rootProtId
     * @return
     */
    Boolean isMember(Long userId, Long rootProtId);
}
