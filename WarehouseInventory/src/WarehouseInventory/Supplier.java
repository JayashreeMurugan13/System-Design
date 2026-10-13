package WarehouseInventory;


public class Supplier {
    private int id;
    private String name;
    private String phone;

    public Supplier(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public void display() {
        System.out.println("Supplier ID : " + id);
        System.out.println("Name        : " + name);
        System.out.println("Phone       : " + phone);
    }
}
