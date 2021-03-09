package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mic.eis.domain.model.OptLog;
import com.mic.eis.domain.vo.OptLogQueryVo;
import com.mic.eis.mapper.OptLogMapper;
import com.mic.eis.service.OptLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author blake.wang
 * @date 2020-07-10 9:40
 */
@Service
public class OptLogServiceImpl extends ServiceImpl<OptLogMapper, OptLog> implements OptLogService {

    @Resource
    private OptLogMapper optLogMapper;

    /**
     * 通过用户名 模糊查询 log 表
     * @param username
     * @return
     */
    @Override
    public List<String> selectUsernameByUsername(String username) {
        QueryWrapper<OptLog> wrapper = new QueryWrapper<>();
        wrapper.like("username", username)
                .select("DISTINCT username");
        List<String> result;
        try {
            result = optLogMapper.selectList(wrapper)
                    .stream().map(OptLog::getUsername)
                    .collect(Collectors.toList());
        }catch (Exception e){
            log.error("通过用户名模糊查询log表异常");
            e.printStackTrace();
            throw e;
        }
        return result;
    }


    /**
     * 通过用户名  查询 log 表
     * @param optLogQueryVo
     * @return
     */
    @Override
    public PageInfo<OptLog> selectOptLogByUsernamePage(OptLogQueryVo optLogQueryVo) {
        PageHelper.startPage(optLogQueryVo.getPageNum(), optLogQueryVo.getPageSize());
        QueryWrapper<OptLog> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(OptLog::getUsername, optLogQueryVo.getUsername());
        List<OptLog> optLogs = optLogMapper.selectList(wrapper);
        return new PageInfo<>(optLogs);
    }

    /**
     * 通过id 删除 log
     * @param id
     */
    @Override
    public void deleteOptLogById(Long id) {
        try {
            optLogMapper.deleteById(id);
        }catch (Exception e){
            log.error("通过id删除log异常");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 批量删除 log 通过log id
     * @param optLogIds
     */
    @Override
    public void batchDeleteOptLog(List<Long> optLogIds) {
        try {
            optLogMapper.deleteBatchIds(optLogIds);
        }catch (Exception  e){
            log.error("通过id批量删除log异常");
            e.printStackTrace();
            throw e;
        }
    }




}
