/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.BufferedReader;// do theo dong
import java.util.ArrayList;
import java.io.FileReader; //read 1 ky tu
import java.util.Collections;

/**
 *
 * @author votra
 */
public class FeastMenuList extends ArrayList<FeastMenu> {

    // do file ra cac feast menu
    public void readFile(String fName) {
        // mở file văn bản để đọc
        try {
            FileReader fr = new FileReader(fName);
            BufferedReader bf = new BufferedReader(fr);
            String line; // dòng chữ
            bf.readLine(); // bỏ dòng giải thích trong file
            //PW001, Wedding party 01, 3750000,"+ Khai vị : súp bong bóng cá, Mem rán; 
            while ((line = bf.readLine()) != null) {
                String[] parts = splitCSVLine(line);
                if (parts.length >= 4) { // 1 dòng hợp lệ có ít nhất 4 chuỗi con
                    String feastCode = parts[0].trim();
                    String feastName = parts[1].trim();
                    int price = Integer.parseInt(parts[2].trim());

                    // Handle ingredients (may contain commas and be quoted)
                    StringBuilder ingredients = new StringBuilder();
                    for (int i = 3; i < parts.length; i++) {
                        if (i > 3) {
                            ingredients.append(",");
                        }
                        ingredients.append(parts[i]);
                    }
                    String ingredientStr = ingredients.toString().trim();

                    // Remove quotes if present
                    if (ingredientStr.startsWith("\"") && ingredientStr.endsWith("\"")) {
                        ingredientStr = ingredientStr.substring(1, ingredientStr.length() - 1);
                    }

                    FeastMenu fm = new FeastMenu(feastCode, feastName, price, ingredientStr);
                    this.add(fm);
                }
            }
            bf.close();
            fr.close(); // Đóng file
        } catch (Exception e) {
            System.out.println("Cannot read data from feastMenu.csv. Please check it.");
        }
    }

    private String[] splitCSVLine(String line) {
        java.util.List<String> result = new java.util.ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());

        return result.toArray(new String[0]);
    }

    // liệt kê toàn bộ feast menu
    public void printAll() {
        if (this.size() == 0) {
            System.out.println("Cannot read data from feastMenu.csv. Please check it.");
            return;
        }
        // Sort by price in ascending order as required
        Collections.sort(this, (fm1, fm2) -> Integer.compare(fm1.getPrice(), fm2.getPrice()));

        System.out.println("---------------------------------------------------");
        System.out.println("List of Set Menus for ordering party:");
        System.out.println("---------------------------------------------------");

        for (FeastMenu fm : this) {
            System.out.println("Code        :" + fm.getFeastCode());
            System.out.println("Name        :" + fm.getFeastName());
            System.out.println("Price       : " + String.format("%,d", fm.getPrice()) + " Vnd");
            System.out.println("Ingredients:");

            formatIngredients(fm.getIngredients());

            System.out.println("---------------------------------------------------");
        }
    }

    private void formatIngredients(String ingredients) {
        if (ingredients == null || ingredients.trim().isEmpty()) {
            System.out.println("No ingredients information available.");
            return;
        }

        String[] courses = ingredients.split("#");

        for (String course : courses) {
            course = course.trim();
            if (!course.isEmpty()) {
                if (!course.startsWith("+")) {
                    System.out.println("+ " + course);
                } else {
                    System.out.println(course);
                }
            }
        }
    }

}
