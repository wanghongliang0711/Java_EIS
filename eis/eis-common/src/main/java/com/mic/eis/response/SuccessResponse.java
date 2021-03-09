package com.mic.eis.response;

/**
 * @author calisto
 * @date 2020-07-07 11:08 上午
 */
public class SuccessResponse<T> extends ResponseEntity<T> {

    /**
     * 设置业务状态为SUCCESS
     */
    public SuccessResponse() {
        super();
        this.code = BizCode.SUCCESS.getCode();
        this.message = BizCode.SUCCESS.getMessage();
    }

    /**
     * 添加返回数据
     * @param data
     * @return
     */
    public SuccessResponse addData(T data) {
        this.data = data;
        return this;
    }
}
