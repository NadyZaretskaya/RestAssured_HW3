package services;

import static io.restassured.RestAssured.given;

import dto.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;


public class UserServiceApi extends ServiceApi {
  private final String basepath = "/user";

  public ValidatableResponse createUser(UserDTO user) {
    ValidatableResponse vr =
        given(spec)
            .basePath(basepath)
            .body(user)
            .when()
            .post()
            .then()
            .log().all();
    vr.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/CreateUser.json"));
    vr.extract().body().as(UserResponseDTO.class);
    return vr;
  }

  public ValidatableResponse getUserByName(String userByName) {
    ValidatableResponse vr =
        given(spec)
            .basePath(basepath)
            .when()
            .get("/" + userByName)
            .then()
            .log().all();
    vr.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/GetUserByName.json"));
    vr.extract().body().as(GetUserByNameResponseDTO.class);
    return vr;
  }

  public ValidatableResponse getUserByName404(String userByName) {
    ValidatableResponse vr =
        given(spec)
            .basePath(basepath)
            .when()
            .get("/" + userByName)
            .then()
            .log().all();
    vr.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/GetUserByName404.json"));
    vr.extract().body().as(GetUserByNameResponse404DTO.class);
    return vr;
  }

  public ValidatableResponse putUser(UpdateUserDTO updateUser, String userName) {
    ValidatableResponse vr =
        given(spec)
            .basePath(basepath)
            .body(updateUser)
            .when()
            .put("/" + userName)
            .then()
            .log().all();
    vr.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/PutUser.json"));
    vr.extract().body().as(UpdateUserResponseDTO.class);
    return vr;
  }

  public ValidatableResponse deleteUser(String userByName) {
    ValidatableResponse vr =
        given(spec)
            .basePath(basepath)
            .when()
            .delete("/" + userByName)
            .then()
            .log().all();
    return vr;
  }
}
