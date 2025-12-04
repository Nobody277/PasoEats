import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Abstract base class that handles core application logic and state.
 * UI implementations (CLI, GUI, etc.) should extend this class.
 */
public abstract class AppController {
    // Core managers
    protected FileManager fileManager;
    protected RestaurantManager restaurantManager;
    protected UserManager userManager;
    protected OrderManager orderManager;
    protected DriverPool driverPool;
    
    // Current session state
    protected UUID currentUserID;
    protected UserRole currentUserRole;

    /**
     * Enum for user roles
     */
    public enum UserRole {
        CUSTOMER,
        DRIVER,
        ADMINISTRATOR,
        NONE
    }

    /**
     * Constructor - initializes all managers
     */
    public AppController() {
        this.fileManager = new FileManager();
        this.restaurantManager = new RestaurantManager(fileManager);
        this.userManager = new UserManager();
        this.orderManager = new OrderManager();
        this.driverPool = new DriverPool(fileManager);
        this.currentUserID = null;
        this.currentUserRole = UserRole.NONE;
    }

    // ==================== Authentication ====================
    /**
     * Attempts to login a customer
     * @param username The customer's username
     * @return Customer if found, null otherwise
     */
    public Customer loginCustomer(String username) {
        Customer customer = getFileManager().getCustomerByUsername(username);
        if (customer != null) {
            currentUserID = customer.getId();
            currentUserRole = UserRole.CUSTOMER;
        }
        return customer;
    }

    /**
     * Attempts to login a driver
     * @param username The driver's username
     * @return Driver if found, null otherwise
     */
    public Driver loginDriver(String username) {
        Driver driver = getFileManager().getDriverByUsername(username);
        if (driver != null) {
            currentUserID = driver.getId();
            currentUserRole = UserRole.DRIVER;
        }
        return driver;
    }

    /**
     * Attempts to login an administrator
     * @param username The administrator's username
     * @return Administrator if found, null otherwise
     */
    public Administrator loginAdministrator(String username) {
        Administrator admin = getFileManager().getAdminByUsername(username);
        if (admin != null) {
            currentUserID = admin.getId();
            currentUserRole = UserRole.ADMINISTRATOR;
        }
        return admin;
    }

    /**
     * Logs out the current user
     */
    public void logout() {
        currentUserID = null;
        currentUserRole = UserRole.NONE;
    }

    /**
     * Checks if a user is currently logged in
     * @return true if logged in
     */
    public boolean isLoggedIn() {
        return currentUserID != null;
    }

    // ==================== Restaurant Operations ====================

    /**
     * Gets all restaurants
     * @return List of all restaurants
     */
    public List<Restaurant> getAllRestaurants() {
        return getRestaurantManager().getRestaurants();
    }

    /**
     * Gets formatted string of all restaurants
     * @return Formatted restaurant list
     */
    public String getAllRestaurantsString() {
        return getRestaurantManager().getAllRestaurantsString();
    }

    /**
     * Gets a restaurant by ID
     * @param restaurantId UUID of the restaurant
     * @return Restaurant if found
     */
    public Restaurant getRestaurant(UUID restaurantId) {
        return getRestaurantManager().findRestaurantById(restaurantId);
    }

