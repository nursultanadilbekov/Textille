

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
    public static void makeOrderForUnavailableMaterials(String materialName, int quantity) {
        try (Connection connection = MyJDBC.getConnection();) {
            // Check if the material is available in the missedmaterials table
            String checkAvailabilityQuery = "SELECT quantity FROM missedmaterials WHERE name = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkAvailabilityQuery)) {
                checkStmt.setString(1, materialName);
                ResultSet resultSet = checkStmt.executeQuery();
                if (resultSet.next()) {
                    // Material is available in missedmaterials table, insert into needmaterials table
                    int availableQuantity = resultSet.getInt("quantity");
                    String insertQuery = "INSERT INTO needmaterials (name, quantity) VALUES (?, ?)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, materialName);
                        insertStmt.setInt(2, quantity);
                        insertStmt.executeUpdate();
                        System.out.println("Order placed successfully for " + quantity + " units of " + materialName);
                    }
                } else {
                    System.out.println("The material is not available for order.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void displayMissedMaterials() {
        try (Connection connection = MyJDBC.getConnection();) {
            // SQL query to select all rows from missedmaterials table
            String selectQuery = "SELECT * FROM missedmaterials";

            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            ResultSet resultSet = selectStmt.executeQuery();

            // Print the header
            System.out.println("ID | Name | Date Added | Price | Quantity");

            // Iterate over the result set and print each row
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String dateAdded = resultSet.getString("date_added");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");

                // Print the row
                System.out.println(id + " | " + name + " | " + dateAdded + " | " + price + " | " + quantity);
            }

            // Close resources
            resultSet.close();
            selectStmt.close();

            System.out.println("Missed materials successfully displayed.");
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
    public boolean saleMaterialById(int materialId, int quantity) {
        try {
            // Insert the sold material into the soldMaterials table
            String insertQuery = "INSERT INTO soldmaterials (id, name, date_added, price, quantity) " +
                    "SELECT id, name, date_added, price, ? FROM materialsforsales WHERE id = ?";
            PreparedStatement insertStmt = MyJDBC.getConnection().prepareStatement(insertQuery);
            insertStmt.setInt(1, quantity);
            insertStmt.setInt(2, materialId);
            int rowsInserted = insertStmt.executeUpdate();

            if (rowsInserted > 0) {
                // Successfully inserted the sale record
                System.out.println("Material with ID " + materialId + " successfully sold.");

                // Update the quantity of materialsForSales with remaining quantity
                String updateQuery = "UPDATE materialsforsales SET quantity = quantity - ? WHERE id = ?";
                PreparedStatement updateStmt = MyJDBC.getConnection().prepareStatement(updateQuery);
                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, materialId);
                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Material quantity updated successfully.");
                    return true;
                } else {
                    System.out.println("Failed to update remaining quantity. Sale operation failed.");
                    return false;
                }
            } else {
                System.out.println("Failed to insert sold material. Sale operation failed.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteOrderById(int orderId) {
        try (Connection connection = MyJDBC.getConnection()) {
            // Prepare the SQL statement to delete the row with the specified ID
            String deleteQuery = "DELETE FROM needmaterials WHERE id = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                // Set the ID parameter for the delete statement
                deleteStmt.setInt(1, orderId);
                // Execute the delete statement
                int rowsDeleted = deleteStmt.executeUpdate();
                // Check if any rows were deleted
                if (rowsDeleted > 0) {
                    System.out.println("Order with ID " + orderId + " successfully deleted.");
                } else {
                    System.out.println("No order found with ID " + orderId + ".");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void listOfDeliveredMaterials() {
        try {
            String query = "SELECT * FROM deliveredmaterials";

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
    public static void DeliveredMaterials(String materialName) {
        try (Connection connection = MyJDBC.getConnection();) {
            // Check if the material is available in the missedmaterials table
            String checkAvailabilityQuery = "SELECT quantity FROM soldmaterials WHERE name = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkAvailabilityQuery)) {
                checkStmt.setString(1, materialName);
                ResultSet resultSet = checkStmt.executeQuery();
                if (resultSet.next()) {
                    // Material is available in missedmaterials table, insert entire available quantity into needmaterials table
                    int availableQuantity = resultSet.getInt("quantity");
                    String insertQuery = "INSERT INTO deliveredmaterials (name, quantity) VALUES (?, ?)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, materialName);
                        insertStmt.setInt(2, availableQuantity); // Insert the entire available quantity
                        insertStmt.executeUpdate();
                        System.out.println("Order placed successfully for " + availableQuantity + " units of " + materialName);
                    }
                } else {
                    System.out.println("The material is not available for delivering.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void printTotalIncome() {
        double totalIncome = 0.0;
        try (Connection connection = MyJDBC.getConnection();) {
            // Query to select all materials and their quantities from the deliveredmaterials table
            String selectQuery = "SELECT quantity FROM deliveredmaterials";
            try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
                ResultSet resultSet = selectStmt.executeQuery();
                while (resultSet.next()) {
                    // Retrieve quantity for each material
                    int quantity = resultSet.getInt("quantity");
                    // Calculate income for each material and add it to the total income
                    totalIncome += quantity * 20.0; // Each unit costs $20
                }
            }
            // Print the total income
            System.out.println("Total income: $" + totalIncome);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void listNeedMaterials() {
        try {
            String query = "SELECT * FROM needmaterials";

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
    public void displayTotalQuantity() {
        try {
            String query = "SELECT SUM(quantity) AS total_quantity FROM needmaterials";

            PreparedStatement stmt = MyJDBC.getConnection().prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int totalQuantity = resultSet.getInt("total_quantity");
                System.out.println("Total Quantity: " + totalQuantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Дополнительные методы для работы с данными...
}


