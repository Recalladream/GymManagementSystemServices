package com.gymmanagementsystembackend.serve.itf;

import com.gymmanagementsystembackend.domain.CoachTable;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.SendPageModel;

import java.util.List;

public interface CoachInterface {
    public void add(CoachTable coachTable);
    public void deleteOne(int id);
    public void update(CoachTable coachTable);
    public SendPageModel getPageCoach(GetPageModel getPageModel);
}
