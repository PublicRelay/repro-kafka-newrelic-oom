/* Copyright (C) PublicRelay, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * The work belongs to the author's employer under work made for hire principles.
 */

package com.publicrelay.nrtest.kafka.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * Kafka Manager - Intended to be used to control kafka consumers/providers
 * within PublicRelay application.
 * 
 * Initial version supports:
 * 
 * 1) Start all kafka consumers manually 2) Stop all kafka consumers manually
 * 
 * In future might be worth adding some control to producer's as well... but it
 * would need to be designed well.
 * 
 * @author Roman Bickersky
 * @since Jan 17, 2018
 */
@Component
public class KafkaManager {

    /**
     * logger.
     */
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private boolean consumersEnabled = false;

    /**
     * Fetch all Kafka MessageListenersContainers known by ApplicationContext
     */
    List<MessageListenerContainer> kafkaMessageListenerContainers;

    /**
     * Starting all Kafka Consumers
     */
    public void startAllKafkaConsumers() {

        if (kafkaMessageListenerContainers == null || kafkaMessageListenerContainers.isEmpty()) {
            getPRLogger().info("No Kafka Consumers to Start");
            return;
        }

        if (consumersEnabled) {
            getPRLogger().warn("Kafka Consumers are already started");
            return;
        }

        getPRLogger().info("Starting Kafka Consumers ({} total)", kafkaMessageListenerContainers.size());

        // Iterating over listenerContainers
        for (MessageListenerContainer consumerContainer : kafkaMessageListenerContainers) {

            // If consumerContainer is not already running
            if (!consumerContainer.isRunning()) {

                getPRLogger().debug("Starting Kafka Consumer Container: [{}]", consumerContainer.toString());
                // Start it
                consumerContainer.start();
            } else {
                getPRLogger().debug("Kafka Consumer Container [{}] is already started", consumerContainer.toString());
            }
        }

        consumersEnabled = true;

    }

    /**
     * Stopping all Kafka Consumers
     */
    public void stopAllKafkaConsumers() {

        if (kafkaMessageListenerContainers == null || kafkaMessageListenerContainers.isEmpty()) {
            getPRLogger().info("No Kafka Consumers to Stop");
            return;
        }

        if (!consumersEnabled) {
            getPRLogger().warn("Kafka Consumers are already stopped");
            return;
        }

        getPRLogger().info("Stopping Kafka Consumers...");

        // Iterating over listenerContainers
        for (MessageListenerContainer consumerContainer : kafkaMessageListenerContainers) {

            // If consumerContainer is running
            if (consumerContainer.isRunning()) {

                getPRLogger().info("Stopping Kafka Consumer Container: [{}]", consumerContainer.toString());
                // Stopping it
                consumerContainer.stop();
            } else {
                getPRLogger().info("Kafka Consumer Container [{}] is already stopped", consumerContainer.toString());
            }
        }
        consumersEnabled = false;
        getPRLogger().info("Kafka Consumers Stopped!");
    }

    @Autowired(required = false)
    public void setKafkaMessageListenerContainers(List<MessageListenerContainer> kafkaMessageListenerContainers) {
        this.kafkaMessageListenerContainers = kafkaMessageListenerContainers;
    }

    /**
     * This is the Getter method for lOG.
     * 
     * @return the lOG
     */
    public Logger getPRLogger() {
        return LOG;
    }

}
