docker rmi -f test/newrelic-oom-repro
docker build -t test/newrelic-oom-repro .

# To run it standalone without docker compose
# docker run --name newrelic-oom-repro-run --network prnet -d  -t test/newrelic-oom-repro