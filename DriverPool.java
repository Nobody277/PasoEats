import java.util.PriorityQueue;

public class DriverPool {
    private PriorityQueue<Driver> driverPool;

    public DriverPool() {
        this.driverPool = new PriorityQueue<>();
    }

    public boolean addDriver(Driver driver) { // TODO, lets talk about ratings
        return driverPool.offer(driver);
    }

    public boolean removeDriver(Driver driver) {// TODO, lets talk about ratings
        return driverPool.remove(driver);
    }

    public Driver getNextAvailableDriver() {
        return driverPool.poll();
    }

    public boolean completeDelivery(Driver driver, double rating) {// TODO, lets talk about ratings
        return driverPool.offer(driver);
    }

    public int getPoolSize() {
        return driverPool.size();
    }

    public boolean isEmpty() {
        return driverPool.isEmpty();
    }

    public boolean updatePool() {
        // TODO, not sure what is intended here. Lets chat about it
        return false;
    }
}
