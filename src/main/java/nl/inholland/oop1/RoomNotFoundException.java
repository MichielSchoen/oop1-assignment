package nl.inholland.oop1;

public class RoomNotFoundException extends Exception {
    public RoomNotFoundException(String name) {
        super("Room '" + name + "' not found.");
    }
}