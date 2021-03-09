package com.mic.eis.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mic.eis.domain.model.OptLog;
import com.mic.eis.domain.vo.OptLogQueryVo;

import java.util.List;

/**
 * log 服务接口实现类
 * @author blake.wang
 * @date 2020-07-10
 */
public interface OptLogService extends IService<OptLog> {

    /**
     * 通过用户名 模糊查询 log 表
     * @param username
     * @return
     */
    List<String> selectUsernameByUsername(String username);

    /**
     * 通过用户名  查询 log 表
     * @param optLogQueryVo
     * @return
     */
    PageInfo<OptLog> selectOptLogByUsernamePage(OptLogQueryVo optLogQueryVo);

    /**
     * 通过id 删除 log
     * @param id
     */
    void deleteOptLogById(Long id);

    /**
     * 批量删除 log 通过log id
     * @param optLogIds
     */
    void batchDeleteOptLog(List<Long> optLogIds);

}
