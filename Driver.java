import java.util.UUID;

public class Driver extends User implements Comparable<Driver> {
    // variables
    private boolean available;
    private double avgRating;
    private int rateCounter;
    private final int[] ratings;
    private UUID currentOrder;

    // constructor
    public Driver(UUID id, String name, String username, String email){
        super(id, name, username, email);
        this.ratings = new int[10];
        for(int i = 0; i < ratings.length; i++){
            ratings[i] = -1; // set empty ratings to -1
        }
        this.rateCounter = 0;
        this.avgRating = 0.0;
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
    public UUID getCurrentOrder() {
        return currentOrder;
    }
    public void setCurrentOrder(UUID currentOrder) {
        this.currentOrder = currentOrder;
    }

    // methods
    public void updateOrderStatus(OrderManager orderManager, OrderManager.Status status){
        orderManager.markStatus(currentOrder, status);
    }
    /**
     * Calculates the average rating from the ratings array... duh?
     */
    private void calcAvgRating(){
        int sum = 0;
        int count = 0;
        for(int rating : ratings){
            if(rating != -1){ // ignore empty ratings
                sum += rating;
                count++;
            }
        }
        avgRating = (count > 0) ? ((double) sum / count) : 0.0;
    }
    /**
     * Adds a new rating to the driver's rating history
     * Maintains only the last 10 ratings using a circular buffer
     * @param newRating Rating value (1-5)
     */
    public void addRating(int newRating){
        if (newRating < 1 || newRating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        ratings[rateCounter] = newRating;
        rateCounter = (rateCounter + 1) % ratings.length;
        calcAvgRating();
    }

    @Override
    public int compareTo(Driver other) {
        return Double.compare(other.avgRating, this.avgRating);
    }
}