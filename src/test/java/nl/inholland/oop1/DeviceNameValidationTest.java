package nl.inholland.oop1;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DeviceNameValidationTest {
    @Test
    void constructorRejectsNullOrEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new LightDevice(null, 10));
        assertThrows(IllegalArgumentException.class, () -> new LightDevice("", 10));
    }

    @Test
    void setterRejectsNullOrEmptyName() {
        Device device = new LightDevice("Lamp", 10);

        assertThrows(IllegalArgumentException.class, () -> device.setName(null));
        assertThrows(IllegalArgumentException.class, () -> device.setName(""));
    }

    @Test
    void thermostatConstructorUsesDeviceNameValidation() {
        assertThrows(IllegalArgumentException.class, () -> new ThermostatDevice(null, 10, "16-24"));
        assertThrows(IllegalArgumentException.class, () -> new ThermostatDevice("", 10, "16-24"));
    }

    @Test
    void nonEmptyNameIsAccepted() {
        assertDoesNotThrow(() -> new LightDevice("Lamp", 10));
        assertDoesNotThrow(() -> new ThermostatDevice("Thermostat", 10, "16-24"));
    }

    @Test
    void constructorAndSetterRejectNegativePowerUsage() {
        assertThrows(IllegalArgumentException.class, () -> new LightDevice("Lamp", -1));
        assertThrows(IllegalArgumentException.class, () -> new ThermostatDevice("Thermostat", -1, "16-24"));
        assertThrows(IllegalArgumentException.class, () -> new LightDevice("Lamp", Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new LightDevice("Lamp", Double.POSITIVE_INFINITY));

        Device device = new LightDevice("Lamp", 10);
        assertThrows(IllegalArgumentException.class, () -> device.setPowerUsage(-1));
        assertThrows(IllegalArgumentException.class, () -> device.setPowerUsage(Double.NaN));
    }

    @Test
    void thermostatConstructorRejectsInvalidRanges() {
        assertThrows(IllegalArgumentException.class, () -> new ThermostatDevice("Thermostat", 10, null));
        assertThrows(IllegalArgumentException.class, () -> new ThermostatDevice("Thermostat", 10, ""));
        assertThrows(IllegalArgumentException.class, () -> new ThermostatDevice("Thermostat", 10, "4-24"));
        assertThrows(IllegalArgumentException.class, () -> new ThermostatDevice("Thermostat", 10, "16-31"));
        assertThrows(IllegalArgumentException.class, () -> new ThermostatDevice("Thermostat", 10, "24-16"));
        assertThrows(IllegalArgumentException.class, () -> new ThermostatDevice("Thermostat", 10, "low-high"));
    }

    @Test
    void thermostatAcceptsInclusiveTemperatureRangeBoundaries() {
        ThermostatDevice thermostat = new ThermostatDevice("Thermostat", 10, "5-30");

        assertEquals(5.0, thermostat.getMinimumTemperature());
        assertEquals(30.0, thermostat.getMaximumTemperature());
        assertEquals(5.0, thermostat.getTargetTemperature());
        assertDoesNotThrow(() -> thermostat.setTargetTemperature(30.0));
    }

    @Test
    void thermostatTemperatureAccessorsSupportDecimalBounds() {
        ThermostatDevice thermostat = new ThermostatDevice("Thermostat", 10, "16.5-23.5");

        assertEquals(16.5, thermostat.getMinimumTemperature());
        assertEquals(23.5, thermostat.getMaximumTemperature());
    }

    @Test
    void thermostatStartsAtMinimumAndAcceptsTargetsWithinRange() {
        ThermostatDevice thermostat = new ThermostatDevice("Thermostat", 10, "16.5-23.5");

        assertEquals(16.5, thermostat.getMinimumTemperature());
        assertEquals(23.5, thermostat.getMaximumTemperature());
        assertEquals(16.5, thermostat.getTargetTemperature());
        assertDoesNotThrow(() -> thermostat.setTargetTemperature(23.5));
        assertEquals(23.5, thermostat.getTargetTemperature());
    }

    @Test
    void thermostatRejectsTargetsOutsideRange() {
        ThermostatDevice thermostat = new ThermostatDevice("Thermostat", 10, "16-24");

        assertThrows(IllegalArgumentException.class, () -> thermostat.setTargetTemperature(15.9));
        assertThrows(IllegalArgumentException.class, () -> thermostat.setTargetTemperature(24.1));
        assertThrows(IllegalArgumentException.class, () -> thermostat.setTargetTemperature(Double.NaN));
    }
}