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
        System.out.println("Main Menu");
        System.out.println("    1. Register");
        System.out.println("    2. Login");
        System.out.println("    3. Shut Down");
        
        //logic start
        mainMenuLogic(readIntInput("Please Choose an Option (1-3) \n"));
    }

    // main menu logic
    public void mainMenuLogic(int input){
        boolean running = true;
        while(running){
            switch(input){
            case 1: // Register
                try{
                    System.out.println("Register as:");
                    System.out.println("    1. Customer");
                    System.out.println("    2. Driver");
                    System.out.println("    3. Administrator");
                    int role = readIntInput("Please Choose an Option (1-3) \n");
                    String username = readStringInput("Enter Username: \n");
                    String name = readStringInput("Enter Name: \n");
                    String email = readStringInput("Enter Email: \n");
                    
                    if(role == 1){
                        Customer customer = createCustomerAccount(name, username, email);
                        if(customer != null){
                            System.out.println("\n\u001B[32mAccount created!\u001B[0m");
                            loginCustomer(username);
                            handleCustomerMenu();
                            running = false;
                        } else {
                            System.out.println("\n\u001B[31mRegistration failed. Username or email may already be taken.\u001B[0m");
                            waitForEnter();
                            printLogin();
                            input = readIntInput("Please Choose an Option (1-3) \n");
                        }
                    } else if(role == 2){
                        Driver driver = createDriverAccount(name, username, email);
                        if(driver != null){
                            System.out.println("\n\u001B[32mAccount created!\u001B[0m");
                            loginDriver(username);
                            handleDriverMenu();
                            running = false;
                        } else {
                            System.out.println("\n\u001B[31mRegistration failed. Username or email may already be taken.\u001B[0m");
                            waitForEnter();
                            printLogin();
                            input = readIntInput("Please Choose an Option (1-3) \n");
                        }
                    } else if(role == 3){
                        Administrator admin = createAdministratorAccount(username, name, email);
                        if(admin != null){
                            System.out.println("\n\u001B[32mAccount created!\u001B[0m");
                            loginAdministrator(username);
                            handleAdministratorMenu();
                            running = false;
                        } else {
                            System.out.println("\n\u001B[31mRegistration failed. Username or email may already be taken.\u001B[0m");
                            waitForEnter();
                            printLogin();
                            input = readIntInput("Please Choose an Option (1-3) \n");
                        }
                    } else {
                        System.out.println("\n\u001B[31mInvalid role selection.\u001B[0m");
                        waitForEnter();
                        printLogin();
                        input = readIntInput("Please Choose an Option (1-3) \n");
                    }
                    break;
                }
                catch(Exception e){
                    System.out.println("Illegal Entry. Exception Caught.");
                    printLogin();
                    input = readIntInput("Please Choose an Option (1-3) \n");
                    break;
                }
            case 2: // Login
                try{
                    System.out.println("Login as:");
                    System.out.println("    1. Customer");
                    System.out.println("    2. Driver");
                    System.out.println("    3. Administrator");
                    int role = readIntInput("Please Choose an Option (1-3) \n");
                    String tempUsername = readStringInput("Please Enter Your Username \n");
                    
                    if(role == 1){
                        Customer customer = loginCustomer(tempUsername);
                        if(customer != null){
                            handleCustomerMenu();
                            running = false;
                        } else {
                            System.out.println("\n\u001B[31mUsername: " + tempUsername + " not found.\u001B[0m");
                            waitForEnter();
                            printLogin();
                            input = readIntInput("Please Choose an Option (1-3) \n");
                        }
                    } else if(role == 2){
                        Driver driver = loginDriver(tempUsername);
                        if(driver != null){
                            handleDriverMenu();
                            running = false;
                        } else {
                            System.out.println("\n\u001B[31mUsername: " + tempUsername + " not found.\u001B[0m");
                            waitForEnter();
                            printLogin();
                            input = readIntInput("Please Choose an Option (1-3) \n");
                        }
                    } else if(role == 3){
                        Administrator administrator = loginAdministrator(tempUsername);
                        if(administrator != null){
                            handleAdministratorMenu();
                            running = false;
                        } else {
                            System.out.println("\n\u001B[31mUsername: " + tempUsername + " not found.\u001B[0m");
                            waitForEnter();
                            printLogin();
                            input = readIntInput("Please Choose an Option (1-3) \n");
                        }
                    } else {
                        System.out.println("\n\u001B[31mInvalid role selection.\u001B[0m");
                        waitForEnter();
                        printLogin();
                        input = readIntInput("Please Choose an Option (1-3) \n");
                    }
                    break;
                }
                catch(Exception e){
                    System.out.println("Illegal Entry. Exception Caught.");
                    printLogin();
                    input = readIntInput("Please Choose an Option (1-3) \n");
                    break;
                }
            case 3: // Shut Down
                shutdown();
                running = false;
                break;
            default:
                System.out.println("\n\u001B[31mInvalid choice. Please enter a number between 1 and 3.\u001B[0m");
                waitForEnter();
                printLogin();
                input = readIntInput("Please Choose an Option (1-3) \n");
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
        System.out.println("    1. Manage Restaurants");
        System.out.println("    2. View All Orders");
        System.out.println("    3. Log Out");
        System.out.println("Please Choose an Option (1-3) \n");
    }

    // menu
    public void handleCustomerMenu(){
        boolean running = true;
        while(running){
            printCustomerMenu();
            int choice = readIntInput("");
            switch(choice){
                case 1:
                    System.out.println("Place An Order - TODO");
                    waitForEnter();
                    break;
                case 2:
                    System.out.println(getAllRestaurantsString());
                    waitForEnter();
                    break;
                case 3:
                    System.out.println("View A Menu - TODO");
                    waitForEnter();
                    break;
                case 4:
                    System.out.println("Rate Most Recent Driver - TODO");
                    waitForEnter();
                    break;
                case 5:
                    logout();
                    running = false;
                    printLogin();
                    mainMenuLogic(readIntInput("Please Choose an Option (1-3) \n"));
                    break;
                default:
                    System.out.println("\n\u001B[31mInvalid choice. Please enter a number between 1 and 5.\u001B[0m");
                    waitForEnter();
            }
        }
    }

    public void handleDriverMenu(){
        boolean running = true;
        while(running){
            printDriverMenu();
            int choice = readIntInput("");
            switch(choice){
                case 1:
                    System.out.println("Change Availability - TODO");
                    waitForEnter();
                    break;
                case 2:
                    double rating = getCurrentDriverRating();
                    System.out.println("Your Rating: " + (rating >= 0 ? rating : "No ratings yet"));
                    waitForEnter();
                    break;
                case 3:
                    System.out.println("Take an Order - TODO");
                    waitForEnter();
                    break;
                case 4:
                    System.out.println("Update Current Order - TODO");
                    waitForEnter();
                    break;
                case 5:
                    logout();
                    running = false;
                    printLogin();
                    mainMenuLogic(readIntInput("Please Choose an Option (1-3) \n"));
                    break;
                default:
                    System.out.println("\n\u001B[31mInvalid choice. Please enter a number between 1 and 5.\u001B[0m");
                    waitForEnter();
            }
        }
    }

    public void handleAdministratorMenu(){
        boolean running = true;
        while(running){
            printAdministratorMenu();
            int choice = readIntInput("");
            switch(choice){
                case 1:
                    System.out.println("Manage Restaurants - TODO");
                    waitForEnter();
                    break;
                case 2:
                    System.out.println("View All Orders - TODO");
                    waitForEnter();
                    break;
                case 3:
                    logout();
                    running = false;
                    printLogin();
                    mainMenuLogic(readIntInput("Please Choose an Option (1-3) \n"));
                    break;
                default:
                    System.out.println("\n\u001B[31mInvalid choice. Please enter a number between 1 and 3.\u001B[0m");
                    waitForEnter();
            }
        }
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