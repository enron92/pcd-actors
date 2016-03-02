package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.utils.ImmutablePair;

/**
 * Reference to actor who lives on local machine.
 *
 * @author Enrico Ceron
 * @since 2015-12-21
 * @param <T> Message subtype.
 */
public final class LocalActorRef<T extends Message> implements ActorRef<T> {

    @Override
    public int compareTo(ActorRef o) {
        return (o.hashCode() - this.hashCode());
    }

    @Override
    public void send(T message, ActorRef to) {
        ((AbsActor<T>) ActorSystemImpl.getInstance().getActor(to))
                .pushMessage(new ImmutablePair<ActorRef<T>, T>(this, message));
    }
}
