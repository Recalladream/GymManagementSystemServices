package com.gymmanagementsystembackend.serve.itf;

import com.gymmanagementsystembackend.domain.SigninTable;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.GiveLessonsModel;
import com.gymmanagementsystembackend.model.SendPageModel;
import com.gymmanagementsystembackend.model.SigninModel;

public interface SigninInterface {
    public void sign(SigninTable signinTable);
    public void add(SigninTable signinTable);
    public SendPageModel getSignInf(GetPageModel getPageModel);
    public SendPageModel getSignCourse(GetPageModel getPageModel);
    public String getSigNinCode(SigninModel signinModel);
}
