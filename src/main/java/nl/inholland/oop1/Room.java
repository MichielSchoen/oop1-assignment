package nl.inholland.oop1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room {
    private String name;
    private final List<Device> devices = new ArrayList<>();
    private SmartHome home;

    public Room(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Room name must not be null or empty.");
        }
        if (home != null) {
            home.validateRoomName(this, name);
        }
        this.name = name;
    }

    public List<Device> getDevices() {
        return Collections.unmodifiableList(new ArrayList<>(devices));
    }

    public void addDevice(Device device) {
        devices.add(java.util.Objects.requireNonNull(device, "Device must not be null."));
    }

    public boolean removeDevice(Device device) {
        return devices.remove(device);
    }

    void attachTo(SmartHome smartHome) {
        if (home != null && home != smartHome) {
            throw new IllegalArgumentException("Room already belongs to another home.");
        }
        home = smartHome;
    }

    void detachFrom(SmartHome smartHome) {
        if (home == smartHome) {
            home = null;
        }
    }
}
