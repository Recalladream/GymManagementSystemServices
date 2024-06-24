package com.gymmanagementsystembackend.serve;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmanagementsystembackend.dao.MaterialMapper;
import com.gymmanagementsystembackend.domain.MaterialTable;
import com.gymmanagementsystembackend.exception.BusinessException;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.SendPageModel;
import com.gymmanagementsystembackend.serve.itf.MaterialInterface;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialRealize implements MaterialInterface {
    @Autowired
    private MaterialMapper materialMapper;

    @Override
    public void addWell(MaterialTable materialTable) {
        String mes="";
        try {
            Boolean checkout=true;
            if (materialTable.getName()==null||"".equals(materialTable.getName())){checkout=false;mes="器材名不能为空！";}
            if (materialTable.getNum()<0||materialTable.getUnitPrice()<0){checkout=false;mes="器材数或单价不能小于0！";}

            if (checkout==true){
                LambdaQueryWrapper<MaterialTable> lqwMaterial=new LambdaQueryWrapper<>();
                lqwMaterial.eq(MaterialTable::getName,materialTable.getName());

                MaterialTable materialTable1=materialMapper.selectOne(lqwMaterial);

                if (materialTable1==null){
                    SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker();
                    long y_id=snowflakeIdWorker.nextId();
                    String n_id=new String(Long.toString(y_id)).substring(12,18);

                    materialTable.setMaterialId(n_id);
                    materialTable.setDestroy("否");
                    materialTable.setTotalPrice(materialTable.getUnitPrice()*materialTable.getNum());

                    materialMapper.insert(materialTable);
                    return;
                }else {
                    mes="此器材已添加";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.material_fail,mes);
    }

    @Override
    public void addBad(MaterialTable materialTable) {
        String mes="";
        try {
            Boolean checkout=true;
            if (materialTable.getName()==null||"".equals(materialTable.getName())){checkout=false;mes="器材名不能为空！";}
            if (materialTable.getNum()<0||materialTable.getUnitPrice()<0){checkout=false;mes="器材数或单价不能小于0！";}

            if (checkout==true){
                LambdaQueryWrapper<MaterialTable> lqwMaterial=new LambdaQueryWrapper<>();
                lqwMaterial.eq(MaterialTable::getName,materialTable.getName());
                lqwMaterial.eq(MaterialTable::getDestroy,"否");

                MaterialTable materialTable1=materialMapper.selectOne(lqwMaterial);

                if (materialTable1!=null){
                    LambdaQueryWrapper<MaterialTable> lqwMaterial1=new LambdaQueryWrapper<>();
                    lqwMaterial1.eq(MaterialTable::getName,materialTable.getName());
                    lqwMaterial1.eq(MaterialTable::getDestroy,"是");

                    MaterialTable materialTable2=materialMapper.selectOne(lqwMaterial1);

                    if (materialTable2==null){
                        materialTable.setMaterialId(materialTable1.getMaterialId());
                        materialTable.setDestroy("是");
                        materialTable.setTotalPrice(materialTable.getUnitPrice()*materialTable.getNum());

                        materialMapper.insert(materialTable);
                        return;
                    }else {
                        mes="损坏器材已存在！";
                    }
                }else {
                    mes="没有购买此器材!";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.material_fail,mes);
    }

    @Override
    public void deleteWell(String materialId) {
        try {
            if (materialId!=null){
                LambdaQueryWrapper<MaterialTable> lqwMaterial=new LambdaQueryWrapper<>();
                lqwMaterial.eq(MaterialTable::getMaterialId,materialId);

                materialMapper.delete(lqwMaterial);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.material_fail,"器材id不能为空！");
    }

    @Override
    public void deleteBad(String materialId) {
        try {
            if (materialId!=null){
                LambdaQueryWrapper<MaterialTable> lqwMaterial=new LambdaQueryWrapper<>();
                lqwMaterial.eq(MaterialTable::getMaterialId,materialId);
                lqwMaterial.eq(MaterialTable::getDestroy,"是");

                materialMapper.delete(lqwMaterial);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.material_fail,"器材id不能为空！");
    }

    @Override
    public void updateWell(MaterialTable materialTable) {
        String mes="";
        try {
            Boolean checkout=true;
            if (materialTable.getMaterialId()==null){checkout=false;mes="器材id不能为空！";}
            if (materialTable.getName()==null||"".equals(materialTable.getName())){checkout=false;mes="器材名不能为空！";}
            if (materialTable.getNum()<0||materialTable.getUnitPrice()<0){checkout=false;mes="器材数或单价不能小于0！";}

            if (checkout==true){
                LambdaQueryWrapper<MaterialTable> lqwMaterial=new LambdaQueryWrapper<>();
                lqwMaterial.eq(MaterialTable::getMaterialId,materialTable.getMaterialId());
                lqwMaterial.eq(MaterialTable::getDestroy,"否");

                materialTable.setTotalPrice(materialTable.getNum()*materialTable.getUnitPrice());

                materialMapper.update(materialTable,lqwMaterial);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.material_fail,mes);
    }

    @Override
    public void updateBad(MaterialTable materialTable) {
        String mes="";
        try {
            Boolean checkout=true;
            if (materialTable.getMaterialId()==null){checkout=false;mes="器材id不能为空！";}
            if (materialTable.getName()==null||"".equals(materialTable.getName())){checkout=false;mes="器材名不能为空！";}
            if (materialTable.getNum()<0||materialTable.getUnitPrice()<0){checkout=false;mes="器材数或单价不能小于0！";}

            if (checkout==true){
                LambdaQueryWrapper<MaterialTable> lqwMaterial=new LambdaQueryWrapper<>();
                lqwMaterial.eq(MaterialTable::getMaterialId,materialTable.getMaterialId());
                lqwMaterial.eq(MaterialTable::getDestroy,"是");

                materialTable.setTotalPrice(materialTable.getNum()*materialTable.getUnitPrice());

                materialMapper.update(materialTable,lqwMaterial);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
        throw new BusinessException(Code.material_fail,mes);
    }

    @Override
    public SendPageModel getWellPage(GetPageModel getPageModel) {
        try {
            SendPageModel sendPageModel=new SendPageModel();
            List<MaterialTable> list=new ArrayList<>();
            LambdaQueryWrapper<MaterialTable> lqwMaterial=new LambdaQueryWrapper<>();
            if (getPageModel.getMaterialName()!=null&&!"".equals(getPageModel.getMaterialName())){
                lqwMaterial.eq(MaterialTable::getName,getPageModel.getMaterialName());
            }
            lqwMaterial.eq(MaterialTable::getDestroy,"否");

            IPage iPage=new Page(getPageModel.getPage(),getPageModel.getSize());

            list=materialMapper.selectList(iPage,lqwMaterial);

            sendPageModel.setPageList(list.toArray(new MaterialTable[0]));
            sendPageModel.setTotal((int) iPage.getTotal());

            return sendPageModel;
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }

    @Override
    public SendPageModel getBadPage(GetPageModel getPageModel) {
        try {
            SendPageModel sendPageModel=new SendPageModel();
            List<MaterialTable> list=new ArrayList<>();
            LambdaQueryWrapper<MaterialTable> lqwMaterial=new LambdaQueryWrapper<>();
            if (getPageModel.getMaterialName()!=null&&!"".equals(getPageModel.getMaterialName())){
                lqwMaterial.eq(MaterialTable::getName,getPageModel.getMaterialName());
            }
            lqwMaterial.eq(MaterialTable::getDestroy,"是");

            IPage iPage=new Page(getPageModel.getPage(),getPageModel.getSize());

            list=materialMapper.selectList(iPage,lqwMaterial);

            sendPageModel.setPageList(list.toArray(new MaterialTable[0]));
            sendPageModel.setTotal((int) iPage.getTotal());

            return sendPageModel;
        }catch (Exception e){
            e.printStackTrace();
            throw new com.boot.exception.SystemException(Code.system_error,"系统出错！请稍等...");
        }
    }
}
