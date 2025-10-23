import java.util.ArrayList;

abstract class User extends Order {
    private int id;
    private ArrayList<String> orders = new ArrayList<>();

    public int getId() {
        return id;
    }

    public ArrayList<String> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<String> orders) {
        this.orders = orders;
    }

    public void setId(int id) {
        this.id = id;
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