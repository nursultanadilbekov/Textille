import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static DatabaseManager dbManager;
    public static void main(String[] args) {

        try {
            dbManager = new DatabaseManager("jdbc:mysql://localhost:3306/login_schema", "root", "pl-2dimagay");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Добро пожаловать в систему 'Промышленное производство текстиля'");
            System.out.print("Введите ваш логин: ");
            String username = scanner.nextLine();
            System.out.print("Введите ваш пароль: ");
            String password = scanner.nextLine();

            if (dbManager.login(username, password)) {
                System.out.println("Авторизация успешна.");
                // Вывести меню в зависимости от роли пользователя
            } else {
                System.out.println("Логин или пароль неверный.");
            }
            String query = "SELECT username , password , role FROM users WHERE username =? AND password = ? ";

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}