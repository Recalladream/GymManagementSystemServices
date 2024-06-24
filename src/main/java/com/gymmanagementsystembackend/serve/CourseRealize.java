package com.gymmanagementsystembackend.serve;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmanagementsystembackend.dao.CourseMapper;
import com.gymmanagementsystembackend.domain.CoachTable;
import com.gymmanagementsystembackend.domain.CourseTable;
import com.gymmanagementsystembackend.exception.BusinessException;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.RedisDataModel;
import com.gymmanagementsystembackend.model.SendPageModel;
import com.gymmanagementsystembackend.serve.itf.CourseInterface;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.ServeTool;
import com.gymmanagementsystembackend.tool.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseRealize implements CourseInterface {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private ServeTool serveTool;

    @Override
    public void addCourse(CourseTable courseTable) {
        String mes="";
        try {
            boolean checkout=true;

            if (courseTable.getName()==null){checkout=false;mes="课程名不能为空！";}
            if (courseTable.getType()==null){checkout=false;mes="课程类型不能为空！";}

            if (checkout==true){

                LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
                lqwCourse.eq(CourseTable::getName,courseTable.getName());
                lqwCourse.eq(CourseTable::getType,courseTable.getType());

                CourseTable courseTable1=courseMapper.selectOne(lqwCourse);
                if (courseTable1==null){
                    SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();
                    long y_id=snowflakeIdWorker.nextId();
                    String n_id=new String(Long.toString(y_id)).substring(12,18);

                    courseTable.setCourseId(n_id);

                    courseMapper.insert(courseTable);

                    serveTool.updateRedisDataStatus(Code.course_check_key,Code.course_check_update_value);

                    return;
                }else {
                    mes="此课程已存在!";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.course_fail,mes);
    }

    @Override
    public void deleteCourse(int id) {
        try {
            courseMapper.deleteById(id);

            serveTool.updateRedisDataStatus(Code.course_check_key,Code.course_check_update_value);
            serveTool.updateRedisDataStatus(Code.giveLessons_check_key,Code.giveLessons_check_update_value);
            serveTool.updateRedisDataStatus(Code.sigNinClass_check_key,Code.sigNinClass_check_update_value);
            serveTool.updateRedisDataStatus(Code.subscribeClass_check_key,Code.subscribeClass_check_update_value);

        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }

    @Override
    public void updateCourse(CourseTable courseTable) {
        String mes="";
        try {
            boolean checkout=true;
            if (courseTable.getCourseId()==null){checkout=false;mes="课程id不能为空！";}
            if (courseTable.getName()==null){checkout=false;mes="课程名不能为空！";}
            if (courseTable.getType()==null){checkout=false;mes="课程类型不能为空！";}

            if (checkout==true){
                LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
                lqwCourse.ne(CourseTable::getId,courseTable.getId());
                lqwCourse.eq(CourseTable::getName,courseTable.getName());
                lqwCourse.eq(CourseTable::getType,courseTable.getType());

                CourseTable courseTable1=courseMapper.selectOne(lqwCourse);

                if (courseTable1==null){
                    courseMapper.updateById(courseTable);

                    serveTool.updateRedisDataStatus(Code.course_check_key,Code.course_check_update_value);
                    serveTool.updateRedisDataStatus(Code.giveLessons_check_key,Code.giveLessons_check_update_value);
                    serveTool.updateRedisDataStatus(Code.sigNinClass_check_key,Code.sigNinClass_check_update_value);
                    serveTool.updateRedisDataStatus(Code.subscribeClass_check_key,Code.subscribeClass_check_update_value);

                    return;
                }else {
                    mes="此课程已存在!";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.course_fail,mes);
    }

    @Override
    public SendPageModel getPageCourse(GetPageModel getPageModel) {
        try {
            String condition="";
            if (getPageModel.getCourseName()!=null&&!"".equals(getPageModel.getCourseName())){
                condition=getPageModel.getCourseName();
            }
            String page=String.valueOf(getPageModel.getPage());
            String size=String.valueOf(getPageModel.getSize());
            String dataKey=Code.course_data_prefix+"_"+page+"_"+size+"_"+condition;

            if (serveTool.checkManagerRedisData(Code.course_check_key,Code.course_check_notUpdate_value)){
                SendPageModel sendPageModel=serveTool.<SendPageModel>getManagerRedisData(dataKey,SendPageModel.class);

                if (sendPageModel!=null){
                    return sendPageModel;
                }
            }

            SendPageModel sendPageModel=new SendPageModel();
            IPage iPage=new Page(getPageModel.getPage(), getPageModel.getSize());

            LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
            if (!condition.isEmpty()){
                lqwCourse.eq(CourseTable::getName,getPageModel.getCourseName());
            }

            List<CourseTable> courseTableList=courseMapper.selectList(iPage,lqwCourse);

            sendPageModel.setPageList(courseTableList.toArray(new CourseTable[0]));
            sendPageModel.setTotal((int) iPage.getTotal());

            RedisDataModel<SendPageModel> redisDataModel=new RedisDataModel<>();
            redisDataModel.setData(sendPageModel);
            redisDataModel.setKey(dataKey);
            redisDataModel.setTimeout(60*60*24);
            serveTool.addManagerRedisData(redisDataModel);
            serveTool.updateRedisDataStatus(Code.course_check_key,Code.coach_check_notUpdate_value);

            return sendPageModel;
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }
}
