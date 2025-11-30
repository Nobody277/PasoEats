import java.util.List;
import java.util.UUID;

public class Restaurant {
    private UUID restaurantId;
    private String name;
    private String category;

    /**
     * Constructor for Restaurant class
     * @param restaurantId UUID
     * @param name
     * @param category
     */
    public Restaurant(UUID restaurantId, String name, String category) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.category = category;
    }

    /**
     * Gets the restaurant ID
     * @return UUID
     */
    public UUID getRestaurantId() {
        return restaurantId;
    }

    /**
     * Gets the restaurant name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the restaurant category
     * @return String
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the menu items for this restaurant from FileManager
     * @param fileManager The FileManager instance to query
     * @return List of MenuItems for this restaurant
     */
    public List<MenuItem> getMenuItems(FileManager fileManager) {
        return fileManager.getMenuItemsForRestaurant(this.restaurantId);
    }

    /**
     * Gets a string representation of all menu items for this restaurant
     * @param fileManager The FileManager instance to query
     * @return String with all menu items
     */
    public String getMenuItemsToString(FileManager fileManager) {
        List<MenuItem> menuItems = fileManager.getMenuItemsForRestaurant(this.restaurantId);
        
        if (menuItems.isEmpty()) {
            return "No menu items available.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== Menu Items for ").append(name).append(" ===\n");
        for (MenuItem menuItem : menuItems) {
            sb.append(menuItem.detailsToString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Sets the restaurant name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the restaurant category
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Convert to file format
     * @return String
     */
    public String toFileFormat() {
        return restaurantId.toString() + ", " + name + ", " + category;
    }

    /**
     * Returns a formatted string with restaurant details
     * @return String
     */
    public String detailsToString() {
        return String.format("%s - %s (%s)", restaurantId.toString(), name, category);
    }
}
