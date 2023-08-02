package com.dingding.mid.exception;

/**
 * 流程异常封装
 */
public class WorkFlowException extends RuntimeException {
    public WorkFlowException(String message) {
        super(message);
    }
}
