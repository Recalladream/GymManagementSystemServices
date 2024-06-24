package com.gymmanagementsystembackend.serve;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmanagementsystembackend.dao.*;
import com.gymmanagementsystembackend.domain.*;
import com.gymmanagementsystembackend.exception.BusinessException;
import com.gymmanagementsystembackend.model.*;
import com.gymmanagementsystembackend.serve.itf.VipInterface;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.IdCardUtil;
import com.gymmanagementsystembackend.tool.ServeTool;
import com.gymmanagementsystembackend.tool.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class VipRealize implements VipInterface {
    @Autowired
    private VipInfMapper vipInfMapper;
    @Autowired
    private VipUserMapper vipUserMapper;
    @Autowired
    private VipFitnessDataMapper vipFitnessDataMapper;
    @Autowired
    private SubscribeMapper subscribeMapper;
    @Autowired
    private ServeTool serveTool;
    @Autowired
    private RegisterMapper registerMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public SendPageModel getPageVip(GetPageModel getPageModel) {
        try {
            String condition="";
            if (getPageModel.getVipName()!=null&&!"".equals(getPageModel.getVipName())){
                condition=getPageModel.getVipName();
            }
            String page=String.valueOf(getPageModel.getPage());
            String size=String.valueOf(getPageModel.getSize());
            String dataKey=Code.vip_data_prefix+"_"+page+"_"+size+"_"+condition;

            if (serveTool.checkManagerRedisData(Code.vip_check_key,Code.vip_check_notUpdate_value)){
                SendPageModel sendPageModel=serveTool.<SendPageModel>getManagerRedisData(dataKey,SendPageModel.class);

                if (sendPageModel!=null){
                    return sendPageModel;
                }
            }

            SendPageModel sendPageModel=new SendPageModel();
            List<VipModel> list=new ArrayList<VipModel>();
            IPage iPage=new Page(getPageModel.getPage(), getPageModel.getSize());

            LambdaQueryWrapper<VipUserTable> lqw=new LambdaQueryWrapper();
            if (!condition.isEmpty())
            lqw.eq(VipUserTable::getName,getPageModel.getVipName());

            List<VipUserTable> userTableList=vipUserMapper.selectList(iPage,lqw);

            if (!userTableList.isEmpty()){
                for (VipUserTable vipUserTable:userTableList){
                    VipModel vipModel=new VipModel();

                    vipModel.setVipId(vipUserTable.getVipId());
                    vipModel.setName(vipUserTable.getName());
                    vipModel.setAge(vipUserTable.getAge());
                    vipModel.setSex(vipUserTable.getSex());
                    vipModel.setPhone(vipUserTable.getPhone());
                    vipModel.setIdentityCard(vipUserTable.getIdentityCard());

                    list.add(vipModel);
                }
            }

            for (VipModel vipModel:list){
                LambdaQueryWrapper<VipInfTable> lqwInf=new LambdaQueryWrapper<>();
                lqwInf.eq(VipInfTable::getVipId,vipModel.getVipId());

                VipInfTable vipInfTable=vipInfMapper.selectOne(lqwInf);

                vipModel.setType(vipInfTable.getType());
                vipModel.setJoinDate(vipInfTable.getJoinDate());
                vipModel.setExpirationDate(vipInfTable.getExpirationDate());
            }

            for (VipModel vipModel:list){
                LambdaQueryWrapper<VipFitnessDataTable> lqwFitness=new LambdaQueryWrapper<>();
                lqwFitness.eq(VipFitnessDataTable::getVipId,vipModel.getVipId());

                VipFitnessDataTable vipFitnessDataTable=vipFitnessDataMapper.selectOne(lqwFitness);

                vipModel.setHeight(vipFitnessDataTable.getHeight());
                vipModel.setWeight(vipFitnessDataTable.getWeight());
                vipModel.setHeartRate(vipFitnessDataTable.getHeartRate());
                vipModel.setBloodPressure(vipFitnessDataTable.getBloodPressure());

                LambdaQueryWrapper<SubscribeTable> lqwSub=new LambdaQueryWrapper<>();
                lqwSub.eq(SubscribeTable::getVipId,vipFitnessDataTable.getVipId());
                List<SubscribeTable> subscribeTableList=subscribeMapper.selectList(lqwSub);

                vipModel.setNumberClass(subscribeTableList.size());
            }

            sendPageModel.setPageList(list.toArray(new VipModel[0]));
            sendPageModel.setTotal((int) iPage.getTotal());

            RedisDataModel<SendPageModel> redisDataModel=new RedisDataModel<>();
            redisDataModel.setData(sendPageModel);
            redisDataModel.setKey(dataKey);
            redisDataModel.setTimeout(60*60*24);
            serveTool.addManagerRedisData(redisDataModel);
            serveTool.updateRedisDataStatus(Code.vip_check_key,Code.vip_check_notUpdate_value);

            return sendPageModel;
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }

    @Override
    public String addVipUser(VipModel vipModel) {
        String mes="";
        try {
            Boolean checkout=true;

            if (vipModel.getName()==null||"".equals(vipModel.getName())){checkout=false;mes="会员姓名不能为空!";}
            if (vipModel.getType()==null){checkout=false;mes="会员类型不能为空！";}
            if (vipModel.getAge()<0||vipModel.getAge()>120){checkout=false;mes="年龄超出范围!";}
            if ("男".equals(vipModel.getSex())||"女".equals(vipModel.getSex())){
            }else {checkout=false;mes="性别不符合规范!";}
            if (vipModel.getPhone().length()!=11){checkout=false;mes="手机号不符合规范！";}
            //if (!IdCardUtil.isIdCardNo(vipModel.getIdentityCard())){checkout=false;mes="身份证不符合规范!";}

            if (checkout==true){
                LambdaQueryWrapper<VipUserTable> lqw=new LambdaQueryWrapper<>();
                lqw.eq(VipUserTable::getPhone,vipModel.getPhone());
                VipUserTable vipUserTable=vipUserMapper.selectOne(lqw);
                if (vipUserTable==null){
                    SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();
                    long y_id=snowflakeIdWorker.nextId();
                    String n_id=new String(Long.toString(y_id)).substring(12,18);

                    vipUserTable=new VipUserTable();
                    vipUserTable.setVipId(n_id);
                    vipUserTable.setName(vipModel.getName());
                    vipUserTable.setAge(vipModel.getAge());
                    vipUserTable.setSex(vipModel.getSex());
                    vipUserTable.setPhone(vipModel.getPhone());
                    vipUserTable.setIdentityCard(vipModel.getIdentityCard());

                    vipUserMapper.insert(vipUserTable);

                    this.addVipInf(n_id, vipModel.getType());
                    this.addVipFitness(n_id);
                    this.addUserAccount(vipModel,n_id);

                    serveTool.updateRedisDataStatus(Code.vip_check_key,Code.vip_check_update_value);

                    return n_id;
                }else {
                    mes="此会员已存在！";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.vip_add_fail,mes);
    }

    @Override
    public void addVipInf(String vipId, String type) {
        try {
            Date joinDate=new Date();
            Date expirationDate=null;
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            String join=simpleDateFormat.format(joinDate);
            String expiration="";

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(joinDate);


            if ("至尊会员".equals(type)){
                calendar.add(Calendar.MONTH,12);
                expirationDate=calendar.getTime();

                expiration=simpleDateFormat.format(expirationDate);

            }else if ("黄金会员".equals(type)){
                calendar.add(Calendar.MONTH,6);
                expirationDate=calendar.getTime();

                expiration=simpleDateFormat.format(expirationDate);

            }else if ("白银会员".equals(type)){
                calendar.add(Calendar.MONTH,3);
                expirationDate=calendar.getTime();

                expiration=simpleDateFormat.format(expirationDate);

            }else if ("青铜会员".equals(type)){
                calendar.add(Calendar.MONTH,1);
                expirationDate=calendar.getTime();

                expiration=simpleDateFormat.format(expirationDate);

            }

            VipInfTable vipInfTable=new VipInfTable();
            vipInfTable.setVipId(vipId);
            vipInfTable.setType(type);
            vipInfTable.setJoinDate(join);
            vipInfTable.setExpirationDate(expiration);

            vipInfMapper.insert(vipInfTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }

    @Override
    public void addVipFitness(String vipId) {
        try {
            VipFitnessDataTable vipFitnessDataTable=new VipFitnessDataTable();
            vipFitnessDataTable.setVipId(vipId);

            vipFitnessDataMapper.insert(vipFitnessDataTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }

    @Override
    public void addUserAccount(VipModel vipModel,String vipId) {
        try {
            Boolean checkAccount=true;
            Boolean checkPhone=true;

            LambdaQueryWrapper<UserTable> lqwUser=new LambdaQueryWrapper<>();
            lqwUser.eq(UserTable::getPhone,vipModel.getPhone());

            List<UserTable> userTableList=userMapper.selectList(lqwUser);
            if (!userTableList.isEmpty()){
                for (UserTable userTable:userTableList){
                    LambdaQueryWrapper<UserTable> lqwUser1=new LambdaQueryWrapper<>();
                    lqwUser1.eq(UserTable::getUserId,userTable.getUserId());

                    userTable.setVipId(vipId);
                    userTable.setName(vipModel.getName());
                    userTable.setAge(vipModel.getAge());
                    userTable.setSex(vipModel.getSex());
                    userTable.setIdentityCard(vipModel.getIdentityCard());

                    userMapper.update(userTable,lqwUser1);

                    LambdaQueryWrapper<RegisterTable> lqwRegister=new LambdaQueryWrapper<>();
                    lqwRegister.eq(RegisterTable::getUserId,userTable.getUserId());

                    RegisterTable registerTable=registerMapper.selectOne(lqwRegister);
                    if ("account".equals(registerTable.getMode()))checkAccount=false;
                    if ("phone".equals(registerTable.getMode()))checkPhone=false;
                }
            }

            Date date=new Date();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            String joinDate=simpleDateFormat.format(date);

            SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();


            if (checkPhone==true){
                long phoneY_id=snowflakeIdWorker.nextId();
                String phoneUserId=new String(Long.toString(phoneY_id)).substring(12,18);

                RegisterTable registerTable=new RegisterTable();
                UserTable userTable=new UserTable();

                registerTable.setUserId(phoneUserId);
                registerTable.setAccount(vipModel.getPhone());
                registerTable.setMode("phone");
                registerTable.setJoinDate(joinDate);

                userTable.setUserId(phoneUserId);
                userTable.setScreenName("user_"+phoneUserId);
                userTable.setName(vipModel.getName());
                userTable.setAge(vipModel.getAge());
                userTable.setSex(vipModel.getSex());
                userTable.setPhone(vipModel.getPhone());
                userTable.setIdentityCard(vipModel.getIdentityCard());
                userTable.setVipId(vipId);

                registerMapper.insert(registerTable);
                userMapper.insert(userTable);

            }

            if (checkAccount==true){
                long accountY_id=snowflakeIdWorker.nextId();
                String accountUserId=new String(Long.toString(accountY_id)).substring(12,18);

                RegisterTable registerTable1=new RegisterTable();
                UserTable userTable1=new UserTable();

                registerTable1.setUserId(accountUserId);
                registerTable1.setMode("account");
                registerTable1.setJoinDate(joinDate);

                userTable1.setUserId(accountUserId);
                userTable1.setScreenName("user_"+accountUserId);
                userTable1.setName(vipModel.getName());
                userTable1.setAge(vipModel.getAge());
                userTable1.setSex(vipModel.getSex());
                userTable1.setPhone(vipModel.getPhone());
                userTable1.setIdentityCard(vipModel.getIdentityCard());
                userTable1.setVipId(vipId);

                registerMapper.insert(registerTable1);
                userMapper.insert(userTable1);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }

    @Override
    public void deleteVipOne(String vipId) {
        try {
            if (vipId!=null){
                LambdaQueryWrapper<VipUserTable> lqwVipUser=new LambdaQueryWrapper<>();
                lqwVipUser.eq(VipUserTable::getVipId,vipId);
                vipUserMapper.delete(lqwVipUser);

                LambdaQueryWrapper<UserTable> lqwUser=new LambdaQueryWrapper<>();
                lqwUser.eq(UserTable::getVipId,vipId);

                List<UserTable> userTableList=userMapper.selectList(lqwUser);

                for (UserTable userTable:userTableList){
                    LambdaQueryWrapper<UserTable> lqwUser1=new LambdaQueryWrapper<>();
                    lqwUser1.eq(UserTable::getUserId,userTable.getUserId());

                    userTable.setVipId(null);

                    userMapper.update(userTable,lqwUser1);
                }

                serveTool.updateRedisDataStatus(Code.vip_check_key,Code.vip_check_update_value);
                serveTool.updateRedisDataStatus(Code.subscribe_check_key,Code.subscribe_check_update_value);
                serveTool.updateRedisDataStatus(Code.sigNin_check_key,Code.sigNin_check_update_value);
                serveTool.updateRedisDataStatus(Code.sigNinNot_check_key,Code.sigNinNot_check_update_value);

                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.vip_delete_fail,"会员id不能为空！");
    }

    @Override
    public void updateVipUser(VipModel vipModel) {
        String mes="";
        try {
            Boolean checkout=true;

            if (vipModel.getVipId()==null){checkout=false;mes="会员id不能为空!";}
            if (vipModel.getName()==null||"".equals(vipModel.getName())){checkout=false;mes="会员姓名不能为空!";}
            if (vipModel.getAge()<0||vipModel.getAge()>120){checkout=false;mes="年龄超出范围!";}
            if ("男".equals(vipModel.getSex())||"女".equals(vipModel.getSex())){
            }else {checkout=false;mes="性别不符合规范!";}
            if (vipModel.getPhone().length()!=11){checkout=false;mes="手机号不符合规范！";}
            //if (!IdCardUtil.isIdCardNo(vipModel.getIdentityCard())){checkout=false;mes="身份证不符合规范!";}

            if (checkout==true){
                LambdaQueryWrapper<VipUserTable> lqw=new LambdaQueryWrapper<>();
                lqw.ne(VipUserTable::getVipId, vipModel.getVipId())
                        .and(wrapper -> wrapper.eq(VipUserTable::getIdentityCard, vipModel.getIdentityCard()).or().eq(VipUserTable::getPhone, vipModel.getPhone()));
                VipUserTable vipUserTable1=vipUserMapper.selectOne(lqw);

                if (vipUserTable1==null){
                    VipUserTable vipUserTable=new VipUserTable();
                    vipUserTable.setName(vipModel.getName());
                    vipUserTable.setAge(vipModel.getAge());
                    vipUserTable.setSex(vipModel.getSex());
                    vipUserTable.setPhone(vipModel.getPhone());
                    vipUserTable.setIdentityCard(vipModel.getIdentityCard());

                    LambdaQueryWrapper<VipUserTable> lqwvip=new LambdaQueryWrapper<>();
                    lqwvip.eq(VipUserTable::getVipId, vipModel.getVipId());
                    vipUserMapper.update(vipUserTable,lqwvip);

                    LambdaQueryWrapper<UserTable> lqwUser=new LambdaQueryWrapper<>();
                    lqwUser.eq(UserTable::getVipId,vipModel.getVipId());

                    UserTable userTable=new UserTable();
                    userTable.setName(vipModel.getName());
                    userTable.setAge(vipModel.getAge());
                    userTable.setSex(vipModel.getSex());
                    userTable.setPhone(vipModel.getPhone());
                    userTable.setIdentityCard(vipModel.getIdentityCard());

                    userMapper.update(userTable,lqwUser);

                    List<UserTable> userTableList=userMapper.selectList(lqwUser);
                    for (UserTable userTable1:userTableList){
                        LambdaQueryWrapper<RegisterTable> lqwRegister=new LambdaQueryWrapper<>();
                        lqwRegister.eq(RegisterTable::getUserId,userTable1.getUserId()).eq(RegisterTable::getMode,"phone");

                        RegisterTable registerTable=registerMapper.selectOne(lqwRegister);

                        if (registerTable!=null){
                            registerTable.setAccount(vipModel.getPhone());

                            registerMapper.update(registerTable,lqwRegister);
                        }

                    }

                    serveTool.updateRedisDataStatus(Code.vip_check_key,Code.vip_check_update_value);
                    serveTool.updateRedisDataStatus(Code.subscribe_check_key,Code.subscribe_check_update_value);
                    serveTool.updateRedisDataStatus(Code.sigNin_check_key,Code.sigNin_check_update_value);
                    serveTool.updateRedisDataStatus(Code.sigNinNot_check_key,Code.sigNinNot_check_update_value);

                    return;
                }else {
                    mes="修改失败！身份证或手机号重复";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }

        throw new BusinessException(Code.vip_update_fail,mes);
    }

    @Override
    public void updateVipInf(VipModel vipModel) {
        String mes="";
        try {
            Boolean checkout=true;

            if (vipModel.getJoinDate()==null){checkout=false;mes="注册日期不能为空!";}
            if (vipModel.getExpirationDate()==null){checkout=false;mes="过期日期不能为空!";}
            if (vipModel.getVipId()==null){checkout=false;mes="会员id不能为空!";}
            if (vipModel.getType()==null){checkout=false;mes="会员类型不能为空!";}

            if (checkout==true){
                VipInfTable vipInfTable=new VipInfTable();
                vipInfTable.setType(vipModel.getType());

                int time=0;
                if ("至尊会员".equals(vipModel.getType()))time=12;
                else if ("黄金会员".equals(vipModel.getType()))time=6;
                else if ("白银会员".equals(vipModel.getType()))time=3;
                else time=1;

                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                Date joinDate=simpleDateFormat.parse(vipModel.getJoinDate());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(joinDate);
                calendar.add(Calendar.MONTH,time);
                String expirationDate=simpleDateFormat.format(calendar.getTime());

                vipInfTable.setExpirationDate(expirationDate);

                LambdaQueryWrapper<VipInfTable> lqwinf=new LambdaQueryWrapper<>();
                lqwinf.eq(VipInfTable::getVipId, vipModel.getVipId());
                vipInfMapper.update(vipInfTable,lqwinf);

                serveTool.updateRedisDataStatus(Code.vip_check_key,Code.vip_check_update_value);

                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.vip_update_fail,mes);
    }

    @Override
    public void updateVipFitness(VipModel vipModel) {
        String mes="";
        try {
            Boolean checkout=true;

            if (vipModel.getHeight()<0||vipModel.getHeight()>250){checkout=false;mes="身高不符合规范!";}
            if (vipModel.getWeight()<0||vipModel.getWeight()>150){checkout=false;mes="体重不符合规范!";}
            if (vipModel.getBloodPressure()<0||vipModel.getBloodPressure()>250){checkout=false;mes="血压不符合规范!";}
            if (vipModel.getHeartRate()<0||vipModel.getHeartRate()>250){checkout=false;mes="心率不符合规范!";}

            if (checkout==true){
                VipFitnessDataTable vipFitnessDataTable=new VipFitnessDataTable();
                vipFitnessDataTable.setHeight(vipModel.getHeight());
                vipFitnessDataTable.setWeight(vipModel.getWeight());
                vipFitnessDataTable.setBloodPressure(vipModel.getBloodPressure());
                vipFitnessDataTable.setHeartRate(vipModel.getHeartRate());

                LambdaQueryWrapper<VipFitnessDataTable> lqwVipFitness=new LambdaQueryWrapper<>();
                lqwVipFitness.eq(VipFitnessDataTable::getVipId,vipModel.getVipId());

                vipFitnessDataMapper.update(vipFitnessDataTable,lqwVipFitness);

                serveTool.updateRedisDataStatus(Code.vip_check_key,Code.vip_check_update_value);

                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.vip_update_fail,mes);
    }

    @Override
    public void renew(RenewModel renewModel) {
        String mes="";
        try {
            Boolean checkout=true;
            if (renewModel.getVipId()==null){checkout=false;mes="会员id不能为空!";}

            if (checkout==true){
                LambdaQueryWrapper<VipInfTable> lqwVipInf=new LambdaQueryWrapper<>();
                lqwVipInf.eq(VipInfTable::getVipId,renewModel.getVipId());

                VipInfTable vipInfTable=vipInfMapper.selectOne(lqwVipInf);

                if (renewModel.getNum()<12&&renewModel.getNum()>=1){
                    if (vipInfTable!=null) {
                        String expirationDate = vipInfTable.getExpirationDate();
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                        Date date=simpleDateFormat.parse(expirationDate);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.MONTH,renewModel.getNum());

                        expirationDate=simpleDateFormat.format(calendar.getTime());

                        vipInfTable.setExpirationDate(expirationDate);

                        vipInfMapper.update(vipInfTable,lqwVipInf);

                        serveTool.updateRedisDataStatus(Code.vip_check_key,Code.vip_check_update_value);

                        return;
                    }else {
                        mes="此会员不存在！";
                    }
                }else {
                    mes="不在续约范围！";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.vip_fail,mes);
    }
}
