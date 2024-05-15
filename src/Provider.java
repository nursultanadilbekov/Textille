import java.util.Scanner;

public class Provider extends User{
    Scanner scanner = new Scanner(System.in);
    public Provider(String username, String password) {
        super(username, password);
    }
    public void displayOptions(){
        int choice ;
        do{
            System.out.println("Hello dear provider!!!!!");
            System.out.println("Please choose one of the options below , if you want to exit choose number 8 ");
            System.out.println("1.Show list of materials for providing\n 2. Show quantity of materials\n3.Show the material with highest quantity of orders for delivery\n4.Show material with lowest quantity of orders for delivery\n5.Exit");
             choice = getChoice();
        }while(choice!=5);
    }
    public int getChoice(){
        int choice = 0 ;
        do{
            choice = scanner.nextInt();
            switch (choice){
                case 1 :
                    System.out.println("Here is the list of materials for providing");
                    break;
                case 2 :
                    System.out.println("Here is the quantity of materials:");
                    break;
                case 3 :
                    System.out.println("Here is the material with the highest quantity of orders for delivery");
                    break;
                case 4 :
                    System.out.println("Here is the material with the lowest quantity of orders for delivery ");
                    break;
                case 5:
                    System.out.println("Exiting...............");
                    break;
                default:
                    System.out.println("Invalid option. Please choose again.");
                    break;
            }
        }while(choice<=1 || choice>4);
        return choice;
    }
}
