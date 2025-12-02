import java.util.UUID;

public class MenuItem {
    private UUID itemId;
    private String name;
    private String category;
    private double price;
    private UUID restaurantId;

    public MenuItem(String name, String category, double price, UUID restaurantId) {
        this.itemId = UUID.randomUUID();
        this.name = name;
        this.category = category;
        this.price = price;
        this.restaurantId = restaurantId;
    }

    // getters and setters
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public UUID getItemId() {
        return itemId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    // Behavior methods
    public String detailsToString() {
        return String.format("%s - %s (%s)", itemId, name, price);
    }
}
