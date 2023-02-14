# Transpose Api Service
API service to transpose a vector or matrix

### Requirements

* java version 11 or later
* linux or OSX operating system


### Scripts

the following list of scripts under PROJECT_ROOT/scripts are present to help build/test/run the application:

* `build.sh` clean, compile and run all unit tests
* `run-integration.sh` executes a series of longer running, end-to-end api service tests
* `start-api-server.sh` starts the api application server listening on default port 8080

you can also manually execute gradle commands:

* `cd PROJECT_ROOT`
* `build and test` ./gradlew build
* `integration test` ./gradlew clean; LEAF_INTEGRATION=true ./gradlew :webapp:test --tests "com.leaflogistics.app.LeafApiServerIntegrationTest.testMaxInput"
* `start api server` ./gradlew clean; ./gradlew :webapp:run

please note the single integration test does not run by default, since it takes quite a bit of time to start the server
and construct the input JSON

### Additional Information
_**input parsing**:_  
  &nbsp;&nbsp; attempts to catch input errors exist.  probably overkill, but I had most of it done before my questions were answered  
  
_**column vector vs row vector**:_  
  &nbsp;&nbsp; I followed the model utilized by the python numpy library.  
  &nbsp;&nbsp; vector input of `[ 1, 2, 3 ]` will be treated as a column vector, where transpose is a no-op and returns:  
  &nbsp;&nbsp; `[ 1, 2, 3 ]`  
  &nbsp;&nbsp; vector input of `[[ 1, 2, 3 ]]` will be treated as a row vector and returns:  
&nbsp;&nbsp; `[[1]`  
&nbsp;&nbsp;&nbsp;&nbsp; `[2]`  
&nbsp;&nbsp;&nbsp;&nbsp; `[3]]`

