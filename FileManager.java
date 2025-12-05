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
        restaurants.clear();
        restaurantsList.clear();
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
        menuItems.clear();
        menuItemsList.clear();
        loadFromFile(MENU_FILE, "menuitems", line -> {
            try {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    UUID itemId = UUID.fromString(parts[0].trim());
                    String name = parts[1].trim();
                    String category = parts[2].trim();
                    BigDecimal price = new BigDecimal(parts[3].trim());
                    UUID restaurantId = UUID.fromString(parts[4].trim());
                    MenuItem item = new MenuItem(name, category, price, restaurantId);
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
        customers.clear();
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
        drivers.clear();
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
        admins.clear();
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
        orders.clear();
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
    public boolean addRestaurant(String name, String category) {
        UUID restaurantId = UUID.randomUUID();
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
    public boolean addMenuItem(String name, String category, BigDecimal price, UUID restaurantId) {
        if (!restaurants.containsKey(restaurantId)) {
            System.err.println("Restaurant with ID " + restaurantId + " does not exist... how did you get here?");
            return false;
        }
        MenuItem item = new MenuItem(name, category, price, restaurantId);
        UUID itemId = item.getItemId();
        if (menuItems.containsKey(itemId)) {
            System.err.println("Menu item with ID " + itemId + " already exists... how? just how?");
            return false;
        }
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
        item.setPrice(newPrice);
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
    public boolean addCustomer(String username, String name, String email) {
        UUID customerId = UUID.randomUUID();
        if (customers.containsKey(customerId)) {
            return false;
        }
        // Check for duplicate username or email
        if (customerUsernameExists(username)) {
            System.err.println("Customer with username " + username + " already exists");
            return false;
        }
        if (customerEmailExists(email)) {
            System.err.println("Customer with email " + email + " already exists");
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

    public Customer getCustomerByUsername(String username) {
        for (Customer customer : customers.values()) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        return null;
    }

    public boolean customerUsernameExists(String username) {
        return getCustomerByUsername(username) != null;
    }

    public boolean customerEmailExists(String email) {
        for (Customer customer : customers.values()) {
            if (customer.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public Map<UUID, Customer> getAllCustomers() {
        return new HashMap<>(customers);
    }

    public boolean addDriver(String username, String name, String email, boolean available) {
        UUID driverId = UUID.randomUUID();
        if (drivers.containsKey(driverId)) {
            return false;
        }
        if (driverUsernameExists(username)) {
            System.err.println("Driver with username " + username + " already exists");
            return false;
        }
        if (driverEmailExists(email)) {
            System.err.println("Driver with email " + email + " already exists");
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

    public Driver getDriverByUsername(String username) {
        for (Driver driver : drivers.values()) {
            if (driver.getUsername().equals(username)) {
                return driver;
            }
        }
        return null;
    }

    public boolean driverUsernameExists(String username) {
        return getDriverByUsername(username) != null;
    }

    public boolean driverEmailExists(String email) {
        for (Driver driver : drivers.values()) {
            if (driver.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public Map<UUID, Driver> getAllDrivers() {
        return new HashMap<>(drivers);
    }

    public boolean addAdmin(String username, String name, String email) {
        UUID adminId = UUID.randomUUID();
        if (admins.containsKey(adminId)) {
            return false;
        }
        if (adminUsernameExists(username)) {
            System.err.println("Administrator with username " + username + " already exists");
            return false;
        }
        if (adminEmailExists(email)) {
            System.err.println("Administrator with email " + email + " already exists");
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

    public Administrator getAdminByUsername(String username) {
        for (Administrator admin : admins.values()) {
            if (admin.getUsername().equals(username)) {
                return admin;
            }
        }
        return null;
    }

    public boolean adminUsernameExists(String username) {
        return getAdminByUsername(username) != null;
    }

    public boolean adminEmailExists(String email) {
        for (Administrator admin : admins.values()) {
            if (admin.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
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
}