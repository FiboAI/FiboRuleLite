package com.fibo.rule.server.exception;

public interface IErrorCode {
    int getCode();

    String getFormatMsg(Object... params);
}
