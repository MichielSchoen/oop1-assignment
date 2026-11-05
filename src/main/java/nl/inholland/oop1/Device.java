package nl.inholland.oop1;

/**
 * Base representation of a named device with an identifier and power usage.
 */
public abstract class Device implements Describable {
    private Integer id;
    private String name;
    private double powerUsage;

    /**
     * Creates a device with no assigned identifier.
     *
     * @param name non-null, non-empty device name
     * @param powerUsage finite, non-negative power usage in watts
     * @throws IllegalArgumentException if the name is null or empty, or power usage is negative or not finite
     */
    public Device(String name, double powerUsage) {
        setName(name);
        setPowerUsage(powerUsage);
    }

    /**
     * Returns this device's identifier.
     *
     * @return the identifier, or {@code null} if the device has not been added to a room
     */
    public Integer getId() {
        return id;
    }

    /**
     * Assigns this device's identifier.
     *
     * @param id identifier to assign; {@code null} clears the identifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns this device's name.
     *
     * @return the device name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the device name.
     *
     * @param name non-null, non-empty name
     * @throws IllegalArgumentException if {@code name} is null or empty
     */
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty.");
        }
        this.name = name;
    }

    /**
     * Returns the device's power usage.
     *
     * @return power usage in watts
     */
    public double getPowerUsage() {
        return powerUsage;
    }

    /**
     * Changes the device's power usage.
     *
     * @param powerUsage finite, non-negative power usage in watts
     * @throws IllegalArgumentException if {@code powerUsage} is negative or not finite
     */
    public void setPowerUsage(double powerUsage) {
        if (!Double.isFinite(powerUsage) || powerUsage < 0) {
            throw new IllegalArgumentException("Power usage must be finite and non-negative.");
        }
        this.powerUsage = powerUsage;
    }

    /**
     * Returns the device type label.
     *
     * @return the concrete device type label
     */
    public abstract String getType();

    /**
     * Returns the formatted description of this device.
     *
     * @return a description containing the identifier, name, and type
     */
    @Override
    public String describe() {
        return "| id: " + id + " || name: " + name + " || type: " + getType() + " |";
    }

    /**
     * Returns the formatted device description.
     *
     * @return the same value as {@link #describe()}
     */
    @Override
    public String toString() {
        return describe();
    }
}
