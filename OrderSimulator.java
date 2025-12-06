import java.util.*;
import java.math.BigDecimal;

public class OrderSimulator {
    private final AppController appController;
    private boolean running;
    private Thread thread;
    private int frame = 0;
    private int lastLines = 0;

    public OrderSimulator(AppController appController) {
        this.appController = appController;
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        if (running) return;
        initData();
        running = true;
        thread = new Thread(this::loop);
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        running = false;
        try {
            if (thread != null) thread.join(1000);
        } catch (Exception e) {}
    }

    private void initData() {
        FileManager fm = appController.getFileManager();
        fm.reset();

        fm.addRestaurant("Burger Joint", "Burgers");
        fm.addRestaurant("Pasta Place", "Italian");
        fm.addRestaurant("Taco Time", "Mexican");

        for (Restaurant r : fm.getAllRestaurants()) {
            if (r.getName().equals("Burger Joint")) {
                fm.addMenuItem("Cheeseburger", "Main", new BigDecimal("9.99"), r.getRestaurantId());
                fm.addMenuItem("Fries", "Side", new BigDecimal("3.99"), r.getRestaurantId());
            } else if (r.getName().equals("Pasta Place")) {
                fm.addMenuItem("Spaghetti", "Main", new BigDecimal("12.99"), r.getRestaurantId());
                fm.addMenuItem("Garlic Bread", "Side", new BigDecimal("4.99"), r.getRestaurantId());
            } else {
                fm.addMenuItem("Taco", "Main", new BigDecimal("2.99"), r.getRestaurantId());
                fm.addMenuItem("Burrito", "Main", new BigDecimal("8.99"), r.getRestaurantId());
            }
        }

        for (int i = 1; i <= 5; i++) {
            fm.addDriver("driver" + i, "Driver " + i, "driver" + i + "@email.com", true);
            fm.addCustomer("customer" + i, "Customer " + i, "customer" + i + "@email.com");
        }
        
        appController.getDriverPool().updatePoolDrivers(fm);
    }

    private void loop() {
        Random rand = new Random();
        while (running) {
            try {
                Thread.sleep(200 + rand.nextInt(300));
                int event = rand.nextInt(100);
                if (event < 60) createOrder(rand);
                else if (event < 90) assignDriver(rand);
                else updateOrder(rand);
                displayStatus();
            } catch (Exception e) {}
        }
    }

    private void createOrder(Random rand) {
        try {
            Map<UUID, Customer> customerMap = appController.getFileManager().getAllCustomers();
            if (customerMap == null || customerMap.isEmpty()) return;
            List<Customer> customers = new ArrayList<>(customerMap.values());
            
            List<Restaurant> restaurants = appController.getAllRestaurants();
            if (restaurants.isEmpty()) return;

            Customer customer = customers.get(rand.nextInt(customers.size()));
            Restaurant restaurant = restaurants.get(rand.nextInt(restaurants.size()));
            List<MenuItem> menu = appController.getRestaurantManager().getMenuItemsForRestaurant(restaurant.getRestaurantId());
            
            if (menu.isEmpty()) return;

            int count = 1 + rand.nextInt(Math.min(3, menu.size()));
            List<String> items = new ArrayList<>();
            Collections.shuffle(menu);
            double total = 0;
            for (int i = 0; i < count; i++) {
                MenuItem item = menu.get(i);
                items.add(item.getItemId().toString());
                total += item.getPrice().doubleValue();
            }

            OrderManager.Order order = appController.getOrderManager().place(customer.getId(), items, total);
            
            if (order != null) {
                List<UUID> itemUuids = new ArrayList<>();
                for (String i : items) itemUuids.add(UUID.fromString(i));
                
                appController.getFileManager().appendOrder(
                    order.getId(),
                    order.getCustomerId(),
                    restaurant.getRestaurantId(),
                    itemUuids,
                    order.getStatus().toString(),
                    null,
                    order.getCreatedAt(),
                    total
                );

                if (!appController.getDriverPool().isEmpty()) {
                    Driver driver = appController.getDriverPool().getNextAvailableDriver();
                    if (driver != null) {
                        OrderManager.Order accepted = appController.getOrderManager().acceptNext(driver.getId());
                        if (accepted != null) {
                            driver.setCurrentOrder(accepted.getId());
                            driver.setAvailable(false);
                            appController.getFileManager().updateDriver(driver.getId(), false, driver.getAvgRating());
                        }
                    }
                }
            }
        } catch (Exception e) {}
    }

