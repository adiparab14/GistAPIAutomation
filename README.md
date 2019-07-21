# Gist API Automation
This project is developed for automated testing of the Gists Github API. The endpoint used was https://api.github.com/

## Test Cases

In case of no authentication test cases, existing gist id is used. In case of with Authentication, a new gist is created every time which is Read, Updated and Deleted.

In case of test cases for rate limit, verification is done for the maximum no of requests available per day for authenticated and un-authenticated access.

- noAuthentication_createGist - Creating gist without Authentication. Expected 401.
- noAuthentication_editGist - Editing gist without Authentication. Expected 401
- noAuthentication_deleteGist - Deleting gist without Authentication. Expected 401.
- noAuthenctication_rateLimit - Requesting rate limit for requests without Authentication. Expected the rate limit for un authenticated requests of different type.
- noAuthenctication_readGistsAll - Retrieving All Gists. Expected list of all public gists.
- noAuthentication_readGistsSpecific - Retrieving the details of the specific gist with gistid.
- withAuthentication_createGist - Creating gist with Authentication.  New gist is created.
- withAuthentication_editGist - Editing gist with Authentication. The specified gist is edited.
- withAuthentication_deleteGist - Deleting gist with Authentication. The specified gist is deleted.
- withAuthentication_rateLimit - Requesting rate limit for requests with Authentication. Expected the rate limit for authenticated requests of different type.
- withAuthentication_readGistsAll - Retrieving All Gists. Expected list of all gists of the authencitcated user.
- withAuthentication_readGistsSpecific - Retrieving the details of the specific gist with gistid.


--Assertions is made on the HTTP response code and in some cases on the gist id.

--All the negative test cases are failing because the Mock API always returns a positive response for incorrect requests as well.

## Framework
All the test cases are available in the TestCases.java class. EncodeImage.java class encodes the .jpg image to Base64 as is required in the Upload Image request.

**REST Assured for API Testing**
Rest assured makes testing of http requests easier. It also provides the given, when and then format to write tests which are easier to understand.

**TestNG is used as the testing framework**
Testing frameworks makes writing test cases easy. It aslo provides features like setup and grouping fo tests.

**Maven is used for dependency management and build**
We are using multiple libraries and Maven makes it extremely easy to make available all required libraries automatedly. Also less configuration is required to build tests on a new machine.


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
