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

    @DisplayName("Request validation GET LIST USERS and attribute per_page, page, total")
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
        Integer page = jsonPath.get("page");
        Integer total = jsonPath.get("total");
        Assertions.assertEquals(6, per_page);
        Assertions.assertEquals(2, page);
        Assertions.assertEquals(12, total);
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

    @Test
    @DisplayName("Check create of user")
    public void createUserTest() {
        Specifications.installSpecification(Specifications.requestSpecBaseSettings(), Specifications.responseSpecStatus201());
        Map<String, String> user = new HashMap<>();
        user.put("name", "morpheus");
        user.put("job", "leader");
        Response response =
                given()
                        .body(user)
                        .post("/api/users")
                        .then()
                        .extract().response();
        JsonPath jsonPath = response.jsonPath();
        String name = jsonPath.get("name");
        String job = jsonPath.get("job");
        Assertions.assertEquals("morpheus", name);
        Assertions.assertEquals("leader", job);
    }

    @Test
    @DisplayName("Check deletion of user")
    public void deleteUserTest() {
        Specifications.installSpecification(Specifications.requestSpecBaseSettings(), Specifications.responseSpecUniqueStatus(204));
        given()
                .when()
                .delete("api/users/2")
                .then();
    }

    @Test
    @DisplayName("Check update of user")
    public void updateUserTest() {
        Specifications.installSpecification(Specifications.requestSpecBaseSettings(), Specifications.responseSpecStatus200());
        Map<String, String> user = new HashMap<>();
        user.put("name", "morpheus");
        user.put("job", "zion resident");
        Response response =
                given()
                        .body(user)
                        .patch("/api/users/2")
                        .then()
                        .extract().response();
        JsonPath jsonPath = response.jsonPath();
        String name = jsonPath.get("name");
        String job = jsonPath.get("job");
        Assertions.assertEquals("morpheus", name);
        Assertions.assertEquals("zion resident", job);
    }

    @Test
    @DisplayName("Check error update of user")
    public void errorUpdateUserTest() {
        Specifications.installSpecification(Specifications.requestSpecBaseSettings(), Specifications.responseSpecUniqueStatus(404));
        Map<String, String> user = new HashMap<>();
        user.put("name", "morpheus");
        user.put("job", "zion resident");
        Response response =
                given()
                        .body(user)
                        .patch("/api/users")
                        .then()
                        .extract().response();

    }
}







