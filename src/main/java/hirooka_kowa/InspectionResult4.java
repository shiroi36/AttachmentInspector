/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hirooka_kowa;


import core.Attachment;
import core.AttachmentInspection2;
import core.InspectionData;
import core.InspectionRotInfo;
import core.SQL_OPE;
import java.text.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author keita
 */
public class InspectionResult4 {

    public static Logger log=LogManager.getLogger(InspectionResult4.class);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        // TODO code application logic here
        //物件名をローマ字表記で記入してください
        String bukken="hirooka_kowa_s";
        //この物件で製作するロット数を入力してください
        //01をめっき水平金物符号とする        
        //水平金物実数８１６ｐ　ロス２４ｐとする（２５３５ロットをロス分とする）
        String[][] rot_type=new String[][]{
            {"2501","t25","12"},{"2502","t25","12"},
            {"2503","t25","12"},{"2504","t25","12"},
            {"2505","t25","12"},{"2506","t25","12"},
            {"2507","t25","12"},{"2508","t25","12"},
            {"2509","t25","12"},{"2510","t25","12"},
            {"2511","t25","12"},{"2512","t25","12"},
            {"2513","t25","12"},{"2514","t25","12"},
            {"2515","t25","12"},{"2516","t25","12"},
            {"2517","t25","12"},{"2518","t25","12"},
            {"2519","t25","12"},{"2520","t25","12"},
            {"2521","t25","12"},{"2522","t25","12"},
            {"2523","t25","12"},{"2524","t25","12"},
            {"2525","t25","12"},{"2526","t25","12"},
            {"2527","t25","12"},{"2528","t25","12"},
            {"2529","t25","12"},{"2530","t25","12"},
            {"2531","t25","12"},{"2532","t25","12"},
            {"2533","t25","12"},{"2534","t25","12"},
   
            
        };
        
        String dbpath="jdbc:h2:tcp://localhost/./00_"+ bukken;
        SQL_OPE sql=new SQL_OPE(dbpath,"junapp","");
        InspectionResult4 ai=new InspectionResult4();
        AttachmentInspection2 ains=new AttachmentInspection2(sql,rot_type);
        
        //別所で設定した検査入力データをここに設定することで、
        //検査報告書docファイルが出力されます
        
        InspectionData idatas[]={
           
//            ai.InspectionSample(),
            ai.Inspection1125(),
            ai.Inspection1201(),
            ai.Inspection1210(),
            ai.Inspection1216(),
            ai.Inspection1224(),
            ai.Inspection0107(),
            
        };
        
        
        //<editor-fold defaultstate="collapsed" desc="処理プログラム">
        for (int i = 0; i < idatas.length; i++) {
            log.info("Input Inspection at DAY:"+idatas[i].getDay());
            ains.Inspection(idatas[i]);
        }
        
