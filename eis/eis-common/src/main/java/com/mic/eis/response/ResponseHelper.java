package com.mic.eis.response;

/**
 * @author calisto
 * @date 2020-07-07 11:07 上午
 */
public class ResponseHelper {

    /**
     * 业务处理成功无返回值
     * @author calisto
     * @date 2019-10-07 16:56:25
     * @param
     **/
    public static SuccessResponse success() {
        return new SuccessResponse();
    }

    /**
     * 业务处理成功并返回数据
     * @author calisto
     * @date 2019-10-07 17:22:35
     * @param data
     **/
    public static <T> SuccessResponse<T> success(T data) {
        return new SuccessResponse<T>().addData(data);
    }

    /**
     * 业务处理失败默认错误代码
     * @author calisto
     * @date 2019-10-07 16:56:25
     * @param
     **/
    public static ErrorResponse failed() {
        return new ErrorResponse();
    }

    /**
     * 业务处理失败自定义错误代码
     * @author calisto
     * @date 2019-10-09 13:24:42
     * @param
     * @param bizCode
     **/
    public static ErrorResponse failed(BizCode bizCode) {
        return new ErrorResponse().addBizCode(bizCode);
    }

    /**
     * 业务处理失败并返回数据
     * @author calisto
     * @date 2019-10-07 17:22:35
     * @param data
     **/
    public static <T> ErrorResponse<T> failed(T data) {
        return new ErrorResponse<T>().addData(data);
    }

    /**
     * 业务处理失败返回数据并自定义错误代码
     * @author calisto
     * @date 2019-10-09 13:26:12
     * @param
     * @param data
     * @param bizCode
     **/
    public static <T> ErrorResponse<T> failed(T data, BizCode bizCode) {
        return new ErrorResponse<T>().addData(data).addBizCode(bizCode);
    }

}
