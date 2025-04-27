/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class User {

    private int id;
    private String lname, fname, midI, add, eAdd, contactNum, status;
    private Date hiredDate;

    public User(int id, String lname, String fname, String midI, String add, String eAdd, String contactNum, Date hiredDate, String status) {
        this.id = id;
        this.lname = lname;
        this.fname = fname;
        this.midI = midI;
        this.add = add;
        this.eAdd = eAdd;
        this.contactNum = contactNum;
        this.hiredDate = hiredDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getLname() {
        return lname;
    }

    public String getFname() {
        return fname;
    }

    public String getMidI() {
        return midI;
    }

    public String getAdd() {
        return add;
    }

    public String getEAdd() {
        return eAdd;
    }

    public String getContactNum() {
        return contactNum;
    }

    public Date getHiredDate() {
        return hiredDate;
    }

    public String getStatus() {
        return status;
    }
    public String getfullName(){
        String fullname = lname + ", " + fname + " " + midI + ".";
        
        return fullname;
    }
}
