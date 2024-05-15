

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.*;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    public boolean login(String username, String password) {
        String query = "SELECT username , password ,  role FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
    public ResultSet listMaterials() throws SQLException {
        String query = "SELECT * FROM materials";
        PreparedStatement stmt = connection.prepareStatement(query);
        return stmt.executeQuery();
    }

    // Дополнительные методы для работы с данными...
}


