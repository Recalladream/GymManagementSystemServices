package com.gymmanagementsystembackend.serve;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmanagementsystembackend.dao.*;
import com.gymmanagementsystembackend.domain.*;
import com.gymmanagementsystembackend.exception.BusinessException;
import com.gymmanagementsystembackend.model.*;
import com.gymmanagementsystembackend.serve.itf.SubscribeInterface;
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
public class SubscribeRealize implements SubscribeInterface {

    @Autowired
    private SubscribeMapper subscribeMapper;

    @Autowired
    private VipUserMapper vipUserMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CoachMapper coachMapper;

    @Autowired
    private GiveLessonsMapper giveLessonsMapper;

    @Autowired
    private SigninRealize signinRealize;

    @Autowired
    private ServeTool serveTool;

    @Override
    public void add(SubscribeTable subscribeTable) {
        Object lock = new Object();
        synchronized (lock) {
            String mes="";
            try {
                Boolean  checkout=true;

                if (subscribeTable.getVipId()==null){checkout=false;mes="会员id不能为空!";}
                if (subscribeTable.getCourseId()==null){checkout=false;mes="课程id不能为空!";}
                if (subscribeTable.getCoachId()==null){checkout=false;mes="教练id不能为空!";}
                if (subscribeTable.getTime()==null){checkout=false;mes="时间不能为空";}
                else {
                    // 定义日期格式
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    // 获取当前日期
                    Date nowdate = new Date();
                    Calendar nowCalendar = Calendar.getInstance();
                    nowCalendar.setTime(nowdate);

                    Date subdate = simpleDateFormat.parse(subscribeTable.getTime());
                    Calendar subCalendar = Calendar.getInstance();
                    subCalendar.setTime(subdate);
                    subCalendar.add(Calendar.DAY_OF_MONTH,1);

                    // 计算天数差
                    int diffInDays = 0;
                    while (nowCalendar.before(subCalendar)) {
                        nowCalendar.add(Calendar.DAY_OF_MONTH, 1);
                        diffInDays++;
                    }

                    //if (diffInDays<0){checkout=false;mes="只能预约当天日期之后的课程!";}
                }
                String period=subscribeTable.getPeriod();
                if (period==null){
                    checkout=false;mes="授课时间段不能为空！";
                }else if ("上午".equals(period)||"下午".equals(period)||"晚上".equals(period)){
                }else {checkout=false;mes="授课时间段不在规定范围！";}

                if (checkout==true){
                    LambdaQueryWrapper<SubscribeTable> lqwSub1=new LambdaQueryWrapper<>();
                    lqwSub1.eq(SubscribeTable::getVipId,subscribeTable.getVipId());
                    lqwSub1.eq(SubscribeTable::getCoachId,subscribeTable.getCoachId());
                    lqwSub1.eq(SubscribeTable::getCourseId,subscribeTable.getCourseId());
                    lqwSub1.eq(SubscribeTable::getTime,subscribeTable.getTime());
                    lqwSub1.eq(SubscribeTable::getPeriod,subscribeTable.getPeriod());

                    int num=subscribeMapper.selectList(lqwSub1).size();
                    if (num==0){
                        LambdaQueryWrapper<SubscribeTable> lqwSub=new LambdaQueryWrapper<>();
                        lqwSub.eq(SubscribeTable::getCoachId,subscribeTable.getCoachId());
                        lqwSub.eq(SubscribeTable::getCourseId,subscribeTable.getCourseId());
                        lqwSub.eq(SubscribeTable::getTime,subscribeTable.getTime());
                        lqwSub.eq(SubscribeTable::getPeriod,subscribeTable.getPeriod());
                        int reservedNumber=subscribeMapper.selectList(lqwSub).size();

                        LambdaQueryWrapper<GiveLessonsTable> lqwGive=new LambdaQueryWrapper<>();
                        lqwGive.eq(GiveLessonsTable::getCoachId,subscribeTable.getCoachId());
                        lqwGive.eq(GiveLessonsTable::getCourseId,subscribeTable.getCourseId());
                        lqwGive.eq(GiveLessonsTable::getTime,subscribeTable.getTime());
                        lqwGive.eq(GiveLessonsTable::getPeriod,subscribeTable.getPeriod());
                        int classMaxPeopleNum=giveLessonsMapper.selectOne(lqwGive).getNumber();

                        if (reservedNumber<=classMaxPeopleNum){

                            subscribeTable.setTime(subscribeTable.getTime());

                            subscribeMapper.insert(subscribeTable);

                            SigninTable signinTable=new SigninTable();
                            signinTable.setVipId(subscribeTable.getVipId());
                            signinTable.setCoachId(subscribeTable.getCoachId());
                            signinTable.setCourseId(subscribeTable.getCourseId());
                            signinTable.setClassTime(subscribeTable.getTime());
                            signinTable.setPeriod(subscribeTable.getPeriod());
                            signinTable.setSign("未签");

                            signinRealize.add(signinTable);

                            serveTool.updateRedisDataStatus(Code.subscribeClass_check_key,Code.subscribeClass_check_update_value);
                            serveTool.updateRedisDataStatus(Code.subscribe_check_key,Code.subscribe_check_update_value);
                            serveTool.updateRedisDataStatus(Code.sigNin_check_key,Code.sigNin_check_update_value);
                            serveTool.updateRedisDataStatus(Code.sigNinNot_check_key,Code.sigNinNot_check_update_value);
                            serveTool.updateRedisDataStatus(Code.sigNinClass_check_key,Code.sigNinClass_check_update_value);

                            return;
                        }else {
                            mes="选课失败！人数已满";
                        }
                    }else {
                        mes="该会员已预约此课程！";
                    }
                }
            }catch(BusinessException e){
                throw e;
            }catch (Exception e){
                e.printStackTrace();
                throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
            }
            throw new BusinessException(Code.subscribe_ok,mes);
        }
    }

