package nl.inholland.oop1;

public class LightDevice extends Device {
    public LightDevice(String name, int powerUsage) {
        super(name, powerUsage);
    }

    @Override
    public String getType() {
        return "Light";
    }
}
