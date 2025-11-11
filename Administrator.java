import java.util.UUID;

public class Administrator extends User {
    // constructor
    public Administrator(UUID id, String name, String username, String email){
        super(id, name, username, email);
    }

    // methods
    public void manageRestaurants(){
        //TODO
    }
    public void viewAllOrders(){
        //TODO optional
    }
}
