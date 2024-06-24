package com.gymmanagementsystembackend.serve.itf;

import com.gymmanagementsystembackend.domain.SubscribeTable;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.SendPageModel;

public interface SubscribeInterface {
    public void add(SubscribeTable subscribeTable);
    public void delete(int id);
    public SendPageModel getPage(GetPageModel getPageModel);

    public SendPageModel getSubScribe(GetPageModel getPageModel);
}
