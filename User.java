import java.util.ArrayList;
import java.util.UUID;

abstract class User {
    // variables
    private UUID id;
    private String name;
    private String username;
    private String email;
    private ArrayList<String> orders;
   
    // constructors
    public User(){
        orders = new ArrayList<>();
    }
    public User(UUID id, String name, String username, String email){
        orders = new ArrayList<>();

        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
    }

    // getters & setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public ArrayList<String> getOrders() {
        return orders;
    }
    public void setOrders(ArrayList<String> orders) {
        this.orders = orders;
    }

}

// User -> getRestraunts -> View Menus -> Select what they want -> Place Order -> Order Created -> Sent to Driver -> Order Status display [Accepted, in-progress, Picked Up, Delivered] -> User Notified -> Order History Updated -> User/driver can view past orders
// Order Statuses: Will be controlled by Order and Driver classes.

// Login Page : 1
// Admin : 2
//  Restaurants:
//      Mcdonalds : 1
//      Taco Bell : 2
//      Burger King : 3
// Customer : 3