import java.util.ArrayList;
import java.util.UUID;

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
    public Restaurant(UUID restaurantId, String name, String category) {
        this.restaurantId = restaurantId.toString();
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

    public ArrayList<MenuItem> getMenuItems(){
        return menuItems;
    }

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
