package com.publicrelay.nrtest.kafka.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.publicrelay.nrtest.kafka.manager.KafkaManager;

/**
 * @author roman
 * @since 11/8/23
 */
@Component
public class ServletInitializationListener {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    KafkaManager kafkaManager;

    @EventListener
    public void handleContextStart(ContextRefreshedEvent cse) {

        if (kafkaManager != null) {
            kafkaManager.startAllKafkaConsumers();
        }

        log.info("Servlet fully initialized!");

    }
}
