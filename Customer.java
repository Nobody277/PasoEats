import java.util.List;
import java.util.UUID;

public class Customer extends User {
    // constructor
    public Customer(UUID id, String name, String username, String email){
        super(id, name, username, email);
    }

    // methods
    public String getRestraunts(RestaurantManager restaurantManager){
        return restaurantManager.getAllRestaurantsString();
    }
    public String getMenu(RestaurantManager restaurantManager, UUID restaurantId){
        Restaurant restaurant = restaurantManager.findRestaurantById(restaurantId);
        return restaurant.getMenuItemsToString(restaurantManager.getFileManager());
    }
    public void placeOrder(OrderManager orderManager, List<String> items){
        orderManager.place(id, items);
    }
    public void rateDriver(OrderManager orderManager, UserManager userManager, int rating){
        if (orders.isEmpty()) {
            return; // No orders to rate
        }
        OrderManager.Order order = orderManager.get(orders.get(orders.size() - 1)); // get most recent order
        UUID driverID = order.getAssignedDriverId(); // find most recent driver
        Driver driver = userManager.getDriver(driverID); 
        driver.addRating(rating); // add rating
    }
}