import java.util.Scanner;

public class Main {
    private static DatabaseManager dbManager;
    public static void main(String[] args) {
        login();
    }
    public static void login(){
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