/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;


public class Sizes {
    int sizeId;
    String sizeName;
    String sizeOz;
    double sizeRate;
    
    public Sizes(int sizeId, String sizeName,String sizeOz,double sizeRate){
        this.sizeId=sizeId;
        this.sizeName=sizeName;
        this.sizeOz=sizeOz;
        this.sizeRate=sizeRate;
    }
    
    public int getSizeId(){
        return sizeId;
    }
    public String getSizeName(){
        return sizeName;
    }
    public String getSizeOz(){
        return sizeOz;
    }
    public double getSizeRate(){
        return sizeRate;
    }
    
    public void setSizeId(int id){
        this.sizeId=id;
    }
    
    public void setSizeName(String name){
        this.sizeName=name;
    }
    
    public void setSizeOz(String oz){
        this.sizeOz=oz;
    }
    
    public void setSizeRate(double rate){
         this.sizeRate=rate;
    }
}
