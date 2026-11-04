package nl.inholland.oop1;

/**
 * Selects values that satisfy a condition.
 *
 * @param <T> type of value evaluated by the selector
 */
@FunctionalInterface
public interface Selector<T> {
    /**
     * Determines whether the supplied value should be selected.
     *
     * @param value value to evaluate
     * @return {@code true} when the value should be selected
     */
    boolean select(T value);
}
