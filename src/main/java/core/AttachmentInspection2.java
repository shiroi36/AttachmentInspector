/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.SQL_OPE;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author keita
 */
public class AttachmentInspection2 {
    Logger log=LogManager.getLogger(AttachmentInspection2.class);

    /**
     * @param args the command line arguments
     */
    
    private final SQL_OPE sql;
    private final SimpleDateFormat sd;
//    private String imgpath;
    private ArrayList<InspectionData> list;
    public static int PALLET_ID_ORDERED=9999;//修正指示を出したパレットの番号
//    private Attachment[] alist0;//修正指示金物の集合
//    private Attachment[] alist1;//修正確認金物の集合
    
    public AttachmentInspection2(SQL_OPE sql){
        this.sql=sql;
        this.sd = new SimpleDateFormat("yyyy-MM-dd");
        this.list=new ArrayList<InspectionData>();
        
    }
    public AttachmentInspection2(SQL_OPE sql,String[][] rotnum_type,int numberinrot){
        this(sql);
        this.init(rotnum_type, numberinrot);
    }
    public AttachmentInspection2(SQL_OPE sql,String[][] rotnum_type_num){
        this(sql);
        this.init(rotnum_type_num);
    }
    
    private String getIMGforRot(int rotno) {
//        log.info("rotno = " + rotno);
        String val = "";
        boolean flag=false;
//        log.info(at.getAttachmentName());

        for (int i = 0; i < list.size(); i++) {
            String imgpath=list.get(i).getImagePath();
            InspectionRotInfo[] rlist0 = list.get(i).getInspectionRot();
            
            if (imgpath.trim().length() == 0) continue;
            for (InspectionRotInfo rot : rlist0) {

                if (rot.compare(rotno)) {
                    if(flag)val="";
                    flag=true;
                    String[] v = rot.getRotPicData();
                    
                    for (int j = 0; j < v.length; j++) {
                        String img = v[j];
                        if(img.trim().length()!=0){
                            val += "IMG:" + imgpath + img + ", ";
                        }
                    }
                }
//                if(flag)break;
                
            }
        }
        
        if(val.trim().length()==0){
            return "ここにロットの全体の写真をならべてください";
        }
        
        return val;
    }
    
    
    private String getIMGforOrdered(Attachment at) {
        String val = "";
        boolean flag=false;
//        log.info(at.getAttachmentName());

        for (int i = 0; i < list.size(); i++) {
            String imgpath=list.get(i).getImagePath();
            Attachment[] alist0 = list.get(i).getRepairAttchment();
            
            if (imgpath.trim().length() == 0) continue;
            for (Attachment attach : alist0) {

                if (attach.compare(at)) {
                    if(flag)val="";
                    flag=true;
                    String[] v = attach.getImagePath();
                    
                    for (int j = 0; j < v.length; j++) {
                        String img = v[j];
                        if(img.trim().length()!=0){
                            val += "IMG:" + imgpath + img + ", ";
                        }
                    }

                    break;
                }
            }
//           if(flag)break;
        }
        
        if(val.trim().length()==0){
            return "ここに製品番号写真と補修位置写真をならべてください";
        }
        
        return val;
    }
    
    private String getIMGforRepaired(Attachment at) {
        String val = "";
        boolean flag=false;
//        log.info(at.getAttachmentName());

        for (int i = 0; i < list.size(); i++) {
            String imgpath=list.get(i).getImagePath();
            Attachment[] alist0 = list.get(i).getRepairCheckedQualified();
            Attachment[] alist1 = list.get(i).getRepairCheckedDisqualified();
            
            if(imgpath.trim().length()==0)continue;
            
            for (Attachment attach : alist0) {

                if (attach.compare(at)) {
                    if(flag)val="";
                    flag=true;
                    String[] v = attach.getImagePath();
                    for (int j = 0; j < v.length; j++) {
                        String img = v[j];
                        if(img.trim().length()!=0){
                            val += "IMG:" + imgpath + img + ", ";
                        }
                    }

                    break;
                }
            }
            for (Attachment attach : alist1) {

                if (attach.compare(at)) {
                    if(flag)val="";
                    flag=true;
                    String[] v = attach.getImagePath();
                    for (int j = 0; j < v.length; j++) {
                        String img = v[j];
                        if(img.trim().length()!=0){
                            val += "IMG:" + imgpath + img + ", ";
                        }
                    }

                    break;
                }
            }
//            if(flag)break;
        }
        if(val.trim().length()==0){
            return "ここに修正後の製品番号写真と補修状況写真をならべてください";
        }
        return val;
    }
    
    public void Inspection(InspectionData id)throws ParseException{
        
        list.add(id);
        
        //ある日の受入検査で検査した金物を記録し、一旦すべて合格とする。
        this.Step1A(id.getInspectionRot(), id.getDay());
        //修正指示を出した金物について、合格記録を消去し、repに記録
        this.Step2(id.getRepairAttchment(), id.getDay());
        //修正指示を確認して合格した金物について、repcheckとqualifiedに記録
        this.Step3(id.getRepairCheckedQualified(), id.getDay());
        //修正指示を確認して不合格した金物について、repcheckとdisqualifiedに記録
        this.Step4(id.getRepairCheckedDisqualified(), id.getDay());
        //マクロ検査を行った金物について、macroとqualifiedに記録
        this.Step5(id.getMacro(), id.getDay());
        
    }


    public void Inspection(
            String day,
            InspectionRotInfo[] inspectionrot,
            Attachment[] repair,
            Attachment[] repairCheckedQualified,
            Attachment[] repairCheckedDisqualified,
            Attachment[] macro,
            String imgpath
            ) throws ParseException{
        
        this.Inspection(new InspectionData(day, 
                inspectionrot, 
                repair, 
                repairCheckedQualified, 
                repairCheckedDisqualified, 
                macro,imgpath));
    }
    
    
    
