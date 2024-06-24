package com.gymmanagementsystembackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gymmanagementsystembackend.domain.CoachTable;
import com.gymmanagementsystembackend.domain.RegisterTable;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoachMapper extends BaseMapper<CoachTable> {
}
