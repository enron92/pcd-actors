package it.unipd.math.pcd.actors.utils;

/**
 * Represents two values together.
 *
 * @author Enrico Ceron
 * @since 2015-01-12
 * @param <T> Type of the first element.
 * @param <U> Type of the second element.
 */
public interface Pair<T, U> {

    /**
     * Returns the first element of the pair.
     *
     * @return First element.
     */
    T getFirst();

    /**
     * Returns the second element of the pair.
     *
     * @return Second element.
     */
    U getSecond();
}
