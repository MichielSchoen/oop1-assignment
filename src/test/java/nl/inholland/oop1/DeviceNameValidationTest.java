package nl.inholland.oop1;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DeviceNameValidationTest {
    @Test
    void constructorRejectsNullOrEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Device(null, 10));
        assertThrows(IllegalArgumentException.class, () -> new Device("", 10));
    }

    @Test
    void setterRejectsNullOrEmptyName() {
        Device device = new Device("Lamp", 10);

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
        assertDoesNotThrow(() -> new Device("Lamp", 10));
        assertDoesNotThrow(() -> new ThermostatDevice("Thermostat", 10, "16-24"));
    }

    @Test
    void constructorAndSetterRejectNegativePowerUsage() {
        assertThrows(IllegalArgumentException.class, () -> new Device("Lamp", -1));
        assertThrows(IllegalArgumentException.class, () -> new ThermostatDevice("Thermostat", -1, "16-24"));

        Device device = new Device("Lamp", 10);
        assertThrows(IllegalArgumentException.class, () -> device.setPowerUsage(-1));
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
    void thermostatRangeSetterRejectsInvalidRangeAndPreservesCurrentRange() {
        ThermostatDevice thermostat = new ThermostatDevice("Thermostat", 10, "16-24");

        assertThrows(IllegalArgumentException.class, () -> thermostat.setRange("16-16"));
        assertEquals("16-24", thermostat.getRange());
    }

    @Test
    void thermostatStartsAtMinimumAndAcceptsTargetsWithinRange() {
        ThermostatDevice thermostat = new ThermostatDevice("Thermostat", 10, "16.5-23.5");

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