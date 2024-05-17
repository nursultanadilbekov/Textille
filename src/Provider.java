import java.util.Scanner;

public class Provider extends User{
    DatabaseManager manager = new DatabaseManager();
    Scanner scanner = new Scanner(System.in);
    public Provider(String username, String password) {
        super(username, password);
    }
    public void displayOptions(){
        int choice ;
        System.out.println("Hello dear provider!!!!!");
        do{
            System.out.println();
            System.out.println("Please choose one of the options below , if you want to exit choose number 5 ");
            System.out.println("1.Show list of materials for providing\n2. Show quantity of materials\n3.Show the material with highest quantity of orders for delivery\n4.Show material with lowest quantity of orders for delivery\n5.Exit");
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
                    System.out.println("Here is the list of materials for providing:");
                    manager.listNeedMaterials();
                    manager.listMaterials();
                    break;
                case 2 :
                    System.out.println("Here is the quantity of materials:");
                    manager.displayTotalQuantity();
                    break;
                case 3 :
                    System.out.println("Here is the material with the highest quantity of orders for delivery");
                    manager.displayHighestQuantityMaterial();
                    break;
                case 4 :
                    System.out.println("Here is the material with the lowest quantity of orders for delivery ");
                    manager.displayLowestQuantityMaterial();
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
}
