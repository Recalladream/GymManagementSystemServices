package com.gymmanagementsystembackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gymmanagementsystembackend.domain.RegisterTable;
import com.gymmanagementsystembackend.domain.SigninTable;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SigninMapper extends BaseMapper<SigninTable> {
}
