import java.util.ArrayDeque;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrderManager {
    public enum Status { PLACED, ACCEPTED, IN_PROGRESS, DELIVERED }

    public static final class Order { // Nested class
        private UUID id;
        private UUID customerId;
        private List<String> items;
        private Status status;
        private UUID assignedDriverId;
        private String createdAt;
        private double totalPrice;

        public Order() {}
        public Order(UUID id, UUID customerId, List<String> items, double totalPrice) {
            this.id = id;
            this.customerId = customerId;
            this.items = items;
            this.status = Status.PLACED;
            this.assignedDriverId = null;
            this.createdAt = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a").format(new Date());
            this.totalPrice = totalPrice;
        }

        public UUID getId() { 
            return this.id;
        }

        public UUID getCustomerId() { 
            return this.customerId;
        }

        public List<String> getItems() { 
            return this.items;
        }

        public Status getStatus() { 
            return this.status;
        }

        public UUID getAssignedDriverId() { 
            return this.assignedDriverId;
        }

        public String getCreatedAt() { 
            return this.createdAt;
        }
        
        public double getTotalPrice() {
            return this.totalPrice;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public void setAssignedDriverId(UUID driverId) {
            this.assignedDriverId = driverId;
        }
    }

    private final Map<UUID, Order> byId = new HashMap<>(); // Map to store orders by their ID
    private final ArrayDeque<UUID> intake = new ArrayDeque<>(); // Queue to store orders in the order they were placed

    public OrderManager() {}

    public Order place(UUID customerId, List<String> items, double totalPrice) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID can't be null");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items list can't be null or empty");
        }
        Order order = new Order(UUID.randomUUID(), customerId, items, totalPrice);
        byId.put(order.getId(), order);
        intake.add(order.getId());
        return order;
    }

    public Order acceptNext(UUID driverId) {
        if (driverId == null) {
            throw new IllegalArgumentException("Driver ID can't be null");
        }
        UUID nextOrderId = intake.poll();
        if (nextOrderId == null) { // No orders check
            return null;
        }
        Order order = byId.get(nextOrderId);
        order.setAssignedDriverId(driverId);
        order.setStatus(Status.ACCEPTED);
        return order;
    }

    public void markStatus(UUID orderId, Status newStatus) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID can't be null");
        }
        if (newStatus == null) {
            throw new IllegalArgumentException("Status can't be null");
        }
        Order order = byId.get(orderId);
        if (order != null) {
            order.setStatus(newStatus);
        }
    }

    public Order get(UUID orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID can't be null");
        }
        return byId.get(orderId);
    }

    public Map<UUID, Order> getAllOrders(){
        return byId;
    }
}