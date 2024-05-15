import java.time.LocalDateTime;

public class Transactions {
    private int id;
    private String name;
    private int quantity;
    private LocalDateTime date_added;
    private int material_id;
    public Transactions(int id, String name, int quantity, LocalDateTime date_added, int material_id) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.date_added = date_added;
        this.material_id = material_id;
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
    public int getMaterial_id() {
        return material_id;
    }
    public void setMaterial_id(int material_id) {
        this.material_id = material_id;
    }

}
