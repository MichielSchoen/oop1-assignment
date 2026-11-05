package nl.inholland.oop1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class SmartHomePowerUsageTest {
    @Test
    void sortedByPowerUsageOrdersDescendingThenByName() throws RoomNotFoundException {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));
        Device low = new LightDevice("Low", 10.25);
        Device equalB = new LightDevice("Zeta", 50.5);
        Device high = new LightDevice("High", 80.75);
        Device equalA = new LightDevice("Alpha", 50.5);
        home.addDeviceToRoom("Living room", low);
        home.addDeviceToRoom("Living room", equalB);
        home.addDeviceToRoom("Living room", high);
        home.addDeviceToRoom("Living room", equalA);

        assertEquals(List.of(high, equalA, equalB, low), home.sortedByPowerUsage());
        assertEquals(4, home.getAllDevices().size());
    }

    @Test
    void totalPowerUsageSumsAllRoomsAndIsZeroForEmptyHome() throws RoomNotFoundException {
        SmartHome home = new SmartHome();
        assertEquals(0, home.totalPowerUsage());

        home.addRoom(new Room("Living room"));
        home.addRoom(new Room("Kitchen"));
        home.addDeviceToRoom("Living room", new LightDevice("Lamp", 20.25));
        home.addDeviceToRoom("Kitchen", new ThermostatDevice("Thermostat", 7.5, "16-24"));

        assertEquals(27.75, home.totalPowerUsage());
    }

    @Test
    void sortedResultsAreIndependentFromHomeDeviceList() throws RoomNotFoundException {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));
        home.addDeviceToRoom("Living room", new LightDevice("Lamp", 10));

        List<Device> sorted = home.sortedByPowerUsage();
        assertTrue(sorted.get(0).getPowerUsage() == 10);
        assertEquals(1, home.getAllDevices().size());
    }
}
