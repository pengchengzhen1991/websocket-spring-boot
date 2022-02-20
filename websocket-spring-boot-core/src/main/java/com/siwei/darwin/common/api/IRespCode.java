package com.siwei.darwin.common.api;


import com.siwei.darwin.common.enums.ICode;

public interface IRespCode extends ICode {

    Integer SUCCESS_CODE = 0;

    @Override
    Integer getCode();

    String getMessage();

    @Override
    default String getName() {
        return this.getMessage();
    }

    default Integer getRespCode() {
        return this.getCode();
    }

}
