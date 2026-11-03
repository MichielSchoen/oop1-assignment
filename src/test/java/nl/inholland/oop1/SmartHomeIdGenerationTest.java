package nl.inholland.oop1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SmartHomeIdGenerationTest {
    @Test
    void firstDeviceGetsIdOneAndPresetIdIsOverwritten() throws RoomNotFoundException {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));
        Device lamp = new LightDevice("Lamp", 10);
        lamp.setId(42);

        int assignedId = home.addDeviceToRoom("Living room", lamp);

        assertEquals(1, assignedId);
        assertEquals(1, lamp.getId());
    }

    @Test
    void idsIncreaseAcrossAllRoomsUsingHighestExistingId() throws RoomNotFoundException {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));
        home.addRoom(new Room("Kitchen"));
        Device first = new LightDevice("Lamp", 10);
        Device second = new ThermostatDevice("Thermostat", 5, "16-24");

        assertEquals(1, home.addDeviceToRoom("Living room", first));
        assertEquals(2, home.addDeviceToRoom("Kitchen", second));
    }

    @Test
    void addingDeviceToUnknownRoomThrowsWithoutAssigningId() {
        SmartHome home = new SmartHome();
        Device device = new LightDevice("Lamp", 10);

        assertThrows(RoomNotFoundException.class, () -> home.addDeviceToRoom("Missing", device));
        assertNull(device.getId());
    }
}
