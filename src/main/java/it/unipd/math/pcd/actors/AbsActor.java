/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 Riccardo Cardin
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p/>
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */

/**
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */
package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.utils.Pair;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Defines common properties of all actors.
 *
 * @author Riccardo Cardin
 * @author Enrico Ceron
 * @version 1.0
 * @since 1.0
 * @param <T> Message subtype.
 */
public abstract class AbsActor<T extends Message> implements Actor<T> {

    /**
     * Self-reference of the actor.
     */
    protected ActorRef<T> self;

    /**
     * Sender of the current message.
     */
    protected ActorRef<T> sender;

    /**
     * Messages container.
     */
    private Mailbox<T> mailbox;

    /**
     * Execution task.
     */
    private final Runnable core;
    private final Lock lock;
    private final Condition mailboxEmpty;

    /**
     * Sets mailbox to an instance of {@code UnboundedMailbox}.
     */
    protected AbsActor() {
        this(new UnboundedMailbox<T>());
    }

    /**
     * Default ctor.
     *
     * @param mailbox Mailbox specialization.
     */
    protected AbsActor(Mailbox<T> mailbox) {
        this.mailbox = mailbox;
        this.core = new ActorCore();
        ActorSystemImpl.getInstance().submitActorCore(this.core);
        this.lock = new ReentrantLock();
        this.mailboxEmpty = lock.newCondition();
    }

    /**
     * Sets the self-referece.
     *
     * @param self The reference to itself
     * @return The actor.
     */
    protected final Actor<T> setSelf(ActorRef<T> self) {
        this.self = self;
        return this;
    }

    /**
     * Tries to push a message and awake execution if successful.
     *
     * @param message Message to insert in the mailbox.
     */
    final void pushMessage(Pair<ActorRef<T>, T> message) {
        mailbox.push(message);
        lock.lock();
        try {
            mailboxEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Actor runnable to execute messages.
     */
    class ActorCore implements Runnable {

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    lock.lock();
                    while (mailbox.isEmpty()) {
                        mailboxEmpty.await();
                    }
                    lock.unlock();
                    execute();
                }
            } catch (InterruptedException e) {
                // TODO Manage thread interruption
            } finally {
                while (!mailbox.isEmpty()) {
                    lock.unlock();
                    execute();
                }
            }
        }

        /**
         * Executes head message.
         */
        private void execute() {
            Pair<ActorRef<T>, T> message = mailbox.pop();
            sender = message.getFirst();
            receive(message.getSecond());
        }
    }
}
