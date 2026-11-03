package nl.inholland.oop1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SmartHome {
    private final List<Room> rooms = new ArrayList<>();

    public SmartHome() {
    }

    public List<Room> getRooms() {
        return Collections.unmodifiableList(new ArrayList<>(rooms));
    }

    public void addRoom(Room room) {
        Objects.requireNonNull(room, "Room must not be null.");
        validateRoomName(room, room.getName());
        room.attachTo(this);
        rooms.add(room);
    }

    public boolean removeRoom(String roomName) throws RoomNotFoundException, RoomNotEmptyException {
        Room room = rooms.stream()
                .filter(existing -> existing.getName().equals(roomName))
                .findFirst()
                .orElseThrow(() -> new RoomNotFoundException(roomName));
        if (!room.getDevices().isEmpty()) {
            throw new RoomNotEmptyException(room.getName(), room.getDevices().size());
        }
        boolean removed = rooms.remove(room);
        if (removed) {
            room.detachFrom(this);
        }
        return removed;
    }

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

    public boolean removeDevice(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Device getById(int id) throws DeviceNotFoundException {
        return rooms.stream()
                .flatMap(room -> room.getDevices().stream())
                .filter(device -> Objects.equals(device.getId(), id))
                .findFirst()
                .orElseThrow(() -> new DeviceNotFoundException(id));
    }

    public List<Device> getAllDevices() {
        return rooms.stream()
                .flatMap(room -> room.getDevices().stream())
                .toList();
    }

    public List<Device> find(java.util.function.Predicate<Device> filter) {
        Objects.requireNonNull(filter, "Filter must not be null.");
        return getAllDevices().stream()
                .filter(filter)
                .toList();
    }

    public <T extends Device> List<T> findByType(Class<T> type) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setTargetTemperature(int id, double targetTemperature) throws DeviceNotFoundException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Device> sortedByPowerUsage() {
        return getAllDevices().stream()
                .sorted(Comparator.comparingInt(Device::getPowerUsage)
                        .reversed()
                        .thenComparing(Device::getName))
                .toList();
    }

    public int totalPowerUsage() {
        return getAllDevices().stream()
                .mapToInt(Device::getPowerUsage)
                .sum();
    }

    public HomeStats stats() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    void validateRoomName(Room room, String name) {
        if (rooms.stream().anyMatch(existing -> existing != room && existing.getName().equals(name))) {
            throw new IllegalArgumentException("Room names must be unique.");
        }
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
