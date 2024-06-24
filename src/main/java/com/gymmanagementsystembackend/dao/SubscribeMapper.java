package com.gymmanagementsystembackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gymmanagementsystembackend.domain.RegisterTable;
import com.gymmanagementsystembackend.domain.SubscribeTable;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubscribeMapper extends BaseMapper<SubscribeTable> {
}
