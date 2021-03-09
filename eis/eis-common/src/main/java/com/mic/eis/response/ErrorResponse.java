package com.mic.eis.response;

/**
 * @author calisto
 * @date 2020-07-07 11:09 上午
 */
public class ErrorResponse<T> extends ResponseEntity<T> {
    /**
     * 设置业务状态为失败
     * @author calisto
     * @date 2019-10-07 17:46:18
     * @param
     **/
    public ErrorResponse() {
        super();
        this.code = BizCode.FAILED.getCode();
        this.message = BizCode.FAILED.getMessage();
    }

    /**
     * 添加具体错误代码
     * @author calisto
     * @date 2019-10-09 13:21:34
     * @param
     **/
    public ErrorResponse addBizCode(BizCode bizCode) {
        this.code = bizCode.getCode();
        this.message = bizCode.getMessage();
        return this;
    }

    /**
     * 添加返回数据
     * @param data
     * @return
     */
    public ErrorResponse addData(T data) {
        this.data = data;
        return this;
    }
}
