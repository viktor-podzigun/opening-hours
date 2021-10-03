
## opening-hours
REST API Service for formatting opening hours of restaurants

### How to build

To build and run all the tests use the following command:
```bash
sbt test

# or with auto-formatting and coverage:
sbt scalafmtAll coverage test coverageReport
```

### How to run

First, start the application server with the command:
```bash
sbt run
```

Then, you can call HTTP endpoint, with `curl`, for example:
```bash
curl -X POST -H "Content-Type: application/json" -d '{"monday" : [{"type" : "open","value" : 32400},{"type" : "close","value" : 72000}]}' 'http://localhost:8080/openinghours/format'
```