    private void init(String[][] rotname_typename_numberinrot){
        sql.executeUpdate("drop table if exists record");
        sql.executeUpdate("create table if not exists record(\n"
                + "    rownumber    int identity(1,1),\n"
                + "    type         varchar,\n"
                + "    rotnum       int,\n"
                + "    serial       int,\n"
                + "    LR           varchar,\n"
                + "    init         date default null,\n"
                + "    rep          date default null,\n"
                + "    repcheck     date default null,\n"
                + "    disqualified date default null,\n"
                + "    qualified    date default null,\n"
                + "    macro    date default null,\n"
                + "    remark       varchar "
                + ");");
        sql.executeUpdate("create index ind_rotnum on record(rotnum)");
        sql.executeUpdate("create index ind_qualified on record(qualified)");
        
        for (int i = 0; i < rotname_typename_numberinrot.length; i++) {
            for (int j = 1; j < Integer.parseInt(rotname_typename_numberinrot[i][2]) + 1; j++) {
                sql.executeUpdate("insert into record(rotnum,type, serial,lr)values("
                        + "" + rotname_typename_numberinrot[i][0] + ","
                        + "'" + rotname_typename_numberinrot[i][1] + "',"
                        + "" + j + ","
                        + "'L')");
                sql.executeUpdate("insert into record(rotnum,type, serial,lr)values("
                        + "" + rotname_typename_numberinrot[i][0] + ","
                        + "'" + rotname_typename_numberinrot[i][1] + "',"
                        + "" + j + ","
                        + "'R')");
             }
        }
        
    }
    
    
    private void init(String[][] rotname_typename,int numberinrot){
        sql.executeUpdate("drop table if exists record");
        sql.executeUpdate("create table if not exists record(\n"
                + "    rownumber    int identity(1,1),\n"
                + "    type         varchar,\n"
                + "    rotnum       int,\n"
                + "    serial       int,\n"
                + "    LR           varchar,\n"
                + "    init         date default null,\n"
                + "    rep          date default null,\n"
                + "    repcheck     date default null,\n"
                + "    disqualified date default null,\n"
                + "    qualified    date default null,\n"
                + "    macro    date default null,\n"
                + "    remark       varchar "
                + ");");
        sql.executeUpdate("create index ind_rotnum on record(rotnum)");
        sql.executeUpdate("create index ind_qualified on record(qualified)");
        
        for (int i = 0; i < rotname_typename.length; i++) {
            for (int j = 1; j < numberinrot + 1; j++) {
                sql.executeUpdate("insert into record(rotnum,type, serial,lr)values("
                        + "" + rotname_typename[i][0] + ","
                        + "'" + rotname_typename[i][1] + "',"
                        + "" + j + ","
                        + "'L')");
                sql.executeUpdate("insert into record(rotnum,type, serial,lr)values("
                        + "" + rotname_typename[i][0] + ","
                        + "'" + rotname_typename[i][1] + "',"
                        + "" + j + ","
                        + "'R')");
             }
        }
        
    }
    
    public void Step1A(InspectionRotInfo[] rotno,String str) throws ParseException{
        for (int i = 0; i < rotno.length; i++) {
            int rot = rotno[i].getRotno();
            this.Step1A(rot, str);
        }
    }
    private void Step1A(int rotno,String str) throws ParseException{
        Date  date = DateFormat.getDateInstance().parse(str);
        String day=sd.format(date);
        sql.executeUpdate("update record set "
                + "init='" + day + "' where rotnum=" + rotno + "");
        sql.executeUpdate("update record set "
                + "qualified='" + day + "' where rotnum=" + rotno + "");
    }
    
    public void Step2(Attachment[] attach,String str) throws ParseException{
        Date  date = DateFormat.getDateInstance().parse(str);
        String day=sd.format(date);
        
        for (int i = 0; i < attach.length; i++) {
            Attachment attachment = attach[i];
            int rotno=attachment.getRotno();
            int serial=attachment.getSerial();
            String ori=attachment.getOrientation();
            String remark=attachment.getRemarks();
            sql.executeUpdate("update record set "
                    + "qualified=null, "
                    + "remark='"+ remark+ "', "
                    + "rep= '"+ day+ "' "
                    + "where "
                    + "rotnum=" + rotno + " and "
                    + "serial=" + serial + " and "
                    + "LR='"+ ori+ "'"
            );
        }
    }
    
    public void Step3(Attachment[] attach,String str) throws ParseException{
        Date  date = DateFormat.getDateInstance().parse(str);
        String day=sd.format(date);
        
        for (int i = 0; i < attach.length; i++) {
            Attachment attachment = attach[i];
            int rotno=attachment.getRotno();
            int serial=attachment.getSerial();
            String ori=attachment.getOrientation();
            
            sql.executeUpdate("update record set "
                    + "qualified= '"+ day+ "', "
                    + "repcheck= '"+ day+ "' "
                    + "where "
                    + "rotnum=" + rotno + " and "
                    + "serial=" + serial + " and "
                    + "LR='"+ ori+ "'"
            );
        }
    }
    
    public void Step4(Attachment[] attach,String str) throws ParseException{
        Date  date = DateFormat.getDateInstance().parse(str);
        String day=sd.format(date);
        
        for (int i = 0; i < attach.length; i++) {
            Attachment attachment = attach[i];
            int rotno=attachment.getRotno();
            int serial=attachment.getSerial();
            String ori=attachment.getOrientation();
            
            sql.executeUpdate("update record set "
                    + "qualified=null ,"
                    + "disqualified= '"+ day+ "', "
                    + "repcheck= '"+ day+ "' "
                    + "where "
                    + "rotnum=" + rotno + " and "
                    + "serial=" + serial + " and "
                    + "LR='"+ ori+ "'"
            );
                       
        }
    }
    public void Step5(Attachment[] attach,String str) throws ParseException{
        Date  date = DateFormat.getDateInstance().parse(str);
        String day=sd.format(date);
        
        for (int i = 0; i < attach.length; i++) {
            Attachment attachment = attach[i];
            int rotno=attachment.getRotno();
            int serial=attachment.getSerial();
            String ori=attachment.getOrientation();
            String remark=attachment.getRemarks();
            
            sql.executeUpdate("update record set "
                    + "rep= '"+ day+ "', "
                    + "qualified= '"+ day+ "', "
                    + "remark='"+ remark+ "', "
//                    + "repcheck= '"+ day+ "', "
                    + "macro= '"+ day+ "' "
                    + "where "
                    + "rotnum=" + rotno + " and "
                    + "serial=" + serial + " and "
                    + "LR='"+ ori+ "'"
            );
                       
        }
    }
    
