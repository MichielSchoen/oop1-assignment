package nl.inholland.oop1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SmartHome {
    private final List<Room> rooms = new ArrayList<>();

    public SmartHome() {
    }

    public List<Room> getRooms() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void addRoom(Room room) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean removeRoom(String roomName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int addDeviceToRoom(String roomName, Device device) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean removeDevice(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Device getById(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Device> getAllDevices() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Device> find(java.util.function.Predicate<Device> filter) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public <T extends Device> List<T> findByType(Class<T> type) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setTargetTemperature(int id, double targetTemperature) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Device> sortedByPowerUsage() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int totalPowerUsage() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public HomeStats stats() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static class HomeStats {
        public double getAveragePowerUsage() {
            throw new UnsupportedOperationException("Not implemented yet");
        }

        public Map<String, Integer> getCountByType() {
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }
}
