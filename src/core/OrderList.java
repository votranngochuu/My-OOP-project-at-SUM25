/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;
import java.util.Date;
import tool.ConsoleInputter;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.io.Serializable;

/**
 *
 * @author votra
 */
public class OrderList extends ArrayList<Order> implements Serializable {
    // Thêm serialVersionUID cố định
    private static final long serialVersionUID = 1L;
    
    // Đánh dấu transient để không serialize các tham chiếu này
    private transient FeastMenuList fmList; // menu list phải có rồi
    private transient CustomerList cList; // phải có trước ds cust

    // ctor
    public OrderList(FeastMenuList fmList, CustomerList cList) {
        this.fmList = fmList;
        this.cList = cList;
    }

    // Setter methods để cập nhật references sau khi deserialize
    public void setFeastMenuList(FeastMenuList fmList) {
        this.fmList = fmList;
    }

    public void setCustomerList(CustomerList cList) {
        this.cList = cList;
    }

    // đặt tiệc
    public void placeOrder() {
        if (fmList == null || fmList.isEmpty()) {
            System.out.println("Chưa có menu");
            return;
        }
        if (this.cList == null || this.cList.isEmpty()) {
            System.out.println("Chưa có customer");
            return;
        }
        String orderID = ConsoleInputter.dateKeyGen(); //ma don hang
        String custCode; // cua khach nao
        custCode = ConsoleInputter.getStr("Cust code",
                CustomerList.custCodePattern, "Cust code: CGK  + 4 digits");
        custCode = custCode.toUpperCase();
        // tim custCode ton tai
        int pos = this.cList.indexOf(new Customer(custCode));
        if (pos < 0) { // khach hang khong ton tai
            System.out.println("Cust. does not exist. ");
            return;
        }
        // Get the customer object after finding the position
        Customer customer = this.cList.get(pos);
        // so ban 1..1000
        String feastMenuCode; // ma menu tiec

        FeastMenu[] menuArray = fmList.toArray(new FeastMenu[0]);
        FeastMenu selectedFm = (FeastMenu) ConsoleInputter.objMenu(menuArray);
        feastMenuCode = selectedFm.getFeastCode();

        int numTable;  // so ban
        numTable = ConsoleInputter.getInt("Num of tables", 1, 1000);
        Date preferedDate; // ngay to chuc phai sau ngay hom nay
        boolean dateValid = false;
        do {
            preferedDate = ConsoleInputter.getDate("Date (dd-MM-yyyy)", "dd-MM-yyyy");
            dateValid = preferedDate.after(new Date());
            if (!dateValid) {
                System.out.println("Prefered date must be after the current day!");
            }
        } while (!dateValid);

        // Check for duplicate order (same customer, menu, date)
        for (Order existingOrder : this) {
            if (existingOrder.getCustCode().equals(custCode)
                    && existingOrder.getFeastMenuCode().equals(feastMenuCode)
                    && isSameDate(existingOrder.getPreferedDate(), preferedDate)) {
                System.out.println("Dupplicate data !");
                return;
            }
        }
        Order newOrder = new Order(orderID, custCode, feastMenuCode, numTable, preferedDate);
        this.add(newOrder);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        long totalCost = (long) selectedFm.getPrice() * numTable;

        System.out.println("----------------------------------------------------------------");
        System.out.println("Customer order information [Order ID: " + orderID + "]");
        System.out.println("----------------------------------------------------------------");
        System.out.println("Customer code : " + custCode);
        System.out.println("Customer name : " + customer.getName());
        System.out.println("Phone number : " + customer.getPhone());
        System.out.println("Email : " + customer.getEmail());
        System.out.println("----------------------------------------------------------------");
        System.out.println("Code of Set Menu: " + feastMenuCode);
        System.out.println("Set menu name : " + selectedFm.getFeastName());
        System.out.println("Event date : " + dateFormat.format(preferedDate));
        System.out.println("Number of tables: " + numTable);
        System.out.println("Price : " + String.format("%,d", selectedFm.getPrice()) + " Vnd");
        System.out.println("Ingredients:");

        formatAndPrintIngredients(selectedFm.getIngredients());

        System.out.println("----------------------------------------------------------------");
        System.out.println("Total cost : " + String.format("%,d", totalCost) + " Vnd");
        System.out.println("----------------------------------------------------------------");
    }