    public Attachment[] getRepairList(String str) throws ParseException{
        Date  date = DateFormat.getDateInstance().parse(str);
        String day=sd.format(date);
        sql.executeUpdate("set @day='"+ day+ "';");
        String[][] sqllist = sql.transpose(sql.getQueryDataString(
                "SELECT rotnum,serial,type,lr,init,\n"
                + "rep,repcheck,disqualified,\n"
                + "qualified,macro,remark\n"
                + " FROM \"PUBLIC\".RECORD \n"
                + " where rep=@day \n"
                + " order by rotnum,serial,LR;"));
        ArrayList<Attachment> list=new ArrayList<Attachment>();
        
        for (int i = 0; i < sqllist.length; i++) {
            String[] atparam = sqllist[i];
//            log.info(Arrays.toString(atparam));
            if(atparam[10].trim().equals("検査対象外"))continue;
            Attachment at=new Attachment(
                    Integer.parseInt(atparam[0]),
                    Integer.parseInt(atparam[1]),
                    atparam[2],
                    atparam[3],
                    atparam[4],
                    atparam[5],
                    atparam[6],
                    atparam[7],
                    atparam[8],
                    atparam[9],
                    atparam[10]
            );
            list.add(at);
        }
        Attachment[] a = list.toArray(new Attachment[list.size()]);
        return a;
    }
    
    public Attachment[][] getInspectedRotStatus(String str) throws ParseException{
        Date  date = DateFormat.getDateInstance().parse(str);
        String day=sd.format(date);
        sql.executeUpdate("set @day='"+ day+ "';");
        int[] rot=sql.getQueryDataInt(
                "SELECT rotnum FROM \"PUBLIC\".RECORD "
                        + "where rep=@day "
                        + "or qualified=@day "
                        + "or disqualified=@day "
                        + "or init=@day "
                        + "group by rotnum order by rotnum;")[0];
        Attachment[][] val=new Attachment[rot.length][];
        for (int i = 0; i < rot.length; i++) {
            int j = rot[i];
            sql.executeUpdate("set @rot="+ j+ ";");
            ArrayList<Attachment> list=new ArrayList<Attachment>();
            String[][] sqllist = sql.transpose(sql.getQueryDataString(
                    "select \n"
                    + "rotnum,serial,type,lr,init,rep,repcheck,\n"
                    + "disqualified,qualified,macro,remark\n"
                    + "from record where rotnum=@rot \n"
                    + "order by rotnum,serial,LR"));
            for (int k = 0; k < sqllist.length; k++) {
                String[] atparam = sqllist[k];
                Attachment at = new Attachment(
                    Integer.parseInt(atparam[0]),
                    Integer.parseInt(atparam[1]),
                    atparam[2],
                    atparam[3],
                    atparam[4],
                    atparam[5],
                    atparam[6],
                    atparam[7],
                    atparam[8],
                    atparam[9],
                    atparam[10]
                );
                list.add(at);
            }
            val[i] = list.toArray(new Attachment[list.size()]);
        }
        return val;
    }
    
    public Attachment[][] getAllStatus(){
        int[] rot=sql.getQueryDataInt(
                "SELECT rotnum FROM \"PUBLIC\".RECORD "
                        + "group by rotnum order by rotnum;")[0];
        Attachment[][] val=new Attachment[rot.length][];
        for (int i = 0; i < rot.length; i++) {
            int j = rot[i];
            sql.executeUpdate("set @rot="+ j+ ";");
            ArrayList<Attachment> list=new ArrayList<Attachment>();
            String[][] sqllist = sql.transpose(sql.getQueryDataString(
                    "select \n"
                    + "rotnum,serial,type,lr,init,rep,repcheck,\n"
                    + "disqualified,qualified,macro,remark\n"
                    + "from record where rotnum=@rot \n"
                    + "order by rotnum,serial,LR"));
            for (int k = 0; k < sqllist.length; k++) {
                String[] atparam = sqllist[k];
                Attachment at = new Attachment(
                        Integer.parseInt(atparam[0]),
                        Integer.parseInt(atparam[1]),
                        atparam[2],
                        atparam[3],
                        atparam[4],
                        atparam[5],
                        atparam[6],
                        atparam[7],
                        atparam[8],
                        atparam[9],
                        atparam[10]
                );
                list.add(at);
            }
            val[i] = list.toArray(new Attachment[list.size()]);
        }
        return val;
    }
    
    public Attachment[] getRepairList(){
        String[][] sqllist = sql.transpose(sql.getQueryDataString(
                "SELECT rotnum,serial,type,lr,init,\n"
                + "rep,repcheck,disqualified,\n"
                + "qualified,macro,remark\n"
                + " FROM \"PUBLIC\".RECORD \n"
                + " where rep is not null \n"
                + " and repcheck is null \n"
                + " and disqualified is null \n"
                + " and qualified is null \n"
                + " and macro is null \n"
                + " order by rotnum,serial,LR;"));
        ArrayList<Attachment> list=new ArrayList<Attachment>();
        
        for (int i = 0; i < sqllist.length; i++) {
            String[] atparam = sqllist[i];
            if(atparam[10].trim().equals("検査対象外"))continue;
            Attachment at=new Attachment(
                        Integer.parseInt(atparam[0]),
                        Integer.parseInt(atparam[1]),
                        atparam[2],
                        atparam[3],
                        atparam[4],
                        atparam[5],
                        atparam[6],
                        atparam[7],
                        atparam[8],
                        atparam[9],
                        atparam[10]
            );
            list.add(at);
        }
        Attachment[] a = list.toArray(new Attachment[list.size()]);
        return a;
    }
    
