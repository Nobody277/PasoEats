import java.util.UUID;

public class RestaurantManager {
    private final UUID restaurantId;
    private String name;
    private String category;

    public RestaurantManager(String name, String category) {
        this.restaurantId = UUID.randomUUID();
        this.name = name;
        this.category = category;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean addRestaurant(String name, String category) {
        // TODO
        return false;
    }

    public boolean deleteRestaurant(UUID restaurantId) {
        // TODO
        return false;
    }

    public String modifyRestaurantMenuItem(UUID restaurantId, UUID item) {
        // TODO
        return null;
    }

    public String getAllRestaurantsString() {
        // TODO
        return null;
    }

}