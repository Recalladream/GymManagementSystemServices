package com.gymmanagementsystembackend.serve;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmanagementsystembackend.dao.CoachMapper;
import com.gymmanagementsystembackend.dao.CourseMapper;
import com.gymmanagementsystembackend.dao.GiveLessonsMapper;
import com.gymmanagementsystembackend.domain.CoachTable;
import com.gymmanagementsystembackend.domain.CourseTable;
import com.gymmanagementsystembackend.domain.GiveLessonsTable;
import com.gymmanagementsystembackend.exception.BusinessException;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.GiveLessonsModel;
import com.gymmanagementsystembackend.model.RedisDataModel;
import com.gymmanagementsystembackend.model.SendPageModel;
import com.gymmanagementsystembackend.serve.itf.GiveLessonsInterface;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.ServeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class GiveLessonsRealize implements GiveLessonsInterface {
    @Autowired
    private GiveLessonsMapper giveLessonsMapper;

    @Autowired
    private CoachMapper coachMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ServeTool serveTool;

    @Override
    public void add(GiveLessonsTable giveLessonsTable) {
        String mes="";
        try {
            boolean checkout=true;

            if (giveLessonsTable.getNumber()<0){checkout=false;mes="课程人数不能小于0！";}
            if (giveLessonsTable.getCoachId()==null){checkout=false;mes="教练id不能为空!";}
            else {
                LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
                lqwCoach.eq(CoachTable::getCoachId,giveLessonsTable.getCoachId());
                CoachTable coachTable=coachMapper.selectOne(lqwCoach);
                if (coachTable==null){checkout=false;mes="此教练不存在！";}
            }
            if (giveLessonsTable.getCourseId()==null){checkout=false;mes="课程id不能为空！";}
            else {
                LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
                lqwCourse.eq(CourseTable::getCourseId,giveLessonsTable.getCourseId());
                CourseTable courseTable=courseMapper.selectOne(lqwCourse);
                if (courseTable==null){checkout=false;mes="此课程不存在！";}
            }
            if (giveLessonsTable.getTime()==null){checkout=false;mes="授课时间不能为空！";}
            else {
                // 定义日期格式
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                // 获取当前日期
                Date nowdate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                Calendar nowCalendar = Calendar.getInstance();
                nowCalendar.setTime(nowdate);

                Date subdate = simpleDateFormat.parse(giveLessonsTable.getTime());
                Calendar subCalendar = Calendar.getInstance();
                subCalendar.setTime(subdate);
                subCalendar.add(Calendar.DAY_OF_MONTH,1);

                // 计算天数差
                int diffInDays = 0;
                while (nowCalendar.before(subCalendar)) {
                    nowCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    diffInDays++;
                }

                if (diffInDays<=3){checkout=false;mes="授课日期应当大于当前日期3天";}
                else {
                    Date subtime=simpleDateFormat.parse(giveLessonsTable.getTime());
                    Calendar calendar=Calendar.getInstance();
                    calendar.setTime(subtime);
                    calendar.add(Calendar.DAY_OF_MONTH,1);
                    subtime=calendar.getTime();

                    giveLessonsTable.setTime(simpleDateFormat.format(subtime));
                }
            }
            String period=giveLessonsTable.getPeriod();
            if (period==null){
                checkout=false;mes="授课时间段不能为空！";
            }else if ("上午".equals(period)||"下午".equals(period)||"晚上".equals(period)){
            }else {checkout=false;mes="授课时间段不在规定范围！";}


            if (checkout==true){
                LambdaQueryWrapper<GiveLessonsTable> lqwGive=new LambdaQueryWrapper<>();
                lqwGive.eq(GiveLessonsTable::getCoachId,giveLessonsTable.getCoachId());
                lqwGive.eq(GiveLessonsTable::getCourseId,giveLessonsTable.getCourseId());
                lqwGive.eq(GiveLessonsTable::getTime,giveLessonsTable.getTime());
                lqwGive.eq(GiveLessonsTable::getPeriod,giveLessonsTable.getPeriod());

                GiveLessonsTable giveLessonsTable1=giveLessonsMapper.selectOne(lqwGive);
                if (giveLessonsTable1==null){
                    giveLessonsMapper.insert(giveLessonsTable);

                    serveTool.updateRedisDataStatus(Code.giveLessons_check_key,Code.giveLessons_check_update_value);
                    serveTool.updateRedisDataStatus(Code.sigNinClass_check_key,Code.sigNinClass_check_update_value);
                    serveTool.updateRedisDataStatus(Code.subscribeClass_check_key,Code.subscribeClass_check_update_value);

                    return;
                }else {
                    mes="授课信息已存在！";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.givelessons_fail,mes);
    }

    @Override
    public void delete(int id) {
        try {
            giveLessonsMapper.deleteById(id);

            serveTool.updateRedisDataStatus(Code.giveLessons_check_key,Code.giveLessons_check_update_value);
            serveTool.updateRedisDataStatus(Code.sigNinClass_check_key,Code.sigNinClass_check_update_value);
            serveTool.updateRedisDataStatus(Code.subscribeClass_check_key,Code.subscribeClass_check_update_value);
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }

    @Override
    public void update(GiveLessonsTable giveLessonsTable) {
        String mes="";
        try{
            boolean checkout=true;
            if (giveLessonsTable.getNumber()<0){checkout=false;mes="课程人数不能小于0！";}
            if (giveLessonsTable.getCoachId()==null){checkout=false;mes="教练id不能为空!";}
            else {
                LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
                lqwCoach.eq(CoachTable::getCoachId,giveLessonsTable.getCoachId());
                CoachTable coachTable=coachMapper.selectOne(lqwCoach);
                if (coachTable==null){checkout=false;mes="此教练不存在！";}
            }
            if (giveLessonsTable.getCourseId()==null){checkout=false;mes="课程id不能为空！";}
            else {
                LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
                lqwCourse.eq(CourseTable::getCourseId,giveLessonsTable.getCourseId());
                CourseTable courseTable=courseMapper.selectOne(lqwCourse);
                if (courseTable==null){checkout=false;mes="此课程不存在！";}
            }
            if (giveLessonsTable.getTime()==null||"".equals(giveLessonsTable.getTime())){checkout=false;mes="授课时间不能为空！";}
            else {
                // 定义日期格式
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                // 获取当前日期
                Date nowdate = new Date();
                Calendar nowCalendar = Calendar.getInstance();
                nowCalendar.setTime(nowdate);

                Date subdate = simpleDateFormat.parse(giveLessonsTable.getTime());
                Calendar subCalendar = Calendar.getInstance();
                subCalendar.setTime(subdate);
                subCalendar.add(Calendar.DAY_OF_MONTH,1);

                // 计算天数差
                int diffInDays = 0;
                while (nowCalendar.before(subCalendar)) {
                    nowCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    diffInDays++;
                }


                if (diffInDays<=3){checkout=false;mes="授课日期应当大于当前日期3天";}
                else {
                    Date subtime=simpleDateFormat.parse(giveLessonsTable.getTime());
                    Calendar calendar=Calendar.getInstance();
                    calendar.setTime(subtime);
                    calendar.add(Calendar.DAY_OF_MONTH,1);
                    subtime=calendar.getTime();

                    giveLessonsTable.setTime(simpleDateFormat.format(subtime));
                }
            }
            String period=giveLessonsTable.getPeriod();
            if (period==null){
                checkout=false;mes="授课时间段不能为空！";
            }else if ("上午".equals(period)||"下午".equals(period)||"晚上".equals(period)){
            }else {checkout=false;mes="授课时间段不在规定范围！";}

            if (checkout==true){
                LambdaQueryWrapper<GiveLessonsTable> lqwGive=new LambdaQueryWrapper<>();
                lqwGive.eq(GiveLessonsTable::getCoachId,giveLessonsTable.getCoachId());
                lqwGive.eq(GiveLessonsTable::getCourseId,giveLessonsTable.getCourseId());
                lqwGive.eq(GiveLessonsTable::getTime,giveLessonsTable.getTime());
                lqwGive.eq(GiveLessonsTable::getPeriod,giveLessonsTable.getPeriod());
                lqwGive.eq(GiveLessonsTable::getNumber,giveLessonsTable.getNumber());

                GiveLessonsTable giveLessonsTable1=giveLessonsMapper.selectOne(lqwGive);
                if (giveLessonsTable1==null){

                    giveLessonsMapper.updateById(giveLessonsTable);
                    serveTool.updateRedisDataStatus(Code.giveLessons_check_key,Code.giveLessons_check_update_value);
                    serveTool.updateRedisDataStatus(Code.sigNinClass_check_key,Code.sigNinClass_check_update_value);
                    serveTool.updateRedisDataStatus(Code.subscribeClass_check_key,Code.subscribeClass_check_update_value);

                    return;
                }else {
                    mes="授课信息已存在!";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.givelessons_fail,mes);
    }

    @Override
    public SendPageModel getPage(GetPageModel getPageModel) {
        try {
            String condition="";
            if ((getPageModel.getCourseName()!=null&&!"".equals(getPageModel.getCourseName()))||(getPageModel.getCoachName()==null||"".equals(getPageModel.getCoachName()))){
                condition=getPageModel.getCourseName()+"_"+getPageModel.getCoachName();
            }
            String page=String.valueOf(getPageModel.getPage());
            String size=String.valueOf(getPageModel.getSize());
            String dataKey=Code.giveLessons_data_prefix+"_"+page+"_"+size+"_"+condition;

            if (serveTool.checkManagerRedisData(Code.giveLessons_check_key,Code.giveLessons_check_notUpdate_value)){
                SendPageModel sendPageModel=serveTool.<SendPageModel>getManagerRedisData(dataKey,SendPageModel.class);

                if (sendPageModel!=null){
                    return sendPageModel;
                }
            }

            List<GiveLessonsModel> list=new ArrayList<>();
            SendPageModel sendPageModel=new SendPageModel();
            IPage iPage=new Page(getPageModel.getPage(), getPageModel.getSize());

            if ((getPageModel.getCoachName()==null||"".equals(getPageModel.getCoachName()))&&(getPageModel.getCourseName()==null||"".equals(getPageModel.getCourseName()))){
                List<GiveLessonsTable> giveLessonsTableList=giveLessonsMapper.selectList(iPage,null);

                for (GiveLessonsTable giveLessonsTable:giveLessonsTableList){
                    LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
                    lqwCoach.eq(CoachTable::getCoachId,giveLessonsTable.getCoachId());
                    CoachTable coachTable=coachMapper.selectOne(lqwCoach);

                    LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
                    lqwCourse.eq(CourseTable::getCourseId,giveLessonsTable.getCourseId());
                    CourseTable courseTable=courseMapper.selectOne(lqwCourse);

                    GiveLessonsModel giveLessonsModel=new GiveLessonsModel();
                    giveLessonsModel.setId(giveLessonsTable.getId());
                    giveLessonsModel.setCoachId(giveLessonsTable.getCoachId());
                    giveLessonsModel.setCoachName(coachTable.getName());
                    giveLessonsModel.setCourseId(giveLessonsTable.getCourseId());
                    giveLessonsModel.setCourseName(courseTable.getName());
                    giveLessonsModel.setTime(giveLessonsTable.getTime());
                    giveLessonsModel.setPeriod(giveLessonsTable.getPeriod());
                    giveLessonsModel.setNumber(giveLessonsTable.getNumber());

                    list.add(giveLessonsModel);
                }
            }else {
                if ((getPageModel.getCoachName()!=null||!"".equals(getPageModel.getCoachName()))&&(getPageModel.getCourseName()==null||"".equals(getPageModel.getCourseName()))){
                    LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
                    lqwCoach.eq(CoachTable::getName,getPageModel.getCoachName());
                    List<CoachTable> coachTableList=coachMapper.selectList(iPage,lqwCoach);

                    for (CoachTable coachTable:coachTableList){
                        LambdaQueryWrapper<GiveLessonsTable> lqwGive=new LambdaQueryWrapper<>();
                        lqwGive.eq(GiveLessonsTable::getCoachId,coachTable.getCoachId());
                        List<GiveLessonsTable> giveLessonsTableList=giveLessonsMapper.selectList(iPage,lqwGive);
                        for (GiveLessonsTable giveLessonsTable:giveLessonsTableList){
                            LambdaQueryWrapper<CoachTable> lqwCoach2=new LambdaQueryWrapper<>();
                            lqwCoach2.eq(CoachTable::getCoachId,giveLessonsTable.getCoachId());
                            CoachTable coachTable2=coachMapper.selectOne(lqwCoach2);

                            LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
                            lqwCourse.eq(CourseTable::getCourseId,giveLessonsTable.getCourseId());
                            CourseTable courseTable=courseMapper.selectOne(lqwCourse);

                            GiveLessonsModel giveLessonsModel=new GiveLessonsModel();
                            giveLessonsModel.setCoachId(giveLessonsTable.getCoachId());
                            giveLessonsModel.setCoachName(coachTable2.getName());
                            giveLessonsModel.setCourseId(giveLessonsModel.getCourseId());
                            giveLessonsModel.setCourseName(courseTable.getName());
                            giveLessonsModel.setTime(giveLessonsTable.getTime());
                            giveLessonsModel.setPeriod(giveLessonsTable.getPeriod());
                            giveLessonsModel.setNumber(giveLessonsTable.getNumber());

                            list.add(giveLessonsModel);
                        }
                    }
                }else if ((getPageModel.getCoachName()==null||"".equals(getPageModel.getCoachName()))&&(getPageModel.getCourseName()!=null||!"".equals(getPageModel.getCourseName()))){
                    LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
                    lqwCourse.eq(CourseTable::getName,getPageModel.getCourseName());
                    List<CourseTable> courseTableList=courseMapper.selectList(iPage,lqwCourse);

                    for (CourseTable courseTable:courseTableList){
                        LambdaQueryWrapper<GiveLessonsTable> lqwGive=new LambdaQueryWrapper<>();
                        lqwGive.eq(GiveLessonsTable::getCourseId,courseTable.getCourseId());
                        List<GiveLessonsTable> giveLessonsTableList=giveLessonsMapper.selectList(iPage,lqwGive);
                        for (GiveLessonsTable giveLessonsTable:giveLessonsTableList){
                            LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
                            lqwCoach.eq(CoachTable::getCoachId,giveLessonsTable.getCoachId());
                            CoachTable coachTable=coachMapper.selectOne(lqwCoach);

                            LambdaQueryWrapper<CourseTable> lqwCourse2=new LambdaQueryWrapper<>();
                            lqwCourse2.eq(CourseTable::getCourseId,giveLessonsTable.getCourseId());
                            CourseTable courseTable2=courseMapper.selectOne(lqwCourse2);

                            GiveLessonsModel giveLessonsModel=new GiveLessonsModel();
                            giveLessonsModel.setCoachId(giveLessonsTable.getCoachId());
                            giveLessonsModel.setCoachName(coachTable.getName());
                            giveLessonsModel.setCourseId(giveLessonsModel.getCourseId());
                            giveLessonsModel.setCourseName(courseTable2.getName());
                            giveLessonsModel.setTime(giveLessonsTable.getTime());
                            giveLessonsModel.setPeriod(giveLessonsTable.getPeriod());
                            giveLessonsModel.setNumber(giveLessonsTable.getNumber());

                            list.add(giveLessonsModel);
                        }
                    }
                }else {
                    LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
                    lqwCoach.eq(CoachTable::getName,getPageModel.getCoachName());
                    List<CoachTable> coachTableList=coachMapper.selectList(iPage,lqwCoach);

                    LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
                    lqwCourse.eq(CourseTable::getName,getPageModel.getCourseName());
                    List<CourseTable> courseTableList=courseMapper.selectList(iPage,lqwCourse);

                    for (CoachTable coachTable:coachTableList){
                        for (CourseTable courseTable:courseTableList){
                            LambdaQueryWrapper<GiveLessonsTable> lqwGive=new LambdaQueryWrapper<>();
                            lqwGive.eq(GiveLessonsTable::getCoachId,coachTable.getCoachId());
                            lqwGive.eq(GiveLessonsTable::getCourseId,courseTable.getCourseId());

                            List<GiveLessonsTable> giveLessonsTableList=giveLessonsMapper.selectList(lqwGive);

                            for (GiveLessonsTable giveLessonsTable:giveLessonsTableList){
                                if (giveLessonsTable!=null){
                                    GiveLessonsModel giveLessonsModel=new GiveLessonsModel();

                                    giveLessonsModel.setCoachId(giveLessonsTable.getCoachId());
                                    giveLessonsModel.setCoachName(coachTable.getName());
                                    giveLessonsModel.setCourseId(giveLessonsModel.getCourseId());
                                    giveLessonsModel.setCourseName(courseTable.getName());
                                    giveLessonsModel.setTime(giveLessonsTable.getTime());
                                    giveLessonsModel.setPeriod(giveLessonsTable.getPeriod());
                                    giveLessonsModel.setNumber(giveLessonsTable.getNumber());

                                    list.add(giveLessonsModel);
                                }
                            }
                        }
                    }
                }

            }


            sendPageModel.setPageList(list.toArray(new GiveLessonsModel[0]));
            sendPageModel.setTotal((int) iPage.getTotal());

            RedisDataModel<SendPageModel> redisDataModel=new RedisDataModel<>();
            redisDataModel.setData(sendPageModel);
            redisDataModel.setKey(dataKey);
            redisDataModel.setTimeout(60*60*24);
            serveTool.addManagerRedisData(redisDataModel);
            serveTool.updateRedisDataStatus(Code.giveLessons_check_key,Code.giveLessons_check_notUpdate_value);

            return sendPageModel;
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }
}
