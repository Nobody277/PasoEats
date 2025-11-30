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
     * Removes a driver from the pool
     * @param driver
     */
    public void removeDriver(Driver driver) {
        driver.setAvailable(false);
        this.driverPool.remove(driver);
    }

    /**
     * Retrieves and removes the next available driver from the pool
     * @return Driver
     */
    public Driver getNextAvailableDriver() {
        return driverPool.poll();
    }

    /**
     * Completes the delivery for a driver and updates their rating
     * @param driver
     * @param rating
     */
    public void completeDelivery(Driver driver, int rating) {
        driver.addRating(rating);
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
