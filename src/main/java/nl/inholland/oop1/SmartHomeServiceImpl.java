package nl.inholland.oop1;

import java.util.List;
import java.util.Objects;

/**
 * Implements smart-home application operations by delegating to a {@link SmartHome}.
 */
public class SmartHomeServiceImpl implements SmartHomeService {
    private final SmartHome smartHome;

    /**
     * Creates a service for the supplied smart home.
     *
     * @param smartHome home to manage
     * @throws NullPointerException if {@code smartHome} is {@code null}
     */
    public SmartHomeServiceImpl(SmartHome smartHome) {
        this.smartHome = Objects.requireNonNull(smartHome, "Smart home must not be null.");
    }

    /** {@inheritDoc} */
    @Override
    public List<Room> fetchAllRoomsAndDevices() {
        return smartHome.getRooms();
    }

    /** {@inheritDoc} */
    @Override
    public Device searchDeviceById(int id) throws DeviceNotFoundException {
        return smartHome.getById(id);
    }

    /** {@inheritDoc} */
    @Override
    public int addLightDevice(String roomName, LightDevice lightDevice) throws RoomNotFoundException {
        return smartHome.addDeviceToRoom(roomName, lightDevice);
    }

    /** {@inheritDoc} */
    @Override
    public int addThermostatDevice(String roomName, ThermostatDevice thermostatDevice)
            throws RoomNotFoundException {
        return smartHome.addDeviceToRoom(roomName, thermostatDevice);
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeDevice(int id) {
        return smartHome.removeDevice(id);
    }

    /** {@inheritDoc} */
    @Override
    public void setTargetTemperature(int id, double target) throws DeviceNotFoundException {
        smartHome.setTargetTemperature(id, target);
    }

    /** {@inheritDoc} */
    @Override
    public List<Device> findDevicesWithPowerUsageLessThan(double max) {
        return smartHome.find(new Selector<>() {
            @Override
            public boolean select(Device device) {
                return device.getPowerUsage() < max;
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public List<Device> getDevicesSortedByPowerUsage() {
        return smartHome.sortedByPowerUsage();
    }

    /** {@inheritDoc} */
    @Override
    public double getTotalPowerUsage() {
        return smartHome.totalPowerUsage();
    }

    /** {@inheritDoc} */
    @Override
    public SmartHome.HomeStats getHomeStats() {
        return smartHome.stats();
    }

    /** {@inheritDoc} */
    @Override
    public void addRoom(String roomName) {
        smartHome.addRoom(new Room(roomName));
    }

    /** {@inheritDoc} */
    @Override
    public void removeRoom(String roomName) throws RoomNotFoundException, RoomNotEmptyException {
        smartHome.removeRoom(roomName);
    }
}
