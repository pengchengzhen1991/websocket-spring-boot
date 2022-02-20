package com.siwei.darwin.common.api;


public class ApiException extends RuntimeException{

    public int code;

    public ApiException() {}

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(IProjectRespCode exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
    }

    public ApiException(IProjectRespCode exception, Throwable cause) {
        super(exception.getMessage(), cause);
        this.code = exception.getCode();
    }

    public ApiException(IProjectRespCode exception, String id) {
        super(id + " " + exception.getMessage());
        this.code = exception.getCode();
    }

    public ApiException(IProjectRespCode exception, Long id) {
        super(String.valueOf(id) + " " + exception.getMessage());
        this.code = exception.getCode();
    }

}
