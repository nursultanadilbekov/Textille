import java.sql.Connection;
import java.sql.SQLException;

import java.sql.*;
import java.util.Scanner;

public class DatabaseManager {
    private static DatabaseManager dbManager;


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
                System.out.println("You entered as " + role);
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
                        System.out.println("Unknown role: " + role);
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

    public void listSoldMaterials() {
        try (Connection connection = MyJDBC.getConnection()) {
            String query = "SELECT id, name, date_added, price, soldquantity FROM materialsforsales WHERE soldquantity > 0";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Sold items:");
            System.out.printf("%-5s%-15s%-12s%-10s%-12s\n", "ID", "Name", "Date Added", "Price", "Sold Quantity");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String dateAdded = resultSet.getString("date_added");
                double price = resultSet.getDouble("price");
                int soldQuantity = resultSet.getInt("soldquantity");
                System.out.printf("%-5d%-15s%-12s%-10.2f%-12d\n", id, name, dateAdded, price, soldQuantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean saleMaterialById(int materialId, int quantity) {
        Connection conn = null;
        PreparedStatement updateStmt = null;
        try {
            conn = MyJDBC.getConnection();
            conn.setAutoCommit(false);

            String updateQuery = "UPDATE materialsforsales SET quantity = quantity - ?, soldquantity = soldquantity + ? WHERE id = ?";
            updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, quantity);
            updateStmt.setInt(2, quantity);
            updateStmt.setInt(3, materialId);
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Material with ID " + materialId + " successfully sold.");
                conn.commit();
                return true;
            } else {
                System.out.println("Sale operation failed.");
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (updateStmt != null) {
                try {
                    updateStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void displayMissedMaterials() {
        try (Connection connection = MyJDBC.getConnection();) {
            String selectQuery = "SELECT * FROM materialsforsales where quantity < 1";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            ResultSet resultSet = selectStmt.executeQuery();

            System.out.println("ID | Name | Date Added | Price | Quantity");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String dateAdded = resultSet.getString("date_added");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");

                System.out.println(id + " | " + name + " | " + dateAdded + " | " + price + " | " + quantity);
            }
            resultSet.close();
            selectStmt.close();

            System.out.println("Missed materials successfully displayed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void makeOrderForUnavailableMaterials(String materialName, int quantity) {
        try (Connection connection = MyJDBC.getConnection();) {
            String checkAvailabilityQuery = "SELECT quantity FROM materialsforsales WHERE name = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkAvailabilityQuery)) {
                checkStmt.setString(1, materialName);
                ResultSet resultSet = checkStmt.executeQuery();
                if (resultSet.next()) {
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

    public void deleteOrderById(int orderId) {
        try (Connection connection = MyJDBC.getConnection()) {
            String deleteQuery = "DELETE FROM needmaterials WHERE id = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, orderId);
                int rowsDeleted = deleteStmt.executeUpdate();
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
        try (Connection connection = MyJDBC.getConnection()) {
            String checkAvailabilityQuery = "SELECT soldquantity FROM materialsforsales WHERE name = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkAvailabilityQuery)) {
                checkStmt.setString(1, materialName);
                ResultSet resultSet = checkStmt.executeQuery();
                if (resultSet.next()) {
                    int soldQuantity = resultSet.getInt("soldquantity");
                    if (soldQuantity == 0) {
                        System.out.println("The material is not available for delivery.");
                    } else {
                        String insertQuery = "INSERT INTO deliveredmaterials (name, quantity) VALUES (?, ?)";
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                            insertStmt.setString(1, materialName);
                            insertStmt.setInt(2, soldQuantity);
                            insertStmt.executeUpdate();
                            System.out.println("Delivered material added successfully with " + soldQuantity + " units of " + materialName);
                        }
                        String updateQuery = "UPDATE materialsforsales SET soldquantity = 0 WHERE name = ?";
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                            updateStmt.setString(1, materialName);
                            updateStmt.executeUpdate();
                        }
                    }
                } else {
                    System.out.println("The material is not available for delivery.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void printTotalIncome() {
        double totalIncome = 0.0;
        try (Connection connection = MyJDBC.getConnection();) {
            String selectQuery = "SELECT quantity FROM deliveredmaterials";
            try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
                ResultSet resultSet = selectStmt.executeQuery();
                while (resultSet.next()) {
                    int quantity = resultSet.getInt("quantity");
                    totalIncome += quantity * 20.0;
                }
            }
            System.out.println("Total income: $" + totalIncome);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void listNeedMaterials() {
        try {
            String query = "SELECT id, name, quantity FROM needmaterials";

            PreparedStatement stmt = MyJDBC.getConnection().prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                System.out.printf("%-5d%-15s%-8d\n", id, name, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    public void displayHighestQuantityMaterial() {
        try (Connection connection = MyJDBC.getConnection()) {
            String query = "SELECT id, name, date_added, price, soldquantity FROM materialsforsales WHERE soldquantity > 0 ORDER BY soldquantity DESC LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Material with the highest sold quantity:");
            System.out.printf("%-5s%-15s%-12s%-10s%-12s\n", "ID", "Name", "Date Added", "Price", "Sold Quantity");
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String dateAdded = resultSet.getString("date_added");
                double price = resultSet.getDouble("price");
                int soldQuantity = resultSet.getInt("soldquantity");
                System.out.printf("%-5d%-15s%-12s%-10.2f%-12d\n", id, name, dateAdded, price, soldQuantity);
            } else {
                System.out.println("No material with sold quantity greater than 0 found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void displayLowestQuantityMaterial() {
        try (Connection connection = MyJDBC.getConnection()) {
            String query = "SELECT id, name, date_added, price, soldquantity FROM materialsforsales WHERE soldquantity > 0 ORDER BY soldquantity ASC LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Material with the lowest sold quantity:");
            System.out.printf("%-5s%-15s%-12s%-10s%-12s\n", "ID", "Name", "Date Added", "Price", "Sold Quantity");
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String dateAdded = resultSet.getString("date_added");
                double price = resultSet.getDouble("price");
                int soldQuantity = resultSet.getInt("soldquantity");
                System.out.printf("%-5d%-15s%-12s%-10.2f%-12d\n", id, name, dateAdded, price, soldQuantity);
            } else {
                System.out.println("No material found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void exit(){
        login();
    }
    public void login(){
        try {
            dbManager = new DatabaseManager();
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Добро пожаловать в систему 'Промышленное производство текстиля'");
                System.out.print("Введите ваш логин: ");
                String username = scanner.nextLine();
                System.out.print("Введите ваш пароль: ");
                String password = scanner.nextLine();

                if (dbManager.login(username, password)) {
                    System.out.println("Авторизация успешна.");
                    break;
                } else {
                    System.out.println("Логин или пароль неверный.");
                    continue;
                }
            }
            String query = "SELECT username , password , role FROM users WHERE username =? AND password = ? ";

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


