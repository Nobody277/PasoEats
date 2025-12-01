import java.util.Map;
import java.util.UUID;

public class Administrator extends User {
    // constructor
    public Administrator(UUID id, String name, String username, String email){
        super(id, name, username, email);
    }

    // methods
    public void manageRestaurants(){
        //TODO
        // open a CLI menu to manage restaurants in
    }
    public Map<UUID, OrderManager.Order> getAllOrders(OrderManager orderManager){
        return orderManager.getAllOrders();
    }
}
