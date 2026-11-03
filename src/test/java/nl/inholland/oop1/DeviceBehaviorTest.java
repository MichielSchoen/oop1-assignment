package nl.inholland.oop1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class DeviceBehaviorTest {
    @Test
    void deviceAccessorsAndDescriptionUseCurrentState() {
        Device device = new Device("Lamp", 12);

        assertNull(device.getId());
        assertEquals("Lamp", device.getName());
        assertEquals(12, device.getPowerUsage());
        assertEquals("| id: null || name: Lamp || type: Device |", device.describe());
        assertEquals(device.describe(), device.toString());

        device.setId(7);
        device.setName("Desk lamp");
        device.setPowerUsage(15);

        assertEquals(7, device.getId());
        assertEquals("Desk lamp", device.getName());
        assertEquals(15, device.getPowerUsage());
    }

    @Test
    void concreteDeviceTypesAndDescriptionsArePolymorphic() {
        LightDevice light = new LightDevice("Lamp", 12);
        ThermostatDevice thermostat = new ThermostatDevice("Thermostat", 5, "16-24");

        assertEquals("Light", light.getType());
        assertEquals("| id: null || name: Lamp || type: Light |", light.describe());
        assertEquals("Thermostat", thermostat.getType());
        assertEquals(
                "| id: null || name: Thermostat || type: Thermostat || range: 16-24 || target temperature: 16.0 |",
                thermostat.describe());
        assertEquals(thermostat.describe(), thermostat.toString());
    }
}
