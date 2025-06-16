/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author votra
 */
public class FeastMenu {

    String feastCode;
    String feastName;
    int price;
    String ingredients;
    // ctors 4 tham số để tạo mới 1 feast menu

    public FeastMenu(String feastCode, String feastName, int price, String ingredients) {
        this.feastCode = feastCode;
        this.feastName = feastName;
        this.price = price;
        this.ingredients = ingredients;
    }

    // ctor 1 tham so (feastCode) de tim menu
    public FeastMenu(String feastCode) {
        this.feastCode = feastCode;
    }

    // override hanh vi equals de tim feast menu
    @Override
    public boolean equals(Object obj) {
        FeastMenu fm = (FeastMenu) obj;
        return this.feastCode.equals(fm.feastCode);
    }

    @Override
    public String toString() {
        return feastCode + " - " + feastName + " (" + String.format("%,d", price) + " Vnd)";
    }

    public String getFeastCode() {
        return feastCode;
    }

    public void setFeastCode(String feastCode) {
        this.feastCode = feastCode;
    }

    public String getFeastName() {
        return feastName;
    }

    public void setFeastName(String feastName) {
        this.feastName = feastName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

}
