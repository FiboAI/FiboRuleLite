package com.fibo.rule.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
@ApiModel("返回对象统一封装")
public class ResponseEntityDto<T> implements Serializable {

    private static final long serialVersionUID = -720807478055084231L;

    @ApiModelProperty("状态:1成功, 0失败")
    private String status;
    @ApiModelProperty("错误码")
    private String error;
    @ApiModelProperty("错误消息")
    private String msg;
    @ApiModelProperty("返回数据")
    private transient T data;

    public ResponseEntityDto() {

    }

    public ResponseEntityDto(String status) {
        this.status = status;
    }

    public ResponseEntityDto(String status, String error) {
        this.status = status;
        this.error = error;
    }

    public ResponseEntityDto(String status, T data) {
        this.status = status;
        this.data = data;
    }

    public ResponseEntityDto(String status, String error, String msg, T data) {
        this.status = status;
        this.error = error;
        this.msg = msg;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public ResponseEntityDto<T> setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getError() {
        return error;
    }

    public ResponseEntityDto<T> setError(String error) {
        this.error = error;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResponseEntityDto<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }






}