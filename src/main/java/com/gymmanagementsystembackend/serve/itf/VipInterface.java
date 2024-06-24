package com.gymmanagementsystembackend.serve.itf;

import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.RenewModel;
import com.gymmanagementsystembackend.model.SendPageModel;
import com.gymmanagementsystembackend.model.VipModel;

import java.util.List;

public interface VipInterface {
    public String addVipUser(VipModel vipModel);
    public void addVipInf(String vipId, String type);
    public void addVipFitness(String vipId);
    public void addUserAccount(VipModel vipModel,String vipId);

    public void deleteVipOne(String vipId);
    public SendPageModel getPageVip(GetPageModel getPageModel);
    public void updateVipUser(VipModel vipModel);
    public void updateVipInf(VipModel vipModel);
    public void updateVipFitness(VipModel vipModel);
    public void renew(RenewModel renewModel);
}