    @Override
    public void delete(int id) {
        try {
            subscribeMapper.deleteById(id);

            serveTool.updateRedisDataStatus(Code.subscribe_check_key,Code.subscribe_check_update_value);
            serveTool.updateRedisDataStatus(Code.subscribeClass_check_key,Code.subscribeClass_check_update_value);
            serveTool.updateRedisDataStatus(Code.sigNin_check_key,Code.sigNin_check_update_value);
            serveTool.updateRedisDataStatus(Code.sigNinNot_check_key,Code.sigNinNot_check_update_value);
            serveTool.updateRedisDataStatus(Code.sigNinClass_check_key,Code.sigNinClass_check_update_value);
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }

    @Override
    public SendPageModel getPage(GetPageModel getPageModel) {
        try {
            String condition="";
            if (getPageModel.getVipName()!=null&&!"".equals(getPageModel.getVipName())){
                condition=getPageModel.getVipName();
            }
            String page=String.valueOf(getPageModel.getPage());
            String size=String.valueOf(getPageModel.getSize());
            String dataKey=Code.subscribe_data_prefix+"_"+page+"_"+size+"_"+condition;

            if (serveTool.checkManagerRedisData(Code.subscribe_check_key,Code.subscribe_check_notUpdate_value)){
                SendPageModel sendPageModel=serveTool.<SendPageModel>getManagerRedisData(dataKey,SendPageModel.class);

                if (sendPageModel!=null){
                    return sendPageModel;
                }
            }

            List<SubscribeModel> list=new ArrayList<>();

            SendPageModel sendPageModel=new SendPageModel();
            IPage iPage=new Page(getPageModel.getPage(), getPageModel.getSize());

            LambdaQueryWrapper<VipUserTable> lqwVipw=new LambdaQueryWrapper<>();
            List<VipUserTable> vipUserList=new ArrayList<>();
            if (condition.isEmpty()){
                lqwVipw.eq(VipUserTable::getName,getPageModel.getVipName());
                vipUserList=vipUserMapper.selectList(iPage,lqwVipw);
            }

            if (!vipUserList.isEmpty()){
                for (VipUserTable vipUserTable:vipUserList){
                    LambdaQueryWrapper<SubscribeTable> lqwSub=new LambdaQueryWrapper<>();
                    lqwSub.eq(SubscribeTable::getVipId,vipUserTable.getVipId());

                    SubscribeTable subscribeTable=subscribeMapper.selectOne(lqwSub);

                    if (subscribeTable!=null){
                        SubscribeModel subscribeModel=new SubscribeModel();
                        subscribeModel.setId(subscribeTable.getId());
                        subscribeModel.setVipId(subscribeTable.getVipId());
                        subscribeModel.setCourseId(subscribeTable.getCourseId());
                        subscribeModel.setCoachId(subscribeTable.getCoachId());
                        subscribeModel.setTime(subscribeTable.getTime());
                        subscribeModel.setPeriod(subscribeTable.getPeriod());

                        LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
                        lqwCourse.eq(CourseTable::getCourseId,subscribeTable.getCourseId());
                        CourseTable courseTable=courseMapper.selectOne(lqwCourse);

                        LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
                        lqwCoach.eq(CoachTable::getCoachId,subscribeTable.getCoachId());
                        CoachTable coachTable=coachMapper.selectOne(lqwCoach);

                        subscribeModel.setVipName(vipUserTable.getName());
                        subscribeModel.setCourseName(courseTable.getName());
                        subscribeModel.setCoachName(coachTable.getName());

                        list.add(subscribeModel);
                    }else {
                        iPage.setTotal(iPage.getTotal()-1);
                    }

                }
            }else {
                List<SubscribeTable> subscribeTableList=subscribeMapper.selectList(iPage,null);

                for (SubscribeTable subscribeTable:subscribeTableList){
                    SubscribeModel subscribeModel1=new SubscribeModel();
                    subscribeModel1.setId(subscribeTable.getId());
                    subscribeModel1.setVipId(subscribeTable.getVipId());
                    subscribeModel1.setCourseId(subscribeTable.getCourseId());
                    subscribeModel1.setCoachId(subscribeTable.getCoachId());
                    subscribeModel1.setTime(subscribeTable.getTime());
                    subscribeModel1.setPeriod(subscribeTable.getPeriod());

                    LambdaQueryWrapper<VipUserTable> lqwVipn=new LambdaQueryWrapper<>();
                    lqwVipn.eq(VipUserTable::getVipId,subscribeTable.getVipId());
                    VipUserTable vipUserTable1=vipUserMapper.selectOne(lqwVipn);

                    LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
                    lqwCourse.eq(CourseTable::getCourseId,subscribeTable.getCourseId());
                    CourseTable courseTable1=courseMapper.selectOne(lqwCourse);

                    LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
                    lqwCoach.eq(CoachTable::getCoachId,subscribeTable.getCoachId());
                    CoachTable coachTable1=coachMapper.selectOne(lqwCoach);

                    subscribeModel1.setVipName(vipUserTable1.getName());
                    subscribeModel1.setCourseName(courseTable1.getName());
                    subscribeModel1.setCoachName(coachTable1.getName());

                    list.add(subscribeModel1);
                }
            }

            sendPageModel.setPageList(list.toArray(new SubscribeModel[0]));
            sendPageModel.setTotal((int) iPage.getTotal());

            RedisDataModel<SendPageModel> redisDataModel=new RedisDataModel();
            redisDataModel.setData(sendPageModel);
            redisDataModel.setKey(dataKey);
            redisDataModel.setTimeout(60*60*24);
            serveTool.addManagerRedisData(redisDataModel);
            serveTool.updateRedisDataStatus(Code.subscribe_check_key,Code.subscribe_check_notUpdate_value);

            return sendPageModel;
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }

    @Override
    public SendPageModel getSubScribe(GetPageModel getPageModel) {
        try {
            String condition="";
            if ((getPageModel.getCourseName()!=null&&!"".equals(getPageModel.getCourseName()))||(getPageModel.getCoachName()==null||"".equals(getPageModel.getCoachName()))){
                condition=getPageModel.getCourseName()+"_"+getPageModel.getCoachName();
            }
            String page=String.valueOf(getPageModel.getPage());
            String size=String.valueOf(getPageModel.getSize());
            String dataKey=Code.subscribeClass_data_prefix+"_"+page+"_"+size+"_"+condition;

            if (serveTool.checkManagerRedisData(Code.subscribeClass_check_key,Code.subscribeClass_check_notUpdate_value)){
                SendPageModel sendPageModel=serveTool.<SendPageModel>getManagerRedisData(dataKey,SendPageModel.class);

                if (sendPageModel!=null){
                    return sendPageModel;
                }
            }

            List<GiveLessonsModel> list=new ArrayList<>();
            SendPageModel sendPageModel=new SendPageModel();
            IPage iPage=new Page(getPageModel.getPage(), getPageModel.getSize());
            System.out.println(getPageModel);
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

                    LambdaQueryWrapper<SubscribeTable> lqwSub=new LambdaQueryWrapper<>();
                    lqwSub.eq(SubscribeTable::getCoachId,giveLessonsTable.getCoachId());
                    lqwSub.eq(SubscribeTable::getCourseId,giveLessonsTable.getCourseId());
                    lqwSub.eq(SubscribeTable::getTime,giveLessonsTable.getTime());
                    lqwSub.eq(SubscribeTable::getPeriod,giveLessonsTable.getPeriod());

                    List<SubscribeTable> subscribeTableList=subscribeMapper.selectList(lqwSub);

                    giveLessonsModel.setNumber(giveLessonsTable.getNumber()-subscribeTableList.size());

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
                            giveLessonsModel.setId(giveLessonsTable.getId());
                            giveLessonsModel.setCoachId(giveLessonsTable.getCoachId());
                            giveLessonsModel.setCoachName(coachTable2.getName());
                            giveLessonsModel.setCourseId(giveLessonsModel.getCourseId());
                            giveLessonsModel.setCourseName(courseTable.getName());
                            giveLessonsModel.setTime(giveLessonsTable.getTime());
                            giveLessonsModel.setPeriod(giveLessonsTable.getPeriod());

                            LambdaQueryWrapper<SubscribeTable> lqwSub=new LambdaQueryWrapper<>();
                            lqwSub.eq(SubscribeTable::getCoachId,giveLessonsTable.getCoachId());
                            lqwSub.eq(SubscribeTable::getCourseId,giveLessonsTable.getCourseId());
                            lqwSub.eq(SubscribeTable::getTime,giveLessonsTable.getTime());
                            lqwSub.eq(SubscribeTable::getPeriod,giveLessonsTable.getPeriod());

                            List<SubscribeTable> subscribeTableList=subscribeMapper.selectList(lqwSub);

                            giveLessonsModel.setNumber(giveLessonsTable.getNumber()-subscribeTableList.size());

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
                            giveLessonsModel.setId(giveLessonsTable.getId());
                            giveLessonsModel.setCoachId(giveLessonsTable.getCoachId());
                            giveLessonsModel.setCoachName(coachTable.getName());
                            giveLessonsModel.setCourseId(giveLessonsModel.getCourseId());
                            giveLessonsModel.setCourseName(courseTable2.getName());
                            giveLessonsModel.setTime(giveLessonsTable.getTime());
                            giveLessonsModel.setPeriod(giveLessonsTable.getPeriod());

                            LambdaQueryWrapper<SubscribeTable> lqwSub=new LambdaQueryWrapper<>();
                            lqwSub.eq(SubscribeTable::getCoachId,giveLessonsTable.getCoachId());
                            lqwSub.eq(SubscribeTable::getCourseId,giveLessonsTable.getCourseId());
                            lqwSub.eq(SubscribeTable::getTime,giveLessonsTable.getTime());
                            lqwSub.eq(SubscribeTable::getPeriod,giveLessonsTable.getPeriod());

                            List<SubscribeTable> subscribeTableList=subscribeMapper.selectList(lqwSub);

                            giveLessonsModel.setNumber(giveLessonsTable.getNumber()-subscribeTableList.size());

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
                                    giveLessonsModel.setId(giveLessonsTable.getId());
                                    giveLessonsModel.setCoachId(giveLessonsTable.getCoachId());
                                    giveLessonsModel.setCoachName(coachTable.getName());
                                    giveLessonsModel.setCourseId(giveLessonsModel.getCourseId());
                                    giveLessonsModel.setCourseName(courseTable.getName());
                                    giveLessonsModel.setTime(giveLessonsTable.getTime());
                                    giveLessonsModel.setPeriod(giveLessonsTable.getPeriod());

                                    LambdaQueryWrapper<SubscribeTable> lqwSub=new LambdaQueryWrapper<>();
                                    lqwSub.eq(SubscribeTable::getCoachId,giveLessonsTable.getCoachId());
                                    lqwSub.eq(SubscribeTable::getCourseId,giveLessonsTable.getCourseId());
                                    lqwSub.eq(SubscribeTable::getTime,giveLessonsTable.getTime());
                                    lqwSub.eq(SubscribeTable::getPeriod,giveLessonsTable.getPeriod());

                                    List<SubscribeTable> subscribeTableList=subscribeMapper.selectList(lqwSub);

                                    giveLessonsModel.setNumber(giveLessonsTable.getNumber()-subscribeTableList.size());

                                    list.add(giveLessonsModel);
                                }
                            }
                        }
                    }
                }

            }

            sendPageModel.setPageList(list.toArray(new GiveLessonsModel[0]));
            sendPageModel.setTotal((int) iPage.getTotal());

            RedisDataModel<SendPageModel> redisDataModel=new RedisDataModel();
            redisDataModel.setData(sendPageModel);
            redisDataModel.setKey(dataKey);
            redisDataModel.setTimeout(60*60*24);
            serveTool.addManagerRedisData(redisDataModel);
            serveTool.updateRedisDataStatus(Code.subscribeClass_check_key,Code.subscribeClass_check_notUpdate_value);

            return sendPageModel;
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }
}
