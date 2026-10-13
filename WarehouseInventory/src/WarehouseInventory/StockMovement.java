package WarehouseInventory;


import java.time.LocalDateTime;

public class StockMovement {
    private int productId;
    private String type;
    private String details;
    private int quantity;
    private LocalDateTime timestamp;

    public StockMovement(int productId, String type,
                         String details, int quantity) {
        this.productId = productId;
        this.type = type;
        this.details = details;
        this.quantity = quantity;
        this.timestamp = LocalDateTime.now();
    }

    public void display() {
        System.out.println(
            timestamp + " | Product: " + productId
            + " | " + type
            + " | Qty: " + quantity
            + " | " + details
        );
    }
}
