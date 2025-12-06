import java.util.ArrayList;
import java.util.UUID;

abstract class User {
    // variables
    protected UUID id;
    protected String name;
    protected String username;
    protected String email;
    protected ArrayList<UUID> orders;
   
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
    /**
     * Gets a copy of the orders list
     * @return Copy of the orders list
     */
    public ArrayList<UUID> getOrders() {
        return new ArrayList<>(orders);
    }
    
    /**
     * Sets the orders list (replaces existing orders)
     * @param orders New orders list
     */
    public void setOrders(ArrayList<UUID> orders) {
        this.orders = new ArrayList<>(orders);
    }
    public void addOrder(UUID orderID){
        orders.add(orderID);
    }
}