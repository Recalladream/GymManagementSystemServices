package com.gymmanagementsystembackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gymmanagementsystembackend.domain.MaterialTable;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MaterialMapper extends BaseMapper<MaterialTable> {
}
