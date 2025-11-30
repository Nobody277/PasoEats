import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileManager {
    private static final String MENU_FILE = "menu.txt";
    private static final String RESTAURANTS_FILE = "restaurants.txt";
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String DRIVERS_FILE = "drivers.txt";
    private static final String ADMINS_FILE = "admins.txt";
    private static final String ORDERS_FILE = "orders.txt";

    // In-memory storage
    private final Map<UUID, MenuItem> menuItems;
    private final List<MenuItem> menuItemsList;
    private final Map<UUID, Restaurant> restaurants;
    private final List<Restaurant> restaurantsList;
    private final Map<UUID, Customer> customers;
    private final Map<UUID, Driver> drivers;
    private final Map<UUID, Administrator> admins;
    private final Map<UUID, OrderData> orders;

    /**
     * Nested class to represent order data as stored in file
     */
    public static class OrderData {
        private final UUID orderId;
        private final UUID customerId;
        private final UUID restaurantId;
        private final List<UUID> itemIds;
        private String status;
        private UUID driverId;
        private final String createdAt;
        private final double totalPrice;

        public OrderData(UUID orderId, UUID customerId, UUID restaurantId, List<UUID> itemIds, String status, UUID driverId, String createdAt, double totalPrice) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.restaurantId = restaurantId;
            this.itemIds = itemIds;
            this.status = status;
            this.driverId = driverId;
            this.createdAt = createdAt;
            this.totalPrice = totalPrice;
        }

        // Getters & Setters for OrderData
        public UUID getOrderId() { return orderId; }
        public UUID getCustomerId() { return customerId; }
        public UUID getRestaurantId() { return restaurantId; }
        public List<UUID> getItemIds() { return itemIds; }
        public String getStatus() { return status; }
        public UUID getDriverId() { return driverId; }
        public String getCreatedAt() { return createdAt; }
        public double getTotalPrice() { return totalPrice; }
        public void setStatus(String status) { this.status = status; }
        public void setDriverId(UUID driverId) { this.driverId = driverId; }
    }

    /**
     * Constructor: Creates all files if they don't exist and loads data into memory
     */
    public FileManager() {
        menuItems = new HashMap<>();
        menuItemsList = new ArrayList<>();
        restaurants = new HashMap<>();
        restaurantsList = new ArrayList<>();
        customers = new HashMap<>();
        drivers = new HashMap<>();
        admins = new HashMap<>();
        orders = new HashMap<>();

        // Create files
        createFiles();

        // Load stuff into memory
        loadRestaurants();
        loadMenuItems();
        loadCustomers();
        loadDrivers();
        loadAdmins();
        loadOrders();
    }

    /**
     * Creates all the files and adds the headers for each file
     */
    private void createFiles() {
        createFile(RESTAURANTS_FILE, "# Format: RestaurantId, Name, Category");
        createFile(MENU_FILE, "# Format: ItemId, Name, Category, Price, RestaurantId");
        createFile(CUSTOMERS_FILE, "# Format: CustomerId, Username, Name, Email");
        createFile(DRIVERS_FILE, "# Format: DriverId, Username, Name, Email, Available, AvgRating");
        createFile(ADMINS_FILE, "# Format: AdminId, Username, Name, Email");
        createFile(ORDERS_FILE, "# Format: OrderId, CustomerId, RestaurantId, ItemIds, Status, DriverId, CreatedAt, TotalPrice");
    }

    private void createFile(String filename, String header) {
        try {
            if (!Files.exists(Paths.get(filename))) {
                Files.write(Paths.get(filename), Arrays.asList(header));
            }
        } catch (IOException e) {
            System.err.println("Error creating file " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Helper method to load data from a file using a parser function
     */
    private void loadFromFile(String filename, String entityName, java.util.function.Consumer<String> parser) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                parser.accept(line);
            }
        } catch (IOException e) {
            System.err.println("Error loading " + entityName + ": " + e.getMessage());
        }
    }

    /**
     * Loads restaurants so Brian is happy :D
     */
    private void loadRestaurants() {
        loadFromFile(RESTAURANTS_FILE, "restaurants", line -> {
            try {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    UUID restaurantId = UUID.fromString(parts[0].trim());
                    String name = parts[1].trim();
                    String category = parts[2].trim();
                    Restaurant restaurant = new Restaurant(restaurantId, name, category);
                    restaurants.put(restaurantId, restaurant);
                    restaurantsList.add(restaurant);
                }
            } catch (Exception e) { }
        });
    }

    /**
     * Load all the menu items so people can eat more food :D
     */
    private void loadMenuItems() {
        loadFromFile(MENU_FILE, "menuitems", line -> {
            try {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    UUID itemId = UUID.fromString(parts[0].trim());
                    String name = parts[1].trim();
                    String category = parts[2].trim();
                    BigDecimal price = new BigDecimal(parts[3].trim());
                    UUID restaurantId = UUID.fromString(parts[4].trim());
                    MenuItem item = new MenuItem(name, category, price.doubleValue(), restaurantId);
                    menuItems.put(itemId, item);
                    menuItemsList.add(item);
                }
            } catch (Exception e) { }
        });
    }

    /**
     * Loads customers so people can order food... duh?
     */
    private void loadCustomers() {
        loadFromFile(CUSTOMERS_FILE, "customers", line -> {
            try {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    UUID id = UUID.fromString(parts[0].trim());
                    String username = parts[1].trim();
                    String name = parts[2].trim();
                    String email = parts[3].trim();
                    Customer customer = new Customer(id, name, username, email);
                    customers.put(id, customer);
                }
            } catch (Exception e) { }
        });
    }

    /**
     * Guess what this one does?
     */
    private void loadDrivers() {
        loadFromFile(DRIVERS_FILE, "drivers", line -> {
            try {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    UUID id = UUID.fromString(parts[0].trim());
                    String username = parts[1].trim();
                    String name = parts[2].trim();
                    String email = parts[3].trim();
                    boolean available = Boolean.parseBoolean(parts[4].trim());
                    double avgRating = Double.parseDouble(parts[5].trim());
                    Driver driver = new Driver(id, name, username, email);
                    driver.setAvailable(available);
                    driver.setAvgRating(avgRating);
                    drivers.put(id, driver);
                }
            } catch (Exception e) { }
        });
    }

    /**
     * Connor... this is why I hate comments. Just read the code.
     */
    private void loadAdmins() {
        loadFromFile(ADMINS_FILE, "admins", line -> {
            try {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    UUID id = UUID.fromString(parts[0].trim());
                    String username = parts[1].trim();
                    String name = parts[2].trim();
                    String email = parts[3].trim();
                    Administrator admin = new Administrator(id, name, username, email);
                    admins.put(id, admin);
                }
            } catch (Exception e) { }
        });
    }

    /**
     * OMFG it I hate copy pasting this code!
     */
    private void loadOrders() {
        loadFromFile(ORDERS_FILE, "orders", line -> {
            try {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    UUID orderId = UUID.fromString(parts[0].trim());
                    UUID customerId = UUID.fromString(parts[1].trim());
                    UUID restaurantId = UUID.fromString(parts[2].trim());
                    List<UUID> itemIds = Arrays.stream(parts[3].trim().split(","))
                        .map(s -> UUID.fromString(s.trim()))
                        .collect(Collectors.toList());
                    String status = parts[4].trim();
                    UUID driverId = parts[5].trim().equals("null") ? null : UUID.fromString(parts[5].trim());
                    String createdAt = parts[6].trim();
                    double totalPrice = Double.parseDouble(parts[7].trim());
                    OrderData order = new OrderData(orderId, customerId, restaurantId, itemIds, status, driverId, createdAt, totalPrice);
                    orders.put(orderId, order);
                }
            } catch (Exception e) { }
        });
    }

    /**
     * WooHoo appending shit to files!
     */
    public boolean addRestaurant(UUID restaurantId, String name, String category) {
        if (restaurants.containsKey(restaurantId)) {
            System.err.println("Restaurant with ID " + restaurantId + " already exists... idiot.");
            return false;
        }
        Restaurant restaurant = new Restaurant(restaurantId, name, category);
        restaurants.put(restaurantId, restaurant);
        restaurantsList.add(restaurant);
        return appendToFile(RESTAURANTS_FILE, restaurantId + ", " + name + ", " + category);
    }

    /**
     * Deleting a restaurant and all its menu items.
     */
    public boolean removeRestaurant(UUID restaurantId) {
        if (!restaurants.containsKey(restaurantId)) {
            return false;
        }

        List<UUID> items = menuItemsList.stream()
            .filter(item -> item.getRestaurantId().equals(restaurantId))
            .map(MenuItem::getItemId)
            .collect(Collectors.toList());

        for (UUID itemId : items) {
            removeMenuItem(itemId);
        }

        // Update the memory version of the restaurant
        Restaurant restaurant = restaurants.remove(restaurantId);
        restaurantsList.remove(restaurant);

        //Update the file version of the restaurant
        return removeFromFile(RESTAURANTS_FILE, restaurantId.toString());
    }

    /**
     * Updating a restaurant crazy huh?
     */
    public boolean updateRestaurant(UUID restaurantId, String newName, String newCategory) {
        if (!restaurants.containsKey(restaurantId)) {
            return false;
        }
        Restaurant restaurant = restaurants.get(restaurantId);
        restaurant.setName(newName);
        restaurant.setCategory(newCategory);
        return updateInFile(RESTAURANTS_FILE, restaurantId.toString(), restaurantId + ", " + newName + ", " + newCategory);
    }

    public Restaurant getRestaurant(UUID restaurantId) {
        return restaurants.get(restaurantId);
    }

    public List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(restaurantsList);
    }

    /**
     * Make a new menu item and add it to the file.
     */
    public boolean addMenuItem(UUID itemId, String name, String category, BigDecimal price, UUID restaurantId) {
        if (!restaurants.containsKey(restaurantId)) {
            System.err.println("Restaurant with ID " + restaurantId + " does not exist... how did you get here?");
            return false;
        }
        if (menuItems.containsKey(itemId)) {
            System.err.println("Menu item with ID " + itemId + " already exists... how? just how?");
            return false;
        }
        MenuItem item = new MenuItem(name, category, price.doubleValue(), restaurantId);
        menuItems.put(itemId, item);
        menuItemsList.add(item);
        return appendToFile(MENU_FILE, itemId + ", " + name + ", " + category + ", " + price + ", " + restaurantId);
    }

    /**
     * Removing a menu item... who would've thought?
     */
    public boolean removeMenuItem(UUID itemId) {
        if (!menuItems.containsKey(itemId)) {
            return false;
        }
        MenuItem item = menuItems.remove(itemId);
        menuItemsList.remove(item);
        return removeFromFile(MENU_FILE, itemId.toString());
    }

    /**
     * Updating a menu item... crazy ik.
     */
    public boolean updateMenuItem(UUID itemId, String newName, String newCategory, BigDecimal newPrice, UUID newRestaurantId) {
        if (!menuItems.containsKey(itemId)) {
            return false;
        }
        if (!restaurants.containsKey(newRestaurantId)) {
            System.err.println("Restaurant with ID " + newRestaurantId + " does not exist");
            return false;
        }
        MenuItem item = menuItems.get(itemId);
        item.setName(newName);
        item.setCategory(newCategory);
        item.setPrice(newPrice.doubleValue());
        item.setRestaurantId(newRestaurantId);
        return updateInFile(MENU_FILE, itemId.toString(), itemId + ", " + newName + ", " + newCategory + ", " + newPrice + ", " + newRestaurantId);
    }

    public MenuItem getMenuItem(UUID itemId) {
        return menuItems.get(itemId);
    }

    public List<MenuItem> getMenuItemsForRestaurant(UUID restaurantId) {
        return menuItemsList.stream()
            .filter(item -> item.getRestaurantId().equals(restaurantId))
            .collect(Collectors.toList());
    }

    public List<MenuItem> getAllMenuItems() {
        return new ArrayList<>(menuItemsList);
    }

    /**
     * Yk what no more comments you lost your comment privileges.
     */
    public boolean addCustomer(UUID customerId, String username, String name, String email) {
        if (customers.containsKey(customerId)) {
            return false;
        }
        Customer customer = new Customer(customerId, name, username, email);
        customers.put(customerId, customer);
        return appendToFile(CUSTOMERS_FILE, customerId + ", " + username + ", " + name + ", " + email);
    }

    public boolean removeCustomer(UUID customerId) {
        if (!customers.containsKey(customerId)) {
            return false;
        }
        customers.remove(customerId);
        return removeFromFile(CUSTOMERS_FILE, customerId.toString());
    }

    public Customer getCustomer(UUID customerId) {
        return customers.get(customerId);
    }

    public Map<UUID, Customer> getAllCustomers() {
        return new HashMap<>(customers);
    }

    public boolean addDriver(UUID driverId, String username, String name, String email, boolean available) {
        if (drivers.containsKey(driverId)) {
            return false;
        }
        Driver driver = new Driver(driverId, name, username, email);
        driver.setAvailable(available);
        driver.setAvgRating(0.0);
        drivers.put(driverId, driver);
        return appendToFile(DRIVERS_FILE, driverId + ", " + username + ", " + name + ", " + email + ", " + available + ", 0.0");
    }

    public boolean removeDriver(UUID driverId) {
        if (!drivers.containsKey(driverId)) {
            return false;
        }
        drivers.remove(driverId);
        return removeFromFile(DRIVERS_FILE, driverId.toString());
    }

    public boolean updateDriver(UUID driverId, boolean available, double avgRating) {
        if (!drivers.containsKey(driverId)) {
            return false;
        }
        Driver driver = drivers.get(driverId);
        driver.setAvailable(available);
        driver.setAvgRating(avgRating);
        return updateDriverInFile(driverId, driver, available, avgRating);
    }

    public Driver getDriver(UUID driverId) {
        return drivers.get(driverId);
    }

    public Map<UUID, Driver> getAllDrivers() {
        return new HashMap<>(drivers);
    }

    public boolean addAdmin(UUID adminId, String username, String name, String email) {
        if (admins.containsKey(adminId)) {
            return false;
        }
        Administrator admin = new Administrator(adminId, name, username, email);
        admins.put(adminId, admin);
        return appendToFile(ADMINS_FILE, adminId + ", " + username + ", " + name + ", " + email);
    }

    public boolean removeAdmin(UUID adminId) {
        if (!admins.containsKey(adminId)) {
            return false;
        }
        admins.remove(adminId);
        return removeFromFile(ADMINS_FILE, adminId.toString());
    }

    public Administrator getAdmin(UUID adminId) {
        return admins.get(adminId);
    }

    public Map<UUID, Administrator> getAllAdmins() {
        return new HashMap<>(admins);
    }

    public boolean appendOrder(UUID orderId, UUID customerId, UUID restaurantId, List<UUID> itemIds, String status, UUID driverId, String createdAt, double totalPrice) {
        OrderData order = new OrderData(orderId, customerId, restaurantId, itemIds, status, driverId, createdAt, totalPrice);
        orders.put(orderId, order);
        String line = formatOrderLine(orderId, customerId, restaurantId, itemIds, status, driverId, createdAt, totalPrice);
        return appendToFile(ORDERS_FILE, line);
    }

    private String formatOrderLine(UUID orderId, UUID customerId, UUID restaurantId, List<UUID> itemIds, String status, UUID driverId, String createdAt, double totalPrice) {
        String itemIdsStr = itemIds.stream()
            .map(UUID::toString)
            .collect(Collectors.joining(","));
        String driverIdText = driverId == null ? "null" : driverId.toString();
        return orderId + ", " + customerId + ", " + restaurantId + ", " + itemIdsStr + ", " + status + ", " + driverIdText + ", " + createdAt + ", " + totalPrice;
    }

    public boolean updateOrder(UUID orderId, String newStatus, UUID driverId) {
        if (!orders.containsKey(orderId)) {
            return false;
        }
        OrderData order = orders.get(orderId);
        order.setStatus(newStatus);
        order.setDriverId(driverId);
        return updateOrderInFile(orderId, order);
    }

    public OrderData getOrder(UUID orderId) {
        return orders.get(orderId);
    }

    public Map<UUID, OrderData> getAllOrders() {
        return new HashMap<>(orders);
    }

    private boolean appendToFile(String filename, String line) {
        try {
            Files.write(Paths.get(filename), (line + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            System.err.println("Error appending to " + filename + ": " + e.getMessage());
            return false;
        }
    }

    private boolean processFileLines(String filename, String operation, java.util.function.Function<String, String> processor) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            List<String> newLines = new ArrayList<>();
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.startsWith("#") || trimmed.isEmpty()) {
                    newLines.add(line);
                    continue;
                }
                String processed = processor.apply(line);
                if (processed != null) {
                    newLines.add(processed);
                }
            }
            Files.write(Paths.get(filename), newLines);
            return true;
        } catch (IOException e) {
            System.err.println("Error " + operation + " " + filename + ": " + e.getMessage());
            return false;
        }
    }

    private boolean removeFromFile(String filename, String id) {
        return processFileLines(filename, "removing from", line -> {
            String trimmed = line.trim();
            if (trimmed.startsWith(id + ",") || trimmed.startsWith(id + " ")) {
                return null;
            }
            return line;
        });
    }

    private boolean updateInFile(String filename, String id, String newLine) {
        return updateInFile(filename, id, line -> newLine);
    }

    private boolean updateInFile(String filename, UUID id, java.util.function.Function<String, String> replacer) {
        return updateInFile(filename, id.toString(), replacer);
    }

    private boolean updateInFile(String filename, String id, java.util.function.Function<String, String> replacer) {
        return processFileLines(filename, "updating", line -> {
            String trimmed = line.trim();
            if (trimmed.startsWith(id + ",") || trimmed.startsWith(id + " ")) {
                return replacer.apply(line);
            }
            return line;
        });
    }

    private boolean updateDriverInFile(UUID driverId, Driver driver, boolean available, double avgRating) {
        String newLine = driverId + ", " + driver.getUsername() + ", " + driver.getName() + ", " + driver.getEmail() + ", " + available + ", " + avgRating;
        return updateInFile(DRIVERS_FILE, driverId, line -> newLine);
    }

    private boolean updateOrderInFile(UUID orderId, OrderData order) {
        String newLine = formatOrderLine(orderId, order.getCustomerId(), order.getRestaurantId(), order.getItemIds(), order.getStatus(), order.getDriverId(), order.getCreatedAt(), order.getTotalPrice());
        return updateInFile(ORDERS_FILE, orderId, line -> newLine);
    }

    // Tests
    public static void main(String[] args) {
        FileManager fm = new FileManager();
        
        UUID restaurant1 = UUID.randomUUID();
        UUID restaurant2 = UUID.randomUUID();
        fm.addRestaurant(restaurant1, "Burgers N Stuff", "Diner");
        fm.addRestaurant(restaurant2, "Coffee Star", "Coffee Shop");
        fm.addRestaurant(restaurant1, "Duplicate", "Test");
        fm.updateRestaurant(restaurant1, "Burgers N Stuff Updated", "Fast Food");
        
        UUID item1 = UUID.randomUUID();
        UUID item2 = UUID.randomUUID();
        UUID item3 = UUID.randomUUID();
        fm.addMenuItem(item1, "Classic Hamburger", "Burgers", new BigDecimal("5.99"), restaurant1);
        fm.addMenuItem(item2, "Cheeseburger", "Burgers", new BigDecimal("6.49"), restaurant1);
        fm.addMenuItem(item3, "Coffee", "Drinks", new BigDecimal("2.99"), restaurant2);
        fm.addMenuItem(UUID.randomUUID(), "Test", "Test", new BigDecimal("1.00"), UUID.randomUUID());
        fm.updateMenuItem(item1, "Classic Hamburger XL", "Burgers", new BigDecimal("7.99"), restaurant1);
        
        UUID customer1 = UUID.randomUUID();
        UUID customer2 = UUID.randomUUID();
        fm.addCustomer(customer1, "john_doe", "John Doe", "john@email.com");
        fm.addCustomer(customer2, "jane_smith", "Jane Smith", "jane@email.com");
        
        UUID driver1 = UUID.randomUUID();
        UUID driver2 = UUID.randomUUID();
        fm.addDriver(driver1, "driver_mike", "Mike Driver", "mike@email.com", true);
        fm.addDriver(driver2, "driver_sarah", "Sarah Driver", "sarah@email.com", false);
        fm.updateDriver(driver1, false, 4.5);
        
        UUID admin1 = UUID.randomUUID();
        fm.addAdmin(admin1, "admin_boss", "Boss Admin", "boss@email.com");
        
        UUID order1 = UUID.randomUUID();
        UUID order2 = UUID.randomUUID();
        List<UUID> order1Items = Arrays.asList(item1, item2);
        List<UUID> order2Items = Arrays.asList(item3);
        fm.appendOrder(order1, customer1, restaurant1, order1Items, "PLACED", null, "10/28/2025 1:15 PM", 12.48);
        fm.appendOrder(order2, customer2, restaurant2, order2Items, "PLACED", null, "10/28/2025 1:20 PM", 2.99);
        fm.updateOrder(order1, "ACCEPTED", driver1);
        
        fm.removeMenuItem(item2);
        fm.removeCustomer(customer2);
        fm.removeRestaurant(restaurant1);
    }
}