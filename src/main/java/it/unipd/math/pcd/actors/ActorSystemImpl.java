package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The fake Singleton implementation of actor system.
 *
 * @author Enrico Ceron
 * @since 2015-12-21
 */
public final class ActorSystemImpl extends AbsActorSystem {

    /**
     * Fake Singleton instance.
     */
    private static ActorSystemImpl instance;

    /**
     * Collects actors execution tasks.
     */
    private final ExecutorService executor;

    /**
     * Fixed number of threads that executor service can run.
     */
    private static final int TOTAL_THREADS = 10;

    /**
     * Public fake Singleton ctor.
     */
    public ActorSystemImpl() {
        super();
        this.executor = Executors.newFixedThreadPool(TOTAL_THREADS);
        instance = this;
    }

    /**
     * Singleton pattern {@code getInstance()} method.
     *
     * @return Fake Singleton instance.
     */
    public static ActorSystemImpl getInstance() {
        if (instance == null) {
            instance = new ActorSystemImpl();
        }
        return instance;
    }

    @Override
    protected ActorRef createActorReference(ActorMode mode) throws IllegalArgumentException {
        if (mode == ActorMode.LOCAL) {
            return new LocalActorRef();
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void stop() {
        for (Map.Entry<ActorRef<?>, Actor<?>> pair : getActors().entrySet()) {
            stop(pair.getKey());
        }
        executor.shutdown();
    }

    @Override
    public void stop(ActorRef<?> actor) throws NoSuchActorException {
        if (getActors().remove(actor) == null) {
            throw new NoSuchActorException();
        }
    }

    /**
     * Submits an actor execution task to executor service.
     *
     * @param core Actor execution core.
     */
    protected void submitActorCore(Runnable core) {
        executor.submit(core);
    }
}
