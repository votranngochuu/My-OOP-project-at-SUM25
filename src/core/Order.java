/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author votra
 */
public class Order implements Serializable{
    String orderID; // ma don hang
    String custCode; // cua khach nao
    String feastMenuCode; // ma menu
    int numTable; // so ban
    Date preferedDate; // ngay to chuc

    
    // ctor
    public Order(String orderID, String custCode, String feastMenuCode, int numTable, Date preferedDate) {
        this.orderID = orderID;
        this.custCode = custCode;
        this.feastMenuCode = feastMenuCode;
        this.numTable = numTable;
        this.preferedDate = preferedDate;
    }
    
    // code phuc vu tim kiem
    public Order(String orderID){
        this.orderID = orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getFeastMenuCode() {
        return feastMenuCode;
    }

    public void setFeastMenuCode(String feastMenuCode) {
        this.feastMenuCode = feastMenuCode;
    }

    public int getNumTable() {
        return numTable;
    }

    public void setNumTable(int numTable) {
        this.numTable = numTable;
    }

    public Date getPreferedDate() {
        return preferedDate;
    }

    public void setPreferedDate(Date preferedDate) {
        this.preferedDate = preferedDate;
    }

    @Override
    public boolean equals(Object obj) {
        Order o = (Order)obj;
        return this.getOrderID().equals(o.getOrderID());
    }
    
}
