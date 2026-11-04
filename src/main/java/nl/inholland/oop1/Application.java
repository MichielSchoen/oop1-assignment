package nl.inholland.oop1;

/**
 * Entry point that creates and connects the smart-home application objects.
 */
public final class Application {
    private Application() {
    }

    /**
     * Creates the in-memory home, service, and console UI, then starts the UI.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        SmartHome smartHome = new SmartHome();
        SmartHomeService smartHomeService = new SmartHomeServiceImpl(smartHome);
        ConsoleUserInterface consoleUserInterface = new ConsoleUserInterface(smartHomeService);
        consoleUserInterface.start();
    }
}
