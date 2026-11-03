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
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setId(Integer id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String getName() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty.");
        }
        this.name = name;
    }

    public int getPowerUsage() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setPowerUsage(int powerUsage) {
        if (powerUsage < 0) {
            throw new IllegalArgumentException("Power usage must not be negative.");
        }
        this.powerUsage = powerUsage;
    }

    public String getType() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String describe() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
