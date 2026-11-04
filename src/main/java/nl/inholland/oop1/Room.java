package nl.inholland.oop1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A named room containing smart-home devices.
 */
public class Room {
    private String name;
    private final List<Device> devices = new ArrayList<>();
    private SmartHome home;

    /**
     * Creates an empty room.
     *
     * @param name non-null, non-empty room name
     * @throws IllegalArgumentException if {@code name} is null or empty
     */
    public Room(String name) {
        setName(name);
    }

    /**
     * Returns this room's name.
     *
     * @return room name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes this room's name.
     *
     * @param name non-null, non-empty name unique within its home, if attached
     * @throws IllegalArgumentException if the name is null, empty, or duplicates another room in the home
     */
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Room name must not be null or empty.");
        }
        if (home != null) {
            home.validateRoomName(this, name);
        }
        this.name = name;
    }

    /**
     * Returns an unmodifiable snapshot of this room's devices.
     *
     * @return a non-null, unmodifiable device list
     */
    public List<Device> getDevices() {
        return Collections.unmodifiableList(new ArrayList<>(devices));
    }

    /**
     * Adds a device to this room.
     *
     * @param device device to add
     * @throws NullPointerException if {@code device} is null
     */
    public void addDevice(Device device) {
        devices.add(java.util.Objects.requireNonNull(device, "Device must not be null."));
    }

    /**
     * Removes the specified device from this room.
     *
     * @param device device to remove
     * @return {@code true} if the room contained and removed the device
     */
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