    /**
     * Gets all drivers from FileManager
     * @return List of all drivers, or empty list if none found
     */
    private List<Driver> getAllDrivers() {
        Map<UUID, Driver> driverMap = appController.getFileManager().getAllDrivers();
        if (driverMap == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(driverMap.values());
    }

    /**
     * Gets available drivers (available and no current order)
     * @return List of available drivers
     */
    private List<Driver> getAvailableDrivers() {
        List<Driver> allDrivers = getAllDrivers();
        List<Driver> available = new ArrayList<>();
        for (Driver driver : allDrivers) {
            if (driver.isAvailable() && driver.getCurrentOrder() == null) {
                available.add(driver);
            }
        }
        return available;
    }

    /**
     * Gets drivers with active orders
     * @return List of drivers with current orders
     */
    private List<Driver> getActiveDrivers() {
        List<Driver> allDrivers = getAllDrivers();
        List<Driver> active = new ArrayList<>();
        for (Driver driver : allDrivers) {
            if (driver.getCurrentOrder() != null) {
                active.add(driver);
            }
        }
        return active;
    }

    private void assignDriver(Random rand) {
        try {
            List<Driver> available = getAvailableDrivers();
            if (available.isEmpty()) return;
            
            Driver driver = available.get(rand.nextInt(available.size()));
            OrderManager.Order order = appController.getOrderManager().acceptNext(driver.getId());
            
            if (order != null) {
                driver.setCurrentOrder(order.getId());
                driver.setAvailable(false);
                appController.getFileManager().updateDriver(driver.getId(), false, driver.getAvgRating());
            }
        } catch (Exception e) {}
    }

    private void updateOrder(Random rand) {
        try {
            List<Driver> active = getActiveDrivers();
            if (active.isEmpty()) return;

            Driver driver = active.get(rand.nextInt(active.size()));
            OrderManager.Order order = appController.getOrderManager().get(driver.getCurrentOrder());

            if (order != null) {
                if (order.getStatus() == OrderManager.Status.ACCEPTED) {
                    driver.updateOrderStatus(appController.getOrderManager(), OrderManager.Status.IN_PROGRESS);
                } else if (order.getStatus() == OrderManager.Status.IN_PROGRESS) {
                    driver.updateOrderStatus(appController.getOrderManager(), OrderManager.Status.DELIVERED);
                    
                    int rating = 1 + rand.nextInt(5);
                    driver.addRating(rating);
                    
                    driver.setCurrentOrder(null);
                    driver.setAvailable(true);
                    appController.getFileManager().updateDriver(driver.getId(), true, driver.getAvgRating());
                    appController.getDriverPool().updatePoolDrivers(appController.getFileManager());
                }
            }
        } catch (Exception e) {}
    }

    public void displayStatus() {
        try {
            Map<UUID, OrderManager.Order> orderMap = appController.getOrderManager().getAllOrders();
            if (orderMap == null) return;

            int placed = 0, accepted = 0, inProgress = 0, delivered = 0;
            for (OrderManager.Order o : orderMap.values()) {
                if (o.getStatus() == OrderManager.Status.PLACED) placed++;
                else if (o.getStatus() == OrderManager.Status.ACCEPTED) accepted++;
                else if (o.getStatus() == OrderManager.Status.IN_PROGRESS) inProgress++;
                else if (o.getStatus() == OrderManager.Status.DELIVERED) delivered++;
            }
            
            int available = 0;
            StringBuilder sb = new StringBuilder();
            String[] anim = {"|", "/", "-", "\\"};
            
            List<Driver> drivers = getAllDrivers();
            for (Driver driver : drivers) {
                if (driver.isAvailable()) available++;
                
                String status = getDriverStatus(driver);
                if (!status.equals("Idle")) {
                    sb.append("\n").append(driver.getName())
                      .append(" (\u001B[33m").append(String.format("%.1f", driver.getAvgRating()))
                      .append("\u001B[0m): ").append(status).append(" ").append(anim[frame % 4]);
                }
            }

            String statusLine = "Orders: [Placed:" + placed + " Accepted:" + accepted + " In Progress:" + inProgress + " Delivered:" + delivered + "]\n" + "Drivers Available: " + available + sb.toString();

            StringBuilder out = new StringBuilder();
            if (lastLines > 0) {
                out.append("\033[").append(lastLines).append("A\033[J");
            }
            out.append(statusLine);
            System.out.println(out.toString());
            
            lastLines = 1;
            for (int i = 0; i < statusLine.length(); i++) {
                if (statusLine.charAt(i) == '\n') lastLines++;
            }
            
            frame++;
        } catch (Exception e) {}
    }

    /**
     * Gets the current status string for a driver based on their order
     * @param driver Driver to check
     * @return Status string ("Idle", "Picking up", or "Delivering")
     */
    private String getDriverStatus(Driver driver) {
        if (driver.getCurrentOrder() == null) {
            return "Idle";
        }
        OrderManager.Order order = appController.getOrderManager().get(driver.getCurrentOrder());
        if (order == null) {
            return "Idle";
        }
        if (order.getStatus() == OrderManager.Status.ACCEPTED) {
            return "Picking up";
        } else if (order.getStatus() == OrderManager.Status.IN_PROGRESS) {
            return "Delivering";
        }
        return "Idle";
    }
}