/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author user
 */
public class InspectionRotInfo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    private final int rotno;
    private final String[] pic;
    
    public InspectionRotInfo(int rotno,String[] pic){
        this.rotno=rotno;
        this.pic=pic;
    }
    public int getRotno(){return rotno;}
    public String[] getRotPicData(){return pic;}
    public boolean compare(int crotno){
            if(rotno==crotno)return true;
            else return false;
    };
    
}
