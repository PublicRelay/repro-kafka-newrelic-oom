# repro-kafka-newrelic-oom
Reproducible Use-Case for Memory Leaking issue detected after switching PublicRelay application to newrelic-agent-8.1.0. (Intended to be used to submit this reprto to newrelic engineering team for investigation)

# Reproducible user-case for Newrelic Kafka OOM


## What we know about the issue:
- 1. OOM is tightly related to the custom Trace instrumentation of the PrKafkaMessageListener class  
    - a. If we add metricName parameter to the @Trace Annotation of  PrKafkaMessageListener.onMessage  
        - Transaction metrics for Kafka Message Listeners appear in Newrelic,  
        - but memory keeps growing until eventually tomcat runs OOM  
    - b. When we remove metric name parameter from the @Trace Annotation of PrKafkaMessageListener.onMessage,  
        - tomcat's memory remains constant,  
        - but no Transactions appear for any of the KafkaListeners  
- 2. Behavior 1b goes away if we switch to the v7.9.0 of the newrelic agent. (But starts appearing again with 7.10.0, so 7.10.0 seem to be the version that introduced this bug).  
- 3. We were able to make Traces appear, by tweaking 2 things  
    - a. Changing kafka listener configuration to: autoStartup=true  
    - b. Adding the following exclusion to newrelic.yml  
        - excludes: org/springframework/web/context/ContextLoaderListener  
    - But we cannot change the autoStartup behavior in Production application   

## Requirements (What you need to have installed to run this repro)
- JDK 17
- Maven (I used 3.3.9)
- Docker (I used 20.10.16)
- Docker Compose (I used 1.29.2)

## How to run this repro:
1. Alter newrelic.yml file:
    - make sure to place proper license_key
    - you can also update the app_name (roman-test-nr-oom will be set by default)

2. Build the application:
```bash
    mvn clean package
```

3. Build docker image for the application:
```bash
    ./docker_build.sh
```

4. Run dockers (Note 3 dockers will be spun up, zookeper, kafka, newrelic-oom-repro)
```bash
    ./docker_run.sh
```

Note: Steps 2, 3 and 4 needed to be performed every time you change application's code
