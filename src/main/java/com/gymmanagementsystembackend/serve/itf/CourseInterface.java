package com.gymmanagementsystembackend.serve.itf;

import com.gymmanagementsystembackend.domain.CourseTable;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.SendPageModel;

public interface CourseInterface {
    public void addCourse(CourseTable courseTable);
    public void deleteCourse(int id);
    public void updateCourse(CourseTable courseTable);
    public SendPageModel getPageCourse(GetPageModel getPageModel);
}
