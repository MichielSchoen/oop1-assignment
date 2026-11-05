package nl.inholland.oop1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Manages the rooms and devices in one in-memory smart home.
 */
public class SmartHome {
    private final List<Room> rooms = new ArrayList<>();

    /**
     * Creates an empty smart home.
     */
    public SmartHome() {
    }

    /**
     * Returns an unmodifiable snapshot of the home's rooms.
     *
     * @return a non-null, unmodifiable list of rooms
     */
    public List<Room> getRooms() {
        return Collections.unmodifiableList(new ArrayList<>(rooms));
    }

    /**
     * Adds a room to the home.
     *
     * @param room room to add
     * @throws NullPointerException if {@code room} is null
     * @throws IllegalArgumentException if another room has the same name or the room belongs to another home
     */
    public void addRoom(Room room) {
        Objects.requireNonNull(room, "Room must not be null.");
        validateRoomName(room, room.getName());
        room.attachTo(this);
        rooms.add(room);
    }

    /**
     * Removes an empty room by name.
     *
     * @param roomName name of the room to remove
     * @throws RoomNotFoundException if no room has the supplied name
     * @throws RoomNotEmptyException if the room still contains devices
     */
    public void removeRoom(String roomName) throws RoomNotFoundException, RoomNotEmptyException {
        Room room = rooms.stream()
                .filter(existing -> existing.getName().equals(roomName))
                .findFirst()
                .orElseThrow(() -> new RoomNotFoundException(roomName));
        if (!room.getDevices().isEmpty()) {
            throw new RoomNotEmptyException(room.getName(), room.getDevices().size());
        }
        rooms.remove(room);
        room.detachFrom(this);
    }

    /**
     * Adds a device to a room, assigning the next home-wide identifier.
     *
     * @param roomName destination room name
     * @param device device to add
     * @return assigned identifier
     * @throws RoomNotFoundException if the room does not exist
     * @throws NullPointerException if {@code device} is null
     */
    public int addDeviceToRoom(String roomName, Device device) throws RoomNotFoundException {
        Objects.requireNonNull(device, "Device must not be null.");
        Room room = rooms.stream()
                .filter(existing -> existing.getName().equals(roomName))
                .findFirst()
                .orElseThrow(() -> new RoomNotFoundException(roomName));

        int nextId = rooms.stream()
                .flatMap(existing -> existing.getDevices().stream())
                .map(Device::getId)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;

        device.setId(nextId);
        room.addDevice(device);
        return nextId;
    }

    /**
     * Removes a device by identifier.
     *
     * @param id identifier of the device
     * @return {@code true} if a device was removed; otherwise {@code false}
     */
    public boolean removeDevice(int id) {
        for (Room room : rooms) {
            for (Device device : room.getDevices()) {
                if (Objects.equals(device.getId(), id)) {
                    return room.removeDevice(id);
                }
            }
        }
        return false;
    }

    /**
     * Finds a device by identifier.
     *
     * @param id identifier to search for
     * @return the matching device
     * @throws DeviceNotFoundException if no device has that identifier
     */
    public Device getById(int id) throws DeviceNotFoundException {
        return rooms.stream()
                .flatMap(room -> room.getDevices().stream())
                .filter(device -> Objects.equals(device.getId(), id))
                .findFirst()
                .orElseThrow(() -> new DeviceNotFoundException(id));
    }

    /**
     * Returns all devices currently assigned to rooms.
     *
     * @return a new, non-null list of devices
     */
    public List<Device> getAllDevices() {
        return rooms.stream()
                .flatMap(room -> room.getDevices().stream())
                .toList();
    }

    /**
     * Selects devices satisfying the supplied selector.
     *
     * @param filter selector applied to each device
     * @return a new, non-null list of matching devices
     * @throws NullPointerException if {@code filter} is null
     */
    public List<Device> find(Selector<Device> filter) {
        Objects.requireNonNull(filter, "Filter must not be null.");
        return getAllDevices().stream()
                .filter(filter::select)
                .toList();
    }

    /**
     * Finds devices assignable to the requested class.
     *
     * @param <T> requested device type
     * @param type class token for the requested device type
     * @return a new, non-null list of matching devices
     * @throws NullPointerException if {@code type} is null
     */
    public <T extends Device> List<T> findByType(Class<T> type) {
        Objects.requireNonNull(type, "Device type must not be null.");
        return getAllDevices().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .toList();
    }

    /**
     * Sets a thermostat's target temperature.
     *
     * @param id device identifier
     * @param targetTemperature requested temperature in degrees Celsius
     * @throws DeviceNotFoundException if no device has the identifier
     * @throws IllegalArgumentException if the identified device is not a thermostat or the target is out of range
     */
    public void setTargetTemperature(int id, double targetTemperature) throws DeviceNotFoundException {
        Device device = getById(id);
        if (!(device instanceof ThermostatDevice thermostat)) {
            throw new IllegalArgumentException("Device with id '" + id + "' is not a thermostat.");
        }
        thermostat.setTargetTemperature(targetTemperature);
    }

    /**
     * Returns all devices ordered by descending power usage, then by name.
     *
     * @return a new, non-null sorted device list
     */
    public List<Device> sortedByPowerUsage() {
        return getAllDevices().stream()
                .sorted(Comparator.comparingDouble(Device::getPowerUsage)
                        .reversed()
                        .thenComparing(Device::getName))
                .toList();
    }

    /**
     * Calculates total power usage across all current devices.
     *
     * @return sum of device power usage in watts
     */
    public double totalPowerUsage() {
        return getAllDevices().stream()
                .mapToDouble(Device::getPowerUsage)
                .sum();
    }

    /**
     * Creates a statistics snapshot from the current devices.
     *
     * @return snapshot of average power usage and counts by type
     */
    public HomeStats stats() {
        return new HomeStats(getAllDevices());
    }

    /**
     * Verifies that a room name is unique in this home.
     *
     * @param room room being named
     * @param name proposed room name
     * @throws IllegalArgumentException if another room already has the name
     */
    void validateRoomName(Room room, String name) {
        if (rooms.stream().anyMatch(existing -> existing != room && existing.getName().equals(name))) {
            throw new IllegalArgumentException("Room names must be unique.");
        }
    }

    /**
     * Immutable statistics snapshot for a smart home.
     */
    public static class HomeStats {
        private final double averagePowerUsage;
        private final Map<String, Integer> countByType;

        /**
         * Creates an immutable statistics snapshot from the supplied devices.
         *
         * @param devices devices included in the snapshot
         * @throws NullPointerException if {@code devices} is null
         */
        public HomeStats(Collection<Device> devices) {
            Objects.requireNonNull(devices, "Devices must not be null.");
            this.averagePowerUsage = devices.stream()
                    .mapToDouble(Device::getPowerUsage)
                    .average()
                    .orElse(0.0);

            Map<String, Integer> counts = new LinkedHashMap<>();
            devices.forEach(device -> counts.merge(device.getType(), 1, Integer::sum));
            this.countByType = Collections.unmodifiableMap(new LinkedHashMap<>(counts));
        }

        /**
         * Returns the arithmetic mean of device power usage.
         *
         * @return average usage in watts, or {@code 0.0} if the snapshot has no devices
         */
        public double getAveragePowerUsage() {
            return averagePowerUsage;
        }

        /**
         * Returns the number of devices for each type present in this snapshot.
         *
         * @return an unmodifiable map of device type labels to counts
         */
        public Map<String, Integer> getCountByType() {
            return countByType;
        }
    }
}
