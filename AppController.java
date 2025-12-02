import java.util.List;
import java.util.UUID;

/**
 * Abstract base class that handles core application logic and state.
 * UI implementations (CLI, GUI, etc.) should extend this class.
 */
public abstract class AppController {
    // Core managers
    protected FileManager fileManager;
    protected RestaurantManager restaurantManager;
    protected UserManager userManager;
    //protected OrderManager orderManager;
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
        //this.userManager = new UserManager();
        //this.orderManager = new OrderManager();
        this.driverPool = new DriverPool(fileManager);
        this.currentUserID = null;
        this.currentUserRole = UserRole.NONE;
    }

    // ==================== Authentication ====================

    /**
     * Attempts to login a customer
     * @param customerId The customer's UUID as string
     * @return Customer if found, null otherwise
     */
    public Customer loginCustomer(String customerId) {
        try {
            UUID id = UUID.fromString(customerId);
            Customer customer = fileManager.getCustomer(id);
            if (customer != null) {
                currentUserID = id;
                currentUserRole = UserRole.CUSTOMER;
            }
            return customer;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Attempts to login a driver
     * @param driverId The driver's UUID as string
     * @return Driver if found, null otherwise
     */
    public Driver loginDriver(String driverId) {
        try {
            UUID id = UUID.fromString(driverId);
            Driver driver = fileManager.getDriver(id);
            if (driver != null) {
                currentUserID = id;
                currentUserRole = UserRole.DRIVER;
            }
            return driver;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Attempts to login an administrator
     * @param adminId The administrator's UUID as string
     * @return Administrator if found, null otherwise
     */
    public Administrator loginAdministrator(String adminId) {
        try {
            UUID id = UUID.fromString(adminId);
            Administrator admin = fileManager.getAdmin(id);
            if (admin != null) {
                currentUserID = id;
                currentUserRole = UserRole.ADMINISTRATOR;
            }
            return admin;
        } catch (IllegalArgumentException e) {
            return null;
        }
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
        return restaurantManager.getRestaurants();
    }

    /**
     * Gets formatted string of all restaurants
     * @return Formatted restaurant list
     */
    public String getAllRestaurantsString() {
        return restaurantManager.getAllRestaurantsString();
    }

    /**
     * Gets a restaurant by ID
     * @param restaurantId UUID of the restaurant
     * @return Restaurant if found
     */
    public Restaurant getRestaurant(UUID restaurantId) {
        return restaurantManager.findRestaurantById(restaurantId);
    }

    /**
     * Gets menu items for a restaurant as formatted string
     * @param restaurantId UUID of the restaurant
     * @return Formatted menu items string
     */
    public String getRestaurantMenuString(UUID restaurantId) {
        Restaurant restaurant = restaurantManager.findRestaurantById(restaurantId);
        if (restaurant == null) {
            return "Restaurant not found.";
        }
        return restaurant.getMenuItemsToString(fileManager);
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
        return restaurantManager.addRestaurant(name, category);
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
        return restaurantManager.removeRestaurant(restaurantId);
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
        return restaurantManager.updateRestaurant(restaurantId, newName, newCategory);
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
        return fileManager.getCustomer(currentUserID);
    }// TODO do i need this?

    /**
     * Adds a new customer (admin only)
     * @param name Customer name
     * @return true if successful
     */
    public boolean addCustomer(String name) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }
        return fileManager.addCustomer(name);
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
        return fileManager.removeCustomer(customerId);
    }

    /**
     * Updates a customer's name (admin only)
     * @param customerId UUID of the customer
     * @param newName New name
     * @return true if successful
     */
    public boolean updateCustomerName(UUID customerId, String newName) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }
        return fileManager.updateCustomerName(customerId, newName);
    }

    // ==================== Driver Operations ====================
    /**
     * Adds a new driver (admin only)
     * @param name Driver name
     * @return true if successful
     */
    public boolean addDriver(String name) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }
        return fileManager.addDriver(name);
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
        return fileManager.removeDriver(driverId);
    }

    /**
     * Updates a driver's name (admin only)
     * @param driverId UUID of the driver
     * @param newName New name
     * @return true if successful
     */
    public boolean updateDriverDetails(UUID driverId, String newName) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }
        return fileManager.updateDriverName(driverId, newName);
    }

    // ==================== Administrator Operations ====================

    /**
     * Adds a new administrator (admin only)
     * @param name Administrator name
     * @return true if successful
     */
    public boolean addAdministrator(String name) {
        if (currentUserRole != UserRole.ADMINISTRATOR) {
            return false;
        }
        return fileManager.addAdministrator(name);
    }

    
    // ==================== Driver Operations ====================

    /**
     * Updates driver availability
     * @param available New availability status
     * @return true if successful
     */
    public boolean updateDriverAvailability(boolean available) {
        if (currentUserRole != UserRole.DRIVER || currentUserID == null) {
            return false;
        }
        Driver driver = fileManager.getDriver(currentUserID);
        if (driver != null) {
            return fileManager.updateDriver(currentUserID, available, driver.getAvgRating());
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
        Driver driver = fileManager.getDriver(currentUserID);
        return driver != null ? driver.getAvgRating() : -1;
    }

    // ==================== Getters ====================

    public UUID getCurrentUserID() {
        return currentUserID;
    }

    public UserRole getCurrentUserRole() {
        return currentUserRole;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public RestaurantManager getRestaurantManager() {
        return restaurantManager;
    }

    public DriverPool getDriverPool() {
        return driverPool;
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
