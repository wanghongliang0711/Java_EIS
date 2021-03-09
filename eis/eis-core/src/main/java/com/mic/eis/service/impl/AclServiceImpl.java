package com.mic.eis.service.impl;

import com.mic.eis.domain.model.Acl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mic.eis.mapper.AclMapper;
import com.mic.eis.service.AclService;
import org.springframework.stereotype.Service;

/**
 * Acl 服务实现类
 *
 * @author calisto
 * @since 2020-07-07
 */
@Service
public class AclServiceImpl extends ServiceImpl<AclMapper, Acl> implements AclService {

}
