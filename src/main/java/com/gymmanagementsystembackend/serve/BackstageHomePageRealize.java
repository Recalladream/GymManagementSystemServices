package com.gymmanagementsystembackend.serve;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmanagementsystembackend.dao.*;
import com.gymmanagementsystembackend.domain.*;
import com.gymmanagementsystembackend.model.BackstageHomePageDataModel;
import com.gymmanagementsystembackend.serve.itf.BackstageHomePageInterface;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.ServeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BackstageHomePageRealize implements BackstageHomePageInterface {
    @Autowired
    private ServeTool serveTool;
    @Autowired
    private VipInfMapper vipInfMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VipUserMapper vipUserMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private CoachMapper coachMapper;
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public BackstageHomePageDataModel getBackgroundStatistics() {
        BackstageHomePageDataModel bhpd=new BackstageHomePageDataModel();
        try {
            //----
            LambdaQueryWrapper<VipInfTable> lqwSupreme=new LambdaQueryWrapper<>();
            lqwSupreme.eq(VipInfTable::getType,"至尊会员");
            int supremeMemberNum=vipInfMapper.selectList(lqwSupreme).size();

            LambdaQueryWrapper<VipInfTable> lqwGold=new LambdaQueryWrapper<>();
            lqwGold.eq(VipInfTable::getType,"黄金会员");
            int goldMemberNum=vipInfMapper.selectList(lqwGold).size();

            LambdaQueryWrapper<VipInfTable> lqwSilver=new LambdaQueryWrapper<>();
            lqwSilver.eq(VipInfTable::getType,"白银会员");
            int silverMemberNum=vipInfMapper.selectList(lqwSilver).size();

            LambdaQueryWrapper<VipInfTable> lqwBronze=new LambdaQueryWrapper<>();
            lqwBronze.eq(VipInfTable::getType,"青铜会员");
            int bronzeMember=vipInfMapper.selectList(lqwBronze).size();

            LambdaQueryWrapper<UserTable> lqwNon=new LambdaQueryWrapper<>();
            lqwNon.isNull(UserTable::getVipId);
            int nonMember=userMapper.selectList(lqwNon).size();

            bhpd.setSupremeMemberNum(supremeMemberNum);
            bhpd.setGoldMemberNum(goldMemberNum);
            bhpd.setSilverMemberNum(silverMemberNum);
            bhpd.setBronzeMember(bronzeMember);
            bhpd.setNonmember(nonMember);
            //----
            int[] interfaceClicks = new int[7];
            int[] userVisits = new int[7];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < 7; i++) {
                Date nowDate = new Date();
                calendar.setTime(nowDate);
                calendar.add(Calendar.DATE, -i); // 直接在日期上减去 i 天

                String yiJuDate = simpleDateFormat.format(calendar.getTime()).replace("-", "_");

                // 假设 serveTool.getManagerRedisData 能够处理 Integer.class 并返回 Integer 类型的值
                Integer interfaceClicksValue = serveTool.getManagerRedisData(Code.interFaceClicks_data_prefix + "_" + yiJuDate, Integer.class);
                Integer userVisitsValue = serveTool.getManagerRedisData(Code.userVisits_data_prefix + "_" + yiJuDate, Integer.class);

                // 将 Integer 类型的值赋给 int 类型的数组元素（自动拆箱）
                interfaceClicks[i] = interfaceClicksValue == null ? 0 : interfaceClicksValue;
                userVisits[i] = userVisitsValue == null ? 0 : userVisitsValue;
            }

            bhpd.setInterfaceClicks(interfaceClicks);
            bhpd.setUserVisits(userVisits);
            //----
            int maleNum;
            int femaleNum;
            int[] maxAge={18,35,55,80};
            int[] minAge={13,19,36,56};
            int[] ageGroupNum=new int[4];
            LambdaQueryWrapper<VipUserTable> lqwMale=new LambdaQueryWrapper<>();
            lqwMale.eq(VipUserTable::getSex,"男");
            maleNum=vipUserMapper.selectList(lqwMale).size();

            LambdaQueryWrapper<VipUserTable> lqwFemale=new LambdaQueryWrapper<>();
            lqwFemale.eq(VipUserTable::getSex,"女");
            femaleNum=vipUserMapper.selectList(lqwFemale).size();

            for (int i=0;i<4;i++){
                LambdaQueryWrapper<VipUserTable> lqwAgeGroup=new LambdaQueryWrapper<>();
                lqwAgeGroup.ge(VipUserTable::getAge,minAge[i]);
                lqwAgeGroup.le(VipUserTable::getAge,maxAge[i]);

                ageGroupNum[i]=vipUserMapper.selectList(lqwAgeGroup).size();
            }

            bhpd.setMaleNum(maleNum);
            bhpd.setFemaleNum(femaleNum);
            bhpd.setAgeGroupNum(ageGroupNum);
            //----
            int fullMaterialNum=0;
            float fullMaterialPriceTotal=0;
            int damageMaterialNum=0;
            float damageMaterialPriceTotal=0;

            List<MaterialTable> materialTableList=materialMapper.selectList(null);

            for (MaterialTable materialTable:materialTableList){
                if ("否".equals(materialTable.getDestroy())){
                    fullMaterialNum+=materialTable.getNum();
                    fullMaterialPriceTotal+=materialTable.getTotalPrice();
                }
                if ("是".equals(materialTable.getDestroy())){
                    damageMaterialNum+=materialTable.getNum();
                    damageMaterialPriceTotal+=materialTable.getTotalPrice();
                }
            }

            bhpd.setFullMaterialNum(fullMaterialNum);
            bhpd.setFullMaterialPriceTotal(fullMaterialPriceTotal);
            bhpd.setDamageMaterialNum(damageMaterialNum);
            bhpd.setDamageMaterialPriceTotal(damageMaterialPriceTotal);
            //----
            int coachNum;
            float salaryTotal=0;
            float maxSalary=Float.MIN_VALUE;
            float minSalary=Float.MAX_VALUE;
            int classNum;

            LambdaQueryWrapper<CoachTable> lqwCoach=new LambdaQueryWrapper<>();
            List<CoachTable> coachTableList=coachMapper.selectList(null);
            coachNum=coachTableList.size();
            for (CoachTable coachTable:coachTableList){
                salaryTotal+=coachTable.getSalary();
                if (coachTable.getSalary()>maxSalary)maxSalary=coachTable.getSalary();
                if (coachTable.getSalary()<minSalary)minSalary=coachTable.getSalary();
            }

            LambdaQueryWrapper<CourseTable> lqwCourse=new LambdaQueryWrapper<>();
            classNum=courseMapper.selectList(null).size();

            bhpd.setCoachNum(coachNum);
            bhpd.setSalaryTotal(salaryTotal);
            bhpd.setMinSalary(minSalary);
            bhpd.setMaxSalary(maxSalary);
            bhpd.setClassNum(classNum);
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        return bhpd;
    }
}
