package nl.inholland.oop1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class SmartHomeLookupTest {
    @Test
    void getByIdFindsDeviceAcrossRooms() throws RoomNotFoundException, DeviceNotFoundException {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));
        home.addRoom(new Room("Kitchen"));
        Device lamp = new LightDevice("Lamp", 10);
        home.addDeviceToRoom("Kitchen", lamp);

        assertEquals(lamp, home.getById(1));
    }

    @Test
    void getByIdThrowsForUnknownId() {
        SmartHome home = new SmartHome();

        DeviceNotFoundException exception = assertThrows(
                DeviceNotFoundException.class,
                () -> home.getById(1));

        assertEquals("Device with id '1' not found.", exception.getMessage());
    }

    @Test
    void getAllDevicesReturnsNewNonNullList() throws RoomNotFoundException {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));
        Device lamp = new LightDevice("Lamp", 10);
        home.addDeviceToRoom("Living room", lamp);

        List<Device> first = home.getAllDevices();
        List<Device> second = home.getAllDevices();

        assertEquals(List.of(lamp), first);
        assertNotSame(first, second);
    }

    @Test
    void findReturnsMatchingDevicesInANewList() throws RoomNotFoundException {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));
        Device lowPowerLamp = new LightDevice("Lamp", 10);
        Device highPowerLamp = new LightDevice("Spotlight", 50);
        home.addDeviceToRoom("Living room", lowPowerLamp);
        home.addDeviceToRoom("Living room", highPowerLamp);

        List<Device> results = home.find(device -> device.getPowerUsage() >= 20);

        assertEquals(List.of(highPowerLamp), results);
        assertNotSame(home.getAllDevices(), results);
    }

    @Test
    void findSupportsAnonymousSelectorImplementation() throws RoomNotFoundException {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));
        Device selected = new LightDevice("Lamp", 10);
        Device ignored = new LightDevice("Spotlight", 50);
        home.addDeviceToRoom("Living room", selected);
        home.addDeviceToRoom("Living room", ignored);

        List<Device> results = home.find(new Selector<Device>() {
            @Override
            public boolean select(Device device) {
                return device.getPowerUsage() < 20;
            }
        });

        assertEquals(List.of(selected), results);
    }

    @Test
    void findRejectsNullFilterAndEmptyHomeReturnsEmptyLists() {
        SmartHome home = new SmartHome();

        assertThrows(NullPointerException.class, () -> home.find(null));
        assertTrue(home.getAllDevices().isEmpty());
        assertTrue(home.find(device -> true).isEmpty());
    }
}
