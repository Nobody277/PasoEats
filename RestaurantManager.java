import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class RestaurantManager {
    private final FileManager fileManager;

    public RestaurantManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Gets the FileManager instance
     * @return FileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * Gets all restaurants via FileManager
     * @return List of all restaurants
     */
    public List<Restaurant> getRestaurants() {
        return this.getFileManager().getAllRestaurants();
    }

    /**
     * Adds a new random UUID restaurant via FileManager
     * @param name Restaurant name
     * @param category Restaurant category
     * @return true if successful
     */
    public boolean addRestaurant(String name, String category) {
        if (name == null || name.trim().isEmpty() || category == null || category.trim().isEmpty()) {
            return false;
        } //bad name or category
        UUID newId = UUID.randomUUID();
        return this.getFileManager().addRestaurant(newId, name.trim(), category.trim());
    }

    /**
     * Removes a restaurant via FileManager
     * @param restaurantId UUID of the restaurant to remove
     * @return true if successful
     */
    public boolean removeRestaurant(UUID restaurantId) {
        return this.getFileManager().removeRestaurant(restaurantId);
    }

    /**
     * Finds a restaurant by ID via FileManager
     * @param restaurantId UUID of the restaurant
     * @return Restaurant if found, null otherwise
     */
    public Restaurant findRestaurantById(UUID restaurantId) {
        return this.getFileManager().getRestaurant(restaurantId);
    }

    /**
     * Updates a restaurant's details via FileManager
     * @param restaurantId UUID of the restaurant
     * @param newName New name
     * @param newCategory New category
     * @return true if successful
     */
    public boolean updateRestaurant(UUID restaurantId, String newName, String newCategory) {
        return this.getFileManager().updateRestaurant(restaurantId, newName, newCategory);
    }

    /**
     * Adds a new menu item for a restaurant via FileManager
     * @param name Menu item name
     * @param category Menu item category
     * @param price Menu item price
     * @return true if successful
     */
    public boolean addRestaurantMenuItem(String name, String category, BigDecimal price, UUID restaurantId) {
        return this.getFileManager().addMenuItem(restaurantId, name, category, price, restaurantId);
    }

    /**
     * Deletes a menu item for a restaurant via FileManager
     * @param itemId UUID of the menu item
     * @return true if successful
     */
    public boolean deleteRestaurantMenuItem(UUID itemId) {
        return this.getFileManager().removeMenuItem(itemId);
    }

    /**
     * Modifies a menu item for a restaurant via FileManager
     * @param itemId UUID of the menu item
     * @param newName New name
     * @param newCategory New category
     * @param newPrice New price
     * @param newRestaurantId New restaurant ID
     * @return true if successful
     */
    public boolean modifyRestaurantMenuItem(UUID itemId, String newName, String newCategory, BigDecimal newPrice, UUID newRestaurantId) {
        return this.getFileManager().updateMenuItem(itemId, newName, newCategory, newPrice, newRestaurantId);
    }

    /**
     * Gets a formatted string of all restaurants
     * @return Formatted string of all restaurants
     */
    public String getAllRestaurantsString() {
        List<Restaurant> restaurants = this.getFileManager().getAllRestaurants();
        if (restaurants.isEmpty()) {
            return "No restaurants available.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== All Restaurants ===\n");
        for (Restaurant restaurant : restaurants) {
            sb.append(restaurant.detailsToString()).append("\n");
        }
        return sb.toString();
    }
}