    public Attachment[] getRepairCheckedList(String str) throws ParseException{
        Date  date = DateFormat.getDateInstance().parse(str);
        String day=sd.format(date);
        sql.executeUpdate("set @day='"+ day+ "';");
        String[][] sqllist = sql.transpose(sql.getQueryDataString(
                "SELECT rotnum,serial,type,lr,init,\n"
                + "rep,repcheck,disqualified,\n"
                + "qualified,macro,remark\n"
                + " FROM \"PUBLIC\".RECORD \n"
                + " where repcheck =@day \n"
                + " order by rotnum,serial,LR;"));
//        log.info(sqllist.length);
        ArrayList<Attachment> list=new ArrayList<Attachment>();
        
//        log.info("sqllist.length = " + sqllist.length);
        for (int i = 0; i < sqllist.length; i++) {
            String[] atparam = sqllist[i];
//            log.info(Arrays.toString(atparam));
            if(atparam[10].trim().equals("検査対象外"))continue;
            Attachment at=new Attachment(
                        Integer.parseInt(atparam[0]),
                        Integer.parseInt(atparam[1]),
                        atparam[2],
                        atparam[3],
                        atparam[4],
                        atparam[5],
                        atparam[6],
                        atparam[7],
                        atparam[8],
                        atparam[9],
                        atparam[10]
            );
            list.add(at);
        }
        Attachment[] a = list.toArray(new Attachment[list.size()]);
        return a;
    }
    
