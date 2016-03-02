package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.utils.Pair;

/**
 * Actor's mailbox.
 *
 * @author Enrico Ceron
 * @since 2015-12-21
 * @param <T> Message subtype.
 */
public interface Mailbox<T extends Message> {

    /**
     * Tries to insert a message in mailbox.
     *
     * @param message Message to be inserted in mailbox.
     */
    void push(Pair<ActorRef<T>, T> message);

    /**
     * Returns head message of the mailbox and deletes it.
     *
     * @return Head message in mailbox.
     */
    Pair<ActorRef<T>, T> pop();

    /**
     * @return True if empty.
     */
    Boolean isEmpty();
}
