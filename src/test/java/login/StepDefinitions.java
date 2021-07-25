package login;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;


import static io.restassured.RestAssured.given;

public class StepDefinitions {

    private int statusCode;
    private Response response;

    @Given("header setup is ready for login api request")
    public void setupHeaderDetails() {
        RestAssured.baseURI = "https://testapi.payarc.net/v1/login";
    }

    @Given("user send login request with credentials {string} and {string} for login")
    public void getUserDetailsTest(String username, String password) {
        response = given()
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body("{ \"username\":\"" + username + "\", \"password\":\"" + password + "\"}")
                .post()
                .then()
                .extract().response();
    }
    @Then("user should get {int} as response code in response")
    public void checkResponse(long responseCode){
        response.getBody().print();
        Assert.assertEquals(responseCode,response.getStatusCode());
    }


    @Then("user should get {string} as response code in response")
    public void validateResponseCodeInResponse(String responseCode) {
       Assert.assertEquals(Integer.parseInt(responseCode),response.getStatusCode());
    }

    @And("user should get {string} as error message in response")
    public void validateErrorMessageInResponse(String errorMessage) {
        JsonPath jsonPath = response.jsonPath();
        String message = jsonPath.get("message");
        Assert.assertEquals("Expected error message should present in the Response",errorMessage,message);

    }
}