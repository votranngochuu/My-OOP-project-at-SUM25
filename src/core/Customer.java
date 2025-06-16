/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.Serializable;

/**
 *
 * @author votra
 */
public class Customer implements Serializable {

    private String custCode;
    private String name;
    private String phone;
    private String email;

    // constructor - ctors
    public Customer() {
        this.custCode = "";
        this.name = "";
        this.phone = "";
        this.email = "";
    }

    public Customer(String custCode, String name, String phone, String email) {
        this.custCode = custCode;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // constructor cho tac vu tim kiem trong nhom
    public Customer(String custCode) {
        this.custCode = custCode;
    }

    // Co che do 2 cust bang nhau de phuc vu viec tim kiem
    // override hanh vi equals
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Customer) {
            Customer other = (Customer) obj;
            return this.custCode.equals(other.custCode);
        }
        return false;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String custName) {
        this.name = custName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return custCode + " | " + name + " | " + phone + " | " + email;
    }

}
