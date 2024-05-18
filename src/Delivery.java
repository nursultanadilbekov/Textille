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
            System.out.println("1.Show list of materials for delivery\n2. Show list of the delivered materials \n3.Deliver order \n4.Show my income\n5.Exit");
            choice = scanner.nextInt();
            getChoice(choice);
            manager.sleep();
        }while(choice!=5);
    }
    public int getChoice(int choosen){
        DatabaseManager manager = new DatabaseManager();
            switch (choosen){
                case 1 :
                    System.out.println("Here is the list of materials for delivering:");
                    manager.listSoldMaterials();
                    break;
                case 2 :
                    System.out.println("Here is the list of delivered materials:");
                    manager.listOfDeliveredMaterials();
                    break;
                case 3 :
                    manager.listSoldMaterials();
                    System.out.println("Enter the name");
                    scanner.nextLine();
                    String materialName = scanner.nextLine();
                    manager.DeliveredMaterials(materialName);
                    break;
                case 4 :
                    System.out.println("Here is your income:");
                    manager.printTotalIncome();
                    break;
                case 5:
                    System.out.println("Exiting...............");
                    manager.exit();
                    break;
                default:
                    System.out.println("Invalid option. Please choose again.");
                    break;
            };
        return 0;
    }
}
