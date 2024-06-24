package com.gymmanagementsystembackend.tool;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymmanagementsystembackend.dao.GiveLessonsMapper;
import com.gymmanagementsystembackend.domain.GiveLessonsTable;
import com.gymmanagementsystembackend.model.RedisDataModel;
import com.gymmanagementsystembackend.model.SendPageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class ServeTool{
    @Autowired
    private GiveLessonsMapper giveLessonsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void deleteExpiredCourses(){
        try {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            Date nowdate=new Date();
            simpleDateFormat.format(nowdate);

            LambdaQueryWrapper<GiveLessonsTable> lqwGive=new LambdaQueryWrapper<>();
            lqwGive.lt(GiveLessonsTable::getTime,simpleDateFormat.format(nowdate));

            int result=giveLessonsMapper.delete(lqwGive);

            if (result>0){
                this.updateRedisDataStatus(Code.giveLessons_check_key,Code.giveLessons_check_update_value);
                this.updateRedisDataStatus(Code.sigNinClass_check_key,Code.sigNinClass_check_update_value);
                this.updateRedisDataStatus(Code.subscribeClass_check_key,Code.subscribeClass_check_update_value);
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }


    public void updateRedisDataStatus(String checkkey,String value)throws Exception{

            stringRedisTemplate.opsForValue().set(checkkey,value,60*60*24, TimeUnit.SECONDS);
    }

    //管理者redis数据处理
    public Boolean checkManagerRedisData(String checkKey,String notUpdateValue)throws Exception{
        String ifUpdateValue=stringRedisTemplate.opsForValue().get(checkKey);

        if (ifUpdateValue!=null&&notUpdateValue!=null){
            if (ifUpdateValue.equals(notUpdateValue)){
                return true;
            }
        }
        return false;
    }

    public void addManagerRedisData(RedisDataModel redisDataModel)throws Exception{
        String jsonData=objectMapper.writeValueAsString(redisDataModel.getData());

        stringRedisTemplate.opsForValue().set(redisDataModel.getKey(),jsonData,redisDataModel.getTimeout(), TimeUnit.SECONDS);
    }

    public <T> T getManagerRedisData(String key,Class<T> type)throws Exception{

        String jsonData = stringRedisTemplate.opsForValue().get(key);

        if (jsonData!=null&&!"".equals(jsonData)){

            T redisData=objectMapper.readValue(jsonData,type);

            return redisData;
        }

        return null;
    }
}
