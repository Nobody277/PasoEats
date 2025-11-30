import java.util.PriorityQueue;

public class DriverPool {
    private PriorityQueue<Driver> driverPool;

    public DriverPool() {
        this.driverPool = new PriorityQueue<>();
    }

    public boolean addDriver(Driver driver) {
        return driverPool.offer(driver);
    }

    public boolean removeDriver(Driver driver) {
        return driverPool.remove(driver);
    }

    public Driver getNextAvailableDriver() {
        return driverPool.poll();
    }

    public boolean completeDelivery(Driver driver, double rating) {// TODO, add rating set with driver method
    }

    public int getPoolSize() {
        return driverPool.size();
    }

    public boolean isEmpty() {
        return driverPool.isEmpty();
    }
}