        if (idatas.length >= 1) {
            log.info("Creating Inspection Report at DAY:" + idatas[idatas.length - 1].getDay());
            ains.OutputOneInspectionResult(
                    new String[]{"t25","t25u","t25d","t25yu","t25yd"},
                    idatas[idatas.length - 1].getDay(),
                    "C:/temp/");
            log.info("Creating Cumulative Inspection Report at DAY:"+idatas[idatas.length-1].getDay());

        }
        
        
        //すべての金物生産情報docファイルを出力します。
        ains.OoutputAllInspectionResult(
                new String[]{"t25",}, 
                "C:/temp/allresult.doc"
        );
//</editor-fold>
        
    }
     //下記のプログラムを参考に、入力を行ってください。
    public InspectionData InspectionSample() throws ParseException{
        
        //ある受入検査の日付
        String day="2020/11/24";
        
        //ある日の受入検査で検査した金物ロット番号
        InspectionRotInfo[] newinspectionrot={
            new InspectionRotInfo(2514,new String[]{"P1030166.JPG","P1030167.JPG"}),
            new InspectionRotInfo(2515,new String[]{"P1030168.JPG","P1030169.JPG"}),

            new InspectionRotInfo(
                    AttachmentInspection2.PALLET_ID_ORDERED,
                    new String[]{"P1030177.JPG",""}),//ここには修正後のパレット写真を
        };
        
        String path="C:\\temp\\アイ・テック受入検査\\2020年11月24日t25量産検査⑩\\写真\\";
//        String path="";
        
        //修正指示を出した金物        
        Attachment[] repair={
            new Attachment(2510,16,Attachment.R,"",new String[]{"P1030171.JPG","P1030172.JPG",""}),
            new Attachment(2514,2,Attachment.L,"寸法不整:リーマーR⁺0.3",new String[]{"P1030164.JPG","P1030165.JPG",""}),
          
            
        };
        
        //以前の修正指示を確認し合格となった金物
        Attachment[] repairCheckedQualified={
            new Attachment(2506,6,Attachment.L,"",new String[]{"P1030127.JPG"}),
            new Attachment(2509,8,Attachment.L,"",new String[]{"P1030129.JPG"}),
            new Attachment(2509,4,Attachment.L,"",new String[]{"P1030131.JPG"}),
            new Attachment(2512,6,Attachment.L,"",new String[]{"P1030133.JPG"}),
       
          
      
        };
        
        //以前の修正指示を確認し不合格となった金物
        Attachment[] repairCheckedDisqualified={
//            new Attachment(2501,24,Attachment.L,"検査対象外",new String[]{"","",""}),
//            new Attachment(2501,24,Attachment.R,"検査対象外",new String[]{"","",""}),
         
        };
        //合格としマクロ検査を行ったもの
        Attachment[] macro={
            new Attachment(2510,16,Attachment.R,"",new String[]{"P1030171.JPG","P1030172.JPG",""}),
//            new Attachment(2501,22,Attachment.R,"",new String[]{"","",""}),
        };
        
        //ここから下は、いじらないでください
        return new InspectionData(day, 
                newinspectionrot, 
                repair, 
                repairCheckedQualified, 
                repairCheckedDisqualified, 
                macro,path);
   
    
    }
  
    public InspectionData Inspection1125() throws ParseException{
        
        //ある受入検査の日付
        String day="2021/11/25";
        
        //ある日の受入検査で検査した金物ロット番号
        InspectionRotInfo[] newinspectionrot={
            new InspectionRotInfo(2501,new String[]{"DSCF1091.jpg"}),

//            new InspectionRotInfo(
//                    AttachmentInspection2.PALLET_ID_ORDERED,
//                    new String[]{"DSCF1090.jpg"}),//ここには修正後のパレット写真を
        };
        
        String path="C:\\temp\\00_広岡計画\\2021年11月25日初品検査\\photo\\";
        
        //修正指示を出した金物        
        Attachment[] repair={
            new Attachment(2501,10,Attachment.R,"ビード不足：GWG",new String[]{"DSCF1081.jpg","DSCF1082.jpg",""}),
            new Attachment(2501,11,Attachment.R,"ビード不足：GWG",new String[]{"DSCF1083.jpg","DSCF1084.jpg",""}),
            new Attachment(2501,5,Attachment.R,"ビード不足：GWG",new String[]{"DSCF1087.jpg","DSCF1088.jpg",""}),
            new Attachment(2501,6,Attachment.R,"ビード不足：GWG",new String[]{"DSCF1085.jpg","DSCF1086.jpg",""}),
        };
        
        //以前の修正指示を確認し合格となった金物
        Attachment[] repairCheckedQualified={
//            new Attachment(1903,02,Attachment.L,"",new String[]{"P1030390.JPG","P1030391.JPG"}),
//            new Attachment(1903,04,Attachment.R,"",new String[]{"P1030392.JPG","P1030393.JPG"}),
//            
//            
       
          
      
        };
        
        //以前の修正指示を確認し不合格となった金物
        Attachment[] repairCheckedDisqualified={
//            new Attachment(2501,24,Attachment.L,"検査対象外",new String[]{"","",""}),
//            new Attachment(2501,24,Attachment.R,"検査対象外",new String[]{"","",""}),
         
        };
        //合格としマクロ検査を行ったもの
        Attachment[] macro={
//            new Attachment(2510,16,Attachment.R,"",new String[]{"P1030171.JPG","P1030172.JPG",""}),
//            new Attachment(2501,22,Attachment.R,"",new String[]{"","",""}),
        };
        
        //ここから下は、いじらないでください
        return new InspectionData(day, 
                newinspectionrot, 
                repair, 
                repairCheckedQualified, 
                repairCheckedDisqualified, 
                macro,path);
   
    
    }
  
  
    public InspectionData Inspection1201() throws ParseException{
        
        //ある受入検査の日付
        String day="2021/12/1";
        
        //ある日の受入検査で検査した金物ロット番号
        InspectionRotInfo[] newinspectionrot={
            new InspectionRotInfo(2502,new String[]{
                "DSCF1188.jpg",
                "DSCF1189.jpg",
            }),
            new InspectionRotInfo(2503,new String[]{"DSCF1190.jpg"
                    ,"DSCF1191.jpg"}),
            new InspectionRotInfo(2504,new String[]{"DSCF1192.jpg"}),
            new InspectionRotInfo(2505,new String[]{
                "DSCF1193.jpg",
                "DSCF1194.jpg",
            }),

            new InspectionRotInfo(
                    AttachmentInspection2.PALLET_ID_ORDERED,
                    new String[]{"DSCF1195.jpg"}),//ここには修正後のパレット写真を
        };
        
        String path="C:\\temp\\00_広岡計画\\2021年12月1日検査\\photo\\";
        
        //修正指示を出した金物        
        Attachment[] repair={
            new Attachment(2505,9,Attachment.R,"母材キズ：G",
                    new String[]{"DSCF1183.jpg","DSCF1184.jpg","DSCF1197.jpg"}),
            new Attachment(2503,8,Attachment.R,"ボルト孔スパッタ：G",
                    new String[]{"DSCF1185.jpg","DSCF1186.jpg"}),
        };
        
        //以前の修正指示を確認し合格となった金物
        Attachment[] repairCheckedQualified={
            new Attachment(2501,10,Attachment.R,"",new String[]{"DSCF1175.jpg","DSCF1176.jpg",}),
            new Attachment(2501,6,Attachment.R,"",new String[]{"DSCF1177.jpg","DSCF1178.jpg",}),
            new Attachment(2501,5,Attachment.R,"",new String[]{"DSCF1179.jpg","DSCF1180.jpg",}),
            new Attachment(2501,11,Attachment.R,"",new String[]{"DSCF1181.jpg","DSCF1182.jpg",}),
            
        };
        
        //以前の修正指示を確認し不合格となった金物
        Attachment[] repairCheckedDisqualified={
//            new Attachment(2501,24,Attachment.L,"検査対象外",new String[]{"","",""}),
//            new Attachment(2501,24,Attachment.R,"検査対象外",new String[]{"","",""}),
         
        };
        //合格としマクロ検査を行ったもの
        Attachment[] macro={
//            new Attachment(2510,16,Attachment.R,"",new String[]{"P1030171.JPG","P1030172.JPG",""}),
//            new Attachment(2501,22,Attachment.R,"",new String[]{"","",""}),
        };
        
        //ここから下は、いじらないでください
        return new InspectionData(day, 
                newinspectionrot, 
                repair, 
                repairCheckedQualified, 
                repairCheckedDisqualified, 
                macro,path);
   
    
    }
  
    public InspectionData Inspection1210() throws ParseException{
        
        //ある受入検査の日付
        String day="2021/12/10";
        
        //ある日の受入検査で検査した金物ロット番号
        InspectionRotInfo[] newinspectionrot={
            new InspectionRotInfo(2506,new String[]{
                "DSCF1236.jpg",
                "DSCF1237.jpg",
            }),
            new InspectionRotInfo(2507,new String[]{
                "DSCF1237.jpg",
                "DSCF1238.jpg",
            }),
            new InspectionRotInfo(2508,new String[]{
                "DSCF1240.jpg",
                "DSCF1239.jpg",
            }),
            new InspectionRotInfo(2509,new String[]{
                "DSCF1240.jpg",
                "DSCF1241.jpg",
            }),
            new InspectionRotInfo(2510,new String[]{
                "DSCF1242.jpg",
                "DSCF1243.jpg",
            }),
            new InspectionRotInfo(2511,new String[]{
                "DSCF1243.jpg",
                "DSCF1244.jpg",
            }),
            new InspectionRotInfo(2512,new String[]{
                "DSCF1244.jpg",
                "DSCF1245.jpg",
            }),
            new InspectionRotInfo(2513,new String[]{
                "DSCF1246.jpg",
                "DSCF1247.jpg",
            }),
            new InspectionRotInfo(2514,new String[]{
                "DSCF1247.jpg",
                "DSCF1248.jpg",
            }),
            new InspectionRotInfo(2515,new String[]{
                "DSCF1249.jpg",
                "DSCF1248.jpg",
            }),
            new InspectionRotInfo(2516,new String[]{
                "DSCF1250.jpg",
                "DSCF1251.jpg",
            }),
            new InspectionRotInfo(2517,new String[]{
                "DSCF1251.jpg",
                "DSCF1252.jpg",
            }),
            new InspectionRotInfo(2518,new String[]{
                "DSCF1252.jpg",
                "DSCF1253.jpg",
            }),
            new InspectionRotInfo(2519,new String[]{
                "DSCF1254.jpg",
                "DSCF1255.jpg",
            }),

            new InspectionRotInfo(
                    AttachmentInspection2.PALLET_ID_ORDERED,
                    new String[]{"DSCF1256.jpg"}),//ここには修正後のパレット写真を
        };
        
        String path="C:\\temp\\00_広岡計画\\2021年12月10日検査\\photo\\";
        
        //修正指示を出した金物        
        Attachment[] repair={
            new Attachment(2515,6,Attachment.L,"垂直板そり",
                    new String[]{"DSCF1219.jpg",
                        "DSCF1218.jpg"}),
            new Attachment(2511,10,Attachment.L,"垂直板そり",
                    new String[]{"DSCF1217.jpg",
                        "DSCF1216.jpg"}),
            new Attachment(2517,5,Attachment.L,"垂直板そり",
                    new String[]{"DSCF1215.jpg",
                        "DSCF1214.jpg"}),
            new Attachment(2515,9,Attachment.L,"垂直板そり",
                    new String[]{"DSCF1212.jpg",
                        "DSCF1211.jpg","DSCF1213.jpg",}),
            new Attachment(2515,11,Attachment.L,"垂直板そり",
                    new String[]{"DSCF1210.jpg",}),
            new Attachment(2516,1,Attachment.L,"垂直板そり",
                    new String[]{"DSCF1208.jpg",
                        "DSCF1209.jpg"}),
        };
        
        //以前の修正指示を確認し合格となった金物
        Attachment[] repairCheckedQualified={
            new Attachment(2503,8,Attachment.R,"",
                    new String[]{"DSCF1220.jpg",
                        "DSCF1221.jpg",
                        "DSCF1223.jpg",
                    }),
            new Attachment(2505,9,Attachment.R,"",
                    new String[]{"DSCF1226.jpg",
                        "DSCF1227.jpg",
                    }),
            
            
        };
        
        //以前の修正指示を確認し不合格となった金物
        Attachment[] repairCheckedDisqualified={
//            new Attachment(2501,24,Attachment.L,"検査対象外",new String[]{"","",""}),
//            new Attachment(2501,24,Attachment.R,"検査対象外",new String[]{"","",""}),
         
        };
        //合格としマクロ検査を行ったもの
        Attachment[] macro={
//            new Attachment(2510,16,Attachment.R,"",new String[]{"P1030171.JPG","P1030172.JPG",""}),
//            new Attachment(2501,22,Attachment.R,"",new String[]{"","",""}),
        };
        
        //ここから下は、いじらないでください
        return new InspectionData(day, 
                newinspectionrot, 
                repair, 
                repairCheckedQualified, 
                repairCheckedDisqualified, 
                macro,path);
   
    
    }
  
    public InspectionData Inspection1216() throws ParseException{
        
        //ある受入検査の日付
        String day="2021/12/16";
        
        //ある日の受入検査で検査した金物ロット番号
        InspectionRotInfo[] newinspectionrot={
            new InspectionRotInfo(2520,new String[]{
                "DSCF0226.jpg",
                "DSCF0227.jpg",
            }),
            new InspectionRotInfo(2521,new String[]{
                "DSCF0227.jpg",
                "DSCF0228.jpg",
            }),
            new InspectionRotInfo(2522,new String[]{
                "DSCF0229.jpg",
                "DSCF0230.jpg",
            }),
            new InspectionRotInfo(2523,new String[]{
                "DSCF0230.jpg",
                "DSCF0231.jpg",
            }),
            new InspectionRotInfo(2524,new String[]{
                "DSCF0231.jpg",
                "DSCF0232.jpg",
            }),
            new InspectionRotInfo(2525,new String[]{
                "DSCF0233.jpg",
                "DSCF0234.jpg",
            }),
            new InspectionRotInfo(2526,new String[]{
                "DSCF0234.jpg",
                "DSCF0235.jpg",
            }),
            new InspectionRotInfo(2527,new String[]{
                "DSCF0235.jpg",
                "DSCF0236.jpg",
            }),
            new InspectionRotInfo(2528,new String[]{
                "DSCF0237.jpg",
                "DSCF0238.jpg",
            }),
            new InspectionRotInfo(2529,new String[]{
                "DSCF0238.jpg",
                "DSCF0239.jpg",
            }),
            

            new InspectionRotInfo(
                    AttachmentInspection2.PALLET_ID_ORDERED,
                    new String[]{"DSCF0240.jpg"}),//ここには修正後のパレット写真を
        };
        
        String path="C:\\temp\\00_広岡計画\\2021年12月16日検査\\photo\\";
        
        //修正指示を出した金物        
        Attachment[] repair={
            new Attachment(2520,6,Attachment.L,"垂直板そり",
                    new String[]{
                        "DSCF0219.jpg",
                        "DSCF0220.jpg",
                        "DSCF0221.jpg",
                    }),
            new Attachment(2520,4,Attachment.R,"垂直板そり",
                    new String[]{"DSCF0217.jpg",
                        "DSCF0218.jpg"}),
        };
        
        //以前の修正指示を確認し合格となった金物
        Attachment[] repairCheckedQualified={
            new Attachment(2511,10,Attachment.L,"",
                    new String[]{
                        "DSCF0203.jpg",
                        "DSCF0204.jpg",
                    }),
            new Attachment(2515,6,Attachment.L,"",
                    new String[]{
                        "DSCF0205.jpg",
                        "DSCF0206.jpg",
                    }),
            new Attachment(2515,9,Attachment.L,"",
                    new String[]{
                        "DSCF0207.jpg",
                        "DSCF0208.jpg",
                    }),
            new Attachment(2517,5,Attachment.L,"",
                    new String[]{
                        "DSCF0209.jpg",
                    }),
            new Attachment(2515,11,Attachment.L,"",
                    new String[]{
                        "DSCF0213.jpg",
                        "DSCF0214.jpg",
                    }),
            new Attachment(2516,1,Attachment.L,"",
                    new String[]{
                        "DSCF0215.jpg",
                        "DSCF0216.jpg",
                    }),
            
            
        };
        
        //以前の修正指示を確認し不合格となった金物
        Attachment[] repairCheckedDisqualified={
//            new Attachment(2501,24,Attachment.L,"検査対象外",new String[]{"","",""}),
//            new Attachment(2501,24,Attachment.R,"検査対象外",new String[]{"","",""}),
         
        };
        //合格としマクロ検査を行ったもの
        Attachment[] macro={
//            new Attachment(2510,16,Attachment.R,"",new String[]{"P1030171.JPG","P1030172.JPG",""}),
//            new Attachment(2501,22,Attachment.R,"",new String[]{"","",""}),
        };
        
        //ここから下は、いじらないでください
        return new InspectionData(day, 
                newinspectionrot, 
                repair, 
                repairCheckedQualified, 
                repairCheckedDisqualified, 
                macro,path);
   
    
    }
  
    public InspectionData Inspection1224() throws ParseException{
        
        //ある受入検査の日付
        String day="2021/12/24";
        
        //ある日の受入検査で検査した金物ロット番号
        InspectionRotInfo[] newinspectionrot={
            new InspectionRotInfo(2530,new String[]{
                "DSCF0828.jpg",
                "DSCF0829.jpg",
            }),
            new InspectionRotInfo(2531,new String[]{
                "DSCF0829.jpg",
                "DSCF0831.jpg",
            }),
            new InspectionRotInfo(2532,new String[]{
                "DSCF0831.jpg",
                "DSCF0832.jpg",
            }),
            new InspectionRotInfo(2533,new String[]{
                "DSCF0833.jpg",
                "DSCF0834.jpg",
            }),
            new InspectionRotInfo(2534,new String[]{
                "DSCF0834.jpg",
                "DSCF0835.jpg",
            }),
            

            new InspectionRotInfo(
                    AttachmentInspection2.PALLET_ID_ORDERED,
                    new String[]{"DSCF0836.jpg"}),//ここには修正後のパレット写真を
        };
        
        String path="C:\\temp\\00_広岡計画\\2021年12月24日検査\\photo\\";
        
        //修正指示を出した金物        
        Attachment[] repair={
            new Attachment(2534,12,Attachment.L,"寸法不整で再製作",
                    new String[]{
                        "DSCF0827.jpg",
                    }),
        };
        
        //以前の修正指示を確認し合格となった金物
        Attachment[] repairCheckedQualified={
            new Attachment(2520,6,Attachment.L,"",
                    new String[]{
                        "DSCF0821.jpg",
                        "DSCF0822.jpg",
                    }),
            new Attachment(2520,4,Attachment.R,"",
                    new String[]{
                        "DSCF0824.jpg",
                        "DSCF0825.jpg",
                    }),
            
        };
        
        //以前の修正指示を確認し不合格となった金物
        Attachment[] repairCheckedDisqualified={
//            new Attachment(2501,24,Attachment.L,"検査対象外",new String[]{"","",""}),
//            new Attachment(2501,24,Attachment.R,"検査対象外",new String[]{"","",""}),
         
        };
        //合格としマクロ検査を行ったもの
        Attachment[] macro={
//            new Attachment(2510,16,Attachment.R,"",new String[]{"P1030171.JPG","P1030172.JPG",""}),
//            new Attachment(2501,22,Attachment.R,"",new String[]{"","",""}),
        };
        
        //ここから下は、いじらないでください
        return new InspectionData(day, 
                newinspectionrot, 
                repair, 
                repairCheckedQualified, 
                repairCheckedDisqualified, 
                macro,path);
   
    
    }
  
    public InspectionData Inspection0107() throws ParseException{
        
        //ある受入検査の日付
        String day="2021/01/07";
        
        //ある日の受入検査で検査した金物ロット番号
        InspectionRotInfo[] newinspectionrot={
            
        };
        
        String path="C:\\temp\\00_広岡計画\\2022年1月7日検査\\photo\\";
        
        //修正指示を出した金物        
        Attachment[] repair={
            
        };
        
        //以前の修正指示を確認し合格となった金物
        Attachment[] repairCheckedQualified={
            new Attachment(2534,12,Attachment.L,"",
                    new String[]{
                        "DSCF0905.jpg",
                    }),
            
        };
        
        //以前の修正指示を確認し不合格となった金物
        Attachment[] repairCheckedDisqualified={
//            new Attachment(2501,24,Attachment.L,"検査対象外",new String[]{"","",""}),
//            new Attachment(2501,24,Attachment.R,"検査対象外",new String[]{"","",""}),
         
        };
        //合格としマクロ検査を行ったもの
        Attachment[] macro={
//            new Attachment(2510,16,Attachment.R,"",new String[]{"P1030171.JPG","P1030172.JPG",""}),
//            new Attachment(2501,22,Attachment.R,"",new String[]{"","",""}),
        };
        
        //ここから下は、いじらないでください
        return new InspectionData(day, 
                newinspectionrot, 
                repair, 
                repairCheckedQualified, 
                repairCheckedDisqualified, 
                macro,path);
   
    
    }
  
}