    private void formatAndPrintIngredients(String ingredients) {
        if (ingredients == null || ingredients.trim().isEmpty()) {
            System.out.println("No ingredients information available.");
            return;
        }

        String[] lines;

        if (ingredients.contains(";")) {
            lines = ingredients.split(";");
        } else if (ingredients.contains("+")) {
            lines = ingredients.split("\\+");
        } else {
            lines = new String[]{ingredients};
        }

        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                if (!line.startsWith("+")) {
                    System.out.println("+ " + line);
                } else {
                    System.out.println(line);
                }
            }
        }
    }

    // update tiệc
    public void updateOrder() {
        if (fmList == null || cList == null) {
            System.out.println("System error: Menu or Customer list not available.");
            return;
        }
        
        // nhập mã orderId
        String orderID = ConsoleInputter.getStr("Enter Order ID to update");
        //Tìm order này
        int pos = this.indexOf(new Order(orderID));
        if (pos < 0) {
            System.out.println("This orderID does not exist");
        } else {
            Order o = this.get(pos);
            // Check if order date has passed
            if (o.getPreferedDate().before(new Date())) {
                System.out.println("Cannot update order whose event date has passed.");
                return;
            }
            // Chọn menu mới
            FeastMenu[] menuArray = fmList.toArray(new FeastMenu[0]);
            FeastMenu newMenu = (FeastMenu) ConsoleInputter.objMenu(menuArray);
            String newFeastMenuCode = newMenu.getFeastCode();
            o.setFeastMenuCode(newFeastMenuCode);

            // Nhập số bàn mới
            int newNumTable = ConsoleInputter.getInt("New number of tables", 1, 1000);
            o.setNumTable(newNumTable);

            // Nhập ngày tổ chức mới
            Date newDate;
            boolean validDate;
            do {
                newDate = ConsoleInputter.getDate("New date (dd-MM-yyyy)", "dd-MM-yyyy");
                validDate = newDate.after(new Date());
                if (!validDate) {
                    System.out.println("Prefered date must be after the current day!");
                }
            } while (!validDate);
            o.setPreferedDate(newDate);

            System.out.println("Order updated successfully.");
        }
    }

    // lưu file nhị phân
    public void saveFile(String fName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fName))) {
            oos.writeObject(this);
            System.out.println("Order list has been saved to file: " + fName);
        } catch (Exception e) {
            System.out.println("Error while saving file: " + e.getMessage());
        }
    }

    // đọc file nhị phân
    public void readFromToFile(String name) {
        File file = new File(name);
        if (!file.exists()) {
            System.out.println("Order file does not exist yet: " + name);
            return;
        }
        
        // Đọc file với xử lý lỗi version mismatch
        try (FileInputStream fis = new FileInputStream(name);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            
            OrderList temp = (OrderList) ois.readObject();
            this.clear();
            this.addAll(temp);
            System.out.println("Successfully loaded " + this.size() + " orders from file: " + name);
            
        } catch (java.io.InvalidClassException e) {
            System.out.println("Warning: Order file format has changed. Unable to load old orders.");
            System.out.println("The old file will be backed up and a new one will be created.");
            
            // Backup file cũ
            File backupFile = new File(name + ".backup");
            if (file.renameTo(backupFile)) {
                System.out.println("Old file backed up as: " + backupFile.getName());
            }
            
        } catch (Exception e) {
            System.out.println("Error loading orders from file: " + e.getMessage());
        }
    }

    private boolean isSameDate(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }

    // Xuất danh sách order
    public void printAll() {
        if (this.isEmpty()) {
            System.out.println("No orders in the system.");
            return;
        }
        
        if (fmList == null || fmList.isEmpty()) {
            System.out.println("Warning: Menu list not available. Cannot display full order details.");
            return;
        }
        
        this.sort((o1, o2) -> o1.getPreferedDate().compareTo(o2.getPreferedDate()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("ID | Event date |Customer ID| Set Menu| Price | Tables | Cost");
        System.out.println("-------------------------------------------------------------------------");

        for (Order o : this) {
            int menuIndex = fmList.indexOf(new FeastMenu(o.getFeastMenuCode()));
            if (menuIndex >= 0) {
                FeastMenu menu = fmList.get(menuIndex);
                long totalCost = (long) menu.getPrice() * o.getNumTable();

                System.out.printf("%s | %s | %s | %s | %,d| %d| %,d%n",
                        o.getOrderID(),
                        dateFormat.format(o.getPreferedDate()),
                        o.getCustCode(),
                        o.getFeastMenuCode(),
                        menu.getPrice(),
                        o.getNumTable(),
                        totalCost);
            } else {
                // Nếu không tìm thấy menu, vẫn hiển thị order với thông tin có sẵn
                System.out.printf("%s | %s | %s | %s | N/A | %d| N/A%n",
                        o.getOrderID(),
                        dateFormat.format(o.getPreferedDate()),
                        o.getCustCode(),
                        o.getFeastMenuCode(),
                        o.getNumTable());
            }
        }
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Total orders: " + this.size());
    }
}