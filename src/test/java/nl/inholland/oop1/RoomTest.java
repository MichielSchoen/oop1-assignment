package nl.inholland.oop1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RoomTest {
    @Test
    void roomRequiresNonNullAndNonEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Room(null));
        assertThrows(IllegalArgumentException.class, () -> new Room(""));
    }

    @Test
    void nameCanBeReadAndChanged() {
        Room room = new Room("Living room");

        assertEquals("Living room", room.getName());
        room.setName("Lounge");
        assertEquals("Lounge", room.getName());
    }

    @Test
    void deviceListIsImmutableAndIndependentOfInternalCollection() {
        Room room = new Room("Living room");
        Device lamp = new LightDevice("Lamp", 10);
        var devices = room.getDevices();

        assertThrows(UnsupportedOperationException.class, () -> devices.add(lamp));
        room.addDevice(lamp);
        assertTrue(devices.isEmpty());
        assertEquals(1, room.getDevices().size());
    }

    @Test
    void removeDeviceRemovesByIdAndReportsWhetherItWasPresent() {
        Room room = new Room("Living room");
        Device lamp = new LightDevice("Lamp", 10);
        lamp.setId(7);
        room.addDevice(lamp);

        assertTrue(room.removeDevice(7));
        assertTrue(room.getDevices().isEmpty());
        assertFalse(room.removeDevice(7));
    }

    @Test
    void homeEnforcesUniqueRoomNamesIncludingRenames() {
        SmartHome home = new SmartHome();
        Room kitchen = new Room("Kitchen");
        home.addRoom(kitchen);
        home.addRoom(new Room("Bedroom"));

        assertThrows(IllegalArgumentException.class, () -> home.addRoom(new Room("Kitchen")));
        assertThrows(IllegalArgumentException.class, () -> kitchen.setName("Bedroom"));
    }

    @Test
    void homeCannotRemoveRoomWhileItContainsDevices() throws RoomNotFoundException {
        SmartHome home = new SmartHome();
        Room room = new Room("Living room");
        home.addRoom(room);
        room.addDevice(new LightDevice("Lamp", 10));

        RoomNotEmptyException exception = assertThrows(
                RoomNotEmptyException.class,
                () -> home.removeRoom("Living room"));
        assertEquals("Room 'Living room' still contains 1 device(s).", exception.getMessage());
    }

    @Test
    void missingRoomOperationsThrowRoomNotFoundWithRequiredMessage() {
        SmartHome home = new SmartHome();
        RoomNotFoundException removeException = assertThrows(
                RoomNotFoundException.class,
                () -> home.removeRoom("Office"));
        RoomNotFoundException addDeviceException = assertThrows(
                RoomNotFoundException.class,
                () -> home.addDeviceToRoom("Office", new LightDevice("Lamp", 10)));

        assertEquals("Room 'Office' not found.", removeException.getMessage());
        assertEquals("Room 'Office' not found.", addDeviceException.getMessage());
    }

    @Test
    void homeCanRemoveEmptyRoom() throws RoomNotFoundException, RoomNotEmptyException {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));

        home.removeRoom("Living room");
        assertTrue(home.getRooms().isEmpty());
    }
}
