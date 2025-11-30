import java.util.ArrayList;
import java.util.UUID;

public class Restaurant {
    private String restaurantId;
    private String name;
    private String category;
    private ArrayList<MenuItem> menuItems;

    /**
     * Constructor for Restaurant class
     * @param restaurantId
     * @param name
     * @param category
     */
    public Restaurant(String restaurantId, String name, String category) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.category = category;
        this.menuItems = new ArrayList<>(); // HashMap
    }

    /**
     * Gets the restaurant ID
     * @return String
     */
    public String getRestaurantId() {
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
     * Gets the menu items for the restaurant
     * @return ArrayList<MenuItem>
     */
    public ArrayList<MenuItem> getMenuItems(){
        return menuItems;
    }

    /**
     * Gets a string representation of all menu items, seperated by new lines
     * @return String
     */
    public String getMenuItemsToString(){
        if (menuItems.isEmpty()) {
            return "No menu items available.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== All Menu Items ===\n");
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
        return restaurantId + ", " + name + ", " + category;
    }

    /* 
    // Parse from file line
    public static Restaurant createFromFileLine(String line) {
        if (line == null || line.trim().isEmpty() || line.startsWith("#")) {
            return null;
        }
        
        String[] parts = line.split(",");
        if (parts.length != 3) {
            return null;
        }
        
        return new Restaurant(
            parts[0].trim(),
            parts[1].trim(),
            parts[2].trim()
        );
    }
    


    public String detailsToString() {
        return String.format("%s - %s (%s)", restaurantId, name, category);
    }
    */


    public ArrayList<MenuItem> loadMenuItemsFromFile() {
        // TODO, load menu items associated with this restaurant from file
        // call the file manager
        return null;
    }
}
