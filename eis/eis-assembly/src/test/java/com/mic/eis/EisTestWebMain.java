package com.mic.eis;

import com.mic.eis.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author calisto
 * @date 2020-07-17 10:47 上午
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EisTestWebMain {

    @Resource
    private RedisUtil redisUtil;

    /**
     * 远程Redis测试
     */
    @Test
    public void testRedis() {
        redisUtil.set("test", 123456);
        System.out.println(redisUtil.get("test"));
    }
}
