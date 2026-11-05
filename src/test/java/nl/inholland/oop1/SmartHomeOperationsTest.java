package nl.inholland.oop1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SmartHomeOperationsTest {
    @Test
    void removeDeviceReturnsWhetherDeviceWasRemoved() throws RoomNotFoundException {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));
        int id = home.addDeviceToRoom("Living room", new LightDevice("Lamp", 10));

        assertTrue(home.removeDevice(id));
        assertFalse(home.removeDevice(id));
        assertTrue(home.getAllDevices().isEmpty());
    }

    @Test
    void findByTypeReturnsTypedDevicesAndEmptyListWhenNoneMatch() throws RoomNotFoundException {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));
        home.addDeviceToRoom("Living room", new LightDevice("Lamp", 10));
        ThermostatDevice thermostat = new ThermostatDevice("Thermostat", 5, "16-24");
        home.addDeviceToRoom("Living room", thermostat);

        assertEquals(java.util.List.of(thermostat), home.findByType(ThermostatDevice.class));
        assertTrue(new SmartHome().findByType(ThermostatDevice.class).isEmpty());
        assertThrows(NullPointerException.class, () -> home.findByType(null));
    }

    @Test
    void setTargetTemperatureUpdatesThermostatAndRejectsOtherDevices() throws Exception {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));
        ThermostatDevice thermostat = new ThermostatDevice("Thermostat", 5, "16-24");
        int thermostatId = home.addDeviceToRoom("Living room", thermostat);
        int lightId = home.addDeviceToRoom("Living room", new LightDevice("Lamp", 10));

        home.setTargetTemperature(thermostatId, 20.5);
        assertEquals(20.5, thermostat.getTargetTemperature());
        assertThrows(IllegalArgumentException.class, () -> home.setTargetTemperature(lightId, 20));
        assertThrows(DeviceNotFoundException.class, () -> home.setTargetTemperature(99, 20));
        assertThrows(IllegalArgumentException.class, () -> home.setTargetTemperature(thermostatId, 30));
    }

    @Test
    void statsAreSnapshotsOfCurrentDevicesAndCountOnlyPresentTypes() throws RoomNotFoundException {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));
        int lampId = home.addDeviceToRoom("Living room", new LightDevice("Lamp", 10));
        home.addDeviceToRoom("Living room", new LightDevice("Desk lamp", 20));
        ThermostatDevice thermostat = new ThermostatDevice("Thermostat", 5, "16-24");
        home.addDeviceToRoom("Living room", thermostat);

        SmartHome.HomeStats snapshot = home.stats();
        assertEquals(35.0 / 3, snapshot.getAveragePowerUsage());
        assertEquals(Map.of("Light", 2, "Thermostat", 1), snapshot.getCountByType());
        assertThrows(UnsupportedOperationException.class, () -> snapshot.getCountByType().put("Other", 1));

        home.removeDevice(lampId);
        assertEquals(35.0 / 3, snapshot.getAveragePowerUsage());
        assertEquals(12.5, home.stats().getAveragePowerUsage());
    }

    @Test
    void emptyHomeStatsHaveZeroAverageAndNoTypeCounts() {
        SmartHome.HomeStats stats = new SmartHome().stats();

        assertEquals(0.0, stats.getAveragePowerUsage());
        assertTrue(stats.getCountByType().isEmpty());
    }

    @Test
    void homeStatsConstructorCreatesImmutableSnapshot() {
        LightDevice lamp = new LightDevice("Lamp", 10.5);
        ThermostatDevice thermostat = new ThermostatDevice("Thermostat", 5.5, "16-24");
        ArrayList<Device> devices = new ArrayList<>(java.util.List.of(lamp, thermostat));

        SmartHome.HomeStats stats = new SmartHome.HomeStats(devices);
        devices.clear();
        lamp.setPowerUsage(100);

        assertEquals(8.0, stats.getAveragePowerUsage());
        assertEquals(Map.of("Light", 1, "Thermostat", 1), stats.getCountByType());
        assertThrows(UnsupportedOperationException.class, () -> stats.getCountByType().clear());
    }

    @Test
    void homeStatsConstructorHandlesEmptyCollection() {
        SmartHome.HomeStats stats = new SmartHome.HomeStats(java.util.List.of());

        assertEquals(0.0, stats.getAveragePowerUsage());
        assertTrue(stats.getCountByType().isEmpty());
    }
}