    /**
     * Adds a new restaurant (admin only)
     * @param name Restaurant name
     * @param category Restaurant category
     * @return true if successful
     */
    public boolean addRestaurant(String name, String category) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }

        return getRestaurantManager().addRestaurant(name, category);
    }

    /**
     * Removes a restaurant (admin only)
     * @param restaurantId UUID of the restaurant
     * @return true if successful
     */
    public boolean removeRestaurant(UUID restaurantId) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }

        return getRestaurantManager().removeRestaurant(restaurantId);
    }

    /**
     * Updates a restaurant's details (admin only)
     * @param restaurantId UUID of the restaurant
     * @param newName New name
     * @param newCategory New category
     * @return true if successful
     */
    public boolean updateRestaurant(UUID restaurantId, String newName, String newCategory) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }

        return getRestaurantManager().updateRestaurant(restaurantId, newName, newCategory);
    }

    // ==================== Restaurant Menu Item Operations ====================
    /**
     * Gets menu items for a restaurant as formatted string
     * @param restaurantId UUID of the restaurant
     * @return Formatted menu items string
     */
    public String getRestaurantMenuString(UUID restaurantId) {
        Restaurant restaurant = getRestaurantManager().findRestaurantById(restaurantId);
        if (restaurant == null) {
            return "Restaurant not found.";
        }

        return restaurant.getMenuItemsToString(getFileManager());
    }

    /**
     * Adds a new menu item to a restaurant (admin only)
     * @param name Menu item name
     * @param category Menu item category
     * @param price Menu item price
     * @param restaurantId UUID of the restaurant
     * @return true if successful
     */
    public boolean addMenuItemToRestaurant(String name, String category, double price, UUID restaurantId) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }

        return getRestaurantManager().addRestaurantMenuItem(name, category, BigDecimal.valueOf(price), restaurantId);
    }

    // ==================== Customer Operations ====================
    /**
     * Gets the current customer's details
     * @return Customer if logged in, null otherwise
     */
    public Customer getCurrentCustomer() {
        if (currentUserRole != UserRole.CUSTOMER || currentUserID == null) {
            return null;
        }

        return getFileManager().getCustomer(currentUserID);
    }

    /**
     * Adds a new customer with random UUID(admin only)
     * @param name Customer name
     * @return true if successful
     */
    public boolean addCustomer(String name, String username, String email) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }

        return getFileManager().addCustomer(UUID.randomUUID(), name, username, email);
    }

    /**
     * Creates a new customer account
     * @param name Customer name
     * @param username Username
     * @param email Email
     * @return Customer if successful, null otherwise
     */
    public Customer createCustomerAccount(String name, String username, String email) {
        UUID customerId = UUID.randomUUID();
        boolean success = getFileManager().addCustomer(customerId, username, name, email);
        if (success) {
            return getFileManager().getCustomer(customerId);
        }
        return null;
    }

    /**
     * Deletes a customer (admin only)
     * @param customerId UUID of the customer
     * @return true if successful
     */
    public boolean deleteCustomer(UUID customerId) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }

        return getFileManager().removeCustomer(customerId);
    }

    /**
     * Rates the driver for the current order (customer only)
     * @param orderId UUID of the order
     * @param rating Rating
     * @return true if successful
     */
    public boolean rateDriver(UUID orderId, double rating) {
        if (currentUserRole != UserRole.CUSTOMER || currentUserID == null) {
            return false;
        }

        OrderManager.Order order = getOrderManager().get(orderId);
        if (order == null || !order.getCustomerId().equals(currentUserID) || order.getId() == null) {
            return false;
        }

        Driver driver = getFileManager().getDriver(order.getAssignedDriverId());

        return getFileManager().updateDriver(driver.getId(), driver.isAvailable(), rating);
    }

    // ==================== Driver Operations ====================
    /**
     * Adds a new driver as available (admin only)
     * @param name Driver name
     * @return true if successful
     */
    public boolean addNewDriver(String name, String username, String email) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }

        return getFileManager().addDriver(UUID.randomUUID(), name, username, email, true);
    }

    /**
     * Creates a new driver account
     * @param name Driver name
     * @param username Username
     * @param email Email
     * @return Driver if successful, null otherwise
     */
    public Driver createDriverAccount(String name, String username, String email) {
        UUID driverId = UUID.randomUUID();
        boolean success = getFileManager().addDriver(driverId, username, name, email, true);
        if (success) {
            return getFileManager().getDriver(driverId);
        }
        return null;
    }

    /**
     * Deletes a driver (admin only)
     * @param driverId UUID of the driver
     * @return true if successful
     */
    public boolean deleteDriver(UUID driverId) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }

        return getFileManager().removeDriver(driverId);
    }

    /**
     * Updates driver availability and updates the driver pool accordingly
     * @param available New availability status
     * @return true if successful
     */
    public boolean updateDriverAvailability(boolean available) {
        if (currentUserRole != UserRole.DRIVER || currentUserID == null) {
            return false;
        }

        Driver driver = getFileManager().getDriver(currentUserID);
        if (driver != null) {
            getFileManager().updateDriver(currentUserID, available, driver.getAvgRating());
            getDriverPool().updatePoolDrivers(getFileManager());
            return true;
        }

        return false;
    }

    /**
     * Gets the current driver's rating
     * @return Rating or -1 if not a driver
     */
    public double getCurrentDriverRating() {
        if (currentUserRole != UserRole.DRIVER || currentUserID == null) {
            return -1;
        }

        Driver driver = getFileManager().getDriver(currentUserID);

        return driver != null ? driver.getAvgRating() : -1;
    }

    /*
    /**
     * Updates a driver's name (admin only)
     * @param driverId UUID of the driver
     * @param newName New name
     * @return true if successful
     */
    /*
    public boolean updateDriverDetails(UUID driverId, String newUsername, String newName, String newEmail) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }
        return getFileManager().updateDriverDetails(driverId, newUsername, newName, newEmail);//TODO: this is not implemented yet in FileManager, or remove this method
    }
    */

    // ==================== Administrator Operations ====================

    /**
     * Adds a new administrator with random UUID (admin only)
     * @param name Administrator name
     * @return true if successful
     */
    public boolean addAdministrator(String username, String name, String email) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }

        return getFileManager().addAdmin(UUID.randomUUID(), username, name, email);
    }

    /**
     * Creates a new admin account
     * @param username Username
     * @param name Administrator name
     * @param email Email
     * @return Administrator if successful, null otherwise
     */
    public Administrator createAdministratorAccount(String username, String name, String email) {
        UUID adminId = UUID.randomUUID();
        boolean success = getFileManager().addAdmin(adminId, username, name, email);
        if (success) {
            return getFileManager().getAdmin(adminId);
        }
        return null;
    }

    /**
     * Deletes an administrator (admin only)
     * @param adminId UUID of the administrator
     * @return true if successful
     */
    public boolean deleteAdministrator(UUID adminId) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }

        return getFileManager().removeAdmin(adminId);
    }

    /*
    /**
     * Update administrator details (admin only)
     * @param adminId UUID of the administrator
     * @param available
     * @return
     */
    /*
    public boolean updateAdministratorDetails(UUID adminId, String newUsername, String newName, String newEmail) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }

        return getFileManager().updateAdministratorDetails(adminId, newUsername, newName, newEmail);//TODO: this is not implemented yet in FileManager, or remove this method
    }
    */

    // ==================== Order Operations ====================
    /**
     * Gets all orders in the system (admin only)
     * @return Map of all orders by UUID
     */
    public Map<UUID, OrderManager.Order> getAllOrders() {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return null;
        }

        return getOrderManager().getAllOrders();
    }

    /**
     * Places a new order and assigns the next available driver (customer only)
     * @param customerId
     * @param items
     * @return Order if successful, null otherwise
     */
    public OrderManager.Order placeOrder(UUID customerId, List<String> items) {
        if (currentUserRole != UserRole.CUSTOMER || currentUserID == null || !currentUserID.equals(customerId)) {
            return null;
        }

        OrderManager.Order newOrder = new OrderManager.Order();
        newOrder = getOrderManager().place(customerId, items);
        if (newOrder == null || getDriverPool().isEmpty()) {
            return null;
        }
        getOrderManager().acceptNext(getDriverPool().getNextAvailableDriver().getId());

        return newOrder;
    }

    /**
     * Update order status
     * @return true if successful
     */
    public void updateOrderStatus(UUID orderId, OrderManager.Status status) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return;
        }
        getOrderManager().markStatus(orderId, status);
    }

    // ==================== Getters ====================
    public UUID getCurrentUserID() {
        return currentUserID;
    }

    public UserRole getCurrentUserRole() {
        return currentUserRole;
    }

    // ==================== Getters for Manager Instances ====================
    public FileManager getFileManager() {
        return fileManager;
    }

    public RestaurantManager getRestaurantManager() {
        return restaurantManager;
    }

    public DriverPool getDriverPool() {
        return driverPool;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    // ==================== Abstract Methods (UI must implement) ====================
    /**
     * Starts the application UI
     */
    public abstract void start();

    /**
     * Shuts down the application
     */
    public abstract void shutdown();
}
