package com.siwei.darwin.common.api;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 前后端交互数据标准
 */
@Data
@Accessors(chain = true)
@ToString
public class ApiEntity<T> implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 消息说明
     */
    private String message;

    /**
     * 结果对象
     */
    private T data;


    private ApiEntity<T> success() {
        return this.setCode(200).setMessage("ok");
    }

    private ApiEntity<T> message(Integer code, String msg){
        return this.setCode(code).setMessage(msg);
    }

    private ApiEntity<T> data(T t){
        return this.success().setData(t);
    }

    private ApiEntity<T> data(T t, Integer code, String msg){
        return this.setData(t).setCode(code).setMessage(msg);
    }

    public static <T> ApiEntity<T> ok(){
        return new ApiEntity<T>().success();
    }


    public static <T> ApiEntity<T> ok(T t){
        return new ApiEntity<T>().data(t);
    }

    public static <T> ApiEntity<T> fail(ApiException apiException){
        return new ApiEntity<T>().message(apiException.code, apiException.getMessage());
    }

    public static <T> ApiEntity<T> fail(IProjectRespCode errorCode){
        return new ApiEntity<T>().message(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> ApiEntity<T> fail(T t, IProjectRespCode errorCode){
        return new ApiEntity<T>().data(t, errorCode.getCode(), errorCode.getMessage());
    }

}
