package com.gymmanagementsystembackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gymmanagementsystembackend.domain.ManagerTable;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ManagerMapper extends BaseMapper<ManagerTable> {
}
