import java.util.PriorityQueue;

public class DriverPool {
    private PriorityQueue<Driver> driverPool;

    /**
     * Constructor for DriverPool class
     */
    public DriverPool() {
        this.driverPool = new PriorityQueue<>();
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
        driver.setAvailable(false);
        this.driverPool.remove(driver);
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
     * Completes the delivery for a driver
     * @param driver
     * @param rating
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
}
