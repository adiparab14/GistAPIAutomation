import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.MultiPartConfig.multiPartConfig;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Positive_Test {

  final String baseURI = "https://private-538b1b-verificationsrv.apiary-mock.com/";
  String uriVerification, uriImage, uriId, id;


  @BeforeTest
  public void setUp() {

    RestAssured.baseURI = baseURI;

    Response response = given().log().everything().contentType(ContentType.JSON).when().get("").then().assertThat()
        .statusCode(200).extract().response();

    uriVerification = response.path("verifications_url");

  }

  @Test
  public void createVerifications() {

    Response response = given().contentType(ContentType.JSON).log().everything().when()
        .param("webhook_url", "www.abc.com").param("reference_number", "abc123").post(uriVerification).then()
        .assertThat().statusCode(201).body(matchesJsonSchemaInClasspath("Verifications.json")).extract().response();

    uriImage = response.path("verification_images_url");
    uriId = response.path("verification_url");
    id = uriId.substring(uriId.lastIndexOf("/") + 1);
  }


  @Test
  public void createVerificationInvalidWebhook() {

    given().contentType(ContentType.JSON).log().everything().when().param("webhook_url", "----")
        .param("reference_number", "abc123").post(uriVerification).then().assertThat().statusCode(400)
        .body(matchesJsonSchemaInClasspath("Error.json")).extract().response();

  }

  @Test
  public void startVerificationCorrectID() {

    Response response = given().contentType(ContentType.JSON).log().everything().when().put(uriId).then().assertThat()
        .statusCode(200).body(matchesJsonSchemaInClasspath("Verification_Status.json")).extract().response();

    Assert.assertEquals(response.path("id"), id);

  }

  @Test
  public void startVerificationIncorrectRessource() {

    given().contentType(ContentType.JSON).log().everything().when().put(uriId).then().assertThat().statusCode(400)
        .body(matchesJsonSchemaInClasspath("Error.json")).extract().response();

  }


  @Test
  public void uploadImageCorrectId() {

    String encodedFile =
        EncodeImage.encodeFileToBase64Binary(new File(getClass().getClassLoader().getResource("test.jpg").getFile()));

    Response response = given().contentType("multipart/form-data").log().all().when()
        .config(config().multiPartConfig(multiPartConfig().defaultSubtype("mixed").defaultBoundary("--BOUNDARY")))
        .multiPart("type", "front", "text-plain").multiPart("auto_start", "false", "text/plain")
        .multiPart("file", encodedFile, "image/jpeg").post(uriImage).then().assertThat().statusCode(202)
        .body(matchesJsonSchemaInClasspath("Verification_AddImage.json")).extract().response();

    Assert.assertEquals(response.path("message"), "Image received");

  }

  @Test
  public void uploadImageInvalid() {

    String encodedFile =
        EncodeImage.encodeFileToBase64Binary(new File(getClass().getClassLoader().getResource("a.jpg").getFile()));

    given().contentType("multipart/form-data").log().all().when()
        .config(config().multiPartConfig(multiPartConfig().defaultSubtype("mixed").defaultBoundary("--BOUNDARY")))
        .multiPart("type", "front", "text-plain").multiPart("auto_start", "false", "text/plain")
        .multiPart("file", encodedFile, "image/jpeg").post(uriImage).then().assertThat().statusCode(400)
        .body(matchesJsonSchemaInClasspath("Error.json")).extract().response();


  }

  @Test
  public void downloadReportCorrectId() {


    given().contentType(ContentType.JSON).log().all().when().get(uriVerification + "/" + id + "/report").then()
        .assertThat().statusCode(200).extract().response();


  }

  @Test
  public void downloadReportInCorrectId() {


    given().contentType(ContentType.JSON).log().all().when().get(uriVerification + "/" + "ABCD" + "/report").then()
        .assertThat().statusCode(400).body(matchesJsonSchemaInClasspath("Error.json")).extract().response();


  }
}
