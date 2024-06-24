package com.gymmanagementsystembackend.serve.itf;

import com.gymmanagementsystembackend.domain.MaterialTable;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.ResultModel;
import com.gymmanagementsystembackend.model.SendPageModel;

public interface MaterialInterface {
    public void addWell(MaterialTable materialTable);
    public void addBad(MaterialTable materialTable);
    public void deleteWell(String materialId);
    public void deleteBad(String materialId);
    public void updateWell(MaterialTable materialTable);
    public void updateBad(MaterialTable materialTable);
    public SendPageModel getWellPage(GetPageModel getPageModel);
    public SendPageModel getBadPage(GetPageModel getPageModel);
}
