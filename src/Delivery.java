import javax.xml.crypto.Data;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Delivery extends User{
    Scanner scanner = new Scanner(System.in);
    DatabaseManager manager = new DatabaseManager();
    public Delivery(String username, String password) {
        super(username, password);
    }
    public void displayOptions(){
        int choice ;
        System.out.println("Hello dear delivery!!!!!");
        do{
            System.out.println();
            System.out.println("Please choose one of the options below , if you want to exit choose number 5 ");
            System.out.println("1.Show list of materials for delivery\n 2. Show list of the delivered materials \n3.Deliver order \n4.Show my income\n5.Exit");
            choice = getChoice();
            manager.sleep();
        }while(choice!=5);
    }
    public int getChoice(){
        int choice = 0 ;
        DatabaseManager manager = new DatabaseManager();
        do{
            choice = scanner.nextInt();
            switch (choice){
                case 1 :
                    System.out.println("Here is the list of materials for delivering");
                    break;
                case 2 :
                    System.out.println("Here is the list of delivered materials:");

                    break;
                case 3 :
                    System.out.println("Enter the name");
                    String materialName = scanner.nextLine();
                    System.out.println("Enter the quantity of material");
                    int materialQuantity = scanner.nextInt();
                    Boolean sold = false ;
                    int price  = 0;
                    addMaterial(materialName  ,price ,  materialQuantity , sold);
                    break;
                case 4 :
                    System.out.println("Here is your income:");
                    break;
                case 5:
                    System.out.println("Exiting...............");
                    manager.exit();
                    break;
                default:
                    System.out.println("Invalid option. Please choose again.");
                    break;
            }
        }while(choice<=1 || choice>4);
        return choice;
    }
    public void addMaterial(String name , int price , int quantity, boolean sold){
        String sql = "INSERT INTO materialsforsales (name , date_added , price , quantity , sold ) VALUES (? , ? , ?, ? , ? ) " ;
        try(Connection con = MyJDBC.getConnection();){
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1,name);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            preparedStatement.setTimestamp(2, timestamp);
            preparedStatement.setInt(3, price);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setBoolean(5 , sold);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Materials added successfully");
            }
            else {
                System.out.println("Failed to add material ");
            }
        }catch (SQLException e){
            System.out.println("Database connection error");
        }

    }


}
