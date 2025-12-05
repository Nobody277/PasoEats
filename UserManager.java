import java.util.UUID;

public class UserManager {
    private FileManager fileManager;
    
    /*
     * Constructor
     */
    public UserManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Gets the FileManager instance.
     * @return
     */
    public FileManager getFileManager() {
        return this.fileManager;
    }

    /**
     * Sets the FileManager instance.
     * @param fileManager
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    // ============ Customer Methods ============
    /**
     * Adds a new customer with random UUID via FileManager
     * @param username Customer username
     * @param name Customer name
     * @param email Customer email
     * @return true if successful
     */
    public boolean addCustomer(String username, String name, String email){
        return this.fileManager.addCustomer(username, name, email);
    }

    /**
     * Gets a customer via FileManager
     * @param customer UUID of the customer
     * @return Customer object if found, null otherwise
     */
    public Customer getCustomer(UUID customer){
        return getFileManager().getCustomer(customer);
    }

    /**
     * Removes a customer via FileManager
     * @param customer UUID of the customer
     * @return true if successful
     */
    public boolean removeCustomer(UUID customer){
        return getFileManager().removeCustomer(customer);
    }

    // ============ Driver Methods ============
    /**
     * Adds a new driver with random UUID via FileManager
     * @param username Driver username
     * @param name Driver name
     * @param email Driver email
     * @param available Initial availability status
     * @return true if successful
     */
    public boolean addDriver(String username, String name, String email, boolean available){
        return this.fileManager.addDriver(username, name, email, available);
    }

    /**
     * Gets a driver via FileManager
     * @param driver UUID of the driver
     * @return Driver object if found, null otherwise
     */
    public Driver getDriver(UUID driver){
        return getFileManager().getDriver(driver);
    }

    /**
     * Removes a driver via FileManager
     * @param driver UUID of the driver
     * @return true if successful
     */
    public boolean removeDriver(UUID driver){
        return getFileManager().removeDriver(driver);
    }

    /**
     * Updates a driver's availability and rating via FileManager
     * @param driverId UUID of the driver
     * @param available New availability status
     * @param avgRating New average rating
     * @return true if successful
     */
    public boolean updateDriverDetails(UUID driverId, boolean available, double avgRating){
        return getFileManager().updateDriver(driverId, available, avgRating);
    }

    // ============ Admin Methods ============
    /**
     * Adds a new admin with random UUID via FileManager
     * @param username Admin username
     * @param name Admin name
     * @param email Admin email
     * @return true if successful
     */
    public boolean addAdmin(String username, String name, String email){
        return this.fileManager.addAdmin(username, name, email);
    }

    /**
     * Gets an admin via FileManager
     * @param admin UUID of the admin
     * @return Admin object if found, null otherwise
     */
    public Administrator getAdmin(UUID admin){
        return getFileManager().getAdmin(admin);
    }

    /**
     * Removes an admin via FileManager
     * @param admin UUID of the admin
     * @return true if successful
     */
    public boolean removeAdmin(UUID admin){
        return getFileManager().removeAdmin(admin);
    }

}