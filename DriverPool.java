import java.util.PriorityQueue;
import java.util.Map;
import java.util.UUID;

public class DriverPool {
    private PriorityQueue<Driver> driverPool;
    private FileManager fileManager;

    /**
     * Constructor for DriverPool class
     */
    public DriverPool(FileManager fileManager) {
        this.driverPool = new PriorityQueue<>();
        this.fileManager = fileManager;
        clearAndUpdatePool(this.fileManager);
    }

    /**
     * Adds a driver to the pool if they are available
     * @param driver
     */
    public void addDriver(Driver driver) {
        if (driver.isAvailable()) {
            this.driverPool.offer(driver);
        }
        // consider feedback for unavailable drivers
    }

    /**
     * Removes a driver from the pool and sets them to unavailable
     * @param driver
     */
    public void removeDriver(Driver driver) {
        this.driverPool.remove(driver);
        driver.setAvailable(false);
        // consider feedback for unavailable drivers
    }

    /**
     * Gets the next available driver from the pool and sets them to unavailable
     * @return Driver
     */
    public Driver getNextAvailableDriver() {
        Driver driver = this.driverPool.poll();
        driver.setAvailable(false);
        return driver;
    }

    /**
     * Completes the delivery for a driver and re-adds them to the pool
     * @param driver
     */
    public void completeDelivery(Driver driver) {
        this.addDriver(driver); // re-add driver to the pool
    }

    /**
     * Gets the current size of the driver pool
     * @return int
     */
    public int getPoolSize() {
        return driverPool.size();
    }

    /**
     * Checks if the driver pool is empty
     * @return boolean
     */
    public boolean isEmpty() {
        return driverPool.isEmpty();
    }

    /**
     * Clears the driver pool
     */
    public void clearPool() {
        driverPool.clear();
    }  

    /**
     * Clears and updates the driver pool using FileManager
     * @param fileManager
     */
    public void clearAndUpdatePool(FileManager fileManager) {
        this.clearPool();
        this.updatePoolDrivers(fileManager);
    }

    /**
     * Updates the driver pool with newly available drivers using FileManager
     * @param fileManager
     */
    public void updatePoolDrivers(FileManager fileManager) {
        // Add available drivers from FM
        Map<UUID, Driver> drivers = fileManager.getAllDrivers();
        if (drivers != null) {
            for (Driver driver : drivers.values()) {
                if (driver.isAvailable() && !driverPool.contains(driver)) {
                    this.addDriver(driver);
                }
            }
        }
    }
}