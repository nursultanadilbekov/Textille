import java.util.Scanner;

public class Delivery extends User{
    Scanner scanner = new Scanner(System.in);
    public Delivery(String username, String password) {
        super(username, password);
    }
    public void displayOptions(){
        int choice ;
        do{
            System.out.println("Hello dear delivery!!!!!");
            System.out.println("Please choose one of the options below , if you want to exit choose number 8 ");
            System.out.println("1.Show list of materials for delivery\n 2. Show list of the delivered materials \n3.Deliver order \n4.Show my income\n5.Exit");
            choice = getChoice();
        }while(choice!=5);
    }
    public int getChoice(){
        int choice = 0 ;
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
                    System.out.println("Enter the serial number:");
                    String serialNumber = scanner.nextLine();

                    break;
                case 4 :
                    System.out.println("Here is your income:");
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
