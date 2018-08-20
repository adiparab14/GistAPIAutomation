# Verifications API
This project is developed for automated testing of the Verifications API. The endpoint used was https://private-538b1b-verificationsrv.apiary-mock.com/

## Test Cases

### Positive
- Create Verification
- Start Verification
- Upload Image
- Download report
### Negative

- Create verification with invalid webhoon url
- Start verification with invalid id
- Upload invalid Image
- Download report with invalid id


--The URI's are referred from the discovery api and the subsequent responses. This makes the test cases independent of the changes in URI's

--Assertions is made on the HTTP response code and the JSON schema. Some assertions are also made on the response message content.

--All the negative test cases are failing because the Mock API always returns a positive response for incorrect requests as well.

## Framework
All the test cases are available in the TestCases.java class. EncodeImage.java class encodes the .jpg image to Base64 as is required in the Upload Image request.

**REST Assured for API Testing**
Rest assured makes testing of http requests easier. It also provides the given, when and then format to write tests which are easier to understand.

**TestNG is used as the testing framework**
Testing frameworks makes writing test cases easy. It aslo provides features like setup and grouping fo tests.

**Maven is used for dependency management and build**
We are using multiple libraries and Maven makes it extremely easy to make available all required libraries automatedly. Also less configuration is required to build tests on a new machine.

JSON schema is validated for all cases using json-schema-validator (https://github.com/fge/json-schema-formats)

## Pre-requisites

__Step 1: Install Maven 3.0.3+__

[Download from here](http://maven.apache.org/download.html)

__Step 2: Ensure maven binaries are on your PATH (ie. you can run `mvn` from anywhere)__

Follow the installation instructions from [here](http://www.baeldung.com/install-maven-on-windows-linux-mac).


## Running Tests

Run the following command in the root folder
```
mvn test
``` 

## Circle CI
The above Project can also be built using Circle CI. The valid config file is available in .circleci

__Step 1: Fork this reposirtory__

__Step 2: Login to [CircleCI](www.circleci.com) with your Github credentials__

__Step 3: Click the Add Project button in the left navigation pane__

__Step 4: Find your repository and click the Setup Button on the far right-hand side of the screen__

__Step5: Scroll past the setup information and click the Start Building button.__

## Additional things which could have been possible
-- All negative test cases could be automated. But as there is not proper response for negative tests, this could not be done.

-- Verification of Webhook
  I tried with a  temporary webhook. But I noticed that there is nothing being posted on it (Probably because it is mock).
  If there was more time I would like to investigate how this can be automated to test subsequent requests.
