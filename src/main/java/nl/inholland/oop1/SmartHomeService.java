package nl.inholland.oop1;

import java.util.List;

/**
 * Application-facing operations for managing a smart home.
 */
public interface SmartHomeService {
    /**
     * Fetches all rooms and their devices.
     *
     * @return a list of rooms
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
     */
    int addLightDevice(String roomName, LightDevice lightDevice) throws RoomNotFoundException;

    /**
     * Adds a thermostat to a room.
     *
     * @param roomName destination room name
     * @param thermostatDevice thermostat to add
     * @return assigned device ID
     * @throws RoomNotFoundException if the room does not exist
     */
    int addThermostatDevice(String roomName, ThermostatDevice thermostatDevice) throws RoomNotFoundException;

    /**
     * Removes a device by its ID.
     *
     * @param id device ID
     * @return {@code true} if a device was removed
     */
    boolean removeDevice(int id);

    /**
     * Changes a thermostat's target temperature.
     *
     * @param id thermostat ID
     * @param target target temperature
     * @throws DeviceNotFoundException if no device has the ID
     */
    void setTargetTemperature(int id, double target) throws DeviceNotFoundException;

    /**
     * Finds devices whose power usage is below the supplied maximum.
     *
     * @param max maximum power usage, exclusive
     * @return matching devices
     */
    List<Device> findDevicesWithPowerUsageLessThan(double max);

    /**
     * Gets all devices sorted by power usage.
     *
     * @return devices sorted by descending power usage
     */
    List<Device> getDevicesSortedByPowerUsage();

    /**
     * Gets the sum of all device power usage.
     *
     * @return total power usage
     */
    double getTotalPowerUsage();

    /**
     * Gets a statistics snapshot for the home.
     *
     * @return current home statistics
     */
    SmartHome.HomeStats getHomeStats();

    /**
     * Adds an empty room.
     *
     * @param roomName room name
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
