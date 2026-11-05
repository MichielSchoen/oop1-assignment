package nl.inholland.oop1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SmartHomeServiceImplTest {
    @Mock
    private SmartHome smartHome;

    private SmartHomeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SmartHomeServiceImpl(smartHome);
    }

    @Test
    void constructorRejectsNullHome() {
        assertThrows(NullPointerException.class, () -> new SmartHomeServiceImpl(null));
    }

    @Test
    void fetchAndSearchDelegateToHome() throws DeviceNotFoundException {
        List<Room> rooms = List.of(new Room("Living room"));
        Device device = new LightDevice("Lamp", 10);
        when(smartHome.getRooms()).thenReturn(rooms);
        when(smartHome.getById(3)).thenReturn(device);

        assertEquals(rooms, service.fetchAllRoomsAndDevices());
        assertEquals(device, service.searchDeviceById(3));
        verify(smartHome).getRooms();
        verify(smartHome).getById(3);
    }

    @Test
    void notFoundExceptionsPropagateWithTheirExactMessages() throws Exception {
        when(smartHome.getById(404)).thenThrow(new DeviceNotFoundException(404));
        doThrow(new RoomNotFoundException("Office")).when(smartHome).removeRoom("Office");
        doThrow(new RoomNotEmptyException("Living room", 2)).when(smartHome).removeRoom("Living room");

        DeviceNotFoundException deviceException = assertThrows(
                DeviceNotFoundException.class, () -> service.searchDeviceById(404));
        RoomNotFoundException roomException = assertThrows(
                RoomNotFoundException.class, () -> service.removeRoom("Office"));
        RoomNotEmptyException nonEmptyException = assertThrows(
                RoomNotEmptyException.class, () -> service.removeRoom("Living room"));

        assertEquals("Device with id '404' not found.", deviceException.getMessage());
        assertEquals("Room 'Office' not found.", roomException.getMessage());
        assertEquals("Room 'Living room' still contains 2 device(s).", nonEmptyException.getMessage());
    }

    @Test
    void addingDeviceTypesDelegatesAndReturnsAssignedIds() throws RoomNotFoundException {
        LightDevice light = new LightDevice("Lamp", 10.25);
        ThermostatDevice thermostat = new ThermostatDevice("Thermostat", 5.75, "16-24");
        when(smartHome.addDeviceToRoom("Living room", light)).thenReturn(4);
        when(smartHome.addDeviceToRoom("Kitchen", thermostat)).thenReturn(5);

        assertEquals(4, service.addLightDevice("Living room", light));
        assertEquals(5, service.addThermostatDevice("Kitchen", thermostat));
        verify(smartHome).addDeviceToRoom("Living room", light);
        verify(smartHome).addDeviceToRoom("Kitchen", thermostat);
    }

    @Test
    void removeAndSetTargetTemperatureDelegateToHome() throws DeviceNotFoundException {
        when(smartHome.removeDevice(7)).thenReturn(true);

        assertTrue(service.removeDevice(7));
        assertFalse(service.removeDevice(8));
        service.setTargetTemperature(9, 21.5);

        verify(smartHome).removeDevice(7);
        verify(smartHome).removeDevice(8);
        verify(smartHome).setTargetTemperature(9, 21.5);
    }

    @Test
    void powerUsageFilterUsesSelectorAndDelegatesToHome() {
        Device lowUsage = new LightDevice("Lamp", 10.25);
        Device highUsage = new LightDevice("Heater", 100.75);
        when(smartHome.find(any())).thenReturn(List.of(lowUsage));

        assertEquals(List.of(lowUsage), service.findDevicesWithPowerUsageLessThan(10.5));
        verify(smartHome).find(argThat(selector ->
                selector.select(lowUsage) && !selector.select(highUsage)));
    }

    @Test
    void sortingPowerAndStatsDelegateToHome() {
        List<Device> sorted = List.of(new LightDevice("Lamp", 10));
        SmartHome.HomeStats stats = org.mockito.Mockito.mock(SmartHome.HomeStats.class);
        when(smartHome.sortedByPowerUsage()).thenReturn(sorted);
        when(smartHome.totalPowerUsage()).thenReturn(12.5);
        when(smartHome.stats()).thenReturn(stats);

        assertEquals(sorted, service.getDevicesSortedByPowerUsage());
        assertEquals(12.5, service.getTotalPowerUsage());
        assertEquals(stats, service.getHomeStats());
        verify(smartHome).sortedByPowerUsage();
        verify(smartHome).totalPowerUsage();
        verify(smartHome).stats();
    }

    @Test
    void roomOperationsDelegateToHome() throws RoomNotFoundException, RoomNotEmptyException {
        service.addRoom("Bedroom");
        service.removeRoom("Bedroom");

        verify(smartHome).addRoom(argThat(room -> room.getName().equals("Bedroom")));
        verify(smartHome).removeRoom("Bedroom");
    }
}
