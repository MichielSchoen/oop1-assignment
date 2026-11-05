package nl.inholland.oop1;

/**
 * Indicates that a requested device identifier is not present in the home.
 * The exception message is {@code "Device with id '<id>' not found."}.
 */
public class DeviceNotFoundException extends Exception {
    /**
     * Creates the exception for a missing device identifier.
     *
     * @param id identifier that could not be found
     */
    public DeviceNotFoundException(int id) {
        super("Device with id '" + id + "' not found.");
    }
}