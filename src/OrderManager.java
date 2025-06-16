/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author votra
 */
import core.*;
import tool.ConsoleInputter;

public class OrderManager {

    public static void main(String[] args) {
        String fmFname = "src/data/FeastMenu.csv";
        String custFname = "src/data/Customers.dat";
        String orderFname = "src/data/feast_order_service.dat";

        FeastMenuList fmList = new FeastMenuList(); // danh sach mon an
        CustomerList cList = new CustomerList(); // danh sach khach hang
        OrderList oList;  //danh sach order
        
        // chuan bi du lieu ban dau ve menu list
        fmList.readFile(fmFname);  // doc file menu CSV
        
        // chuan bi danh sach khach hang
        // BUG FIX: Phải đọc từ file customer, không phải file menu!
        cList.readFromFile(custFname);  // CHANGED: từ fmFname sang custFname

        oList = new OrderList(fmList, cList);
        oList.readFromToFile(orderFname);
        
        // Cập nhật lại references sau khi đọc file
        oList.setFeastMenuList(fmList);
        oList.setCustomerList(cList);

        // code quan ly chuc nang cua chuong trinh
        int choice;
        boolean changed = false;

        do {
            try {
                choice = ConsoleInputter.intMenu(
                        "Register customer",
                        "Update customer information",
                        "Search for customer information by name",
                        "Display feast menus",
                        "Place a feast order",
                        "Update order information",
                        "Save data to file",
                        "Display Customer or Order lists",
                        "Others-Quit"
                );
                switch (choice) {
                    case 1:
                        cList.addNew();
                        changed = true;
                        break;
                    case 2:
                        cList.updateCust();
                        changed = true;
                        break;
                    case 3:
                        cList.printByName();
                        break;
                    case 4:
                        fmList.printAll();
                        break;
                    case 5:
                        oList.placeOrder();
                        changed = true;
                        break;
                    case 6:
                        oList.updateOrder();
                        changed = true;
                        break;
                    case 7:
                        if (changed) {
                            if (!cList.isEmpty()) {
                                cList.saveFile(custFname);
                            }
                            if (!oList.isEmpty()) {
                                oList.saveFile(orderFname);
                            }
                            changed = false;
                            System.out.println("Data saved successfully!");
                        } else {
                            System.out.println("No changes to save.");
                        }
                        break;
                    case 8:
                        int displayChoice = ConsoleInputter.intMenu("Display Customers", "Display Orders");
                        if (displayChoice == 1) {
                            cList.printAll();
                        } else {
                            oList.printAll();
                        }
                        break;
                    case 9:
                        if (changed) {
                            boolean resp = ConsoleInputter.getBoolean("Data changed. Save or not?");
                            if (resp) {
                                if (!cList.isEmpty()) {
                                    cList.saveFile(custFname);
                                }
                                if (!oList.isEmpty()) {
                                    oList.saveFile(orderFname);
                                }
                            }
                        }
                        System.out.println("Good luck.");
                        break;
                    default:
                        System.out.println("Invalid choice! Please select 1-9.");
                        choice = 0;
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 9.");
                choice = 0;
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try again.");
                choice = 0;
            }
        } while (choice != 9);

    }
}