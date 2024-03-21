/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;



/**
 *
 * @author keita
 */
public class InspectionData {

    private final String day;
    private final Attachment[] repair;
    private final Attachment[] repairCheckedQualified;
    private final Attachment[] repairCheckedDisqualified;
    private final Attachment[] macro;
    private final InspectionRotInfo[] inspectionrot;
    private final String imgpath;

    public InspectionData (String day,
            InspectionRotInfo[] inspectionrot,
            Attachment[] repair,
            Attachment[] repairCheckedQualified,
            Attachment[] repairCheckedDisqualified,
            Attachment[] macro,
            String imgpath){
        this.day=day;
        this.inspectionrot=inspectionrot;
        this.repair=repair;
        this.repairCheckedQualified=repairCheckedQualified;
        this.repairCheckedDisqualified=repairCheckedDisqualified;
        this.macro=macro;
        this.imgpath=imgpath;
    }
    
    public String getImagePath(){return imgpath;}
    public String getDay(){return day;}
    public InspectionRotInfo[] getInspectionRot(){return inspectionrot;}
    public Attachment[] getRepairAttchment(){return repair;}
    public Attachment[] getRepairCheckedQualified(){return repairCheckedQualified;}
    public Attachment[] getRepairCheckedDisqualified(){return repairCheckedDisqualified;}
    public Attachment[] getMacro(){return macro;}
    
    
    
    
    
}