    public String CheckRot(String str ) throws ParseException{
        Date  date = DateFormat.getDateInstance().parse(str);
        String day=sd.format(date);
        log.info("day = " + day);
        sql.executeUpdate("set @day='"+ day+ "';");
        String[] rots=sql.getQueryDataString("select rotnum from record "
                + "where init=@day group by rotnum order by rotnum;")[0];
        String rot="";
        for (int i = 0; i < rots.length; i++) {
            String rot1 = rots[i];
            rot+=rot1+",";
        }
        return rot;
    }
    public int[] matome(String type,String str ) throws ParseException{
        Date  date = DateFormat.getDateInstance().parse(str);
        String day=sd.format(date);
        sql.executeUpdate("set @day='"+ day+ "';");
        sql.executeUpdate("set @type='"+ type+ "';");
        //その日に新たに検査した数
        int num1=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where init=@day "
                        + "and type=@type "
                        + "and disqualified is null "
                        + ";")[0][0];
        num1+=sql.getQueryDataInt(
                "select count(*) from record where init <> @day"
                        + " and repcheck=@day"
                        + " and type=@type "
                        + " and disqualified is null "
                        + " and rep <> @day;")[0][0];
        num1+=sql.getQueryDataInt(
                "select count(*) from record where init  <>  @day"
                        + " and (repcheck  <>  @day or repcheck is null)"
                        + " and type=@type "
                        + " and disqualified is null "
                        + " and rep=@day;")[0][0];
        //修正指示なしで合格とした数
        int num2=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where init=@day "
                        + " and type=@type "
                        + "and qualified=@day "
                        + "and disqualified is null "
                        + "and macro is null; ")[0][0];
        //修正指示をした数
        int num3=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where rep=@day "
                        + " and type=@type "
                        + "and disqualified is null ;")[0][0];
        //修正が残っている数
        int num30=sql.getQueryDataInt(
                "select count(*) from record where rep=@day"
                        + " and type=@type "
                        + " and repcheck is null "
                        + " and qualified is null "
                        + " and disqualified is null "
                        + " and macro is null ;")[0][0];
        //以前の修正内容を確認し合格とした数
        int num4=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where repcheck=@day "
                        + " and type=@type "
                        + " and qualified=@day;")[0][0];
        //以前の修正内容を確認し不合格とした数
        int num5=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where repcheck=@day "
                        + " and type=@type "
                        + " and disqualified=@day;")[0][0];
        //マクロ試験対象とした数
        int num6=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where macro=@day "
                        + " and type=@type "
                        + ";")[0][0];
        //その日に、マクロ試験を抜いて合格した数(製品合格)
        int num7=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where qualified=@day "
                        + " and type=@type "
                        + "and macro is null;")[0][0];
        return new int[]{num1,num2,num3,num30,num4,num5,num6,num7};
    }
    public int[] matomeL(String type,String str ) throws ParseException{
        String ori="L";
        Date  date = DateFormat.getDateInstance().parse(str);
        String day=sd.format(date);
        sql.executeUpdate("set @day='"+ day+ "';");
        sql.executeUpdate("set @type='"+ type+ "';");
        //その日に新たに検査した数
        int num1=sql.getQueryDataInt(
                "select count(*) from record where init=@day"
                        + " and disqualified is null "
                        + " and type=@type "
                        + " and LR='"+ ori+ "' "
                        + ";")[0][0];
        num1+=sql.getQueryDataInt(
                "select count(*) from record where init  <>  @day"
                        + " and disqualified is null "
                        + " and type=@type "
                        + " and repcheck=@day"
                        + " and rep  <>  @day"
                        + " and LR='"+ ori+ "' ;")[0][0];
        num1+=sql.getQueryDataInt(
                "select count(*) from record where init  <>  @day"
                        + " and disqualified is null "
                        + " and type=@type "
                        + " and (repcheck  <>  @day or repcheck is null)"
                        + " and rep=@day"
                        + " and LR='"+ ori+ "' ;")[0][0];
        //修正指示なしで合格とした数
        int num2=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where init=@day and qualified=@day "
                        + "and macro is null"
                        + " and type=@type "
                        + " and LR='"+ ori+ "' "
                        + "; ")[0][0];
        
        //修正指示をした数
        int num3=sql.getQueryDataInt(
                "select count(*) from record where rep=@day"
                        + " and disqualified is null "
                        + " and type=@type "
                        + " and LR='"+ ori+ "' "
                        + ";")[0][0];
        //修正が残っている数
        int num30=sql.getQueryDataInt(
                "select count(*) from record where rep=@day"
                        + " and repcheck is null "
                        + " and type=@type "
                        + " and qualified is null "
                        + " and disqualified is null "
                        + " and LR='"+ ori+ "' "
                        + " and macro is null ;")[0][0];
        
        //以前の修正内容を確認し合格とした数
        int num4=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where repcheck=@day "
                        + " and qualified=@day"
                        + " and type=@type "
                        + " and LR='"+ ori+ "' "
                        + ";")[0][0];
        //以前の修正内容を確認し不合格とした数
        int num5=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where repcheck=@day "
                        + " and disqualified=@day"
                        + " and type=@type "
                        + " and LR='"+ ori+ "' "
                        + ";")[0][0];
        //マクロ試験対象とした数
        int num6=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where macro=@day"
                        + " and type=@type "
                        + " and LR='"+ ori+ "' "
                        + ";")[0][0];
        //その日に、マクロ試験を抜いて合格した数(製品合格)
        int num7=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where qualified=@day "
                        + " and macro is null "
                        + " and type=@type "
                        + " and LR='"+ ori+ "' "
                        + ";")[0][0];
        return new int[]{num1,num2,num3,num30,num4,num5,num6,num7};
    }
    public int[] matomeR(String type,String str ) throws ParseException{
        String ori="R";
        Date  date = DateFormat.getDateInstance().parse(str);
        String day=sd.format(date);
        sql.executeUpdate("set @day='"+ day+ "';");
        sql.executeUpdate("set @type='"+ type+ "';");
        //その日に新たに検査した数
        int num1=sql.getQueryDataInt(
                "select count(*) from record where init=@day"
                        + " and disqualified is null "
                        + " and type=@type "
                        + " and LR='"+ ori+ "' "
                        + ";")[0][0];
        num1+=sql.getQueryDataInt(
                "select count(*) from record where init  <>  @day"
                        + " and disqualified is null "
                        + " and type=@type "
                        + " and repcheck=@day"
                        + " and rep  <>  @day"
                        + " and LR='"+ ori+ "' ;")[0][0];
        num1+=sql.getQueryDataInt(
                "select count(*) from record where init  <>  @day"
                        + " and disqualified is null "
                        + " and type=@type "
                        + " and (repcheck  <>  @day or repcheck is null)"
                        + " and rep=@day"
                        + " and LR='"+ ori+ "' ;")[0][0];
        //修正指示なしで合格とした数
        int num2=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where init=@day and qualified=@day "
                        + " and type=@type "
                        + "and macro is null"
                        + " and LR='"+ ori+ "' "
                        + "; ")[0][0];
        
        //修正指示をした数
        int num3=sql.getQueryDataInt(
                "select count(*) from record where rep=@day"
                        + " and type=@type "
                        + " and disqualified is null "
                        + " and LR='"+ ori+ "' "
                        + ";")[0][0];
        //修正が残っている数
        int num30=sql.getQueryDataInt(
                "select count(*) from record where rep=@day"
                        + " and type=@type "
                        + " and repcheck is null "
                        + " and qualified is null "
                        + " and disqualified is null "
                        + " and LR='"+ ori+ "' "
                        + " and macro is null ;")[0][0];
        
        //以前の修正内容を確認し合格とした数
        int num4=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where repcheck=@day "
                        + " and type=@type "
                        + " and qualified=@day"
                        + " and LR='"+ ori+ "' "
                        + ";")[0][0];
        //以前の修正内容を確認し不合格とした数
        int num5=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where repcheck=@day "
                        + " and type=@type "
                        + " and disqualified=@day"
                        + " and LR='"+ ori+ "' "
                        + ";")[0][0];
        //マクロ試験対象とした数
        int num6=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where macro=@day"
                        + " and type=@type "
                        + " and LR='"+ ori+ "' "
                        + ";")[0][0];
        //その日に、マクロ試験を抜いて合格した数(製品合格)
        int num7=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where qualified=@day "
                        + " and macro is null"
                        + " and type=@type "
                        + " and LR='"+ ori+ "' "
                        + ";")[0][0];
        return new int[]{num1,num2,num3,num30,num4,num5,num6,num7};
    }
    
    public int[] matomeall(String type) throws ParseException{
        
        //検査総数
        int num1=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where init is not null "
                        + " and disqualified is null "
                        + " and type= '"+ type+ "'"
                        + ";")[0][0];
        
        
        //修正が残っている数
        int num2=sql.getQueryDataInt(
                "select count(*) from record where rep  is not  null"
                        + " and repcheck is null "
                        + " and qualified is null "
                        + " and disqualified is null "
                        + " and type= '"+ type+ "'"
                        + " and macro is null ;")[0][0];
        //不合格とした数
        int num5=sql.getQueryDataInt(
                "select count(*) from record where repcheck is not null"
                        + " and disqualified is not null"
                        + " and type= '"+ type+ "'"
                        + ";")[0][0];
        //マクロ試験対象とした数
        int num6=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where macro is not null "
                        + " and disqualified is null "
                        + " and type= '"+ type+ "'"
                        + ";")[0][0];
        //その日に、マクロ試験を抜いて合格した数(製品合格)
        int num7=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where qualified is not null "
                        + " and disqualified is null "
                        + " and type= '"+ type+ "'"
                        + "and macro is null;")[0][0];
        return new int[]{num1,num2,num5,num6,num7};
    }
    
    public int[] matomeall_L(String type) throws ParseException{
        String ori="L";
        //検査総数
        int num1=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where init is not null"
                        + " and disqualified is null "
                        + " and LR='"+ ori+ "' "
                        + " and type= '"+ type+ "'"
                        + ";")[0][0];
        
        //修正が残っている数
        int num2=sql.getQueryDataInt(
                "select count(*) from record where rep is not null"
                        + " and repcheck is null "
                        + " and qualified is null "
                        + " and disqualified is null "
                        + " and LR='"+ ori+ "' "
                        + " and type= '"+ type+ "'"
                        + " and macro is null ;")[0][0];
        //不合格とした数
        int num5=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where repcheck is not null "
                        + " and LR='"+ ori+ "' "
                        + " and type= '"+ type+ "'"
                        + " and disqualified is not null;")[0][0];
        //マクロ試験対象とした数
        int num6=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where macro is not null"
                        + " and disqualified is null "
                        + " and LR='"+ ori+ "' "
                        + " and type= '"+ type+ "'"
                        + ";")[0][0];
        //その日に、マクロ試験を抜いて合格した数(製品合格)
        int num7=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where qualified is not null "
                        + "and macro is null"
                        + " and disqualified is null "
                        + " and LR='"+ ori+ "' "
                        + " and type= '"+ type+ "'"
                        + ";")[0][0];
        return new int[]{num1,num2,num5,num6,num7};
    }
    
    public int[] matomeall_R(String type) throws ParseException{
        String ori="R";
        //検査総数
        int num1=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where init is not null"
                        + " and disqualified is null "
                        + " and LR='"+ ori+ "' "
                        + " and type= '"+ type+ "'"
                        + ";")[0][0];
        //修正が残っている数
        int num2=sql.getQueryDataInt(
                "select count(*) from record where rep is not null"
                        + " and repcheck is null "
                        + " and qualified is null "
                        + " and disqualified is null "
                        + " and LR='"+ ori+ "' "
                        + " and type= '"+ type+ "'"
                        + " and macro is null ;")[0][0];
        //不合格とした数
        int num5=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where repcheck is not null "
                        + " and LR='"+ ori+ "' "
                        + " and type= '"+ type+ "'"
                        + " and disqualified is not null;")[0][0];
        //マクロ試験対象とした数
        int num6=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where macro is not null"
                        + " and disqualified is null "
                        + " and LR='"+ ori+ "' "
                        + " and type= '"+ type+ "'"
                        + ";")[0][0];
        //その日に、マクロ試験を抜いて合格した数(製品合格)
        int num7=sql.getQueryDataInt(
                "select count(*) from record "
                        + "where qualified is not null "
                        + "and macro is null"
                        + " and disqualified is null "
                        + " and LR='"+ ori+ "' "
                        + " and type= '"+ type+ "'"
                        + ";")[0][0];
        return new int[]{num1,num2,num5,num6,num7};
    }
    
    public void OoutputAllInspectionResult(String[] types,String outputdatapath) throws ParseException{
        
        AttachmentInspection2 ains=new AttachmentInspection2(sql);
        
        WordWriter ww=new WordWriter(outputdatapath);
        
        
        ww.setText("---------------全体結果----------------");
        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            int[] numall = ains.matomeall(type);
            int[] numall_L = ains.matomeall_L(type);
            int[] numall_R = ains.matomeall_R(type);

            ww.RETURN();
            String[] header2 = {
                "接合金物",
                "検査総数", //0
                "未修正数", //1
                "マクロ試験数", //3
                "製品合格数", //4
                "検査対象外", //2
            };
            String[][] content2 = {
                {"L側(" + type + ")", "" + numall_L[0], "" + numall_L[1], "" + numall_L[3], "" + numall_L[4], "" + numall_L[2],},
                {"R側(" + type + ")", "" + numall_R[0], "" + numall_R[1], "" + numall_R[3], "" + numall_R[4], "" + numall_R[2],},
                {"合計(" + type + ")", "" + numall[0], "" + numall[1], "" + numall[3], "" + numall[4], "" + numall[2],}
            };
            ww.setTable(header2, content2, 20, false);
        }
        ww.setText("\t\t※1  (検査総数) = (未修正数) + (マクロ試験数) + (製品合格数)");
        ww.setText("\t\t※2  検査対象外品は工場が受入検査対象から外した製品である");
        ww.PAGE_RETURN();

        ww.setText("---------------金物詳細記録----------------");
        Attachment[][] atlists=ains.getAllStatus();
        
        String[] header0 = {
//            "タイプ",
            "製品番号",
            "初回検査",
            "修正指示",
            "修正確認",
            "検査対象外",
            "合格",
            "マクロ試験",
            "修正部分",
        };
        for (int i = 0; i < atlists.length; i++) {
            Attachment[] atlist = atlists[i];
            int rotno=atlist[0].getRotno();
            String ty=atlist[0].gettype();
            String[][] content0=new String[atlist.length][];
            for (int j = 0; j < atlist.length; j++) {
                Attachment attachment = atlist[j];
                content0[j] = new String[]{
//                    attachment.gettype(),
                    attachment.getAttachmentName(),
                    attachment.getInitDate(),
                    attachment.getRepairOrderDate(),
                    attachment.getRepairCheckDate(),
                    attachment.getDisqualifiedDate(),
                    attachment.getQualifiedDate(),
                    attachment.getMacroDate(),
                    attachment.getRemarks().split(":")[0]
                };
            }
            ww.setText("第"+rotno+ "ロット　【"+ ty+ "】",1);
            ww.setTable(header0, content0, 20);
        }
        
        ww.finish();

    }
    
    public void OutputOneInspectionResult(String[] types,String day, String outputdatapath) throws ParseException{
    
//        AttachmentInspection2 ains=new AttachmentInspection2(sql);
        
//        String rots = ains.CheckRot(day);
        Attachment[] reps = this.getRepairList(day);
        Attachment[] repchecks = this.getRepairCheckedList(day);
        
        Date  date = DateFormat.getDateInstance().parse(day);
        String day0=sd.format(date);
        String[] inspectedRot=sql.getQueryDataString(
                "SELECT rotnum FROM \"PUBLIC\".RECORD"
                + " where init='"+ day0+ "' group by rotnum;")[0];
        
        String day2=day.replace("/", "-");
        WordWriter ww=new WordWriter(outputdatapath+day2+".doc");
        WordWriter ww2=new WordWriter(outputdatapath+day2+"_unRepaired.doc");
        int count=0;
        
        
        
        ww.setText("---------------"+ day+ "結果----------------");
        
        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            ww.RETURN();
            int[] num0 = this.matome(type, day);
            int[] num0L = this.matomeL(type, day);
            int[] num0R = this.matomeR(type, day);
            String[] header00 = {
                "接合金物", //0
                "検査数", //0
                "製品合格", //7
                "無指示合格", //1
                "補修合格", //4
                "補修指示", //2
                "未補修", //3
                "マクロ", //6
                "検査対象外", //5
            };
            String[][] content00 = {
                {"L側("+ type+ ")", "" + num0L[0], "" + num0L[7], "" + num0L[1],
                    "" + num0L[4], "" + num0L[2], "" + num0L[3], "" + num0L[6], "" + num0L[5],},
                {"R側("+ type+ ")", "" + num0R[0], "" + num0R[7], "" + num0R[1],
                    "" + num0R[4], "" + num0R[2], "" + num0R[3], "" + num0R[6], "" + num0R[5],},
                {"合計("+ type+ ")", "" + num0[0], "" + num0[7], "" + num0[1],
                    "" + num0[4], "" + num0[2], "" + num0[3], "" + num0[6], "" + num0[5],}
            };
            ww.setTable(header00, content00, 15, false);
        }
        ww.setText("\t\t※1  (検査数) = (製品合格) + (補修指示)");
        ww.setText("\t\t※2  (製品合格) = (無指示合格) + (補修合格)");
        ww.setText("\t\t※3  (補修指示) = (未補修) + (マクロ)");
        ww.setText("\t\t※4  検査対象外品は工場が受入検査対象から外した製品である");
        
        
        ww.setText("---------------全体結果----------------");
        
        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            int[] numall = this.matomeall(type);
            int[] numall_L = this.matomeall_L(type);
            int[] numall_R = this.matomeall_R(type);
            ww.RETURN();
            String[] header2 = {
                "接合金物",
                "検査総数", //0
                "未修正数", //1
                "マクロ試験数", //3
                "製品合格数", //4
                "検査対象外", //2
            };
            String[][] content2 = {
                {"L側("+ type+ ")", "" + numall_L[0], "" + numall_L[1], "" + numall_L[3], "" + numall_L[4], "" + numall_L[2],},
                {"R側("+ type+ ")", "" + numall_R[0], "" + numall_R[1], "" + numall_R[3], "" + numall_R[4], "" + numall_R[2],},
                {"合計("+ type+ ")", "" + numall[0], "" + numall[1], "" + numall[3], "" + numall[4], "" + numall[2],}
            };
            ww.setTable(header2, content2, 20, false);

        }
        ww.setText("\t\t※1  (検査総数) = (未修正数) + (マクロ試験数) + (製品合格数)");
        ww.setText("\t\t※2  検査対象外品は工場が受入検査対象から外した製品である");
        
        ww.PAGE_RETURN();
        
        ww.setText("---------------検査後のパレット");
        for (int i = 0; i < inspectedRot.length; i++) {
            String l=sql.getQueryDataString("SELECT count(LR) FROM \"PUBLIC\".RECORD "
                    + "where init='"+ day0+ "' and "
                    + "qualified='"+ day0+ "' and "
                    + "LR='L' "
                    + "and rotnum="+ inspectedRot[i]+ ";"
            )[0][0];
            String r=sql.getQueryDataString("SELECT count(LR) FROM \"PUBLIC\".RECORD "
                    + "where init='"+ day0+ "' and "
                    + "qualified='"+ day0+ "' and "
                    + "LR='R' "
                    + "and rotnum="+ inspectedRot[i]+ ";"
            )[0][0];
            
            String img = this.getIMGforRot(Integer.parseInt(inspectedRot[i]));
            
            String[][] cont3={
                {"検査日時："+day+"   /   第"+inspectedRot[i]+"ロット結果"},
                {"合格個数　L："+ l+ "ピース　R:"+ r+ "ピース"},
                {img},
            };
            ww.setTableWithImage(cont3);
            ww.RETURN();
        }
        
        
        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            ww.RETURN();
            int[] num0 = this.matome(type, day);
            int[] num0L = this.matomeL(type, day);
            int[] num0R = this.matomeR(type, day);
            if(num0L[4]==0&&num0R[4]==0){
            String[][] c3 = {
                {"【確認】検査日時：" + day + "   /   製品タイプ("
                    + type+ ")修正後合格品は存在しません!!!"},
                {"合格個数　L：" + "" + num0L[4] + "ピース　R:" + "" + num0R[4] + "ピース"},
                {""},};
            ww.setTableWithImage(c3);
            }else{
            String img = this.getIMGforRot(AttachmentInspection2.PALLET_ID_ORDERED);
            String[][] c3 = {
                {"検査日時：" + day + "   /   ("+ type+ ")修正後合格品"},
                {"合格個数　L：" + "" + num0L[4] + "ピース　R:" + "" + num0R[4] + "ピース"},
                {img},};
            ww.setTableWithImage(c3);
            }
            ww.RETURN();
        }
        
        
        
        ww.PAGE_RETURN();
        ww.setText("---------------修正指示品");
        
        String[] header3 = {
            "接合金物",
            "修正内容",
        };
        
        String[][] content3=new String[reps.length][];
        for (int j = 0; j < reps.length; j++) {
            Attachment rep = reps[j];
            content3[j]=new String[]{rep.getAttachmentName()+"("+ rep.gettype()+ ")",rep.getRemarks()};
        }
        if(reps.length==0){
//            ww.PAGE_RETURN();
        }else{
            ww.setTable(50,header3,content3,40,true);
        }
        count=0;
        for (int i = 0; i < reps.length; i++) {
            Attachment rep = reps[i];
            String rem=rep.getRemarks();
            String rem2=this.getSpecificOrder(rem);
            String img = this.getIMGforOrdered(rep);
            String[][] cont3;
            cont3 = new String[][]{
                {"補修指示日時：" + rep.getRepairOrderDate()
                    + "   /   製品番号：" + rep.getAttachmentName() + "(" + rep.gettype() + ")"
                    + "   /   不整内容：" + rem},
                {"補修指示内容：　" + rem2},
                {img},};

            ww.setTableWithImage(cont3);
            count=this.countPageReturn(count, 4, ww);
        }
        ww.PAGE_RETURN();
        
        ww.setText("---------------修正確認品");
        String[] header4 = {
            "接合金物",
            "修正指示",
            "修正内容",
            "結果",
        };
        String[][] content4=new String[repchecks.length][];
        for (int j = 0; j < repchecks.length; j++) {
            Attachment repcheck = repchecks[j];
            content4[j]=new String[]{
                repcheck.getAttachmentName()+"("+ repcheck.gettype()+ ")",
                repcheck.getRepairOrderDate(),
                repcheck.getRemarks(),
                repcheck.getInspectionResult(),
            };
        }
        if(repchecks.length==0){
//            ww.PAGE_RETURN();
        }else{
            ww.setTable(50,header4,content4,40,true);
        }        
        count=0;
        for (int i = 0; i < repchecks.length; i++) {
            Attachment rep = repchecks[i];
            String rem=rep.getRemarks();
            String rem2=this.getSpecificOrder(rem);
            
            String img0 = this.getIMGforOrdered(rep);
            String img = this.getIMGforRepaired(rep);
            String[][] cont3;
            
            cont3 = new String[][]{
                {"補修指示日時：" + rep.getRepairOrderDate()
                    + "   /   製品番号：" + rep.getAttachmentName() + "(" + rep.gettype() + ")"
                    + "   /   不整内容：" + rem},
                {"補修指示内容：　" + rem2},
                {img0},
                {"補修確認日時：" + rep.getRepairCheckDate() + "   /   補修確認結果：　" + rep.getInspectionResult()},
                {img},};

            
            ww.setTableWithImage(cont3);
            count=this.countPageReturn(count, 2, ww);
        }
        ww.PAGE_RETURN();
        
        ww.setText("---------------要修正金物一覧");
