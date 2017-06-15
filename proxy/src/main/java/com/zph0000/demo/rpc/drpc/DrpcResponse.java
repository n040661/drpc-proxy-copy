package com.zph0000.demo.rpc.drpc;

import com.zph0000.demo.util.SerializableUtil;

/**
 * Created by zph  Date：2017/6/12.
 */
public class DrpcResponse {

    private int code;//code

    private String msg;//返回错误信息

    private Object data;//返回参数

    private String exception;//异常

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setException(Throwable exception) {
        this.exception = SerializableUtil.ObjToStr(exception);
    }

    public String getException() {
        return exception;
    }

    public boolean hasException() {
        return exception != null;
    }
}
