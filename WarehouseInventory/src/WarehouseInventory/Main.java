package WarehouseInventory;

import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);

    static Map<Integer, Warehouse> warehouses = new LinkedHashMap<>();
    static Map<Integer, Product> catalogue = new LinkedHashMap<>();
    static Map<Integer, Supplier> suppliers = new LinkedHashMap<>();

    static List<PurchaseOrder> orders = new ArrayList<>();
    static List<Shipment> shipments = new ArrayList<>();
    static List<StockMovement> movements = new ArrayList<>();

    static int nextProductId = 101;

    static int readInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    static double readDouble(String message) {
        while (true) {
            System.out.print(message);
            try {
                double value = Double.parseDouble(sc.nextLine().trim());
                if (Double.isFinite(value) && value >= 0)
                    return value;
            } catch (NumberFormatException e) {
                // Handle invalid input below.
            }
            System.out.println("Enter a valid non-negative price.");
        }
    }

    static String readText(String message) {
        System.out.print(message);
        return sc.nextLine().trim();
    }

    static Warehouse findWarehouse() {
        int id = readInt("Enter warehouse ID: ");
        Warehouse w = warehouses.get(id);
        if (w == null)
            System.out.println("Warehouse not found.");
        return w;
    }

    static void addProduct() {
        String name = readText("Product name: ");
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        double price = readDouble("Price: ");
        int qty = readInt("Initial quantity: ");
        int reorder = readInt("Reorder level: ");

        if (qty < 0 || reorder < 0) {
            System.out.println("Quantities cannot be negative.");
            return;
        }

        Warehouse w = findWarehouse();
        if (w == null) return;

        int id = nextProductId++;
        Product p = new Product(id, name, price, 0, reorder);
        catalogue.put(id, p);

        Product stock = new Product(id, name, price, qty, reorder);
        w.addProduct(stock);

        movements.add(new StockMovement(
                id, "ADD PRODUCT", w.getName(), qty));

        System.out.println("Product created. ID: " + id);
    }

    static void viewProducts() {
        for (Warehouse w : warehouses.values())
            w.displayProducts();
    }

    static void addStock() {
        Warehouse w = findWarehouse();
        if (w == null) return;

        int id = readInt("Product ID: ");
        Product p = w.getProduct(id);

        if (p == null) {
            System.out.println("Product not found in warehouse.");
            return;
        }

        int qty = readInt("Quantity to add: ");
        if (qty <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        p.addStock(qty);
        movements.add(new StockMovement(
                id, "STOCK RECEIVED", w.getName(), qty));

        System.out.println("Stock added successfully.");
    }

    static void transferStock() {
        Warehouse source = findWarehouse();
        if (source == null) return;

        Warehouse destination = findWarehouse();
        if (destination == null) return;

        if (source.getId() == destination.getId()) {
            System.out.println("Choose different warehouses.");
            return;
        }

        int id = readInt("Product ID: ");
        int qty = readInt("Quantity to transfer: ");

        if (source.transferStock(destination, id, qty)) {
            movements.add(new StockMovement(
                    id, "TRANSFER",
                    source.getName() + " -> " + destination.getName(),
                    qty));
            System.out.println("Transfer successful.");
        } else {
            System.out.println("Transfer failed. Check stock and quantity.");
        }
    }

    static void checkStock() {
        Warehouse w = findWarehouse();
        if (w == null) return;

        int id = readInt("Product ID: ");
        Product p = w.getProduct(id);

        if (p == null) {
            System.out.println("Product not found.");
            return;
        }

        p.display();
        System.out.println(p.isLowStock()
                ? "WARNING: Low stock!" : "Stock level is sufficient.");
    }

    static void createPurchaseOrder() {
        System.out.println("Available suppliers:");
        for (Supplier s : suppliers.values())
            s.display();

        int sid = readInt("Supplier ID: ");
        Supplier supplier = suppliers.get(sid);
        if (supplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        int pid = readInt("Product ID: ");
        Product product = catalogue.get(pid);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        int qty = readInt("Order quantity: ");
        if (qty <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        Warehouse w = findWarehouse();
        if (w == null) return;

        PurchaseOrder order =
                new PurchaseOrder(supplier, product, qty);

        if (order.receiveStock(w)) {
            orders.add(order);
            movements.add(new StockMovement(
                    pid, "PURCHASE RECEIVED", w.getName(), qty));
            order.display();
        }
    }

    static void dispatchShipment() {
        Warehouse w = findWarehouse();
        if (w == null) return;

        int pid = readInt("Product ID: ");
        int qty = readInt("Shipment quantity: ");
        String destination = readText("Destination: ");

        if (qty <= 0 || destination.isEmpty()) {
            System.out.println("Enter valid shipment details.");
            return;
        }

        Shipment shipment = new Shipment(destination, pid, qty);

        if (shipment.dispatch(w)) {
            shipments.add(shipment);
            movements.add(new StockMovement(
                    pid, "DISPATCH", destination, qty));
            shipment.display();
        } else {
            System.out.println("Dispatch failed. Insufficient stock "
                    + "or invalid product.");
        }
    }

    static void lowStockReport() {
        boolean found = false;

        for (Warehouse w : warehouses.values()) {
            for (Product p : w.getProducts()) {
                if (p.isLowStock()) {
                    System.out.println(w.getName() + ": "
                            + p.getName() + " | Quantity: "
                            + p.getQuantity());
                    found = true;
                }
            }
        }

        if (!found)
            System.out.println("No low-stock products.");
    }

    static void movementHistory() {
        if (movements.isEmpty()) {
            System.out.println("No stock movements recorded.");
            return;
        }

        for (StockMovement m : movements)
            m.display();
    }

    public static void main(String[] args) {
        warehouses.put(1, new Warehouse(1, "Warehouse A"));
        warehouses.put(2, new Warehouse(2, "Warehouse B"));

        suppliers.put(1, new Supplier(
                1, "ABC Suppliers", "9876543210"));
        suppliers.put(2, new Supplier(
                2, "Global Traders", "9876501234"));

        while (true) {
            System.out.println("\n=== WAREHOUSE INVENTORY SYSTEM ===");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Add Stock");
            System.out.println("4. Transfer Stock");
            System.out.println("5. Check Stock");
            System.out.println("6. Create Purchase Order");
            System.out.println("7. Dispatch Shipment");
            System.out.println("8. Low-Stock Report");
            System.out.println("9. Stock Movement History");
            System.out.println("0. Exit");

            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1: addProduct(); break;
                case 2: viewProducts(); break;
                case 3: addStock(); break;
                case 4: transferStock(); break;
                case 5: checkStock(); break;
                case 6: createPurchaseOrder(); break;
                case 7: dispatchShipment(); break;
                case 8: lowStockReport(); break;
                case 9: movementHistory(); break;
                case 0:
                    System.out.println("Thank you!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
