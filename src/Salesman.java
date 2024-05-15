import java.util.Scanner;

public class Salesman extends User{
    Scanner scanner = new Scanner(System.in);
    public Salesman(String username, String password) {
        super(username, password);
    }
    public void displayOptions(){
        int choice ;
        do{
            System.out.println("Hello dear salesman!!!!!");
            System.out.println("Please choose one of the options below , if you want to exit choose number 7");
            System.out.println("1.Show list of materials for sale\n 2. Search material : \n3.Show report about sale \n4.Sale material \n5.Order missing materials\n6.Delete material order\n7.Exit");
            choice = getChoice();
        }while(choice!=5);
    }
    public int getChoice(){
        int choice = 0 ;
        do{
            choice = scanner.nextInt();
            switch (choice){
                case 1 :
                    System.out.println("Here is the list of materials for sale");
                    break;
                case 2 :
                    System.out.println("Please choose one of the options:\n1.Search by name\n2.Search by date ");
                    int option = scanner.nextInt();
                    if(option == 1){

                    } else if (option == 2 ) {

                    }
                    else{
                        System.out.println("Please choose 1 or 2 ");
                    }
                    break;
                case 3 :
                    System.out.println("Here is the report about sale");


                    break;
                case 4 :
                    System.out.println("Provide material id that you want to sale");
                    int materialIdScanner = scanner.nextInt();
                    break;
                case 5:
                    System.out.println("Provide  the missing material name ");
                    String materialName  = scanner.nextLine();
                    System.out.println("Provide the missing material quantity");
                    int materialQuantity = scanner.nextInt();
                    break;
                case 6:
                    System.out.println("Provide material order id to delete it ");
                    break;
                case 7:
                    System.out.println("Exiting....");
                    break;
                default:
                    System.out.println("Invalid option. Please choose again.");
                    break;
            }
        }while(choice<=1 || choice>4);
        return choice;
    }
}
