package com.gymmanagementsystembackend.serve;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boot.exception.SystemException;
import com.gymmanagementsystembackend.dao.ManagerMapper;
import com.gymmanagementsystembackend.dao.RegisterMapper;
import com.gymmanagementsystembackend.dao.UserMapper;
import com.gymmanagementsystembackend.domain.ManagerTable;
import com.gymmanagementsystembackend.domain.RegisterTable;
import com.gymmanagementsystembackend.domain.UserTable;
import com.gymmanagementsystembackend.exception.BusinessException;
import com.gymmanagementsystembackend.model.RegisterModel;
import com.gymmanagementsystembackend.serve.itf.RegisterInterface;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.MD5;
import com.gymmanagementsystembackend.tool.ServeTool;
import com.gymmanagementsystembackend.tool.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RegisterRealize implements RegisterInterface {

    @Autowired
    private RegisterMapper registerMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ServeTool serveTool;

    @Override
    public void registerUserAccount(RegisterModel registerModel){
        String mes="";
        try {
            Boolean check=true;

            if (registerModel.getAccount()==null||"".equals(registerModel.getAccount())){check=false;mes="账号不能为空!";}
            if (registerModel.getPassword()==null||"".equals(registerModel.getPassword())){check=false;mes="密码不能为空!";}
            if (registerModel.getCode()==null||"".equals(registerModel.getCode())){check=false;mes="验证码不能为空!";}
            if (registerModel.getPhone().length()!=11){check=false;mes="手机号为11位!";}

            if (check==true){
                LambdaQueryWrapper<RegisterTable> lqw=new LambdaQueryWrapper<>();
                lqw.eq(RegisterTable::getAccount,registerModel.getAccount()).eq(RegisterTable::getMode,"account");
                RegisterTable reg=registerMapper.selectOne(lqw);
                if (reg==null){
                    String code=serveTool.<String>getManagerRedisData("AccountRegister"+"_"+registerModel.getPhone()+"_"+"code",String.class);

                    if (code!=null||!"".equals(code)){
                        if (registerModel.getCode().equals(code)){
                            Boolean checkAccount=true;
                            LambdaQueryWrapper<UserTable> lqwUser1=new LambdaQueryWrapper<>();
                            lqwUser1.eq(UserTable::getPhone,registerModel.getPhone());

                            List<UserTable> userTableList1=userMapper.selectList(lqwUser1);
                            for (UserTable userTable:userTableList1){
                                LambdaQueryWrapper<RegisterTable> lqwRegister=new LambdaQueryWrapper<>();
                                lqwRegister.eq(RegisterTable::getUserId,userTable.getUserId());
                                lqwRegister.eq(RegisterTable::getMode,"account");
                                lqwRegister.isNull(RegisterTable::getAccount);
                                lqwRegister.isNull(RegisterTable::getPassword);

                                RegisterTable registerTable=registerMapper.selectOne(lqwRegister);
                                if (registerTable!=null){
                                    registerTable.setAccount(registerModel.getAccount());
                                    registerTable.setPassword(MD5.getMd5edData(registerModel.getPassword()));

                                    registerMapper.update(registerTable,lqwRegister);
                                }

                                checkAccount=false;
                            }

                            if (checkAccount==true){
                                SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();
                                long y_id=snowflakeIdWorker.nextId();
                                String userId=new String(Long.toString(y_id)).substring(12,18);

                                Date date=new Date();
                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                                String joinDate=simpleDateFormat.format(date);


                                RegisterTable registerTable=new RegisterTable();
                                registerTable.setAccount(registerModel.getAccount());
                                registerTable.setPassword(MD5.getMd5edData(registerModel.getPassword()));
                                registerTable.setMode("account");
                                registerTable.setUserId(userId);
                                registerTable.setJoinDate(joinDate);

                                registerMapper.insert(registerTable);

                                UserTable userTable=new UserTable();
                                userTable.setUserId(userId);
                                userTable.setPhone(registerModel.getPhone());
                                userTable.setScreenName("user_"+registerModel.getAccount());

                                //检查此手机号是否已开通vip--1
                                LambdaQueryWrapper<UserTable> lqwUser=new LambdaQueryWrapper<>();
                                lqwUser.eq(UserTable::getPhone,registerModel.getPhone());

                                List<UserTable> userTableList=userMapper.selectList(lqwUser);

                                for (UserTable userTable1:userTableList){
                                    if (userTable1.getVipId()!=null&&!"".equals(userTable1.getVipId())){

                                        userTable.setName(userTable1.getName());
                                        userTable.setAge(userTable1.getAge());
                                        userTable.setSex(userTable1.getSex());
                                        userTable.setIdentityCard(userTable1.getIdentityCard());
                                        userTable.setVipId(userTable1.getVipId());

                                        break;
                                    }
                                }
                                //--1

                                userMapper.insert(userTable);
                            }

                            return;
                        }else {
                            mes="验证码错误!";
                        }
                    }else {
                        mes="请获取验证码!";
                    }
                }else {
                    mes="账号已存在！";
                }
            }
        }catch (Exception b){
           b.printStackTrace();
           throw new SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.register_fail,mes);
    }

    @Override
    public long registerUserPhone(String phone){
        try {
            SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();
            long y_id=snowflakeIdWorker.nextId();
            String n_id=new String(Long.toString(y_id)).substring(12,18);

            Date date=new Date();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            String joinDate=simpleDateFormat.format(date);

            RegisterTable registerTable=new RegisterTable();
            registerTable.setAccount(phone);
            registerTable.setMode("phone");
            registerTable.setUserId(n_id);
            registerTable.setJoinDate(joinDate);

            registerMapper.insert(registerTable);

            UserTable userTable=new UserTable();
            userTable.setUserId(n_id);
            userTable.setScreenName("user_"+phone);

            userMapper.insert(userTable);

            return Long.parseLong(n_id);
        }catch (Exception b){
            b.printStackTrace();
            throw new SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }
}
