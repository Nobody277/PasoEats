import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Command Line Interface implementation of the PasoEats application.
 * Extends AppController to use core business logic.
 */
public class CLI extends AppController {
    // CLI-specific variables
    private Scanner scanner;

    // constructor
    public CLI(){
        super(); // Initialize AppController (managers, etc.)
        scanner = new Scanner(System.in);
    }

    // start app
    @Override
    public void start(){
        printWelcome();
        printLogin();
    }

    @Override
    public void shutdown() {
        System.out.println("Goodbye");
        System.out.println("Shutting Down...");
        scanner.close();
    }

    // print messages
    public void printWelcome(){
        System.out.println("CS 233 Paso Eats Project");
        System.out.println("Welcome!");
    }

    public void printLogin(){
        System.out.println("Role Menu");
        System.out.println("    1. Customer");
        System.out.println("    2. Driver");
        System.out.println("    3. Administrator");
        System.out.println("    4. Shut Down");
        
        //logic start
        loginLogic(readIntInput("Please Choose an Option (1-4) \n"));
    }

    // login logic
    public void loginLogic(int input){
        boolean running = true;
        String tempID;
        while(running){
            switch(input){
            case 1: 
                try{
                    tempID = readStringInput("Please Enter Your Customer ID \n");
                    Customer customer = loginCustomer(tempID);
                    if(customer != null){
                        printCustomerMenu();
                    }
                    else{ 
                        System.out.println("\n\u001B[31mID: " + tempID + " not found.\u001B[0m");
                        waitForEnter();
                    }
                    break;
                }
                catch(Exception e){
                    System.out.println("Illegal Entry. Exception Caught.");
                    break;
                }
            case 2:
                try{
                    tempID = readStringInput("Please Enter Your Driver ID \n");
                    Driver driver = loginDriver(tempID);
                    if(driver != null){
                        printDriverMenu();
                    }
                    else{ 
                        System.out.println("\n\u001B[31mID: " + tempID + " not found.\u001B[0m");
                        waitForEnter();
                    }
                    break;
                }
                catch(Exception e){
                    System.out.println("Illegal Entry. Exception Caught.");
                    break;
                }
                
            case 3: 
                try{
                    tempID = readStringInput("Please Enter Your Administrator ID \n");
                    Administrator administrator = loginAdministrator(tempID);
                    if(administrator != null){
                        printAdministratorMenu();
                    }
                    else{ 
                        System.out.println("\n\u001B[31mID: " + tempID + " not found.\u001B[0m");
                        waitForEnter();
                    }
                    break;
                }
                catch(Exception e){
                    System.out.println("Illegal Entry. Exception Caught.");
                    break;
                }
            case 4: 
                shutdown();
                running = false;
                break;
            default:
                System.out.println("\n\u001B[31mInvalid choice. Please enter a number between 1 and 4.\u001B[0m");
                waitForEnter();
            }
        }
    }

    // prints for customer
    public void printCustomerMenu(){
        System.out.println("Customer Menu");
        System.out.println("    1. Place An Order");
        System.out.println("    2. View Restaurants");
        System.out.println("    3. View A Menu");
        System.out.println("    4. Rate Most Recent Driver");
        System.out.println("    5. Log Out");
        System.out.println("Please Choose an Option (1-5) \n"); // change to read int input method
    }

    // prints for driver
    public void printDriverMenu(){
        System.out.println("Driver Menu");
        System.out.println("    1. Change Availability");
        System.out.println("    2. View Your Rating");
        System.out.println("    3. Take an Order");
        System.out.println("    4. Update Current Order");
        System.out.println("    5. Log Out");
        System.out.println("Please Choose an Option (1-5) \n"); // change to read int input method
    }

    //prints for administrator
    public void printAdministratorMenu(){
        System.out.println("Administrator Menu");
        System.out.println("1. Manage Restaurants");
        System.out.println("2. View All Orders");
        System.out.println("3. Log Out");
        System.out.println("Please Choose an Option (1-3) \n"); // change to read int input method
    }

    // utility methods
    private int readIntInput(String prompt) { // read an int input, if invalid repeat
        int input;
        while (true) {
            System.out.print(prompt);
            try {
                input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                System.out.println("\u001B[31mError: Invalid input. Please enter a whole number.\u001B[0m");
                scanner.nextLine();
            }
        }
    }
    private String readStringInput(String prompt) { // read a string input, if invalid repeat
        String input;
        while (true) {
             System.out.print(prompt);
             input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("\u001B[31mError: Input cannot be empty.\u001B[0m");
            }
        }
    }
    private void waitForEnter() { // wait for user to press enter
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

}
