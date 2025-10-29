import java.util.*;

public class Orders {
    public enum Status { PLACED, ACCEPTED, IN_PROGRESS, DELIVERED }

    public static final class Order { // Nested class
        private UUID id;
        private UUID customerId;
        private List<String> items;
        private Status status;
        private UUID assignedDriverId;
        private String createdAt;

        public Order() {}
        public Order(UUID id, UUID customerId, List<String> items) {}

        public UUID getId() { return this.id; }
        public UUID getCustomerId() { return this.customerId; }
        public List<String> getItems() { return this.items; }
        public Status getStatus() { return this.status; }
        public UUID getAssignedDriverId() { return this.assignedDriverId; }
        public String getCreatedAt() { return this.createdAt; }

        public void setStatus(Status status) {}
        public void setAssignedDriverId(UUID driverId) {}
    }

    private final Map<UUID, Order> byId = new HashMap<>(); // Map to store orders by their ID
    private final ArrayDeque<UUID> intake = new ArrayDeque<>(); // Queue to store orders in the order they were placed

    public Orders() {}

    public Order place(UUID customerId, List<String> items) { return null; } // Customer places an order

    public Order acceptNext(UUID driverId) { return null; } // Driver accepts the next order

    public void markStatus(UUID orderId, Status newStatus) {} // Mark the status of an order

    public Order get(UUID orderId) { return null; } // Get an order by its ID

    /*
    Play by play for a customer placing an order:
    Customer C1 places an order: placeOrder(C1, ["Item1", "Item2", "Item3"]) which triggers Orders.place(C1, items)
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