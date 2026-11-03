package nl.inholland.oop1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room {
    private String name;
    private final List<Device> devices = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setName(String name) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Device> getDevices() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void addDevice(Device device) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean removeDevice(Device device) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
