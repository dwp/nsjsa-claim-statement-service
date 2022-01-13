# Claim Statement Service

## About

Job Seekers Allowance microservice - generates a printable claim statement page listing all the questions the claimant was asked, and all their responses.

## Getting Started

### Prerequisites

* Java 8
* Maven

### Installing

mvn install

`java -jar target/claim-statement-service-0.0.1-SNAPSHOT.jar`

Browse to: http://localhost:8080/

## PublicKey

In application.properties, the services.publicKey needs to be populated with a good RSA key.
To create this, and set it, run ./createPublicKey.sh.  This is a one time operation.  Please take
care not to check this change in.

### Development
You can use dev profile to run on port 8082 and avoid conflicts

`java -jar target/claim-statement-service-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=dev`

## Running the tests
mvn test

## interface test
curl -X GET http://localhost:8082/nsjsa/test/ninotest

## Running Pa11y
We use Pa11y from the command line. To be able to run it you should install NPM (comes with Node - download link: https://nodejs.org/en/download/), and then install the pa11y npm package using NPM:

    npm install -g pa11y

Once the command line tool is installed, you can run pa11y to check the site against A, AA and AAA accessibility standards.

In the command line go to the project root and run:

    pa11y http://localhost:8080
    
    OR if you run dev profile
    
    pa11y http://localhost:8082

pa11y has a configuration file in the project root


# Dependencies

This service requires nsjsa-commons to build.
