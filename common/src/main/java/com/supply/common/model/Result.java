package com.supply.common.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.io.Serializable;


/**
  * @description 响应信息主体.
  * @author wjd
  * @date 2022/7/7
  */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;


    private int code = HttpStatus.OK.value();

    private String message;

    private T data;

    public static<T> Result<T> ok() {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.OK.value());
        result.setMessage("操作成功");
        return result;
    }

    public static<T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.OK.value());
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    public static<T> Result<T> ok(String msg, T data) {
        Result<T> result = new Result<T>();
        result.setCode(HttpStatus.OK.value());
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    public static<T> Result<T> error() {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常,请联系管理员");
    }

    public static<T> Result<T> error(String msg) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }

    public static<T> Result<T> error(String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    public static<T> Result<T> error(int code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

    public boolean isOk() {
        return this.code == HttpStatus.OK.value();
    }
}
