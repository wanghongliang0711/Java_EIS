package com.mic.eis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.domain.model.PartTest;
import com.mic.eis.mapper.PartTestMapper;
import com.mic.eis.service.PartTestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author calisto
 * @since 2020-08-07
 */
@Service
public class PartTestServiceImpl extends ServiceImpl<PartTestMapper, PartTest> implements PartTestService {

    @Resource
    private PartTestMapper partTestMapper;

}
