

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DatabaseManager {



    public boolean login(String username, String password) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null  ;
        String query = "SELECT username , password ,  role FROM users WHERE username = ? AND password = ?";
        try  {
            connection = MyJDBC.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println("Вы вошли как " + role);
                switch (role) {
                    case "salesman":
                        Salesman salesman = new Salesman(username , password);
                        salesman.displayOptions();
                        break;
                    case "delivery":
                        Delivery delivery = new Delivery(username , password);
                        delivery.displayOptions();
                        break;
                    case "provider":
                        Provider provider = new Provider(username,  password);
                        provider.displayOptions();
                        break;
                    default:
                        System.out.println("Неизвестная роль: " + role);
                        break;
                }
                return true;
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void listMaterials() {
        try {
            String query = "SELECT * FROM materialsforsales";

            PreparedStatement stmt = MyJDBC.getConnection().prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String dateAdded = resultSet.getString("date_added");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                System.out.printf("%-5d%-15s%-12s%-10.2f%-8d\n", id, name, dateAdded, price, quantity);
            }
        } catch (Exception e) {

        }
    }
    public ResultSet searchMaterialsByName(String materialName) {
        try {
            String query = "SELECT * FROM materialsforsales WHERE name LIKE ?";
            PreparedStatement stmt =  MyJDBC.getConnection().prepareStatement(query);
            stmt.setString(1, "%" + materialName + "%");

            ResultSet resultSet = stmt.executeQuery();
            System.out.printf("%-5s%-15s%-12s%-10s%-8s\n", "ID", "Name", "Date Added", "Price", "Quantity");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String dateAdded = resultSet.getString("date_added");

                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                System.out.printf("%-5d%-15s%-12s%-10.2f%-8d\n", id, name, dateAdded, price, quantity);
            }

            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public ResultSet searchMaterialsByDateAdded(String dateAdded) {
        try {
            String query = "SELECT * FROM materialsforsales WHERE date_added = ?";
            PreparedStatement stmt =  MyJDBC.getConnection().prepareStatement(query);
            stmt.setString(1, dateAdded);

            ResultSet resultSet = stmt.executeQuery();
            System.out.printf("%-5s%-15s%-12s%-10s%-8s\n", "ID", "Name", "Date Added", "Price", "Quantity");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String dateAdded2 = resultSet.getString("date_added");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                System.out.printf("%-5d%-15s%-12s%-10.2f%-8d\n", id, name, dateAdded2, price, quantity);
            }

            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public int countMaterials() {
        try {
            String query = "SELECT COUNT(*) AS material_count FROM materialsforsales";
            PreparedStatement stmt =  MyJDBC.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("material_count");
            } else {
                return 0; // Return 0 if no result is found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate an error occurred
        }
    }
    public boolean saleMaterialById(int materialId, int quantity) {
        try {
            // Check if the material with the given ID exists and has enough quantity
            String checkQuery = "SELECT * FROM materialsforsales WHERE id = ? AND quantity >= ?";
            PreparedStatement checkStmt =  MyJDBC.getConnection().prepareStatement(checkQuery);
            checkStmt.setInt(1, materialId);
            checkStmt.setInt(2, quantity);
            ResultSet checkRs = checkStmt.executeQuery();
            if (checkRs.next()) {
                // Material with the given ID and enough quantity found, proceed with the sale
                String updateQuery = "UPDATE materialsforsales SET quantity = quantity - ? WHERE id = ?";
                PreparedStatement updateStmt =  MyJDBC.getConnection().prepareStatement(updateQuery);
                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, materialId);
                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Material with ID " + materialId + " successfully sold.");
                    return true;
                } else {
                    System.out.println("Failed to update material quantity. Sale operation failed.");
                    return false;
                }
            } else {
                // Material with the given ID or enough quantity not found
                System.out.println("Material with ID " + materialId + " not found or insufficient quantity.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void listSoldMaterials() {
        try {
            String query = "SELECT id ,name ,date_added ,price, quantity  FROM soldmaterials"
                      ;
            PreparedStatement stmt =  MyJDBC.getConnection().prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();
            System.out.printf("%-5s%-15s%-12s%-10s%-8s\n", "ID", "Name", "Date Added", "Price", "Quantity Sold");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String dateAdded = resultSet.getString("date_added");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                System.out.printf("%-5d%-15s%-12s%-10.2f%-8d\n", id, name, dateAdded, price, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void makeOrderForUnavailableMaterials() {
        try (Connection connection = MyJDBC.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter the name of the material you want to order:");
            String materialName = scanner.nextLine().trim();

            System.out.println("Enter the quantity you want to order:");
            int quantity = scanner.nextInt();

            // Check if the material is available in the materialsforsale table
            String checkAvailabilityQuery = "SELECT quantity FROM materialsforsale WHERE name = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkAvailabilityQuery)) {
                checkStmt.setString(1, materialName);
                ResultSet resultSet = checkStmt.executeQuery();
                if (resultSet.next()) {
                    int availableQuantity = resultSet.getInt("quantity");
                    if (availableQuantity == 0) {
                        System.out.println("The stock of this material is empty. Please make an order for supply.");
                    } else if (availableQuantity < quantity) {
                        System.out.println("The requested quantity exceeds the available stock. Available quantity: " + availableQuantity);
                    } else {
                        // Update the materialsforsale table to reduce the available quantity
                        String updateQuery = "UPDATE materialsforsale SET quantity = ? WHERE name = ?";
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, availableQuantity - quantity);
                            updateStmt.setString(2, materialName);
                            updateStmt.executeUpdate();
                        }

                        // Insert the order into the needmaterials table
                        String insertQuery = "INSERT INTO needmaterials (name, quantity) VALUES (?, ?)";
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                            insertStmt.setString(1, materialName);
                            insertStmt.setInt(2, quantity);
                            insertStmt.executeUpdate();
                        }

                        System.out.println("Order placed successfully for " + quantity + " units of " + materialName);
                    }
                } else {
                    System.out.println("The material is not available for sale.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void exit(){
        System.out.println("Program finished, we will be glad to see you again!");
        System.exit(0);
    }
    // Дополнительные методы для работы с данными...
}


