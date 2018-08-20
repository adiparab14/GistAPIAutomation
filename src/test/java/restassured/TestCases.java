package restassured;
import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.MultiPartConfig.multiPartConfig;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class TestCases {

  final String baseURI = "https://private-538b1b-verificationsrv.apiary-mock.com/";
  String webhookParam = "webhook_url";
  String webhookValue = "www.abc.com";
  String refNumberParam = "reference_number";
  String refNumberValue = "abc123";
  String uriVerification, uriImage, uriId, id;


  @BeforeSuite
  public void setUp() {

    RestAssured.baseURI = baseURI;

    Response response = given().contentType(ContentType.JSON).when().get("").then().assertThat()
        .statusCode(200).extract().response();

    uriVerification = response.path("verifications_url");

  }

  @Test (groups ={"positive"})
  public void createVerifications() {

    Response response = given().contentType(ContentType.JSON).when()
        .param(webhookParam, webhookValue).param(refNumberParam, refNumberValue).post(uriVerification).then()
        .assertThat().statusCode(201).body(matchesJsonSchemaInClasspath("Verifications.json")).extract().response();

    uriImage = response.path("verification_images_url");
    uriId = response.path("verification_url");
    id = uriId.substring(uriId.lastIndexOf("/") + 1);
  }


  @Test(groups ={"negative"})
  public void createVerificationInvalidWebhook() {

    given().contentType(ContentType.JSON).when().param(webhookParam, "----")
        .param(refNumberParam, refNumberValue).post(uriVerification).then().assertThat().statusCode(400)
        .body(matchesJsonSchemaInClasspath("Error.json")).extract().response();

  }

  @Test(groups ={"positive"})
  public void startVerificationCorrectID() {

    Response response = given().contentType(ContentType.JSON).when().put(uriId).then().assertThat()
        .statusCode(200).body(matchesJsonSchemaInClasspath("Verification_Status.json")).extract().response();

    Assert.assertEquals(response.path("id"), id);

  }

  @Test (groups ={"negative"})
  public void startVerificationIncorrectRessource() {

    given().contentType(ContentType.JSON).when().put(uriId).then().assertThat().statusCode(400)
        .body(matchesJsonSchemaInClasspath("Error.json")).extract().response();

  }


  @Test(groups ={"positive"})
  public void uploadImageCorrectId() {

    String encodedFile =
        EncodeImage.encodeFileToBase64Binary(new File(getClass().getClassLoader().getResource("test.jpg").getFile()));

    Response response = given().contentType("multipart/form-data").when()
        .config(config().multiPartConfig(multiPartConfig().defaultSubtype("mixed").defaultBoundary("--BOUNDARY")))
        .multiPart("type", "front", "text-plain").multiPart("auto_start", "false", "text/plain")
        .multiPart("file", encodedFile, "image/jpeg").post(uriImage).then().assertThat().statusCode(202)
        .body(matchesJsonSchemaInClasspath("Verification_AddImage.json")).extract().response();

    Assert.assertEquals(response.path("message"), "Image received");

  }

  @Test(groups ={"negative"})
  public void uploadImageInvalid() {

    String encodedFile =
        EncodeImage.encodeFileToBase64Binary(new File(getClass().getClassLoader().getResource("test.jpg").getFile()));

    given().contentType("multipart/form-data").when()
        .config(config().multiPartConfig(multiPartConfig().defaultSubtype("mixed").defaultBoundary("--BOUNDARY")))
        .multiPart("type", "front", "text-plain").multiPart("auto_start", "false", "text/plain")
        .multiPart("file", encodedFile, "image/jpeg").post(uriImage).then().assertThat().statusCode(400)
        .body(matchesJsonSchemaInClasspath("Error.json")).extract().response();


  }

  @Test(groups ={"positive"})
  public void downloadReportCorrectId() {


    given().contentType(ContentType.JSON).when().get(uriVerification + "/" + id + "/report").then()
        .assertThat().statusCode(200).extract().response();


  }

  @Test(groups ={"negative"})
  public void downloadReportInCorrectId() {


    given().contentType(ContentType.JSON).when().get(uriVerification + "/" + "ABCD" + "/report").then()
        .assertThat().statusCode(400).body(matchesJsonSchemaInClasspath("Error.json")).extract().response();


  }
}
