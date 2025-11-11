import java.util.UUID;

public class Driver extends User {
    // variables
    private boolean available;
    private double avgRating;
    private int rateCounter;
    private int[] ratings;
    private UUID currentOrder;

    // constructor
    public Driver(UUID id, String name, String username, String email){
        super(id, name, username, email);
        ratings = new int[10];
        rateCounter = 0;
        avgRating = 0;
    }
    
    // getters & setters
    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
    public double getAvgRating() {
        return avgRating;
    }
    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }
    public int getRateCounter() {
        return rateCounter;
    }
    public void setRateCounter(int rateCounter) {
        this.rateCounter = rateCounter;
    }
    public int[] getRatings() {
        return ratings;
    }
    public void setRatings(int[] ratings) {
        this.ratings = ratings;
    }
    public UUID getCurrentOrder() {
        return currentOrder;
    }
    public void setCurrentOrder(UUID currentOrder) {
        this.currentOrder = currentOrder;
    }

    // methods
    public void updateOrder(){
        //TODO
    }
    public void calcAvgRating(){
        //TODO
    }
    public void addRating(int newRating){
        ratings[rateCounter] = newRating;
        rateCounter = (rateCounter + 1) % ratings.length;
        calcAvgRating();
    }
}