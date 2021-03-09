package com.mic.eis.config;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author calisto
 * @date 2020-07-07 10:53 上午
 */
public class CustonSessionIdGenerator implements SessionIdGenerator {

    @Override
    public Serializable generateId(Session session) {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
