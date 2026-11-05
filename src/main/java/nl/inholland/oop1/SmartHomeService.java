package nl.inholland.oop1;

import java.util.List;

/**
 * Application-facing operations for managing a smart home.
 */
public interface SmartHomeService {
    /**
     * Fetches all rooms and their devices.
     *
     * @return an unmodifiable snapshot of the rooms, or an empty list if there are no rooms
     */
    List<Room> fetchAllRoomsAndDevices();

    /**
     * Finds a device by its ID.
     *
     * @param id device ID
     * @return the matching device
     * @throws DeviceNotFoundException if no device has the ID
     */
    Device searchDeviceById(int id) throws DeviceNotFoundException;

    /**
     * Adds a light to a room.
     *
     * @param roomName destination room name
     * @param lightDevice light to add
     * @return assigned device ID
     * @throws RoomNotFoundException if the room does not exist
     * @throws NullPointerException if {@code lightDevice} is null
     */
    int addLightDevice(String roomName, LightDevice lightDevice) throws RoomNotFoundException;

    /**
     * Adds a thermostat to a room.
     *
     * @param roomName destination room name
     * @param thermostatDevice thermostat to add
     * @return assigned device ID
     * @throws RoomNotFoundException if the room does not exist
     * @throws NullPointerException if {@code thermostatDevice} is null
     */
    int addThermostatDevice(String roomName, ThermostatDevice thermostatDevice) throws RoomNotFoundException;

    /**
     * Removes a device by its ID.
     *
     * @param id device ID
     * @return {@code true} if a device was removed; otherwise {@code false}
     */
    boolean removeDevice(int id);

    /**
     * Changes a thermostat's target temperature.
     *
     * @param id thermostat ID
     * @param target target temperature
     * @throws DeviceNotFoundException if no device has the ID
     * @throws IllegalArgumentException if the device is not a thermostat or the target is non-finite or out of range
     */
    void setTargetTemperature(int id, double target) throws DeviceNotFoundException;

    /**
     * Finds devices whose power usage is below the supplied maximum.
     *
     * @param max exclusive maximum power usage in watts; fractional values are supported
     * @return a non-null list of matching devices, empty if none match
     */
    List<Device> findDevicesWithPowerUsageLessThan(double max);

    /**
     * Gets all devices sorted by power usage.
     *
     * @return a non-null list sorted by descending power usage and then by name
     */
    List<Device> getDevicesSortedByPowerUsage();

    /**
     * Gets the sum of all device power usage.
     *
     * @return total power usage in watts, including fractional values
     */
    double getTotalPowerUsage();

    /**
     * Gets a statistics snapshot for the home.
     *
     * @return an immutable snapshot of the current home statistics
     */
    SmartHome.HomeStats getHomeStats();

    /**
     * Adds an empty room.
     *
     * @param roomName room name
     * @throws IllegalArgumentException if the room name is null or empty, or duplicates an existing room
     */
    void addRoom(String roomName);

    /**
     * Removes an empty room.
     *
     * @param roomName room name
     * @throws RoomNotFoundException if the room does not exist
     * @throws RoomNotEmptyException if the room contains devices
     */
    void removeRoom(String roomName) throws RoomNotFoundException, RoomNotEmptyException;
}
