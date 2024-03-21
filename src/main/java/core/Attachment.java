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
public class Attachment {
    public static String L="L";
    public static String R="R";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    private final int rotno;
    private final int serialno;
    private final String orientation;
    private String remark;
    private final String init;
    private final String repair;
    private final String repaircheck;
    private final String Disqualified;
    private final String qualified;
    private final String macro;
    private final String type;
    private final String[] imgpath;
    
    public Attachment(int rotno,int serialno,String L){
        this(rotno,serialno,L,"");
    }
    
    public Attachment(int rotno,int serialno,String L,String remark){
        this(rotno,serialno,L,"","","","","","",remark,new String[]{});
    }
    
    public Attachment(int rotno,int serialno,String L,String remark,String[] imgpath){
        this(rotno,serialno,L,"","","","","","",remark,imgpath);
    }
    
    public Attachment(
            int rotno,
            int serialno,
            String L,
            String init,
            String repair,
            String repaircheck,
            String Disqualified,
            String qualified,
            String macro,
            String remark,
            String[] imgpath
    ){
        this(rotno,serialno,"",L,init,repair,
                repaircheck,Disqualified,qualified,macro,remark,imgpath);
    }
    
    public Attachment(
            int rotno,
            int serialno,
            String L,
            String init,
            String repair,
            String repaircheck,
            String Disqualified,
            String qualified,
            String macro,
            String remark
    ){
        this(rotno,serialno,"",L,init,repair,
                repaircheck,Disqualified,qualified,macro,remark,new String[]{});
    }
    
    public Attachment(
            int rotno,
            int serialno,
            String type,
            String L,
            String init,
            String repair,
            String repaircheck,
            String Disqualified,
            String qualified,
            String macro,
            String remark
    ){
        this.rotno=rotno;
        this.serialno=serialno;
        this.orientation=L;
        this.type=type;
        
        this.init=this.getString(init);
        this.repair=this.getString(repair);
        this.repaircheck=this.getString(repaircheck);
        this.Disqualified=this.getString(Disqualified);
        this.qualified=this.getString(qualified);
        this.macro=this.getString(macro);
        this.remark=this.getString(remark);
        this.imgpath=new String[]{};
    }
    
    public Attachment(
            int rotno,
            int serialno,
            String type,
            String L,
            String init,
            String repair,
            String repaircheck,
            String Disqualified,
            String qualified,
            String macro,
            String remark,
            String[] imgpath
    ){
        this.rotno=rotno;
        this.serialno=serialno;
        this.orientation=L;
        this.type=type;
        
        this.init=this.getString(init);
        this.repair=this.getString(repair);
        this.repaircheck=this.getString(repaircheck);
        this.Disqualified=this.getString(Disqualified);
        this.qualified=this.getString(qualified);
        this.macro=this.getString(macro);
        this.remark=this.getString(remark);
        this.imgpath=imgpath;
    }
    public int getRotno(){return rotno;}
    public int getSerial(){return serialno;}
    public String getOrientation(){return orientation;}
    public String getRemarks(){return remark;}
    public String getAttachmentName(){
        String s0 = String.format("%02d", rotno);
        String s1 = String.format("%02d", serialno);
        return s0+"-"+s1+orientation.toUpperCase();
    }
    
    public String gettype(){return type;}
    public String getInitDate(){return init;}
    public String getRepairOrderDate(){return repair;}
    public String getRepairCheckDate(){return repaircheck;}
    public String getQualifiedDate(){return qualified;}
    public String getDisqualifiedDate(){return Disqualified;}
    public String getMacroDate(){return macro;}
    public String[] getImagePath(){return imgpath;}
    
    public boolean compare(Attachment at){
        boolean flag=true;
        
        if(rotno!=at.getRotno())return false;
        if(serialno!=at.getSerial())return false;
        if(!orientation.trim().toLowerCase().equals(at.getOrientation().trim().toLowerCase()))return false;
        
        return flag;
    }
    
    
    public String getInspectionResult(){
        if(Disqualified.trim().length()>2)return "検査対象外";
        else if(qualified.trim().length()>2&&macro.trim().equals("-"))return "合格(製品)";
        else if(qualified.trim().length()>2&&macro.trim().length()>2)return "合格(マクロ試験用)";
        else return "-";
    }
    
    private String getString(String str){
        if(str==null||str.trim().isEmpty())return " - ";
        else return str;
    }    
    
}
