import java.util.ArrayList;

public class Restaurant {
    private String restaurantId;
    private String name;
    private String category;
    private ArrayList<MenuItem> menuItems;

    public Restaurant(String restaurantId, String name, String category) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.category = category;
        this.menuItems = new ArrayList<>(); // HashMap

    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Convert to file format
    public String toFileFormat() {
        return restaurantId + ", " + name + ", " + category;
    }

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

    public ArrayList<MenuItem> loadMenuItemsFromFile() {
        // TODO, load menu items associated with this restaurant from file
        return null;
    }
}
