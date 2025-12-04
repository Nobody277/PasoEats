import java.io.File;
import java.util.UUID;

public class UserManager {
    final FileManager fileManager = new FileManager();
    
    /*
     * Constructor
     */
    public UserManager(FileManager fileManager) {
        this.setFileManager(fileManager);
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
        this.fileManager = fileManager; // final
    }

    // ============ Customer Methods ============
    /**
     * Adds a new customer with random UUID via FileManager
     * @param name Customer name
     * @param email Customer email
     * @param password Customer password
     * @return true if successful
     */
    public boolean addCustomer(String name, String email, String password){
        UUID newId = UUID.randomUUID();
        return this.fileManager.addCustomer(newId, name, email, password);
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

    /**
     * Updates a customer's information via FileManager
     * @param customerId UUID of the customer
     * @param name New name
     * @param email New email
     * @param password New password
     * @return true if successful
     */
    public boolean updateCustomer(UUID customerId, String name, String email, String password){
        return getFileManager().updateCustomer(customerId, name, email, password);
    }//TODO: this is not implemented yet in FileManager, or remove this method

    // ============ Driver Methods ============
    public boolean addDriver(String name, String email, String password){
        UUID newId = UUID.randomUUID();
        return this.fileManager.addDriver(newId, name, email, password, true);
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
     * Updates a driver's information via FileManager
     * @param driverId UUID of the driver
     * @param name New name
     * @param email New email
     * @param password New password
     * @return true if successful
     */
    public boolean updateDriverDetails(UUID driverId, String name, String email, String password){
        return getFileManager().updateDriver(driverId, name, email, password);
    }//TODO: this is not implemented yet in FileManager, or remove this method

    // ============ Admin Methods ============
    /**
     * Adds a new admin with random UUID via FileManager
     * @param name
     * @param email
     * @param password
     * @return
     */
    public boolean addAdmin(String name, String email, String password){
        UUID newId = UUID.randomUUID();
        return this.fileManager.addAdmin(newId, name, email, password);
    }

    /**
     * Gets an admin via FileManager
     * @param admin UUID of the admin
     * @return Admin object if found, null otherwise
     */
    public Admin getAdmin(UUID admin){
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

    /**
     * Updates an admin's information via FileManager
     * @param adminId UUID of the admin
     * @param name New name
     * @param email New email
     * @param password New password
     * @return true if successful
     */
    public boolean updateAdmin(UUID adminId, String name, String email, String password){
        return getFileManager().updateAdmin(adminId, name, email, password);
    }//TODO: this is not implemented yet in FileManager, or remove this method
}