import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

/**
 * Command Line Interface implementation of the PasoEats application.
 * Extends AppController to use core business logic.
 */
public class CLI extends AppController {
    // CLI-specific variables
    private Scanner scanner;
    private OrderSimulator simulator;

    // constructor
    public CLI(){
        super(); // Initialize AppController (managers, etc.)
        scanner = new Scanner(System.in);
        simulator = new OrderSimulator(this);
    }

    // start app
    @Override
    public void start(){
        printWelcome();
        printLogin();
    }

    @Override
    public void shutdown() {
        if (simulator != null) {
            simulator.stop();
        }
        System.out.println("Goodbye");
        System.out.println("Shutting Down...");
        scanner.close();
        System.exit(0);
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
        System.out.println("    3. View Live");
        System.out.println("    4. Shut Down");
        
        //logic start
        mainMenuLogic(readIntInput("Please Choose an Option (1-4) \n"));
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
            case 3:
                try {
                    System.out.println("Press Enter to stop simulation and return to menu");
                    simulator.start();

                    Thread statusThread = new Thread(() -> {
                        while (simulator.isRunning()) {
                            try {
                                Thread.sleep(500);
                                simulator.displayStatus();
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                    });
                    statusThread.setDaemon(true);
                    statusThread.start();
                    
                    scanner.nextLine();
                    simulator.stop();
                } catch (Exception e) {
                    System.out.println("\n\u001B[31mError in simulation: " + e.getMessage() + "\u001B[0m");
                    simulator.stop();
                }
                waitForEnter();
                printLogin();
                input = readIntInput("Please Choose an Option (1-4) \n");
                break;
            case 4: // Shut Down
                simulator.stop();
                shutdown();
                return;
            default:
                System.out.println("\n\u001B[31mInvalid choice. Please enter a number between 1 and 4.\u001B[0m");
                waitForEnter();
                printLogin();
                input = readIntInput("Please Choose an Option (1-4) \n");
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
                    try {
                        System.out.println("\nAvailable Restaurants:");
                        System.out.println(getAllRestaurantsString());
                        
                        List<Restaurant> restaurants = getAllRestaurants();
                        if (restaurants.isEmpty()) {
                            System.out.println("\n\u001B[31mNo restaurants available.\u001B[0m");
                            waitForEnter();
                            break;
                        }
                        
                        int restaurantIndex = readIntInput("Select a restaurant: ");
                        if (restaurantIndex < 1 || restaurantIndex > restaurants.size()) {
                            System.out.println("\n\u001B[31mInvalid restaurant selection.\u001B[0m");
                            waitForEnter();
                            break;
                        }
                        
                        Restaurant selectedRestaurant = restaurants.get(restaurantIndex - 1);
                        List<MenuItem> menuItems = getRestaurantManager().getMenuItemsForRestaurant(selectedRestaurant.getRestaurantId());
                        
                        if (menuItems.isEmpty()) {
                            System.out.println("\n\u001B[31mNo menu items available for this restaurant.\u001B[0m");
                            waitForEnter();
                            break;
                        }
                        
                        System.out.println("\nMenu Items:");
                        for (int i = 0; i < menuItems.size(); i++) {
                            System.out.println("    " + (i + 1) + ". " + menuItems.get(i).detailsToString());
                        }
                        
                        String indicesInput = readStringInput("Enter items (Example: 1,2,3): ");
                        String[] indexTokens = indicesInput.split(",");
                        List<String> items = new ArrayList<>();

                        for (String token : indexTokens) {
                            String trimmed = token.trim();
                            if (trimmed.isEmpty()) {
                                continue;
                            }
                            try {
                                int itemIndex = Integer.parseInt(trimmed);
                                if (itemIndex < 1 || itemIndex > menuItems.size()) {
                                    System.out.println("\n\u001B[31mInvalid item selection: " + itemIndex + "\u001B[0m");
                                    items.clear();
                                    break;
                                }
                                MenuItem selectedItem = menuItems.get(itemIndex - 1);
                                items.add(selectedItem.getItemId().toString());
                            } catch (NumberFormatException nfe) {
                                System.out.println("\n\u001B[31mInvalid item entry: " + trimmed + " (must be a number)\u001B[0m");
                                items.clear();
                                break;
                            }
                        }

                        if (items.isEmpty()) {
                            System.out.println("\n\u001B[31mNo items selected.\u001B[0m");
                            waitForEnter();
                            break;
                        }
                        
                        UUID customerId = getCurrentUserID();
                        OrderManager.Order order = placeOrder(customerId, items);
                        
                        if (order != null) {
                            System.out.println("\n\u001B[32mOrder placed!\u001B[0m");
                            System.out.println("Order ID: " + order.getId());
                            System.out.println("Status: " + order.getStatus());
                            if (order.getAssignedDriverId() != null) {
                                System.out.println("Driver assigned: " + order.getAssignedDriverId());
                            }
                        } else {
                            System.out.println("\n\u001B[31mFailed to place order. No drivers available.\u001B[0m");
                        }
                    } catch (Exception e) {
                        System.out.println("\n\u001B[31mError placing order: " + e.getMessage() + "\u001B[0m");
                    }
                    waitForEnter();
                    break;
                case 2:
                    System.out.println(getAllRestaurantsString());
                    waitForEnter();
                    break;
                case 3:
                    try {
                        System.out.println("\nAvailable Restaurants:");
                        System.out.println(getAllRestaurantsString());
                        
                        List<Restaurant> restaurants = getAllRestaurants();
                        if (restaurants.isEmpty()) {
                            System.out.println("\n\u001B[31mNo restaurants available.\u001B[0m");
                            waitForEnter();
                            break;
                        }
                        
                        int restaurantIndex = readIntInput("Select a restaurant: ");
                        if (restaurantIndex < 1 || restaurantIndex > restaurants.size()) {
                            System.out.println("\n\u001B[31mInvalid restaurant selection.\u001B[0m");
                            waitForEnter();
                            break;
                        }
                        
                        Restaurant selectedRestaurant = restaurants.get(restaurantIndex - 1);
                        System.out.println("\n" + getRestaurantMenuString(selectedRestaurant.getRestaurantId()));
                    } catch (Exception e) {
                        System.out.println("\n\u001B[31mError viewing menu: " + e.getMessage() + "\u001B[0m");
                    }
                    waitForEnter();
                    break;
                case 4:
                    try {
                        UUID customerId = getCurrentUserID();
                        Map<UUID, OrderManager.Order> allOrders = getOrderManager().getAllOrders();
                        
                        // Find most recent order for this customer with an assigned driver
                        OrderManager.Order mostRecentOrder = null;
                        for (OrderManager.Order order : allOrders.values()) {
                            if (order.getCustomerId().equals(customerId) && order.getAssignedDriverId() != null) {
                                if (mostRecentOrder == null || order.getCreatedAt().compareTo(mostRecentOrder.getCreatedAt()) > 0) {
                                    mostRecentOrder = order;
                                }
                            }
                        }
                        
                        if (mostRecentOrder == null) {
                            System.out.println("\n\u001B[31mNo orders with assigned drivers found.\u001B[0m");
                        } else {
                            int rating = readIntInput("Enter rating (1-5): ");
                            if (rating < 1 || rating > 5) {
                                System.out.println("\n\u001B[31mInvalid rating. Please enter a number between 1 and 5.\u001B[0m");
                            } else {
                                UUID driverId = mostRecentOrder.getAssignedDriverId();
                                Driver driver = getFileManager().getDriver(driverId);
                                if (driver != null) {
                                    driver.addRating(rating);
                                    getFileManager().updateDriver(driverId, driver.isAvailable(), driver.getAvgRating());
                                    System.out.println("\n\u001B[32mRating submitted successfully!\u001B[0m");
                                } else {
                                    System.out.println("\n\u001B[31mDriver not found.\u001B[0m");
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("\n\u001B[31mError rating driver: " + e.getMessage() + "\u001B[0m");
                    }
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
                    try {
                        Driver driver = getFileManager().getDriver(getCurrentUserID());
                        if (driver != null) {
                            boolean currentStatus = driver.isAvailable();
                            boolean newStatus = !currentStatus;
                            driver.setAvailable(newStatus);
                            getFileManager().updateDriver(getCurrentUserID(), newStatus, driver.getAvgRating());
                            System.out.println("\n\u001B[32mAvailability changed to " + (newStatus ? "Available" : "Unavailable") + "\u001B[0m");
                        } else {
                            System.out.println("\n\u001B[31mDriver not found.\u001B[0m");
                        }
                    } catch (Exception e) {
                        System.out.println("\n\u001B[31mError changing availability: " + e.getMessage() + "\u001B[0m");
                    }
                    waitForEnter();
                    break;
                case 2:
                    double rating = getCurrentDriverRating();
                    System.out.println("Your Rating: " + (rating >= 0 ? rating : "No ratings yet"));
                    waitForEnter();
                    break;
                case 3:
                    try {
                        UUID driverId = getCurrentUserID();
                        Driver driver = getFileManager().getDriver(driverId);
                        if (driver != null) {
                            if (driver.getCurrentOrder() != null) {
                                System.out.println("\n\u001B[31mYou already have an active order. Please complete it first.\u001B[0m");
                            } else if (!driver.isAvailable()) {
                                System.out.println("\n\u001B[31mYou are not available. Please change your availability first.\u001B[0m");
                            } else {
                                OrderManager.Order order = getOrderManager().acceptNext(driverId);
                                if (order != null) {
                                    driver.setCurrentOrder(order.getId());
                                    driver.setAvailable(false);
                                    getFileManager().updateDriver(driverId, false, driver.getAvgRating());
                                    System.out.println("\n\u001B[32mOrder accepted successfully!\u001B[0m");
                                    System.out.println("Order ID: " + order.getId());
                                    System.out.println("Status: " + order.getStatus());
                                } else {
                                    System.out.println("\n\u001B[31mNo orders available to accept.\u001B[0m");
                                }
                            }
                        } else {
                            System.out.println("\n\u001B[31mDriver not found.\u001B[0m");
                        }
                    } catch (Exception e) {
                        System.out.println("\n\u001B[31mError taking order: " + e.getMessage() + "\u001B[0m");
                    }
                    waitForEnter();
                    break;
                case 4:
                    try {
                        UUID driverId = getCurrentUserID();
                        Driver driver = getFileManager().getDriver(driverId);
                        if (driver != null) {
                            UUID currentOrderId = driver.getCurrentOrder();
                            if (currentOrderId == null) {
                                System.out.println("\n\u001B[31mYou don't have a current order.\u001B[0m");
                            } else {
                                OrderManager.Order order = getOrderManager().get(currentOrderId);
                                if (order == null) {
                                    System.out.println("\n\u001B[31mOrder not found.\u001B[0m");
                                } else {
                                    OrderManager.Status currentStatus = order.getStatus();
                                    System.out.println("\nCurrent Order ID: " + currentOrderId);
                                    System.out.println("Current Status: " + currentStatus);
                                    
                                    if (currentStatus == OrderManager.Status.ACCEPTED) {
                                        System.out.println("\n    1. Mark as In Progress");
                                        int statusChoice = readIntInput("Select option (1): ");
                                        if (statusChoice == 1) {
                                            driver.updateOrderStatus(getOrderManager(), OrderManager.Status.IN_PROGRESS);
                                            System.out.println("\n\u001B[32mOrder status updated to IN_PROGRESS.\u001B[0m");
                                        } else {
                                            System.out.println("\n\u001B[31mInvalid choice.\u001B[0m");
                                        }
                                    } else if (currentStatus == OrderManager.Status.IN_PROGRESS) {
                                        System.out.println("\n    1. Mark as Delivered");
                                        int statusChoice = readIntInput("Select option (1): ");
                                        if (statusChoice == 1) {
                                            driver.updateOrderStatus(getOrderManager(), OrderManager.Status.DELIVERED);
                                            driver.setCurrentOrder(null);
                                            driver.setAvailable(true);
                                            getFileManager().updateDriver(driverId, true, driver.getAvgRating());
                                            System.out.println("\n\u001B[32mOrder delivered! You are now available.\u001B[0m");
                                        } else {
                                            System.out.println("\n\u001B[31mInvalid choice.\u001B[0m");
                                        }
                                    } else {
                                        System.out.println("\n\u001B[31mOrder is already " + currentStatus + ". No updates available.\u001B[0m");
                                    }
                                }
                            }
                        } else {
                            System.out.println("\n\u001B[31mDriver not found.\u001B[0m");
                        }
                    } catch (Exception e) {
                        System.out.println("\n\u001B[31mError updating order: " + e.getMessage() + "\u001B[0m");
                    }
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
                    try {
                        System.out.println("\nManage Restaurants");
                        System.out.println("    1. View All Restaurants");
                        System.out.println("    2. Add Restaurant");
                        System.out.println("    3. Remove Restaurant");
                        System.out.println("    4. Update Restaurant");
                        System.out.println("    5. Add Menu Item to Restaurant");
                        int restaurantChoice = readIntInput("Select option (1-5): ");
                        
                        switch(restaurantChoice) {
                            case 1:
                                System.out.println("\n" + getAllRestaurantsString());
                                break;
                            case 2:
                                String name = readStringInput("Enter restaurant name: ");
                                String category = readStringInput("Enter category: ");
                                if (addRestaurant(name, category)) {
                                    System.out.println("\n\u001B[32mRestaurant added successfully!\u001B[0m");
                                } else {
                                    System.out.println("\n\u001B[31mFailed to add restaurant.\u001B[0m");
                                }
                                break;
                            case 3:
                                System.out.println("\n" + getAllRestaurantsString());
                                List<Restaurant> restaurants = getAllRestaurants();
                                if (!restaurants.isEmpty()) {
                                    int index = readIntInput("Select restaurant to remove: ");
                                    if (index >= 1 && index <= restaurants.size()) {
                                        if (removeRestaurant(restaurants.get(index - 1).getRestaurantId())) {
                                            System.out.println("\n\u001B[32mRestaurant removed successfully!\u001B[0m");
                                        } else {
                                            System.out.println("\n\u001B[31mFailed to remove restaurant.\u001B[0m");
                                        }
                                    } else {
                                        System.out.println("\n\u001B[31mInvalid selection.\u001B[0m");
                                    }
                                }
                                break;
                            case 4:
                                System.out.println("\n" + getAllRestaurantsString());
                                restaurants = getAllRestaurants();
                                if (!restaurants.isEmpty()) {
                                    int index = readIntInput("Select restaurant to update: ");
                                    if (index >= 1 && index <= restaurants.size()) {
                                        String newName = readStringInput("Enter new name: ");
                                        String newCategory = readStringInput("Enter new category: ");
                                        if (updateRestaurant(restaurants.get(index - 1).getRestaurantId(), newName, newCategory)) {
                                            System.out.println("\n\u001B[32mRestaurant updated successfully!\u001B[0m");
                                        } else {
                                            System.out.println("\n\u001B[31mFailed to update restaurant.\u001B[0m");
                                        }
                                    } else {
                                        System.out.println("\n\u001B[31mInvalid selection.\u001B[0m");
                                    }
                                }
                                break;
                            case 5:
                                System.out.println("\n" + getAllRestaurantsString());
                                restaurants = getAllRestaurants();
                                if (!restaurants.isEmpty()) {
                                    int index = readIntInput("Select restaurant: ");
                                    if (index >= 1 && index <= restaurants.size()) {
                                        String itemName = readStringInput("Enter menu item name: ");
                                        String itemCategory = readStringInput("Enter menu item category: ");
                                        System.out.print("Enter price: ");
                                        double price = scanner.nextDouble();
                                        scanner.nextLine();
                                        if (addMenuItemToRestaurant(itemName, itemCategory, price, restaurants.get(index - 1).getRestaurantId())) {
                                            System.out.println("\n\u001B[32mMenu item added successfully!\u001B[0m");
                                        } else {
                                            System.out.println("\n\u001B[31mFailed to add menu item.\u001B[0m");
                                        }
                                    } else {
                                        System.out.println("\n\u001B[31mInvalid selection.\u001B[0m");
                                    }
                                }
                                break;
                            default:
                                System.out.println("\n\u001B[31mInvalid choice.\u001B[0m");
                        }
                    } catch (Exception e) {
                        System.out.println("\n\u001B[31mError managing restaurants: " + e.getMessage() + "\u001B[0m");
                    }
                    waitForEnter();
                    break;
                case 2:
                    try {
                        Map<UUID, OrderManager.Order> orders = getAllOrders();
                        if (orders == null || orders.isEmpty()) {
                            System.out.println("\n\u001B[31mNo orders found.\u001B[0m");
                        } else {
                            System.out.println("\nAll Orders:");
                            for (OrderManager.Order order : orders.values()) {
                                System.out.println("Order ID: " + order.getId());
                                System.out.println("Customer ID: " + order.getCustomerId());
                                System.out.println("Status: " + order.getStatus());
                                System.out.println("Items: " + order.getItems());
                                System.out.println("Driver ID: " + (order.getAssignedDriverId() != null ? order.getAssignedDriverId() : "Not assigned"));
                                System.out.println("Created At: " + order.getCreatedAt());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("\n\u001B[31mError viewing orders: " + e.getMessage() + "\u001B[0m");
                    }
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