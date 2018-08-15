import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class Positive_Test {

    final String baseURI = "https://private-538b1b-verificationsrv.apiary-mock.com/";
    String Url;

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

        Url = response.path("verifications_url");
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

       // Response response =
                given()
                        .contentType(ContentType.JSON)
                        .log().body()
                        .when()
                        .param("webhook_url","www.abc.com")
                        .param("reference_number","abc123")
                        .post(Url)
                        .then()
                        .assertThat()
                        .statusCode(201)
                        .extract().response().prettyPrint();



    }
}
