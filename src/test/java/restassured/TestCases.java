package restassured;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TestCases {

	final String baseURI = "https://api.github.com/";
	final String token = "59cc50961ecae37f75d5ed1f69147c8304619b53 ";
	String gistId;
	File createGist = new File("src/main/resources/CreateGist.json");
	File editGist = new File("src/main/resources/EditGist.json");

	@BeforeSuite
	public void setUp() {

		RestAssured.baseURI = baseURI;

	}

	@Test
	public void noAuthentication_readGistsAll() {

		given().when().get("/gists").then().assertThat().statusCode(200);
	}

	@Test
	public void noAuthentication_readGistsSpecific() {

		Response response = given().when().get("/gists/437b031138d0d816eb00c39480e38224").then().assertThat()
				.statusCode(200).extract().response();

		Assert.assertEquals(response.path("id"), "437b031138d0d816eb00c39480e38224");
	}

	@Test
	public void noAuthentication_createGist() {

		given().when().body(createGist).post("/gists").then().assertThat().statusCode(401).extract().response();
	}

	@Test
	public void noAuthentication_editGist() {

		given().when().body(editGist).patch("/gists/" + gistId).then().assertThat().statusCode(404).extract()
				.response();
	}

	@Test
	public void noAuthentication_deleteGist() {

		given().when().body(createGist).delete("/gists/" + gistId).then().assertThat().statusCode(404).extract()
				.response();
	}

	@Test
	public void noAuthentication_rateLimit() {

		Response response = given().when().get("/rate_limit").then().assertThat().statusCode(200).extract().response();
		Assert.assertEquals(response.path("rate.limit").toString(), "60");
	}

	@Test
	public void withAuthentication_rateLimit() {

		Response response = given().auth().oauth2(token).when().get("/rate_limit").then().assertThat().statusCode(200)
				.extract().response();
		Assert.assertEquals(response.path("rate.limit").toString(), "5000");
	}

	@Test
	public void withAuthentication_readGistsAll() {

		given().auth().oauth2(token).when().get("/gists").then().assertThat().statusCode(200);
	}

	@Test
	public void withAuthentication_readGistsSpecific() {

		Response response = given().auth().oauth2(token).when().get("/gists/437b031138d0d816eb00c39480e38224").then()
				.assertThat().statusCode(200).extract().response();

		Assert.assertEquals(response.path("id"), "437b031138d0d816eb00c39480e38224");
	}

	@Test(priority = 0)
	public void withAuthentication_createGist() {

		Response response = given().auth().oauth2(token).when().body(createGist).post("/gists").then().assertThat()
				.statusCode(201).extract().response();

		gistId = response.path("id");
	}

	@Test(priority = 1)
	public void withAuthentication_editGist() {

		Response response = given().auth().oauth2(token).when().body(editGist).patch("/gists/" + gistId).then()
				.assertThat().statusCode(200).extract().response();
		Assert.assertEquals(response.path("id"), gistId);
	}

	@Test(priority = 2)
	public void withAuthentication_deleteGist() {

		Response response = given().auth().oauth2(token).when().delete("/gists/" + gistId).then().assertThat()
				.statusCode(204).extract().response();
		Assert.assertEquals(response.asString(), "");
	}

}
