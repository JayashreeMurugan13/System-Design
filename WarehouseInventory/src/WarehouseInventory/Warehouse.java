package WarehouseInventory;


import java.util.*;

public class Warehouse {
    private int id;
    private String name;
    private Map<Integer, Product> products = new LinkedHashMap<>();

    public Warehouse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public void addProduct(Product p) {
        products.put(p.getId(), p);
    }

    public Product getProduct(int productId) {
        return products.get(productId);
    }

    public Collection<Product> getProducts() {
        return products.values();
    }

    public boolean transferStock(Warehouse destination,
                                 int productId, int qty) {
        Product source = getProduct(productId);

        if (source == null || qty <= 0 ||
            source.getQuantity() < qty)
            return false;

        Product target = destination.getProduct(productId);

        if (target == null) {
            target = new Product(productId, source.getName(),
                    source.getPrice(), 0, source.getReorderLevel());
            destination.addProduct(target);
        }

        source.removeStock(qty);
        target.addStock(qty);
        return true;
    }

    public void displayProducts() {
        System.out.println("\nWarehouse: " + name);
        System.out.printf("%-6s %-18s %-10s %-10s %-10s%n",
                "ID", "Product", "Price", "Quantity", "Reorder");
        for (Product p : products.values())
            p.display();
    }
}
