package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.utils.Pair;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Unbounded actor's mailbox.
 *
 * @author Enrico Ceron
 * @since 2015-12-21
 * @param <T> Message subtype.
 */
public final class UnboundedMailbox<T extends Message> implements Mailbox<T> {

    /**
     * Messages queue.
     */
    private ConcurrentLinkedQueue<Pair<ActorRef<T>, T>> queue;

    /**
     * Default ctor.
     */
    public UnboundedMailbox() {
        this.queue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void push(Pair<ActorRef<T>, T> message) {
        queue.add(message);
    }

    @Override
    public Pair<ActorRef<T>, T> pop() {
        return queue.poll();
    }

    @Override
    public Boolean isEmpty() {
        return queue.isEmpty();
    }
}
