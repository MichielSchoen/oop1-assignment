package nl.inholland.oop1;

public class DeviceNotFoundException extends Exception {
    public DeviceNotFoundException(int id) {
        super("Device with id '" + id + "' not found.");
    }
}