package nl.inholland.oop1;

/**
 * Indicates that a room could not be removed because it contains devices.
 * The exception message is {@code "Room '<name>' still contains <deviceCount> device(s)."}.
 */
public class RoomNotEmptyException extends Exception {
    /**
     * Creates the exception for a non-empty room.
     *
     * @param name room name
     * @param deviceCount number of devices still in the room
     */
    public RoomNotEmptyException(String name, int deviceCount) {
        super("Room '" + name + "' still contains " + deviceCount + " device(s).");
    }
}