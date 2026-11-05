package nl.inholland.oop1;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Presents the smart-home operations as a menu-driven console application.
 */
public class ConsoleUserInterface {
    private final SmartHomeService smartHomeService;
    private final Scanner scanner;

    /**
     * Creates a console user interface backed by the supplied service.
     *
     * @param smartHomeService service used to manage the home
     * @throws NullPointerException if {@code smartHomeService} is null
     */
    public ConsoleUserInterface(SmartHomeService smartHomeService) {
        this.smartHomeService = Objects.requireNonNull(smartHomeService, "Smart home service must not be null.");
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the menu and handles commands until the user exits or input ends.
     * Invalid input and expected domain errors are reported before the menu is shown again.
     */
    public void start() {
        System.out.println("Welcome to the Smart House Application!");
        boolean running = true;
        while (running) {
            printMenu();
            String input = readLine("Enter choice: ");
            if (input == null) {
                return;
            }

            try {
                switch (Integer.parseInt(input.trim())) {
                    case 1 -> showRooms();
                    case 2 -> searchDevice();
                    case 3 -> addLightDevice();
                    case 4 -> addThermostatDevice();
                    case 5 -> removeDevice();
                    case 6 -> findDevicesByPowerUsage();
                    case 7 -> System.out.println(
                            "Total power usage: " + smartHomeService.getTotalPowerUsage() + " W");
                    case 8 -> showStats();
                    case 9 -> addRoom();
                    case 10 -> removeRoom();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice. Enter a number from 0 to 10.");
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Invalid input: " + exception.getMessage());
            } catch (DeviceNotFoundException | RoomNotFoundException | RoomNotEmptyException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("""
                What do you want to do?
                1. View all rooms and their devices
                2. Search for a device (by id)
                3. Add a light device
                4. Add a thermostat device
                5. Remove a device (by id)
                6. Find devices whose power usage is less than a value
                7. Total power usage of all devices
                8. Stats (avg power usage and count by type)
                9. Add a room (by name)
                10. Remove a room (by name)
                0. Exit""");
    }

    private void showRooms() {
        List<Room> rooms = smartHomeService.fetchAllRoomsAndDevices();
        if (rooms.isEmpty()) {
            System.out.println("There are no rooms.");
            return;
        }

        for (Room room : rooms) {
            System.out.println(room.getName() + ":");
            if (room.getDevices().isEmpty()) {
                System.out.println("  (no devices)");
            } else {
                room.getDevices().forEach(device -> System.out.println("  " + device.describe()));
            }
        }
    }

    private void searchDevice() throws DeviceNotFoundException {
        Integer id = readInteger("Device id: ");
        if (id != null) {
            System.out.println(smartHomeService.searchDeviceById(id).describe());
        }
    }

    private void addLightDevice() throws RoomNotFoundException {
        String roomName = readLine("Room name: ");
        String name = readLine("Device name: ");
        Double powerUsage = readDouble("Power usage (W, decimals allowed): ");
        if (roomName != null && name != null && powerUsage != null) {
            int id = smartHomeService.addLightDevice(roomName, new LightDevice(name, powerUsage));
            System.out.println("Added device with id " + id + ".");
        }
    }

    private void addThermostatDevice() throws RoomNotFoundException {
        String roomName = readLine("Room name: ");
        String name = readLine("Device name: ");
        Double powerUsage = readDouble("Power usage (W, decimals allowed): ");
        String range = readLine("Temperature range (min-max): ");
        if (roomName != null && name != null && powerUsage != null && range != null) {
            int id = smartHomeService.addThermostatDevice(
                    roomName, new ThermostatDevice(name, powerUsage, range));
            System.out.println("Added device with id " + id + ".");
        }
    }

    private void removeDevice() {
        Integer id = readInteger("Device id: ");
        if (id != null) {
            if (smartHomeService.removeDevice(id)) {
                System.out.println("Device removed.");
            } else {
                System.out.println("No device found with that id.");
            }
        }
    }

    private void findDevicesByPowerUsage() {
        Double maximumPowerUsage = readDouble("Maximum power usage (W, decimals allowed): ");
        if (maximumPowerUsage != null) {
            List<Device> devices = smartHomeService.findDevicesWithPowerUsageLessThan(maximumPowerUsage);
            if (devices.isEmpty()) {
                System.out.println("No matching devices.");
            } else {
                devices.forEach(device -> System.out.println(device.describe()));
            }
        }
    }

    private void showStats() {
        SmartHome.HomeStats stats = smartHomeService.getHomeStats();
        System.out.println("Average power usage: " + stats.getAveragePowerUsage() + " W");
        for (Map.Entry<String, Integer> entry : stats.getCountByType().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private void addRoom() {
        String roomName = readLine("Room name: ");
        if (roomName != null) {
            smartHomeService.addRoom(roomName);
            System.out.println("Room added.");
        }
    }

    private void removeRoom() throws RoomNotFoundException, RoomNotEmptyException {
        String roomName = readLine("Room name: ");
        if (roomName != null) {
            smartHomeService.removeRoom(roomName);
            System.out.println("Room removed.");
        }
    }

    private Integer readInteger(String prompt) {
        String input = readLine(prompt);
        if (input == null) {
            return null;
        }
        try {
            return Integer.valueOf(input.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Please enter a whole number.", exception);
        }
    }

    private Double readDouble(String prompt) {
        String input = readLine(prompt);
        if (input == null) {
            return null;
        }
        try {
            double value = Double.parseDouble(input.trim());
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Please enter a finite number.");
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Please enter a number.", exception);
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }
}
