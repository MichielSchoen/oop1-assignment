package nl.inholland.oop1;

/**
 * Indicates that a requested room name is not present in the home.
 * The exception message is {@code "Room '<name>' not found."}.
 */
public class RoomNotFoundException extends Exception {
    /**
     * Creates the exception for a missing room name.
     *
     * @param name room name that could not be found
     */
    public RoomNotFoundException(String name) {
        super("Room '" + name + "' not found.");
    }
}