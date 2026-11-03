package nl.inholland.oop1;

public class RoomNotEmptyException extends Exception {
    public RoomNotEmptyException(String name, int deviceCount) {
        super("Room '" + name + "' still contains " + deviceCount + " device(s).");
    }
}