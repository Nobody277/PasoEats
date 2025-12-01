import java.util.*;

public class OrderManager {
    public enum Status { PLACED, ACCEPTED, IN_PROGRESS, DELIVERED }

    public static final class Order { // Nested class
        private UUID id;
        private UUID customerId;
        private List<String> items;
        private Status status;
        private UUID assignedDriverId;
        private String createdAt;

        public Order() {}
        public Order(UUID id, UUID customerId, List<String> items) {
            this.id = id;
            this.customerId = customerId;
            this.items = items;
            this.status = Status.PLACED;
            this.assignedDriverId = null;
            this.createdAt = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a").format(new Date());
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

    public Order place(UUID customerId, List<String> items) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID can't be null");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items list can't be null or empty");
        }
        Order order = new Order(UUID.randomUUID(), customerId, items);
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

    public static void main(String[] args) {
        OrderManager manager = new OrderManager();
        UUID customerId = UUID.randomUUID();
        List<String> items = Arrays.asList("Item1", "Item2", "Item3");
        Order order = manager.place(customerId, items);
        System.out.println("Order placed: " + order.getId());
        UUID driverId = UUID.randomUUID();
        Order acceptedOrder = manager.acceptNext(driverId);
        System.out.println("Order accepted: " + acceptedOrder.getId());
    }

    /*
    Play by play for a customer placing an order:
    Customer C1 places an order: placeOrder(C1, ["Item1", "Item2", "Item3"]) which triggers OrderManager.place(C1, items)
    Which creates a new Order object with a random UUID for the ID (Oa) // Oa = a81bc81b-dead-4e5d-abff-90865d1e13b1
    customerId = C1.getId() // C1 = 123e4567-e89b-12d3-a456-426614174000
    status = Status.PLACED // PLACED = "PLACED"
    assignedDriverId = null (since no driver has accepted the order yet) // D1 = 123e4567-e89b-12d3-a456-426614174000
    createdAt = "10/28/2025 1:15" // The current date and time
    Store it in a map for tracking by its ID (byId) `byId.put(Oa, order)`
    Add the order ID to the intake queue (intake.add(Oa))

    What the data looks like:
    byId = { // (map)
        Oa: Order{id=Oa, customerId=C1, items=["Item1", "Item2", "Item3"], status=PLACED, assignedDriverId=null, createdAt="10/28/2025 1:15"}
    }
    // With more orders the map will look like this:
    byId = { // (map)
        Oa: Order{id=Oa, customerId=C1, items=["Item1", "Item2", "Item3"], status=PLACED, assignedDriverId=null, createdAt="10/28/2025 1:15"},
        Ob: Order{id=Ob, customerId=C2, items=["Item4", "Item5", "Item6"], status=PLACED, assignedDriverId=null, createdAt="10/28/2025 1:16"},
        Oc: Order{id=Oc, customerId=C3, items=["Item7", "Item8", "Item9"], status=PLACED, assignedDriverId=null, createdAt="10/28/2025 1:17"},
        Od: Order{id=Od, customerId=C4, items=["Item10", "Item11", "Item12"], status=PLACED, assignedDriverId=null, createdAt="10/28/2025 1:18"},
        Oe: Order{id=Oe, customerId=C5, items=["Item13", "Item14", "Item15"], status=PLACED, assignedDriverId=null, createdAt="10/28/2025 1:19"}
    }
    
    intake = [ Oa ] // (queue) FIFO (First In, First Out)
    // With more orders the queue will look like this: [ Oa, Ob, Oc, Od, Oe, Of, Og, Oh, Oi, Oj, Ok, Ol, Om, On, Oo, Op, Oq, Or, Os, Ot, Ou, Ov, Ow, Ox, Oy, Oz ]
    
    The order now exists in memory.
    It's waiting in the queue for a driver to accept it.
    The system can look it up anytime by its ID (Oa) in the map.
    Nothing else happens until a driver accepts it.

    Customer -> create order -> store in map -> enqueue id -> wait for driver
    */
}