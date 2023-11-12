FROM tomcat:10-jdk17

ADD target/kafka-newrelic-oom-repro-*.war webapps/
ADD newrelic.yml newrelic/
RUN cd newrelic && wget https://download.newrelic.com/newrelic/java-agent/newrelic-agent/8.6.0/newrelic-agent-8.6.0.jar

ENTRYPOINT catalina.sh run