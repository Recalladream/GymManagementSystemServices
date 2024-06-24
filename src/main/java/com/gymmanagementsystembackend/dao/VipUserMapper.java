package com.gymmanagementsystembackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gymmanagementsystembackend.domain.VipUserTable;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VipUserMapper extends BaseMapper<VipUserTable> {
}
