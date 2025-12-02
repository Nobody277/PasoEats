import java.util.UUID;

public class MenuItem {
    private UUID itemId;
    private String name;
    private String category;
    private double price;
    private UUID restaurantId;

    /*
     * Constructor for MenuItem class
     * @param name of the menu item
     * @param category of the menu item
     * @param price of the menu item
     * @param restaurantId of the menu item
     */
    public MenuItem(String name, String category, double price, UUID restaurantId) {
        this.itemId = UUID.randomUUID();
        this.name = name;
        this.category = category;
        this.price = price;
        this.restaurantId = restaurantId;
    }

    // getters and setters
    /**
     * Gets the name of the menu item
     * @return name of the menu item
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the menu item
     * @param name of the menu item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the category of the menu item
     * @return category of the menu item
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the menu item
     * @param category of the menu item
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the price of the menu item
     * @return price of the menu item
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the menu item
     * @param price of the menu item
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the item ID of the menu item
     * @return item ID of the menu item
     */
    public UUID getItemId() {
        return itemId;
    }

    /**
     * Gets the restaurant ID associated with the menu item
     * @return restaurant ID of the menu item
     */
    public UUID getRestaurantId() {
        return restaurantId;
    }

    /**
     * Sets the restaurant ID associated with the menu item
     * @param restaurantId of the menu item
     */
    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    // Behavior methods
    /**
     * Convert to string format for display
     * @return String
     */
    public String detailsToString() {
        return String.format("%s - %s (%s)", itemId, name, price);
    }
}
