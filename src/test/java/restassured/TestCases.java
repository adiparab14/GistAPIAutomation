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
  String gistId;
  File createGist = new File("src/main/resources/CreateGist.json");
  
  @BeforeSuite
  public void setUp() {

    RestAssured.baseURI = baseURI;
    

  }

  @Test
  public void noAuthenctication_readGistsAll() {

    		given().
    		when().
    			get("/gists").
    		then().
    		assertThat().statusCode(200);
  }

  @Test
  public void noAuthenctication_readGistsSpecific() {
	  
	 Response response = 
			 given().
			 when().
			 	get("/gists/437b031138d0d816eb00c39480e38224").
			 then().
			 	assertThat().statusCode(200).extract().response();
	 
	 Assert.assertEquals(response.path("id"),"437b031138d0d816eb00c39480e38224");
	  }
  
  @Test
  public void noAuthenctication_createGist() {
	  
	 //Response response = 
			 given().
			 when().
			 	body(createGist).post("/gists").
			 then().
			 	assertThat().statusCode(401).extract().response();
	  }
  
  @Test
  public void noAuthenctication_editGist() {
	  
	 //Response response = 
			 given().
			 when().
			 	body(createGist).patch("/gists/"+gistId).
			 then().
			 	assertThat().statusCode(404).extract().response();
	  }
  
  @Test
  public void noAuthenctication_deleteGist() {
	  
	 //Response response = 
			 given().
			 when().
			 	body(createGist).delete("/gists/"+gistId).
			 then().
			 	assertThat().statusCode(404).extract().response();
	  }
  
  @Test (priority = 0)
  public void withAuthenctication_createGist() {
	  
	 Response response = 
			 given().
			 	auth().oauth2("c3d7ac635de7e52abe94b09be697160842e5df0c").
			 when().
			 	body(createGist).post("/gists").
			 then().
			 	assertThat().statusCode(201).extract().response();
	 
	  gistId = response.path("id");
	  }
  
  @Test (priority = 1)
  public void withAuthenctication_editGist_Negative() {
	  
	 Response response = 
			 given().
			 	auth().oauth2("c3d7ac635de7e52abe94b09be697160842e5df0c").
			 when().
			 	body(createGist).patch("/gists/"+gistId).
			 then().
			 	assertThat().statusCode(200).extract().response();
	 Assert.assertEquals(response.path("id"),gistId);
	  }
  
  @Test (priority = 2)
  public void noAuthenctication_deleteGist_Negative() {
	  
	  Response response = 
			  given().
			  	auth().oauth2("c3d7ac635de7e52abe94b09be697160842e5df0c").
			  when().
			  	body(createGist).delete("/gists/"+gistId).
			  then().assertThat().statusCode(204).extract().response();
	  Assert.assertEquals(response.asString(),"");
	  }


}
