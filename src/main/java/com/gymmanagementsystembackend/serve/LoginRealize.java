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
import com.gymmanagementsystembackend.model.RedisDataModel;
import com.gymmanagementsystembackend.model.UserLoginModel;
import com.gymmanagementsystembackend.serve.itf.LoginInterface;
import com.gymmanagementsystembackend.tool.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginRealize implements LoginInterface {
    @Autowired
    private RegisterMapper registerMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RegisterRealize registerRealize;

    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ServeTool serveTool;

    @Autowired
    private MSGServiceImpl msgService;

    @Override
    public UserLoginModel loginUserAccount(String account, String password) {
        String mes="";
        try {
            LambdaQueryWrapper<RegisterTable> lqw=new LambdaQueryWrapper<>();
            lqw.eq(RegisterTable::getAccount,account).eq(RegisterTable::getMode,"account");
            RegisterTable registerTable= registerMapper.selectOne(lqw);
            if (registerTable!=null){
                String y_account=registerTable.getAccount();
                String y_password=registerTable.getPassword();

                if (password!=null){
                    String md5hex= MD5.getMd5edData(password);
                    if (md5hex.equals(y_password)){
                        LambdaQueryWrapper<UserTable> lqwUser=new LambdaQueryWrapper<>();
                        lqwUser.eq(UserTable::getUserId,registerTable.getUserId());
                        UserTable userTable=userMapper.selectOne(lqwUser);

                        int userId=Integer.parseInt(registerTable.getUserId());
                        String token= JwtToken.creatToken(account,(long)userId);

                        UserLoginModel userLoginModel=new UserLoginModel();
                        userLoginModel.setVipId(userTable.getVipId());
                        userLoginModel.setToken(token);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                        Date date=new Date();
                        String userVisitDate=simpleDateFormat.format(date).replace("-","_");;

                        Integer userVisitsDataEd=serveTool.<Integer>getManagerRedisData(Code.userVisits_data_prefix+"_"+userVisitDate,Integer.class);
                        Integer userVisitsDataNew=1;
                        if (userVisitsDataEd!=null)userVisitsDataNew=userVisitsDataEd+1;

                        RedisDataModel<Integer> redisDataModel=new RedisDataModel<>();
                        redisDataModel.setKey(Code.userVisits_data_prefix+"_"+userVisitDate);
                        redisDataModel.setData(userVisitsDataNew);
                        redisDataModel.setTimeout(60*60*24*7);
                        serveTool.addManagerRedisData(redisDataModel);

                        return userLoginModel;
                    }else {
                        mes="密码错误";
                    }
                }else {
                    mes="密码不能为空！";
                }
            }else {
                if (account!=null){
                    mes="账号不存在！";
                }else {
                    mes="账号不能为空！";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new SystemException(Code.system_error,"系统出错！请稍等...");
        }

        throw new BusinessException(Code.login_fail,mes);
    }

    @Override
    public UserLoginModel loginUserPhone(String phone, String getCode) {
        String mes="";
        try {
            Boolean check=true;

            if (phone==null||"".equals(phone)){check=false;mes="手机号不能为空!";}

            if (check==true){
                LambdaQueryWrapper<RegisterTable> lqw=new LambdaQueryWrapper<>();
                lqw.eq(RegisterTable::getAccount,phone).eq(RegisterTable::getMode,"phone");
                RegisterTable registerTable= registerMapper.selectOne(lqw);
                if (registerTable!=null){
                    int userId=Integer.parseInt(registerTable.getUserId());
                    String sendCode=serveTool.<String>getManagerRedisData("PhoneLogin"+"_"+phone+"_"+"code",String.class);
                    if (getCode!=null){
                        if (sendCode!=null){
                            if (getCode.equals(sendCode)){
                                LambdaQueryWrapper<UserTable> lqwUser=new LambdaQueryWrapper<>();
                                lqwUser.eq(UserTable::getUserId,registerTable.getUserId());
                                UserTable userTable=userMapper.selectOne(lqwUser);

                                String token= JwtToken.creatToken(phone,(long)userId);

                                UserLoginModel userLoginModel=new UserLoginModel();
                                userLoginModel.setVipId(userTable.getVipId());
                                userLoginModel.setToken(token);

                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                                Date date=new Date();
                                String userVisitDate=simpleDateFormat.format(date).replace("-","_");;

                                Integer userVisitsDataEd=serveTool.<Integer>getManagerRedisData(Code.userVisits_data_prefix+"_"+userVisitDate,Integer.class);
                                Integer userVisitsDataNew=1;
                                if (userVisitsDataEd!=null)userVisitsDataNew=userVisitsDataEd+1;

                                RedisDataModel<Integer> redisDataModel=new RedisDataModel<>();
                                redisDataModel.setKey(Code.userVisits_data_prefix+"_"+userVisitDate);
                                redisDataModel.setData(userVisitsDataNew);
                                redisDataModel.setTimeout(60*60*24*7);
                                serveTool.addManagerRedisData(redisDataModel);

                                return userLoginModel;
                            }else {
                                mes="验证码错误！";
                            }
                        }else {
                            mes="请获取验证码!";
                        }
                    }else {
                        mes="验证码不能为空！";
                    }
                }else {
                    String sendCode=serveTool.<String>getManagerRedisData("PhoneLogin"+"_"+phone+"_"+"code",String.class);
                    if (getCode!=null){
                        if (sendCode!=null){
                            if (getCode.equals(sendCode)){
                                long userId=registerRealize.registerUserPhone(phone);
                                String token= JwtToken.creatToken(phone,userId);

                                UserLoginModel userLoginModel=new UserLoginModel();
                                userLoginModel.setToken(token);

                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                                Date date=new Date();
                                String userVisitDate=simpleDateFormat.format(date).replace("-","_");

                                Integer userVisitsDataEd=serveTool.<Integer>getManagerRedisData(Code.userVisits_data_prefix+"_"+userVisitDate,Integer.class);
                                Integer userVisitsDataNew=1;
                                if (userVisitsDataEd!=null)userVisitsDataNew=userVisitsDataEd+1;

                                RedisDataModel<Integer> redisDataModel=new RedisDataModel<>();
                                redisDataModel.setKey(Code.userVisits_data_prefix+"_"+userVisitDate);
                                redisDataModel.setData(userVisitsDataNew);
                                redisDataModel.setTimeout(60*60*24*7);
                                serveTool.addManagerRedisData(redisDataModel);

                                return userLoginModel;
                            }else {
                                mes="验证码错误！";
                            }
                        }else {
                            mes="请获取验证码!";
                        }
                    }else {
                        mes="验证码不能为空！";
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new SystemException(Code.system_error,"系统出错！请稍等...");
        }

        throw new BusinessException(Code.login_fail,mes);
    }

    @Override
    public String loginManagerAccount(String account, String password) {
        String mes="";
        try {
            LambdaQueryWrapper<ManagerTable> lqw=new LambdaQueryWrapper<>();
            lqw.eq(ManagerTable::getAccount,account);
            ManagerTable managerTable= managerMapper.selectOne(lqw);
            if (managerTable!=null){
                String y_account=managerTable.getAccount();
                String y_password=managerTable.getPassword();

                if (password!=null&&password.equals(y_password)){
                    int managerId=Integer.parseInt(managerTable.getManagerId());

                    String token= JwtToken.creatToken(account,(long)managerId);

                    return token;
                }else {
                    if (password!=null){
                        mes="密码错误！";
                    }else {
                        mes="密码不能为空！";
                    }
                }
            }else {
                if (account!=null){
                    mes="账号不存在！";
                }else {
                    mes="账号不能为空！";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new SystemException(Code.system_error,"系统出错！请稍等...");
        }

        throw new BusinessException(Code.login_fail,mes);
    }

    @Override
    public void getPhoneLoginCode(String phone) {
        String mes="";
        try {
            if (phone!=null&&!"".equals(phone)){
                String code=serveTool.<String>getManagerRedisData("PhoneLogin"+"_"+phone+"_"+"code",String.class);

                if (code!=null&&!"".equals(code)){
                    mes="请勿重复获取!";
                }else {
                    SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();
                    long y_id=snowflakeIdWorker.nextId();
                    code=new String(Long.toString(y_id)).substring(12,18);

                    RedisDataModel<String> redisDataModel=new RedisDataModel<>();
                    redisDataModel.setKey("PhoneLogin"+"_"+phone+"_"+"code");
                    redisDataModel.setTimeout(120);
                    redisDataModel.setData(code);

                    serveTool.addManagerRedisData(redisDataModel);

                    //发送验证码
                    Map map = new HashMap();
                    map.put("code",code);
                    boolean b = msgService.send(map,phone);
                }

                return;
            }else {
                mes="手机号不能为空!";
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.get_code_fail,mes);
    }

    @Override
    public void getAccountRegisterCode(String phone) {
        String mes="";
        try {
            if (phone!=null&&!"".equals(phone)){
                String code=serveTool.<String>getManagerRedisData("AccountRegister"+"_"+phone+"_"+"code",String.class);
                if (code!=null&&!"".equals(code)){
                    mes="请勿重复获取!";
                }else {
                    SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();
                    long y_id=snowflakeIdWorker.nextId();
                    code=new String(Long.toString(y_id)).substring(12,18);

                    RedisDataModel<String> redisDataModel=new RedisDataModel<>();
                    redisDataModel.setKey("AccountRegister"+"_"+phone+"_"+"code");
                    redisDataModel.setTimeout(120);
                    redisDataModel.setData(code);

                    serveTool.addManagerRedisData(redisDataModel);

                    //发送验证码
                    Map map = new HashMap();
                    map.put("code",code);
                    boolean b = msgService.send(map,phone);
                }

                return;
            }else {
                mes="手机号不能为空!";
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.get_code_fail,mes);
    }
}
