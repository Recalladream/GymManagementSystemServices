package com.gymmanagementsystembackend.serve.itf;

import com.gymmanagementsystembackend.domain.GiveLessonsTable;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.SendPageModel;

public interface GiveLessonsInterface {
    public void add(GiveLessonsTable giveLessonsTable);
    public void delete(int id);
    public void update(GiveLessonsTable giveLessonsTable);
    public SendPageModel getPage(GetPageModel getPageModel);
}
