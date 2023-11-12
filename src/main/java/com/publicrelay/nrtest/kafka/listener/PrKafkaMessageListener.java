package com.publicrelay.nrtest.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

/**
 * Set of common methods exposed to all Kafka Listeners
 * @author roman
 */
public abstract class PrKafkaMessageListener<K, V> implements MessageListener<K, V>, DelayableMessageListener {

    /**
     * logger.
     */
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Delay in milliseconds, @see #delayIfNeeded(long) for details
     */
    private Long delay;

    /**
     * Set to true to track lag in NewRelic
     */
    private Boolean trackLag;

    /* (non-Javadoc)
     * @see org.springframework.kafka.listener.GenericMessageListener#onMessage(java.lang.Object)
     */
    @Override
    // Commenting this out while troubleshooting OOM issue with NewRelic engineer
    //@Trace(dispatcher = true, metricName = "prKafkaMessageListener")
    @Trace(dispatcher = true)
    public void onMessage(final ConsumerRecord<K, V> data) {

        delayIfNeeded(data.timestamp());

        if (Boolean.TRUE == getTrackLag()) {
            NewRelic.addCustomParameter("lag_millis", System.currentTimeMillis() - data.timestamp());
        }

        onMessageValue(data.value());
    }

    /**
     * In most of the cases we won't need the entire ConsumerRecord just the message value
     *
     * @param message
     */
    abstract public void onMessageValue(V message);

    /**
     * @return the delay
     */
    public Long getDelay() {
        return delay;
    }

    /**
     * @param delay the delay to set
     */
    public void setDelay(final Long delay) {
        this.delay = delay;
    }

    /**
     * Gets trackLag
     * @return value of trackLag
     */
    public Boolean getTrackLag() {
        return trackLag;
    }

    /**
     * Sets trackLag
     * @param trackLag value of trackLag
     */
    public void setTrackLag(final Boolean trackLag) {
        this.trackLag = trackLag;
    }

    public Logger getPRLogger() {
        return log;
    }
}
