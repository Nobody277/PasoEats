import java.util.List;
import java.util.UUID;

public class RestaurantManager {
    private final FileManager fileManager;

    public RestaurantManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Gets all restaurants from FileManager's in-memory storage
     * @return List of all restaurants
     */
    public List<Restaurant> getRestaurants() {
        return fileManager.getAllRestaurants();
    }

    /**
     * Adds a new restaurant via FileManager
     * @param name Restaurant name
     * @param category Restaurant category
     * @return true if successful
     */
    public boolean addRestaurant(String name, String category) {
        if (name == null || name.trim().isEmpty() || category == null || category.trim().isEmpty()) {
            return false;
        }
        UUID newId = UUID.randomUUID();
        return fileManager.addRestaurant(newId, name.trim(), category.trim());
    }

    /**
     * Removes a restaurant via FileManager
     * @param restaurantId UUID of the restaurant to remove
     * @return true if successful
     */
    public boolean removeRestaurant(UUID restaurantId) {
        return fileManager.removeRestaurant(restaurantId);
    }

    /**
     * Finds a restaurant by ID via FileManager
     * @param restaurantId UUID of the restaurant
     * @return Restaurant if found, null otherwise
     */
    public Restaurant findRestaurantById(UUID restaurantId) {
        return fileManager.getRestaurant(restaurantId);
    }

    /**
     * Updates a restaurant's details
     * @param restaurantId UUID of the restaurant
     * @param newName New name
     * @param newCategory New category
     * @return true if successful
     */
    public boolean updateRestaurant(UUID restaurantId, String newName, String newCategory) {
        return fileManager.updateRestaurant(restaurantId, newName, newCategory);
    }

    public String modifyRestaurantMenuItem(UUID itemId) {
        // TODO
        // creates a new MenuItem object
        // adds it to the menuItems.txt file 
        // adds it to the restaurant's menu (or updates the menu from the file)        
        return null;
    }
}