
## opening-hours
REST API Service for formatting opening hours of restaurants

### How to build

To build and run all the tests use the following command:
```bash
sbt test

# or with auto-formatting and coverage:
sbt scalafmtAll coverage test coverageReport
```

### How to run locally

First, start the application server with the command:
```bash
sbt run
```

Then, you can call HTTP endpoint, with `curl`, for example:
```bash
curl -v -X POST -H "Content-Type: application/json" -d '{"monday" : [{"type" : "open","value" : 32400},{"type" : "close","value" : 72000}]}' 'http://localhost:8080/openinghours/format'
```

### Comments

The "main logic" for formatting opening hours is in
the [OpeningHoursService.scala](src/main/scala/openinghours/OpeningHoursService.scala) file.

The integration tests for the endpoint is in
the [OpeningHoursApiSpec.scala](src/test/scala/it/OpeningHoursApiSpec.scala) file.

### Part 2

I think that the input data could be better represented
with the following JSON structure:
```json
{
  "days" : [
    {
      "dayofweek" : "monday",
      "hours" : [
        {
          "type" : "open",
          "value" : 32400
        },
        {
          "type" : "close",
          "value" : 72000
        }
      ]
    }
  ]
}
```
Note, that this structure do not have `dynamic keys`,
thus is easier to map within the code. Even though
it is more verbose.
