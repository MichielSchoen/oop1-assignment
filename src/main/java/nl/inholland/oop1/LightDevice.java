package nl.inholland.oop1;

/**
 * A device that represents a light.
 */
public class LightDevice extends Device {
    /**
     * Creates a light with the specified name and power usage.
     *
     * @param name non-null, non-empty device name
     * @param powerUsage finite, non-negative power usage in watts
     * @throws IllegalArgumentException if the name is null or empty, or power usage is negative
     */
    public LightDevice(String name, double powerUsage) {
        super(name, powerUsage);
    }

    /**
     * Returns the light device type label.
     *
     * @return {@code "Light"}
     */
    @Override
    public String getType() {
        return "Light";
    }
}
