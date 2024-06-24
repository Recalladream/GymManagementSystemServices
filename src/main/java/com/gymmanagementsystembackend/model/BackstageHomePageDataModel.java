package com.gymmanagementsystembackend.model;

import lombok.Data;

@Data
public class BackstageHomePageDataModel {
    public int supremeMemberNum;
    public int goldMemberNum;
    public int silverMemberNum;
    public int bronzeMember;
    public int nonmember;

    public int interfaceClicks[];
    public int userVisits[];

    public int maleNum;
    public int femaleNum;
    public int ageGroupNum[];

    public int fullMaterialNum;
    public float fullMaterialPriceTotal;
    public int damageMaterialNum;
    public float damageMaterialPriceTotal;

    public int coachNum;
    public float salaryTotal;
    public float maxSalary;
    public float minSalary;
    public int classNum;
}
