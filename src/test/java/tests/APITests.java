package tests;

import io.restassured.internal.RestAssuredResponseOptionsGroovyImpl;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;


public class APITests extends Specifications {

    @DisplayName("Request validation GET LIST USERS and attribute per_page")
    @Test
    void checkListUserAttributePerPage() {
        Specifications.installSpecification(Specifications.requestSpecBaseSettings(), Specifications.responseSpecStatus200());
        Response response =
        given()
                .when()
                .get("/api/users?page=2")
                .then()
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        Integer per_page = jsonPath.get("per_page");
        Assertions.assertEquals(6, per_page);
    }

    @Test
    @DisplayName("Check successful user registration")
    public void successfulUserRegistrationTest() {
        Specifications.installSpecification(Specifications.requestSpecBaseSettings(), Specifications.responseSpecStatus200());
        Map<String, String> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");
        user.put("password", "pistol");
        Response response =
                given()
                        .body(user)
                        .post("api/register")
                        .then()
                        .extract().response();
        JsonPath jsonPath = response.jsonPath();
        int id = jsonPath.get("id");
        String token = jsonPath.get("token");
        Assertions.assertEquals(4, id);
        Assertions.assertEquals("QpwL5tke4Pnpja7X4", token);
    }

    @Test
    @DisplayName("Check unsuccessful user registration")
    public void unsuccessfulUserRegistrationTest() {
        Specifications.installSpecification(Specifications.requestSpecBaseSettings(), Specifications.responseSpecError400());
        Map<String, String> body = new HashMap<>();
        body.put("email", "sydney@fife");
        Response response =
                given()
                        .body(body)
                        .post("api/register")
                        .then()
                        .extract().response();
        JsonPath jsonPath = response.jsonPath();
        String error = jsonPath.get("error");
        Assertions.assertEquals("Missing password", error);
    }
}







