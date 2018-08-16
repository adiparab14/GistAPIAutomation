import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

public class Positive_Test {

    final String baseURI = "https://private-538b1b-verificationsrv.apiary-mock.com/";
    String uriVerification;
    String uriImage;
    String uriId;

    @BeforeTest
    public void setUp() {
        RestAssured.baseURI = baseURI;

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .when()
                        .get()
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .extract().response();

        uriVerification = response.path("verifications_url");
    }

   /* @Test
        public void verifyDiscoveryAPI() {

        Response response =
                given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        Url = response.path("verifications_url");

    }
*/
   @Test
    public void createVerifications() {

       Response response =
                given()
                        .contentType(ContentType.JSON)
                        .log().everything()
                        .when()
                        .param("webhook_url","www.abc.com")
                        .param("reference_number","abc123")
                        .post(uriVerification)
                        .then()
                        .assertThat()
                        .statusCode(201)
                        .body(matchesJsonSchemaInClasspath("Verifications.json"))
                        .extract().response();

       uriImage = response.path("verification_images_url");
       uriId = response.path("verification_url");
    }

    @Test
    public void startVerificationCorrectID() {

        // Response response =
        given()
                .contentType(ContentType.JSON)
                .log().everything()
                .when()
                .put(uriId)
                .then()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("Verification_Status.json"))
                .extract().response().prettyPrint();
    }

    @Test
    public void uploadImageCorrectId() {

    // Response response =
    given().contentType(ContentType.JSON).log().everything().when()

        .multiPart("file", new File("test.jpg")).param("type", "front").post(uriImage).then().assertThat()
        .statusCode(202)
        // .body(matchesJsonSchemaInClasspath("Verification_Status.json"))
        .extract().response().prettyPrint();
  }
}


