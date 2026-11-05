package nl.inholland.oop1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class DeviceBehaviorTest {
    @Test
    void deviceAccessorsAndDescriptionUseCurrentState() {
        Device device = new LightDevice("Lamp", 12);

        assertNull(device.getId());
        assertEquals("Lamp", device.getName());
        assertEquals(12, device.getPowerUsage());
        assertEquals("| id: null || name: Lamp || type: Light |", device.describe());
        assertEquals(device.describe(), device.toString());

        device.setId(7);
        device.setName("Desk lamp");
        device.setPowerUsage(15.5);

        assertEquals(7, device.getId());
        assertEquals("Desk lamp", device.getName());
        assertEquals(15.5, device.getPowerUsage());
    }

    @Test
    void powerUsageSupportsFractionalWatts() {
        LightDevice light = new LightDevice("Lamp", 12.75);

        assertEquals(12.75, light.getPowerUsage());
        light.setPowerUsage(8.25);
        assertEquals(8.25, light.getPowerUsage());
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
