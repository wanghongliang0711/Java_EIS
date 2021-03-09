package com.mic.eis.response;

import lombok.Data;
import lombok.Getter;

/**
 * @author calisto
 * @date 2020-07-07 11:07 上午
 */

@Getter
public class ResponseEntity<T> {

    /**
     * 业务处理状态
     */
    protected String code;

    /**
     * message
     */
    protected String message;

    /**
     * 数据
     */
    protected T data;
}
