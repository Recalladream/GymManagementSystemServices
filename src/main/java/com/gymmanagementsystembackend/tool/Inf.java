package com.gymmanagementsystembackend.tool;

import com.gymmanagementsystembackend.model.ResultModel;

public class Inf {
    public static ResultModel unify(String code, Object data, String mes){
        return new ResultModel(code,data,mes);
    }
}
