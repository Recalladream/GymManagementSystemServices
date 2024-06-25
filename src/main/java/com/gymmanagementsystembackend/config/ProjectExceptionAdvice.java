package com.gymmanagementsystembackend.config;

import com.gymmanagementsystembackend.exception.BusinessException;
import com.boot.exception.SystemException;
import com.gymmanagementsystembackend.model.ResultModel;
import com.gymmanagementsystembackend.tool.Code;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProjectExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResultModel doBusinessException(BusinessException bex){
        return new ResultModel(bex.getCode(),null,bex.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public ResultModel doSystemException(SystemException sex){
        return new ResultModel(sex.getCode(),null,sex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultModel doException(Exception ex){
        ex.printStackTrace();
        return new ResultModel(Code.unknown_error,null,"未知错误...");
    }

}
