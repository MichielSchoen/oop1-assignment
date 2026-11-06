package nl.inholland.oop1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ConsoleUserInterfaceTest {
    @Test
    void menuCanSetThermostatTargetTemperature() throws RoomNotFoundException {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));
        ThermostatDevice thermostat = new ThermostatDevice("Thermostat", 5, "16-24");
        home.addDeviceToRoom("Living room", thermostat);

        String output = runUi("11\n1\n21.5\n0\n", home);

        assertEquals(21.5, thermostat.getTargetTemperature());
        assertTrue(output.contains("11. Set a thermostat target temperature"));
        assertTrue(output.contains("Target temperature updated to 21.5 °C."));
    }

    @Test
    void invalidThermostatUpdatesReportErrorsAndReturnToMenu() throws RoomNotFoundException {
        SmartHome home = new SmartHome();
        home.addRoom(new Room("Living room"));
        home.addDeviceToRoom("Living room", new LightDevice("Lamp", 10));
        ThermostatDevice thermostat = new ThermostatDevice("Thermostat", 5, "16-24");
        home.addDeviceToRoom("Living room", thermostat);

        String output = runUi("11\n1\n20\n11\n2\n25\n11\n99\n20\n0\n", home);

        assertEquals(16.0, thermostat.getTargetTemperature());
        assertTrue(output.contains("Device with id '1' is not a thermostat."));
        assertTrue(output.contains("Target temperature must be within the thermostat range."));
        assertTrue(output.contains("Device with id '99' not found."));
        assertTrue(output.indexOf("11. Set a thermostat target temperature")
                < output.lastIndexOf("11. Set a thermostat target temperature"));
    }

    private String runUi(String input, SmartHome home) {
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(capturedOutput, true, StandardCharsets.UTF_8));
            new ConsoleUserInterface(new SmartHomeServiceImpl(home)).start();
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }
        return capturedOutput.toString(StandardCharsets.UTF_8);
    }
}
