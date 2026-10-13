package WarehouseInventory;
public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private int reorderLevel;

    public Product(int id, String name, double price,
                   int quantity, int reorderLevel) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getReorderLevel() { return reorderLevel; }

    public void addStock(int qty) {
        if (qty <= 0)
            throw new IllegalArgumentException("Quantity must be positive.");
        quantity += qty;
    }

    public boolean removeStock(int qty) {
        if (qty <= 0 || qty > quantity)
            return false;
        quantity -= qty;
        return true;
    }

    public boolean isLowStock() {
        return quantity <= reorderLevel;
    }

    public void display() {
        System.out.printf("%-6d %-18s %-10.2f %-10d %-10d%n",
                id, name, price, quantity, reorderLevel);
    }
}

