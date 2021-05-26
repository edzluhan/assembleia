# Performance tests

## Available scenarios

- POST /vote [here](simulations/assembly/PostVoteSimulation.scala)

## Parameterizing the tests

By the default:

- the request rate is 1 request per second
- the duration is 1 second

If you want to change these, set the following environment variables:

```
RATE=10 # or any other value you want to represent the requests per second
DURATION=3600 # or any other value that represents for how long the tests should run (in seconds)
```

## Running the tests

You should be able to run the performance tests by running the following command at the root of your project:

```
./gradlew clean gatlingRun
```

## Checking the reports

The reports will be stored in the following path:

`build/reports/gatling/{simulation}-{timestamp}/index.html`
