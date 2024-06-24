package com.gymmanagementsystembackend.serve;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmanagementsystembackend.dao.*;
import com.gymmanagementsystembackend.domain.*;
import com.gymmanagementsystembackend.exception.BusinessException;
import com.gymmanagementsystembackend.model.*;
import com.gymmanagementsystembackend.serve.itf.SigninInterface;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.ServeTool;
import com.gymmanagementsystembackend.tool.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SigninRealize implements SigninInterface {
    @Autowired
    private SigninMapper signinMapper;
    @Autowired
    private CoachMapper coachMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private VipUserMapper vipUserMapper;
    @Autowired
    private GiveLessonsMapper giveLessonsMapper;
    @Autowired
    private SubscribeMapper subscribeMapper;
    @Autowired
    private ServeTool serveTool;

    @Override
    public void sign(SigninTable signinTable) {
        String mes="";
        try {
            Boolean checkout=true;

            if (signinTable.getVipId()==null){checkout=false;mes="会员id不能为空!";}
            if (signinTable.getCoachId()==null){checkout=false;mes="教练id不能为空!";}
            if (signinTable.getCourseId()==null){checkout=false;mes="课程id不能为空!";}
            if (signinTable.getClassTime()==null){checkout=false;mes="授课时间不能为空!";}
            String period=signinTable.getPeriod();
            if (period==null){
                checkout=false;mes="授课时间段不能为空！";
            }else if ("上午".equals(period)||"下午".equals(period)||"晚上".equals(period)){
            }else {checkout=false;mes="授课时间段不在规定范围！";}

            if (checkout==true){
                LambdaQueryWrapper<SigninTable> lqwSig=new LambdaQueryWrapper<>();
                lqwSig.eq(SigninTable::getVipId,signinTable.getVipId());
                lqwSig.eq(SigninTable::getCoachId,signinTable.getCoachId());
                lqwSig.eq(SigninTable::getCourseId,signinTable.getCourseId());
                lqwSig.eq(SigninTable::getClassTime,signinTable.getClassTime());
                lqwSig.eq(SigninTable::getPeriod,signinTable.getPeriod());

                SigninTable signinTable1=signinMapper.selectOne(lqwSig);

                if (signinTable1!=null){
                    if("未签".equals(signinTable1.getSign())){
                        Date date=new Date();
                        SimpleDateFormat simpleDateFormatn=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String signTime=simpleDateFormatn.format(date);

                        signinTable1.setSign("已签");
                        signinTable1.setSignTime(signTime);

                        signinMapper.update(signinTable1,lqwSig);

                        serveTool.updateRedisDataStatus(Code.sigNin_check_key,Code.sigNin_check_update_value);
                        serveTool.updateRedisDataStatus(Code.sigNinNot_check_key,Code.sigNinNot_check_update_value);
                        serveTool.updateRedisDataStatus(Code.sigNinClass_check_key,Code.sigNinClass_check_update_value);

                        return;
                    }else {
                        mes="该会员已签到!";
                    }
                }else {
                    mes="该会员没有预约此课程!";
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.signin_fail,mes);
    }

    @Override
    public void add(SigninTable signinTable) {
        String mes="";
        try {
            Boolean checkout=true;

            if (signinTable.getVipId()==null){checkout=false;mes="会员id不能为空!";}
            if (signinTable.getCoachId()==null){checkout=false;mes="教练id不能为空!";}
            if (signinTable.getCourseId()==null){checkout=false;mes="课程id不能为空!";}
            if (signinTable.getClassTime()==null){checkout=false;mes="授课时间不能为空!";}
            String period=signinTable.getPeriod();
            if (period==null){
                checkout=false;mes="授课时间段不能为空！";
            }else if ("上午".equals(period)||"下午".equals(period)||"晚上".equals(period)){
            }else {checkout=false;mes="授课时间段不在规定范围！";}

            if (checkout==true){

                signinMapper.insert(signinTable);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.signin_fail,mes);
    }

    @Override
    public SendPageModel getSignInf(GetPageModel getPageModel) {
        try {
            String condition="";
            if (getPageModel.getVipName()!=null&&!"".equals(getPageModel.getVipName())){
                condition=getPageModel.getVipName()+"_";
            }
            condition+=getPageModel.getSign()+"_"+getPageModel.getCoachId()+"_"+getPageModel.getCourseId()+"_"+getPageModel.getClassTime()+"_"+getPageModel.getPeriod();
            String page=String.valueOf(getPageModel.getPage());
            String size=String.valueOf(getPageModel.getSize());
            String dataKey="";

            if ("已签".equals(getPageModel.getSign())){
                dataKey=Code.sigNin_data_prefix+"_"+page+"_"+size+"_"+condition;
                if (serveTool.checkManagerRedisData(Code.sigNin_check_key,Code.sigNin_check_notUpdate_value)){
                    SendPageModel sendPageModel=serveTool.<SendPageModel>getManagerRedisData(dataKey,SendPageModel.class);

                    if (sendPageModel!=null&&sendPageModel.getTotal()!=0){
                        return sendPageModel;
                    }
                }
            }
            if ("未签".equals(getPageModel.getSign())){
                dataKey=Code.sigNinNot_data_prefix+"_"+page+"_"+size+"_"+condition;
                if (serveTool.checkManagerRedisData(Code.sigNinNot_check_key,Code.sigNinNot_check_notUpdate_value)){
                    SendPageModel sendPageModel=serveTool.<SendPageModel>getManagerRedisData(dataKey,SendPageModel.class);

                    if (sendPageModel!=null&&sendPageModel.getTotal()!=0){
                        return sendPageModel;
                    }
                }
            }


            SendPageModel sendPageModel=new SendPageModel();
            List<SigninModel> list=new ArrayList<>();

            IPage iPage=new Page(getPageModel.getPage(),getPageModel.getSize());

            LambdaQueryWrapper<SigninTable> lqwSig=new LambdaQueryWrapper<>();

            if (getPageModel.getVipName()!=null&&!"".equals(getPageModel.getVipName())){
                LambdaQueryWrapper<VipUserTable> lqwVipUser=new LambdaQueryWrapper<>();
                lqwVipUser.eq(VipUserTable::getName,getPageModel.getVipName());

                List<VipUserTable> vipUserTableList=vipUserMapper.selectList(lqwVipUser);

                for (VipUserTable vipUserTable:vipUserTableList){

                    lqwSig.eq(SigninTable::getVipId,vipUserTable.getVipId());
                    lqwSig.eq(SigninTable::getCoachId,getPageModel.getCoachId());
                    lqwSig.eq(SigninTable::getCourseId,getPageModel.getCourseId());
                    lqwSig.eq(SigninTable::getClassTime,getPageModel.getClassTime());
                    lqwSig.eq(SigninTable::getPeriod,getPageModel.getPeriod());
                    lqwSig.eq(SigninTable::getSign,getPageModel.getSign());

                    SigninTable signinTable=signinMapper.selectOne(lqwSig);
                    if (signinTable!=null){
                        LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
                        lqwCoach.eq(CoachTable::getCoachId,signinTable.getCoachId());

                        CoachTable coachTable=coachMapper.selectOne(lqwCoach);

                        LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
                        lqwCourse.eq(CourseTable::getCourseId,signinTable.getCourseId());

                        CourseTable courseTable=courseMapper.selectOne(lqwCourse);

                        SigninModel signinModel=new SigninModel();
                        signinModel.setVipId(vipUserTable.getVipId());
                        signinModel.setVipName(vipUserTable.getName());
                        signinModel.setCoachId(coachTable.getCoachId());
                        signinModel.setCoachName(coachTable.getName());
                        signinModel.setCourseId(courseTable.getCourseId());
                        signinModel.setCourseName(courseTable.getName());

                        signinModel.setPeriod(signinTable.getPeriod());
                        signinModel.setClassTime(signinTable.getClassTime());
                        signinModel.setSignTime(signinTable.getSignTime());

                        list.add(signinModel);
                    }
                }
            }else {
                lqwSig.eq(SigninTable::getCoachId,getPageModel.getCoachId());
                lqwSig.eq(SigninTable::getCourseId,getPageModel.getCourseId());
                lqwSig.eq(SigninTable::getClassTime,getPageModel.getClassTime());
                lqwSig.eq(SigninTable::getPeriod,getPageModel.getPeriod());
                lqwSig.eq(SigninTable::getSign,getPageModel.getSign());

                List<SigninTable> signinTableList=signinMapper.selectList(iPage,lqwSig);

                for (SigninTable signinTable:signinTableList){
                    LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
                    lqwCoach.eq(CoachTable::getCoachId,signinTable.getCoachId());

                    CoachTable coachTable=coachMapper.selectOne(lqwCoach);

                    LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
                    lqwCourse.eq(CourseTable::getCourseId,signinTable.getCourseId());

                    CourseTable courseTable=courseMapper.selectOne(lqwCourse);

                    LambdaQueryWrapper<VipUserTable> lqwVipUser=new LambdaQueryWrapper<>();
                    lqwVipUser.eq(VipUserTable::getVipId,signinTable.getVipId());

                    VipUserTable vipUserTable=vipUserMapper.selectOne(lqwVipUser);

                    SigninModel signinModel=new SigninModel();
                    signinModel.setVipId(vipUserTable.getVipId());
                    signinModel.setVipName(vipUserTable.getName());
                    signinModel.setCoachId(coachTable.getCoachId());
                    signinModel.setCoachName(coachTable.getName());
                    signinModel.setCourseId(courseTable.getCourseId());
                    signinModel.setCourseName(courseTable.getName());

                    signinModel.setPeriod(signinTable.getPeriod());
                    signinModel.setClassTime(signinTable.getClassTime());
                    signinModel.setSignTime(signinTable.getSignTime());

                    list.add(signinModel);
                }
            }

            sendPageModel.setPageList(list.toArray(new SigninModel[0]));
            sendPageModel.setTotal((int) iPage.getTotal());

            RedisDataModel<SendPageModel> redisDataModel=new RedisDataModel<>();
            redisDataModel.setData(sendPageModel);
            redisDataModel.setKey(dataKey);
            redisDataModel.setTimeout(60*60*24);
            serveTool.addManagerRedisData(redisDataModel);
            if ("已签".equals(getPageModel.getSign())){
                serveTool.updateRedisDataStatus(Code.sigNin_check_key,Code.sigNin_check_notUpdate_value);

            }
            if ("未签".equals(getPageModel.getSign())){
                serveTool.updateRedisDataStatus(Code.sigNinNot_check_key,Code.sigNinNot_check_notUpdate_value);
            }

            return sendPageModel;
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }

    @Override
    public SendPageModel getSignCourse(GetPageModel getPageModel) {
        try {
            String condition="";
            if ((getPageModel.getCourseName()!=null&&!"".equals(getPageModel.getCourseName()))||(getPageModel.getCoachName()!=null||!"".equals(getPageModel.getCoachName()))){
                condition=getPageModel.getCourseName()+"_"+getPageModel.getCoachName();
            }
            String page=String.valueOf(getPageModel.getPage());
            String size=String.valueOf(getPageModel.getSize());
            String dataKey=Code.sigNinClass_data_prefix+"_"+page+"_"+size+"_"+condition;

            if (serveTool.checkManagerRedisData(Code.sigNinClass_check_key,Code.sigNinClass_check_notUpdate_value)){
                SendPageModel sendPageModel=serveTool.<SendPageModel>getManagerRedisData(dataKey,SendPageModel.class);

                if (sendPageModel!=null){
                    return sendPageModel;
                }
            }

            List<GiveLessonsModel> list=new ArrayList<>();
            SendPageModel sendPageModel=new SendPageModel();
            IPage iPage=new Page(getPageModel.getPage(), getPageModel.getSize());

            Date date=new Date();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            String time=simpleDateFormat.format(date);

            if ((getPageModel.getCoachName()==null||"".equals(getPageModel.getCoachName()))&&(getPageModel.getCourseName()==null||"".equals(getPageModel.getCourseName()))){
                LambdaQueryWrapper<GiveLessonsTable> lqwGive=new LambdaQueryWrapper<>();
                lqwGive.eq(GiveLessonsTable::getTime,time);

                List<GiveLessonsTable> lessonsTableList=giveLessonsMapper.selectList(lqwGive);

                for (GiveLessonsTable giveLessonsTable:lessonsTableList){
                    GiveLessonsModel giveLessonsModel=new GiveLessonsModel();

                    LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
                    lqwCoach.eq(CoachTable::getCoachId,giveLessonsTable.getCoachId());
                    CoachTable coachTable=coachMapper.selectOne(lqwCoach);

                    LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
                    lqwCourse.eq(CourseTable::getCourseId,giveLessonsTable.getCourseId());
                    CourseTable courseTable=courseMapper.selectOne(lqwCourse);

                    giveLessonsModel.setCoachId(coachTable.getCoachId());
                    giveLessonsModel.setCoachName(coachTable.getName());
                    giveLessonsModel.setCourseId(courseTable.getCourseId());
                    giveLessonsModel.setCourseName(courseTable.getName());
                    giveLessonsModel.setTime(giveLessonsTable.getTime());
                    giveLessonsModel.setPeriod(giveLessonsTable.getPeriod());

                    LambdaQueryWrapper<SigninTable> lqwSig=new LambdaQueryWrapper<>();
                    lqwSig.eq(SigninTable::getCoachId,coachTable.getCoachId());
                    lqwSig.eq(SigninTable::getCourseId,courseTable.getCourseId());
                    lqwSig.eq(SigninTable::getClassTime,giveLessonsTable.getTime());
                    lqwSig.eq(SigninTable::getPeriod,giveLessonsTable.getPeriod());
                    lqwSig.eq(SigninTable::getSign,"已签");
                    int signedNum=signinMapper.selectList(lqwSig).size();

                    LambdaQueryWrapper<SubscribeTable> lqwSub=new LambdaQueryWrapper<>();
                    lqwSub.eq(SubscribeTable::getCoachId,giveLessonsTable.getCoachId());
                    lqwSub.eq(SubscribeTable::getCourseId,giveLessonsTable.getCourseId());
                    lqwSub.eq(SubscribeTable::getTime,giveLessonsTable.getTime());
                    lqwSub.eq(SubscribeTable::getPeriod,giveLessonsTable.getPeriod());
                    int reservationNumber=subscribeMapper.selectList(lqwSub).size();

                    giveLessonsModel.setNumber(reservationNumber-signedNum);

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
                        lqwGive.eq(GiveLessonsTable::getTime,time);
                        List<GiveLessonsTable> giveLessonsTableList=giveLessonsMapper.selectList(iPage,lqwGive);

                        for (GiveLessonsTable giveLessonsTable:giveLessonsTableList){
                            GiveLessonsModel giveLessonsModel=new GiveLessonsModel();

                            LambdaQueryWrapper<CoachTable> lqwCoach1=new LambdaQueryWrapper<>();
                            lqwCoach1.eq(CoachTable::getCoachId,giveLessonsTable.getCoachId());
                            CoachTable coachTable1=coachMapper.selectOne(lqwCoach1);

                            LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
                            lqwCourse.eq(CourseTable::getCourseId,giveLessonsTable.getCourseId());
                            CourseTable courseTable=courseMapper.selectOne(lqwCourse);

                            giveLessonsModel.setCoachId(coachTable1.getCoachId());
                            giveLessonsModel.setCoachName(coachTable1.getName());
                            giveLessonsModel.setCourseId(courseTable.getCourseId());
                            giveLessonsModel.setCourseName(courseTable.getName());
                            giveLessonsModel.setTime(giveLessonsTable.getTime());
                            giveLessonsModel.setPeriod(giveLessonsTable.getPeriod());

                            LambdaQueryWrapper<SigninTable> lqwSig=new LambdaQueryWrapper<>();
                            lqwSig.eq(SigninTable::getCoachId,coachTable.getCoachId());
                            lqwSig.eq(SigninTable::getCourseId,courseTable.getCourseId());
                            lqwSig.eq(SigninTable::getClassTime,giveLessonsTable.getTime());
                            lqwSig.eq(SigninTable::getPeriod,giveLessonsTable.getPeriod());
                            lqwSig.eq(SigninTable::getSign,"已签");
                            int signedNum=signinMapper.selectList(lqwSig).size();

                            LambdaQueryWrapper<SubscribeTable> lqwSub=new LambdaQueryWrapper<>();
                            lqwSub.eq(SubscribeTable::getCoachId,giveLessonsTable.getCoachId());
                            lqwSub.eq(SubscribeTable::getCourseId,giveLessonsTable.getCourseId());
                            lqwSub.eq(SubscribeTable::getTime,giveLessonsTable.getTime());
                            lqwSub.eq(SubscribeTable::getPeriod,giveLessonsTable.getPeriod());
                            int reservationNumber=subscribeMapper.selectList(lqwSub).size();

                            giveLessonsModel.setNumber(reservationNumber-signedNum);

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
                        lqwGive.eq(GiveLessonsTable::getTime,time);
                        List<GiveLessonsTable> giveLessonsTableList=giveLessonsMapper.selectList(iPage,lqwGive);

                        for (GiveLessonsTable giveLessonsTable:giveLessonsTableList){
                            GiveLessonsModel giveLessonsModel=new GiveLessonsModel();

                            LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
                            lqwCoach.eq(CoachTable::getCoachId,giveLessonsTable.getCoachId());
                            CoachTable coachTable=coachMapper.selectOne(lqwCoach);

                            LambdaQueryWrapper<CourseTable> lqwCourse1=new LambdaQueryWrapper<>();
                            lqwCourse1.eq(CourseTable::getCourseId,giveLessonsTable.getCourseId());
                            CourseTable courseTable1=courseMapper.selectOne(lqwCourse1);

                            giveLessonsModel.setCoachId(coachTable.getCoachId());
                            giveLessonsModel.setCoachName(coachTable.getName());
                            giveLessonsModel.setCourseId(courseTable1.getCourseId());
                            giveLessonsModel.setCourseName(courseTable1.getName());
                            giveLessonsModel.setTime(giveLessonsTable.getTime());
                            giveLessonsModel.setPeriod(giveLessonsTable.getPeriod());

                            LambdaQueryWrapper<SigninTable> lqwSig=new LambdaQueryWrapper<>();
                            lqwSig.eq(SigninTable::getCoachId,coachTable.getCoachId());
                            lqwSig.eq(SigninTable::getCourseId,courseTable.getCourseId());
                            lqwSig.eq(SigninTable::getClassTime,giveLessonsTable.getTime());
                            lqwSig.eq(SigninTable::getPeriod,giveLessonsTable.getPeriod());
                            lqwSig.eq(SigninTable::getSign,"已签");
                            int signedNum=signinMapper.selectList(lqwSig).size();

                            LambdaQueryWrapper<SubscribeTable> lqwSub=new LambdaQueryWrapper<>();
                            lqwSub.eq(SubscribeTable::getCoachId,giveLessonsTable.getCoachId());
                            lqwSub.eq(SubscribeTable::getCourseId,giveLessonsTable.getCourseId());
                            lqwSub.eq(SubscribeTable::getTime,giveLessonsTable.getTime());
                            lqwSub.eq(SubscribeTable::getPeriod,giveLessonsTable.getPeriod());
                            int reservationNumber=subscribeMapper.selectList(lqwSub).size();

                            giveLessonsModel.setNumber(reservationNumber-signedNum);

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
                            lqwGive.eq(GiveLessonsTable::getTime,time);

                            List<GiveLessonsTable> giveLessonsTableList=giveLessonsMapper.selectList(lqwGive);

                            for (GiveLessonsTable giveLessonsTable:giveLessonsTableList){
                                GiveLessonsModel giveLessonsModel=new GiveLessonsModel();

                                LambdaQueryWrapper<CoachTable> lqwCoach1=new LambdaQueryWrapper<>();
                                lqwCoach1.eq(CoachTable::getCoachId,giveLessonsTable.getCoachId());
                                CoachTable coachTable1=coachMapper.selectOne(lqwCoach1);

                                LambdaQueryWrapper<CourseTable> lqwCourse1=new LambdaQueryWrapper<>();
                                lqwCourse1.eq(CourseTable::getCourseId,giveLessonsTable.getCourseId());
                                CourseTable courseTable1=courseMapper.selectOne(lqwCourse1);

                                giveLessonsModel.setCoachId(coachTable1.getCoachId());
                                giveLessonsModel.setCoachName(coachTable1.getName());
                                giveLessonsModel.setCourseId(courseTable1.getCourseId());
                                giveLessonsModel.setCourseName(courseTable1.getName());
                                giveLessonsModel.setTime(giveLessonsTable.getTime());
                                giveLessonsModel.setPeriod(giveLessonsTable.getPeriod());

                                LambdaQueryWrapper<SigninTable> lqwSig=new LambdaQueryWrapper<>();
                                lqwSig.eq(SigninTable::getCoachId,coachTable.getCoachId());
                                lqwSig.eq(SigninTable::getCourseId,courseTable.getCourseId());
                                lqwSig.eq(SigninTable::getClassTime,giveLessonsTable.getTime());
                                lqwSig.eq(SigninTable::getPeriod,giveLessonsTable.getPeriod());
                                lqwSig.eq(SigninTable::getSign,"已签");
                                int signedNum=signinMapper.selectList(lqwSig).size();

                                LambdaQueryWrapper<SubscribeTable> lqwSub=new LambdaQueryWrapper<>();
                                lqwSub.eq(SubscribeTable::getCoachId,giveLessonsTable.getCoachId());
                                lqwSub.eq(SubscribeTable::getCourseId,giveLessonsTable.getCourseId());
                                lqwSub.eq(SubscribeTable::getTime,giveLessonsTable.getTime());
                                lqwSub.eq(SubscribeTable::getPeriod,giveLessonsTable.getPeriod());
                                int reservationNumber=subscribeMapper.selectList(lqwSub).size();

                                giveLessonsModel.setNumber(reservationNumber-signedNum);

                                list.add(giveLessonsModel);
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
            serveTool.updateRedisDataStatus(Code.sigNinClass_check_key,Code.sigNinClass_check_notUpdate_value);

            return sendPageModel;
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }

    @Override
    public String getSigNinCode(SigninModel signinModel) {
        try {
            String sigNinCode="";

            String condition=signinModel.getCoachId()+"_"+signinModel.getCourseId()+"_"+signinModel.getClassTime()+"_"+signinModel.getPeriod();
            String dataKey=Code.sigNinCode_data_prefix+"_"+condition;

            sigNinCode=serveTool.<String>getManagerRedisData(dataKey,String.class);

            if (sigNinCode!=null&&!"".equals(sigNinCode)){
                return sigNinCode;
            }else {
                SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();
                long y_id=snowflakeIdWorker.nextId();
                sigNinCode=new String(Long.toString(y_id)).substring(12,18);

                RedisDataModel<String> redisDataModel=new RedisDataModel<>();
                redisDataModel.setKey(dataKey);
                redisDataModel.setTimeout(120);
                redisDataModel.setData(sigNinCode);
                serveTool.addManagerRedisData(redisDataModel);
                return sigNinCode;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }
}
