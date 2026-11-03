package nl.inholland.oop1;

public class Device {
    private Integer id;
    private String name;
    private int powerUsage;

    public Device(String name, int powerUsage) {
        setName(name);
        setPowerUsage(powerUsage);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty.");
        }
        this.name = name;
    }

    public int getPowerUsage() {
        return powerUsage;
    }

    public void setPowerUsage(int powerUsage) {
        if (powerUsage < 0) {
            throw new IllegalArgumentException("Power usage must not be negative.");
        }
        this.powerUsage = powerUsage;
    }

    public String getType() {
        return "Device";
    }

    public String describe() {
        return "| id: " + id + " || name: " + name + " || type: " + getType() + " |";
    }

    @Override
    public String toString() {
        return describe();
    }
}
