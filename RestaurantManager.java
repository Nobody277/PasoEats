import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class RestaurantManager {
    private static final String RESTAURANTS_FILE = "restaurants.txt";
    private ArrayList<Restaurant> restaurants; //HashMap
    private int nextIdNumber;

    public RestaurantManager() {
        this.restaurants = new ArrayList<>();
        loadRestaurantsFromFile();
    }

    /**
     * Loads all restaurants from the file into memory
     */
    private void loadRestaurantsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RESTAURANTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Restaurant restaurant = Restaurant.createFromFileLine(line);
                if (restaurant != null) {
                    restaurants.add(restaurant);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Restaurants file not found.");
        } catch (IOException e) {
            System.err.println("Error loading restaurants: " + e.getMessage());
        }
    }

    /**
     * Saves all restaurants back to the file
     */
    private boolean saveRestaurantsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESTAURANTS_FILE))) {
            writer.write("# Format: RestaurantId, Name, Category\n"); // header
            for (Restaurant restaurant : restaurants) {
                writer.write(restaurant.toFileFormat() + "\n");
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving restaurants: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generates a new unique restaurant ID
     */
    private String generateRestaurantId() {
        return UUID.randomUUID().toString();
    }

    public ArrayList<Restaurant> getRestaurants() {
        return new ArrayList<>(restaurants); // Return copy to prevent external modification
    }

    public boolean addRestaurant(String name, String category) {
        if (name == null || name.trim().isEmpty() || category == null || category.trim().isEmpty()) {
            return false;
        }

        String newId = generateRestaurantId();
        Restaurant newRestaurant = new Restaurant(newId, name.trim(), category.trim());
        restaurants.add(newRestaurant);
        
        return saveRestaurantsToFile();
    }

    public boolean removeRestaurant(String restaurantId) {
        Restaurant toRemove = null;
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getRestaurantId().equals(restaurantId)) {
                toRemove = restaurant;
                break;
            }
        }
        
        if (toRemove != null) {
            restaurants.remove(toRemove);
            return saveRestaurantsToFile();
        }
        
        return false;
    }

    public Restaurant findRestaurantById(String restaurantId) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getRestaurantId().equals(restaurantId)) {
                return restaurant;
            }
        }
        return null;
    }

    public String modifyRestaurantMenuItem(UUID itemId) {
        // TODO
        // creates a new MenuItem object
        // adds it to the menuItems.txt file 
        // adds it to the restaurant's menu (or updates the menu from the file)        
        return null;
    }

    public String getAllRestaurantsString() {
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