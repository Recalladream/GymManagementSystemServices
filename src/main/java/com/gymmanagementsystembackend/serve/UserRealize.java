package com.gymmanagementsystembackend.serve;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmanagementsystembackend.config.ProjectInterceptor;
import com.gymmanagementsystembackend.dao.*;
import com.gymmanagementsystembackend.domain.*;
import com.gymmanagementsystembackend.exception.BusinessException;
import com.gymmanagementsystembackend.model.*;
import com.gymmanagementsystembackend.serve.itf.UserInterface;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.ServeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRealize implements UserInterface {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VipRealize vipRealize;

    @Autowired
    private ServeTool serveTool;
    @Autowired
    private SubscribeMapper subscribeMapper;
    @Autowired
    private CoachMapper coachMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private VipInfMapper vipInfMapper;
    @Autowired
    private VipFitnessDataMapper vipFitnessDataMapper;
    @Autowired
    private RegisterMapper registerMapper;
    @Autowired
    private SigninRealize signinRealize;

    @Override
    public void updateUser(UserModel userModel) {
        String mes="";
        try {
            Boolean checkout=true;

            if (userModel.getScreenName()==null||"".equals(userModel.getScreenName())){checkout=false;mes="网名不能为空!";}
            if (userModel.getName()==null||"".equals(userModel.getName())){checkout=false;mes="真实姓名不能为空!";}
            if (userModel.getAge()<0||userModel.getAge()>120){checkout=false;mes="年龄超出范围!";}
            if ("男".equals(userModel.getSex())||"女".equals(userModel.getSex())){
            }else {checkout=false;mes="性别不符合规范!";}
            if (userModel.getPhone().length()!=11){checkout=false;mes="手机号不符合规范！";}
            // if (!IdCardUtil.isIdCardNo(vipModel.getIdentityCard())){checkout=false;mes="身份证不符合规范!";}

            if (checkout==true){
                //ThreadLocalManage threadLocalManage=ThreadLocalManage.getInstance();
                String userId= ProjectInterceptor.getDataFromThreadLocal();

                LambdaQueryWrapper<UserTable> lqw=new LambdaQueryWrapper<>();
                lqw.eq(UserTable::getUserId,userId);
                UserTable userTable= userMapper.selectOne(lqw);
                if (userTable!=null){
                    userTable.setScreenName(userModel.getScreenName());
                    userTable.setName(userModel.getName());
                    userTable.setAge(userModel.getAge());
                    userTable.setSex(userModel.getSex());
                    userTable.setPhone(userModel.getPhone());
                    userTable.setIdentityCard(userModel.getIdentityCard());

                    userMapper.update(userTable,lqw);

                    LambdaQueryWrapper<RegisterTable> lqwRegister=new LambdaQueryWrapper<>();
                    lqwRegister.eq(RegisterTable::getAccount,userTable.getPhone());
                    lqwRegister.eq(RegisterTable::getMode,"phone");

                    RegisterTable registerTable=registerMapper.selectOne(lqwRegister);
                    if (registerTable!=null){
                        registerTable.setAccount(userModel.getPhone());

                        registerMapper.update(registerTable,lqwRegister);
                    }

                    if (userModel.getVipId()!=null&&!"".equals(userModel.getVipId())){
                        VipModel vipModel=new VipModel();
                        vipModel.setVipId(userModel.getVipId());
                        vipModel.setName(userModel.getName());
                        vipModel.setAge(userModel.getAge());
                        vipModel.setSex(userModel.getSex());
                        vipModel.setPhone(userModel.getPhone());
                        vipModel.setIdentityCard(userModel.getIdentityCard());

                        vipRealize.updateVipUser(vipModel);
                    }

                    serveTool.updateRedisDataStatus(Code.vip_check_key,Code.vip_check_update_value);

                    return;
                }else {
                    mes="该用户不存在!";
                }
            }
        }catch(BusinessException e){
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.user_update_fail,mes);
    }

    @Override
    public SendPageModel getYiBooked(GetPageModel getPageModel) {
        try {
            SendPageModel sendPageModel=new SendPageModel();
            List<SubscribeModel> list=new ArrayList<>();
            IPage iPage=new Page(getPageModel.getPage(), getPageModel.getSize());

            LambdaQueryWrapper<SubscribeTable> lqwSub=new LambdaQueryWrapper<>();
            lqwSub.eq(SubscribeTable::getVipId,getPageModel.getVipId());

            List<SubscribeTable> subscribeTableList=subscribeMapper.selectList(iPage,lqwSub);

            for (SubscribeTable subscribeTable:subscribeTableList){
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

                subscribeModel.setCourseName(courseTable.getName());
                subscribeModel.setCoachName(coachTable.getName());

                list.add(subscribeModel);
            }

            sendPageModel.setPageList(list.toArray(new SubscribeModel[0]));
            sendPageModel.setTotal((int) iPage.getTotal());

            return sendPageModel;
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }

    @Override
    public UserModel getUserInf(String vipId) {
        try {
            String userId=ProjectInterceptor.getDataFromThreadLocal();

            UserModel userModel=new UserModel();

            if (userId!=null&&!"".equals(userId)){
                LambdaQueryWrapper<UserTable> lqwUser=new LambdaQueryWrapper<>();
                lqwUser.eq(UserTable::getUserId,userId);
                UserTable userTable=userMapper.selectOne(lqwUser);

                LambdaQueryWrapper<RegisterTable> lqwRegister=new LambdaQueryWrapper<>();
                lqwRegister.eq(RegisterTable::getUserId,userId);
                RegisterTable registerTable=registerMapper.selectOne(lqwRegister);

                if (userTable!=null&&registerTable!=null){
                    userModel.setScreenName(userTable.getScreenName());
                    userModel.setName(userTable.getName());
                    userModel.setAge(userTable.getAge());
                    userModel.setSex(userTable.getSex());
                    userModel.setPhone(userTable.getPhone());
                    userModel.setIdentityCard(userTable.getIdentityCard());
                    userModel.setJoinDate(registerTable.getJoinDate());
                }
            }


            if (vipId!=null&&!"".equals(vipId)){
                LambdaQueryWrapper<VipInfTable> lqwInf=new LambdaQueryWrapper<>();
                lqwInf.eq(VipInfTable::getVipId,vipId);
                VipInfTable vipInfTable=vipInfMapper.selectOne(lqwInf);

                if (vipInfTable!=null){
                    userModel.setVipId(vipInfTable.getVipId());
                    userModel.setType(vipInfTable.getType());
                    userModel.setVipJoinDate(vipInfTable.getJoinDate());
                    userModel.setExpirationDate(vipInfTable.getExpirationDate());
                }

                LambdaQueryWrapper<VipFitnessDataTable> lqwFitness=new LambdaQueryWrapper<>();
                lqwFitness.eq(VipFitnessDataTable::getVipId,vipId);
                VipFitnessDataTable vipFitnessDataTable=vipFitnessDataMapper.selectOne(lqwFitness);

                if (vipFitnessDataTable!=null){
                    userModel.setHeight(vipFitnessDataTable.getHeight());
                    userModel.setWeight(vipFitnessDataTable.getWeight());
                    userModel.setHeartRate(vipFitnessDataTable.getHeartRate());
                    userModel.setBloodPressure(vipFitnessDataTable.getBloodPressure());
                }

                LambdaQueryWrapper<SubscribeTable> lqwSub=new LambdaQueryWrapper<>();
                lqwSub.eq(SubscribeTable::getVipId,vipId);
                List<SubscribeTable> subscribeTableList=subscribeMapper.selectList(lqwSub);

                if (!subscribeTableList.isEmpty()){
                    userModel.setNumberClass(subscribeTableList.size());
                }
            }

            return userModel;
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }

    @Override
    public String openVip(VipModel vipModel) {
        try {
            String userId=ProjectInterceptor.getDataFromThreadLocal();

            String vipId=vipRealize.addVipUser(vipModel);

            if (vipId!=null&&!"".equals(vipId)){
                LambdaQueryWrapper<UserTable> lqwUser=new LambdaQueryWrapper<>();
                lqwUser.eq(UserTable::getUserId,userId);

                UserTable userTable=userMapper.selectOne(lqwUser);
                userTable.setVipId(vipId);

                userMapper.update(userTable,lqwUser);
                return vipId;
            }
        }catch(BusinessException e){
            throw e;
        }catch (Exception e){
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.vip_fail,"开通失败！");
    }

    @Override
    public void UserSigNin(SigninModel signinModel) {
        String mes="";
        try {
            String condition=signinModel.getCoachId()+"_"+signinModel.getCourseId()+"_"+signinModel.getClassTime()+"_"+signinModel.getPeriod();
            String dataKey=Code.sigNinCode_data_prefix+"_"+condition;
            System.out.println(dataKey);
            String sigNinCode=serveTool.<String>getManagerRedisData(dataKey,String.class);
            System.out.println(sigNinCode);
            System.out.println(signinModel.getSignCode());

            if (sigNinCode!=null&&!"".equals(sigNinCode)){
                if (signinModel.getSignCode()!=null&&!"".equals(signinModel.getSignCode())){
                    if (signinModel.getSignCode().equals(sigNinCode)){
                        SigninTable signinTable=new SigninTable();
                        signinTable.setVipId(signinModel.getVipId());
                        signinTable.setCoachId(signinModel.getCoachId());
                        signinTable.setCourseId(signinModel.getCourseId());
                        signinTable.setClassTime(signinModel.getClassTime());
                        signinTable.setPeriod(signinModel.getPeriod());

                        signinRealize.sign(signinTable);
                        return;
                    }else {
                        mes="签到码错误!";
                    }
                }else {
                    mes="签到码不能为空!";
                }
            }else {
                mes="签到码已过期!";
            }
        }catch(BusinessException e) {
            throw e;
        }
        catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }

        throw new BusinessException(Code.signin_fail,mes);
    }
}
