package com.siwei.darwin.common.api;

public interface IProjectRespCode extends IRespCode {

    String reg = "\\{\\}";

    Integer INTERNAL_PROJECT_CODE = 10;


    Integer getProjectCode();

    @Override
    default Integer getRespCode() {
        return this.getProjectCode() * 100000 + this.getCode();
    }

}

