package com.gymmanagementsystembackend;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmanagementsystembackend.dao.GiveLessonsMapper;
import com.gymmanagementsystembackend.domain.GiveLessonsTable;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.MD5;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class ServiceTest {
    @Autowired
    private GiveLessonsMapper giveLessonsMapper;

//    @Test
//    public void deleteExpiredCourses(){
//        try {
//            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
//            Date nowdate=new Date();
//            simpleDateFormat.format(nowdate);
//
//            LambdaQueryWrapper<GiveLessonsTable> lqwGive=new LambdaQueryWrapper<>();
//            lqwGive.lt(GiveLessonsTable::getTime,simpleDateFormat.format(nowdate));
//
//            giveLessonsMapper.delete(lqwGive);
//        }catch (Exception e){
//            e.printStackTrace();
//            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
//        }
//    }
}