//        ww.RETURN();
        String[] header1 = {
            "金物名前",
            "修正指示",
            "修正部分",
        };
        Attachment[] repsall = this.getRepairList();
        String[][] content1 = new String[repsall.length][];
//        log.info("repsall.length = " + repsall.length);
        for (int j = 0; j < repsall.length; j++) {
            Attachment repall = repsall[j];
            content1[j] = new String[]{
                repall.getAttachmentName()+"("+ repall.gettype()+ ")",
                repall.getRepairOrderDate(),
                repall.getRemarks()
            };
        }
        
        if(repsall.length==0){
//            ww.PAGE_RETURN();
        } else {
            ww.setTable(50, header1, content1, 40, true);

            ww2.setTable(50, header1, content1, 40, true);
        }

        
        count=0;
        for (int i = 0; i < repsall.length; i++) {
            Attachment rep = repsall[i];
            String rem=rep.getRemarks();
            String rem2=this.getSpecificOrder(rem);
            
            String img0 = this.getIMGforOrdered(rep);
            String[][] cont3;
            
            cont3 = new String[][]{
                {"補修指示日時：" + rep.getRepairOrderDate()
                    + "   /   製品番号：" + rep.getAttachmentName() + "(" + rep.gettype() + ")"
                    + "   /   不整内容：" + rem},
                {"補修指示内容：　" + rem2},
                {img0}
            };

            ww2.setTableWithImage(cont3);
            count=this.countPageReturn(count, 4, ww2);
        }
        ww2.finish();

        ww.setText("---------------金物詳細記録----------------");
        Attachment[][] atlists=this.getInspectedRotStatus(day);
        
        String[] header0 = {
//            "タイプ",
            "製品番号",
            "初回検査",
            "修正指示",
            "修正確認",
            "検査対象外",
            "合格",
            "マクロ試験",
            "修正部分",
        };
        for (int i = 0; i < atlists.length; i++) {
            Attachment[] atlist = atlists[i];
            int rotno=atlist[0].getRotno();
            String typ=atlist[0].gettype();
            String[][] content0=new String[atlist.length][];
            for (int j = 0; j < atlist.length; j++) {
                Attachment attachment = atlist[j];
                content0[j] = new String[]{
//                    attachment.gettype(),
                    attachment.getAttachmentName(),
                    attachment.getInitDate(),
                    attachment.getRepairOrderDate(),
                    attachment.getRepairCheckDate(),
                    attachment.getDisqualifiedDate(),
                    attachment.getQualifiedDate(),
                    attachment.getMacroDate(),
                    attachment.getRemarks().split(":")[0]
                };
            }
            ww.setText("第"+ rotno+ "ロット【"+ typ+ "】",1);
            
            ww.setTable(header0, content0, 20);
        }
        
        ww.finish();

    }
    
    private int countPageReturn(int count,int Regurated,WordWriter ww){
        if(count==Regurated-1){
            ww.PAGE_RETURN();
            count=0;
            return count;
        }else{
            ww.RETURN();
            count++;
            return count;
        }
    }
    
    private String getSpecificOrder(String rem){
            String rem2="";
            if(rem.toLowerCase().contains("g")){
                rem2+="溶接余盛を考慮し、";
                if(rem.toLowerCase().contains("gwg")){
                    rem2+="ガウジング→再溶接→グラインダ仕上げにより修正する。";
                }else{
                    rem2+="グラインダ処理とする。";
                }
            }else if(rem.toLowerCase().contains("r")){
                rem2+="精査した結果ボルト孔が予定寸法より小さいので、"
                        + "リーマーにより全ボルト孔を拡張する";
            }
            return rem2;
    }
    
}
