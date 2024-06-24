package com.gymmanagementsystembackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gymmanagementsystembackend.domain.CourseTable;
import com.gymmanagementsystembackend.domain.RegisterTable;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper extends BaseMapper<CourseTable> {
}
