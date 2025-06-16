/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;
import tool.ConsoleInputter;
import java.io.*;
import java.util.Collections;

/**
 *
 * @author votra
 */
public class CustomerList extends ArrayList<Customer> {

    public static final String custCodePattern = "^[CGKcgk]\\d{4}$";
    public static final String custNamePattern = "^[a-zA-Z ]{2,25}$";
    public static final String phonePattern = "^\\d{9}|\\d{11}$";
    public static final String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public void addNew() {
        String custCode = ConsoleInputter.getStr("New custCode", custCodePattern, "Pattern: CGK + 4 digits");
        String custName = ConsoleInputter.getStr("Customer name", custNamePattern, "Only letters and spaces, 2-25 characters");
        String phone = ConsoleInputter.getStr("Phone number", phonePattern, "Phone must be 9 or 11 digits");
        String email = ConsoleInputter.getStr("Email address", emailPattern, "Invalid email format (example: user@example.com)");
        Customer newCust = new Customer(custCode, custName, phone, email);
        
        this.add(newCust);
        System.out.println("Customer has been added to list");
    }

    public void updateCust() {
        String custCode = ConsoleInputter.getStr("Customer code to update");
        custCode = custCode.toUpperCase();

        int pos = this.indexOf(new Customer(custCode));
        if (pos < 0) {
            System.out.println("This customer does not exist.");
            return;
        }

        Customer customer = this.get(pos);

        // Update name
        String newName = ConsoleInputter.getStr("New name (Enter to keep current: " + customer.getName() + ")");
        if (!newName.trim().isEmpty()) {
            customer.setName(newName);
        }

        // Update phone
        String newPhone = ConsoleInputter.getStr("New phone (Enter to keep current: " + customer.getPhone() + ")");
        if (!newPhone.trim().isEmpty()) {
            customer.setPhone(newPhone);
        }

        // Update email
        String newEmail = ConsoleInputter.getStr("New email (Enter to keep current: " + customer.getEmail() + ")");
        if (!newEmail.trim().isEmpty()) {
            customer.setEmail(newEmail);
        }

        System.out.println("Customer updated successfully!");
    }

    public void printByName() {
        String searchName = ConsoleInputter.getStr("Enter name to search");
        ArrayList<Customer> matches = new ArrayList<>();

        for (Customer c : this) {
            if (c.getName().toLowerCase().contains(searchName.toLowerCase())) {
                matches.add(c);
            }
        }

        if (matches.isEmpty()) {
            System.out.println("No one matches the search criteria!");
        } else {
            Collections.sort(matches, (c1, c2) -> c1.getName().compareTo(c2.getName()));

            System.out.println("Matching Customers: " + searchName);
            System.out.println("------------------------------------------------------------------");
            System.out.println("Code | Customer Name | Phone | Email");
            System.out.println("------------------------------------------------------------------");

            for (Customer c : matches) {
                System.out.printf("%-5s | %-20s | %-12s | %-25s%n",
                        c.getCustCode(), c.getName(), c.getPhone(), c.getEmail());
            }
            System.out.println("------------------------------------------------------------------");
        }
    }

    public void printAll() {
        if (this.isEmpty()) {
            System.out.println("Does not have any customer information.");
            return;
        }

        // Sort by name alphabetically
        Collections.sort(this, (c1, c2) -> c1.getName().compareTo(c2.getName()));

        System.out.println("Customers information:");
        System.out.println("----------------------------------------------------------------");
        System.out.println("Code | Customer Name | Phone | Email");
        System.out.println("----------------------------------------------------------------");

        for (Customer c : this) {
            System.out.printf("%-5s | %-20s | %-12s | %-25s%n",
                    c.getCustCode(), c.getName(), c.getPhone(), c.getEmail());
        }
        System.out.println("----------------------------------------------------------------");
    }

    public void saveFile(String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
            System.out.println("Customer data has been successfully saved to \"" + fileName + "\".");
        } catch (Exception e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    public void readFromFile(String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                return; // File doesn't exist yet
            }

            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            CustomerList temp = (CustomerList) ois.readObject();
            this.clear();
            this.addAll(temp);
            ois.close();
            fis.close();
        } catch (Exception e) {
            // File might not exist or be empty, which is fine for first run
        }
    }
}
