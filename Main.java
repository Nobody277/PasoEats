public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to PasoEats!");
        RestaurantManager manager = new RestaurantManager();
        System.out.println(manager.getAllRestaurantsString());
    }
}