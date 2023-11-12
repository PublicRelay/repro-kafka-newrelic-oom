/*
 * Copyright (C) PublicRelay, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  The work belongs to the author's employer under work made for hire principles.
 */

package com.publicrelay.nrtest.kafka.job;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author roman
 * @since 10/19/23
 */
public class TestNewrelicOOMJob {

    /**
     * logger.
     */
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private KafkaTemplate<Long, String> testNewrelicOOMKafkaTemplate;

    @Scheduled(fixedDelay = 5000) // 1000 messages every 5 seconds
    public void run() {
        final int num = 1000;
        getPRLogger().info("Sending {} messages to testNewrelicOOMKafkaTemplate...", num);

        for (int i = 0; i < num; i++) {
            final UUID uuid = UUID.randomUUID();

            getTestNewrelicOOMKafkaTemplate().sendDefault(Long.valueOf(i), uuid.toString());
        }
    }

    /**
     * Gets testNewrelicOOMKafkaTemplate
     * @return value of testNewrelicOOMKafkaTemplate
     */
    public KafkaTemplate<Long, String> getTestNewrelicOOMKafkaTemplate() {
        return testNewrelicOOMKafkaTemplate;
    }

    /**
     * Sets testNewrelicOOMKafkaTemplate
     * @param testNewrelicOOMKafkaTemplate value of testNewrelicOOMKafkaTemplate
     */
    public void setTestNewrelicOOMKafkaTemplate(
            final KafkaTemplate<Long, String> testNewrelicOOMKafkaTemplate) {
        this.testNewrelicOOMKafkaTemplate = testNewrelicOOMKafkaTemplate;
    }

    public Logger getPRLogger() {
        return log;
    }
}
