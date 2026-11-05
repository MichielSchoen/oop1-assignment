package nl.inholland.oop1;

/**
 * A device with a bounded temperature range and adjustable target temperature.
 */
public class ThermostatDevice extends Device {
    private final String temperatureRange;
    private final double minimumTemperature;
    private final double maximumTemperature;
    private double targetTemperature;

    /**
     * Creates a thermostat and initializes its target to the range minimum.
     *
     * @param name non-null, non-empty device name
     * @param powerUsage finite, non-negative power usage in watts
     * @param range temperature range formatted as {@code "<min>-<max>"}
     * @throws IllegalArgumentException if device data is invalid, or the range is malformed, non-finite,
     *         outside 5-30 degrees Celsius, or has a minimum not below its maximum
     */
    public ThermostatDevice(String name, double powerUsage, String range) {
        super(name, powerUsage);
        double[] bounds = parseRange(range);
        this.temperatureRange = range;
        this.minimumTemperature = bounds[0];
        this.maximumTemperature = bounds[1];
        this.targetTemperature = bounds[0];
    }

    /**
     * Returns the minimum allowed temperature.
     *
     * @return minimum temperature in degrees Celsius
     */
    public double getMinimumTemperature() {
        return minimumTemperature;
    }

    /**
     * Returns the maximum allowed temperature.
     *
     * @return maximum temperature in degrees Celsius
     */
    public double getMaximumTemperature() {
        return maximumTemperature;
    }

    /**
     * Returns the current target temperature.
     *
     * @return target temperature in degrees Celsius
     */
    public double getTargetTemperature() {
        return targetTemperature;
    }

    /**
     * Sets the target temperature.
     *
     * @param targetTemperature target temperature in degrees Celsius
     * @throws IllegalArgumentException if the target is non-finite or outside the configured range
     */
    public void setTargetTemperature(double targetTemperature) {
        if (!Double.isFinite(targetTemperature)
                || targetTemperature < minimumTemperature
                || targetTemperature > maximumTemperature) {
            throw new IllegalArgumentException("Target temperature must be within the thermostat range.");
        }
        this.targetTemperature = targetTemperature;
    }

    /**
     * Returns the thermostat type label.
     *
     * @return {@code "Thermostat"}
     */
    @Override
    public String getType() {
        return "Thermostat";
    }

    /**
     * Returns the thermostat description, including its range and target.
     *
     * @return formatted thermostat description
     */
    @Override
    public String describe() {
        return "| id: " + getId()
                + " || name: " + getName()
                + " || type: " + getType()
                + " || range: " + temperatureRange
                + " || target temperature: " + targetTemperature
                + " |";
    }

    private static double[] parseRange(String range) {
        if (range == null || range.isEmpty()) {
            throw new IllegalArgumentException("Temperature range must not be null or empty.");
        }

        String[] parts = range.split("-", -1);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Temperature range must have the format '<min>-<max>'.");
        }

        try {
            double minimum = Double.parseDouble(parts[0]);
            double maximum = Double.parseDouble(parts[1]);
            if (!Double.isFinite(minimum) || !Double.isFinite(maximum)
                    || minimum < 5 || maximum > 30 || minimum >= maximum) {
                throw new IllegalArgumentException("Temperature range must be within 5-30 and min must be less than max.");
            }
            return new double[] { minimum, maximum };
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Temperature range must have the format '<min>-<max>'.", exception);
        }
    }
}
