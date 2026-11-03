package nl.inholland.oop1;

public class ThermostatDevice extends Device {
    private String range;
    private double targetTemperature;

    public ThermostatDevice(String name, int powerUsage, String range) {
        super(name, powerUsage);
        double[] bounds = parseRange(range);
        this.range = range;
        this.targetTemperature = bounds[0];
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        double[] bounds = parseRange(range);
        if (targetTemperature < bounds[0] || targetTemperature > bounds[1]) {
            throw new IllegalArgumentException("Current target temperature must be within the new range.");
        }
        this.range = range;
    }

    public double getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(double targetTemperature) {
        double[] bounds = parseRange(range);
        if (!Double.isFinite(targetTemperature)
                || targetTemperature < bounds[0]
                || targetTemperature > bounds[1]) {
            throw new IllegalArgumentException("Target temperature must be within the thermostat range.");
        }
        this.targetTemperature = targetTemperature;
    }

    @Override
    public String getType() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String describe() {
        throw new UnsupportedOperationException("Not implemented yet");
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
