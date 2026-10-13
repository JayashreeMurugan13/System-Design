package WarehouseInventory;


import java.time.LocalDate;

public class PurchaseOrder {
    private static int nextId = 1001;

    private int orderId;
    private Supplier supplier;
    private Product product;
    private int quantity;
    private String status;
    private LocalDate orderDate;

    public PurchaseOrder(Supplier supplier, Product product,
                         int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException(
                    "Order quantity must be positive.");

        this.orderId = nextId++;
        this.supplier = supplier;
        this.product = product;
        this.quantity = quantity;
        this.status = "CREATED";
        this.orderDate = LocalDate.now();
    }

    public int getOrderId() { return orderId; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public String getStatus() { return status; }

    public boolean receiveStock(Warehouse warehouse) {
        if (!status.equals("CREATED"))
            return false;

        Product stored = warehouse.getProduct(product.getId());

        if (stored == null) {
            stored = new Product(product.getId(),
                    product.getName(), product.getPrice(),
                    0, product.getReorderLevel());
            warehouse.addProduct(stored);
        }

        stored.addStock(quantity);
        status = "RECEIVED";
        return true;
    }

    public void display() {
        System.out.println("\nOrder ID : " + orderId);
        System.out.println("Supplier : " + supplier.getName());
        System.out.println("Product  : " + product.getName());
        System.out.println("Quantity : " + quantity);
        System.out.println("Date     : " + orderDate);
        System.out.println("Status   : " + status);
    }
}
