import java.util.UUID;
// UUID uuid = UUID.randomUUID();

public abstract class Order {
    private int orderId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public abstract <T> T createOrder(); // Returns an order id and a UUID
}