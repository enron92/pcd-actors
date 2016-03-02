package it.unipd.math.pcd.actors.utils;

/**
 * Represents two immutable values together.
 *
 * @author Enrico Ceron
 * @since 2015-01-12
 * @param <T> Type of the first element.
 * @param <U> Type of the second element.
 */
public final class ImmutablePair<T, U> implements Pair<T, U> {

    private final T first;

    private final U second;

    /**
     * Ctor.
     *
     * @param first First value of pair.
     * @param second Second value of pair.
     */
    public ImmutablePair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public T getFirst() {
        return first;
    }

    @Override
    public U getSecond() {
        return second;
    }
}
