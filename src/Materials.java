import java.time.LocalDateTime;

public class Materials {
    public int id;
    private String name;
    private double price;
    private int quantity;
    private LocalDateTime date_added;
    private String serialNumber;
    private Boolean sold;


    public Materials(String name, double price, int quantity, LocalDateTime date_added , String serialNumber , Boolean sold) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.date_added = date_added;
        this.serialNumber = serialNumber;
        this.sold = sold;

    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;

    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;

    }
    public LocalDateTime getDate_added() {
        return date_added;
    }
    public void setDate_added(LocalDateTime date_added) {
        this.date_added = date_added;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }
}
