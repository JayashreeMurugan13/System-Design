package WarehouseInventory;


public class Shipment {
    private static int nextId = 5001;

    private int shipmentId;
    private String destination;
    private String status;
    private int productId;
    private int quantity;

    public Shipment(String destination, int productId, int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException(
                    "Shipment quantity must be positive.");

        this.shipmentId = nextId++;
        this.destination = destination;
        this.productId = productId;
        this.quantity = quantity;
        this.status = "CREATED";
    }

    public boolean dispatch(Warehouse warehouse) {
        Product product = warehouse.getProduct(productId);

        if (product == null || !product.removeStock(quantity))
            return false;

        status = "DISPATCHED";
        return true;
    }

    public void display() {
        System.out.println("\nShipment ID : " + shipmentId);
        System.out.println("Product ID  : " + productId);
        System.out.println("Quantity    : " + quantity);
        System.out.println("Destination : " + destination);
        System.out.println("Status      : " + status);
    }
}
