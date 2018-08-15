package com.bac.utils;

import java.io.Serializable;

/*
返回结果封装, 实现可序列化,生成有参构造方法,get/set方法
 */
public class BacResult implements Serializable {
    private boolean success;
    private String message;


    public BacResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
