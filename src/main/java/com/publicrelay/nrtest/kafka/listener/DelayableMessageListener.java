/*
 * Copyright (C) PublicRelay, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * The work belongs to the author's employer under work made for hire principles.
 */

package com.publicrelay.nrtest.kafka.listener;

import java.util.Date;

import org.slf4j.Logger;

/**
 * Interface to Delay Kafka Consumers
 */
public interface DelayableMessageListener {

    /**
     * PrLogger
     * @return
     */
    public Logger getPRLogger();

    /**
     * Desired Delay in milliseconds
     * @return
     */
    public Long getDelay();

    /**
     * Adding support for smart delays
     * Will delay consumption only if message was received right away, will skip delay if processing old backlog.
     * <p>
     * For example:
     * if delay is set to 1 second, and message was sent 1ms ago, consumer will delay consumption by 999ms
     * if delay is set to 1 second, and message was sent 500ms ago, consumer will delay consumption by 500ms
     * if delay is set to 1 second, and message was sent 1s ago, consumer will consume message right away
     * if delay is set to 1 second, and message was sent 2s ago, consumer will consume message right away
     * @param submittedTs - timestamp when message was submitted by producer
     */
    public default void delayIfNeeded(final long submittedTs) {
        if (getDelay() != null && getDelay() > 0) {

            final long currentTs = new Date().getTime();
            final long sleepForMs = getDelay() - (currentTs - submittedTs);

            getPRLogger().trace("currentTs: {}", currentTs);
            getPRLogger().trace("submittedTs: {}", submittedTs);
            getPRLogger().trace("delay: {}", getDelay());

            if (sleepForMs > 0) {

                getPRLogger().info("Sleeping for {}ms to accommodate desired delay", sleepForMs);

                try {
                    Thread.sleep(sleepForMs);
                } catch (final InterruptedException e) {
                    getPRLogger().warn("Interrupted due to {}", e.getMessage());
                    // Restore interrupted state...
                    Thread.currentThread()
                          .interrupt();

                }

            } else {
                getPRLogger().trace("sleepForMs: {}, No need to sleep", sleepForMs);
            }

        }
    }
}
