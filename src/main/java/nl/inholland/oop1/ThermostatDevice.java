package nl.inholland.oop1;

public class ThermostatDevice extends Device {
    private String range;
    private double targetTemperature;

    public ThermostatDevice(String name, int powerUsage, String range) {
        super(name, powerUsage);
        this.range = range;
    }

    public String getRange() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setRange(String range) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public double getTargetTemperature() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setTargetTemperature(double targetTemperature) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String getType() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String describe() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
