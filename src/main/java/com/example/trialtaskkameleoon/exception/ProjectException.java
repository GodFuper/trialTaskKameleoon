package com.example.trialtaskkameleoon.exception;

import lombok.Getter;

@Getter
public class ProjectException extends Exception {
    private ErrorCode errorCode;

    public ProjectException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.name();
    }
}
