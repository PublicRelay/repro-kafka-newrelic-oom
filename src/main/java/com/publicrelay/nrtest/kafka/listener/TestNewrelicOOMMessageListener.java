/*
 * Copyright (C) PublicRelay, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  The work belongs to the author's employer under work made for hire principles.
 */

package com.publicrelay.nrtest.kafka.listener;

import com.newrelic.api.agent.NewRelic;

/**
 * @author roman
 * @since 10/19/23
 */
public class TestNewrelicOOMMessageListener extends PrKafkaMessageListener<Long, String> {


    @Override
    public void onMessageValue(final String message) {

        NewRelic.addCustomParameter("message", message);

        getPRLogger().info("Processing message: {}", message);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
