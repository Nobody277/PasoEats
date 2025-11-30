import java.util.ArrayList;
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
     * Gets the menu items for the restaurant from the file manager
     * @return ArrayList<MenuItem>
     */
    public ArrayList<MenuItem> getMenuItems(){
        //todo, broken!!
        return new ArrayList<>();
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
     * Returns a formatted string with restaurant details
     * @return String
     */
    public String detailsToString() {
        return String.format("%s - %s (%s)", restaurantId.toString(), name, category);
    }
}
