package com.gymmanagementsystembackend.serve;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.exception.SystemException;
import com.gymmanagementsystembackend.dao.CoachMapper;
import com.gymmanagementsystembackend.domain.CoachTable;
import com.gymmanagementsystembackend.exception.BusinessException;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.RedisDataModel;
import com.gymmanagementsystembackend.model.SendPageModel;
import com.gymmanagementsystembackend.serve.itf.CoachInterface;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.ServeTool;
import com.gymmanagementsystembackend.tool.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachRealize implements CoachInterface {
    @Autowired
    private CoachMapper coachMapper;
    @Autowired
    private ServeTool serveTool;

    @Override
    public void add(CoachTable coachTable) {
        String mes="";
        try {
            boolean checkout=true;

            if (coachTable.getAge()<0||coachTable.getAge()>120){checkout=false;mes="年龄在0-120！";}
            if (coachTable.getSalary()<0){checkout=false;mes="薪水不能小于0";}
            if ("男".equals(coachTable.getSex())||"女".equals(coachTable.getSex())){
            }else {checkout=false;mes="性别不符合规范！";}
            //if (!IdCardUtil.isIdCardNo(coachTable.getIdentityCard())){checkout=false;mes="身份证号不符合规范！";}

            if (checkout==true){
                LambdaQueryWrapper<CoachTable> lqw=new LambdaQueryWrapper<>();
                lqw.eq(CoachTable::getIdentityCard, coachTable.getIdentityCard());
                CoachTable coachTable2=coachMapper.selectOne(lqw);
                if (coachTable2==null){
                    SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();
                    long y_id=snowflakeIdWorker.nextId();
                    String n_id=new String(Long.toString(y_id)).substring(12,18);

                    coachTable.setCoachId(n_id);

                    coachMapper.insert(coachTable);

                    serveTool.updateRedisDataStatus(Code.coach_check_key,Code.coach_check_update_value);

                    return;
                }else {
                    mes="此教练已存在！";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.coach_fail,mes);
    }

    @Override
    public void deleteOne(int id) {
        try {
            coachMapper.deleteById(id);

            serveTool.updateRedisDataStatus(Code.coach_check_key,Code.coach_check_update_value);
            serveTool.updateRedisDataStatus(Code.giveLessons_check_key,Code.giveLessons_check_update_value);
            serveTool.updateRedisDataStatus(Code.sigNinClass_check_key,Code.sigNinClass_check_update_value);
            serveTool.updateRedisDataStatus(Code.subscribeClass_check_key,Code.subscribeClass_check_update_value);

        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }

    @Override
    public void update(CoachTable coachTable) {
        String mes="";
        try {
            boolean checkout=true;

            if (coachTable.getAge()<0||coachTable.getAge()>120){checkout=false;mes="年龄在0-120！";}
            if (coachTable.getCoachId()==null){checkout=false;mes="教练id不能为空！";}
            if (coachTable.getSalary()<0){checkout=false;mes="薪水不能小于0";}
            //if (!IdCardUtil.isIdCardNo(coachTable.getIdentityCard())){checkout=false;mes="身份证号不符合规范！";}

            if (checkout==true){
                LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
                lqwCoach.ne(CoachTable::getId,coachTable.getId());
                lqwCoach.eq(CoachTable::getIdentityCard, coachTable.getIdentityCard());
                CoachTable coachTable1=coachMapper.selectOne(lqwCoach);

                if (coachTable1==null){
                    coachMapper.updateById(coachTable);

                    serveTool.updateRedisDataStatus(Code.coach_check_key,Code.coach_check_update_value);
                    serveTool.updateRedisDataStatus(Code.giveLessons_check_key,Code.giveLessons_check_update_value);
                    serveTool.updateRedisDataStatus(Code.sigNinClass_check_key,Code.sigNinClass_check_update_value);
                    serveTool.updateRedisDataStatus(Code.subscribeClass_check_key,Code.subscribeClass_check_update_value);

                    return;
                }else {
                    mes="此教练已存在!";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.coach_fail,mes);
    }

    @Override
    public SendPageModel getPageCoach(GetPageModel getPageModel) {
        try {
            String condition="";
            if (getPageModel.getCoachName()!=null&&!"".equals(getPageModel.getCoachName())){
                condition=getPageModel.getCoachName();
            }
            String page=String.valueOf(getPageModel.getPage());
            String size=String.valueOf(getPageModel.getSize());
            String dataKey=Code.coach_data_prefix+"_"+page+"_"+size+"_"+condition;

            if (serveTool.checkManagerRedisData(Code.coach_check_key,Code.coach_check_notUpdate_value)){
                SendPageModel sendPageModel=serveTool.<SendPageModel>getManagerRedisData(dataKey,SendPageModel.class);

                if (sendPageModel!=null){
                    return sendPageModel;
                }
            }

            SendPageModel sendPageModel=new SendPageModel();
            IPage iPage=new Page(getPageModel.getPage(), getPageModel.getSize());

            LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
            if (!condition.isEmpty()){
                lqwCoach.eq(CoachTable::getName,getPageModel.getCoachName());
            }

            List<CoachTable> userTableList=coachMapper.selectList(iPage,lqwCoach);

            sendPageModel.setPageList(userTableList.toArray(new CoachTable[0]));
            sendPageModel.setTotal((int) iPage.getTotal());

            RedisDataModel<SendPageModel> redisDataModel=new RedisDataModel<>();
            redisDataModel.setData(sendPageModel);
            redisDataModel.setKey(dataKey);
            redisDataModel.setTimeout(60*60*24);
            serveTool.addManagerRedisData(redisDataModel);
            serveTool.updateRedisDataStatus(Code.coach_check_key,Code.coach_check_notUpdate_value);

            return sendPageModel;
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }
}
