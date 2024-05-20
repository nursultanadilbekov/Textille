import java.util.Scanner;


public class Salesman extends User{
    Scanner scanner = new Scanner(System.in);
    DatabaseManager manager = new DatabaseManager();
    public Salesman(String username, String password) {
        super(username, password);
    }
    public void displayOptions(){
        int choice ;
        System.out.println("Hello dear salesman!!!!!");
        do{
            System.out.println();
            System.out.println("Please choose one of the options below , if you want to exit choose number 7");
            System.out.println("1.Show list of materials for sale\n2. Search material : \n3.Show report about sale \n4.Sale material \n5.Order missing materials\n6.Delete material order\n7.Exit");
            choice = scanner.nextInt();
            getChoice(choice);
            manager.sleep();
        }while(choice!=7);
    }
    public int getChoice(int choosen){
        DatabaseManager manager = new DatabaseManager();

        switch (choosen){
            case 1 :
                System.out.println("Here is the list of materials for sale:");
                manager.listMaterials();
                break;
            case 2 :
                System.out.println("Please choose one of the options:\n1.Search by name\n2.Search by date ");
                int option = scanner.nextInt();
                scanner.nextLine();
                if(option == 1){
                    System.out.println("Please print the name of material:");
                    String name = scanner.nextLine();
                    manager.searchMaterialsByName(name);
                } else if (option == 2 ) {
                    System.out.println("Please print the date of material:");
                    String date = scanner.nextLine();
                    manager.searchMaterialsByDateAdded(date);
                }
                else{
                    System.out.println("Please choose 1 or 2:");
                }
                break;
            case 3 :
                System.out.println("Here is the report about sale:");
                manager.listSoldMaterials();
                break;
            case 4 :
                System.out.println("Provide material id that you want to sale:");
                int materialId = scanner.nextInt();
                System.out.println("Provide the material quantity:");
                int numberOfMaterial = scanner.nextInt();
                manager.saleMaterialById(materialId,numberOfMaterial);
                break;
            case 5:
                System.out.println("Missed materials:");
                manager.displayMissedMaterials();
                scanner.nextLine();
                System.out.println("Provide  the missing material name:");
                String materialName  = scanner.nextLine();
                System.out.println("Provide the missing material quantity:");
                int materialQuantity = scanner.nextInt();
                scanner.nextLine();
                manager.makeOrderForUnavailableMaterials(materialName,materialQuantity);
                break;
            case 6:
                System.out.println("Provide material order id to delete it:");
                int matrerialId = scanner.nextInt();
                manager.deleteOrderById(matrerialId);
                break;
            case 7:
                System.out.println("Exiting....");
                manager.exit();
                break;
            default:
                System.out.println("Invalid option. Please choose again.");
                break;
        };
        return 0;
    }
}

