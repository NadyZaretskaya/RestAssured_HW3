package services;

import static io.restassured.RestAssured.given;

import dto.*;
//import dto.UpdateUserDTO;
//import dto.UpdateUserResponseDTO;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class ServiceApi {
  private final String baseurl = System.getProperty("base.url", "https://petstore.swagger.io/v2");
  private final String basepath = "/user";
  private RequestSpecification spec;
  public ServiceApi(){
    spec=given()
            .baseUri(baseurl)
            .basePath(basepath)
            .contentType(ContentType.JSON)
            .log().all();
  }

  public ValidatableResponse createUser(UserDTO user){
    ValidatableResponse vr =
            given(spec)
                    .body(user)
                    .when()
                    .post()
                    .then()
                    .log().all();
    vr.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/CreateUser.json"));
    vr.extract().body().as(UserResponseDTO.class);
    return vr;
  }

  public ValidatableResponse getUserByName(String userByName){
    ValidatableResponse vr =
            given(spec)
                    .when()
                    .get("/"+userByName)
                    .then()
                    .log().all();
    vr.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/GetUserByName.json"));
    vr.extract().body().as(GetUserByNameResponseDTO.class);
    return vr;
  }

  public ValidatableResponse getUserByName404(String userByName){
    ValidatableResponse vr =
            given(spec)
                    .when()
                    .get("/"+userByName)
                    .then()
                    .log().all();
    vr.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/GetUserByName404.json"));
    vr.extract().body().as(GetUserByNameResponse404DTO.class);
    return vr;
  }

  public ValidatableResponse putUser(UpdateUserDTO updateUser, String userName){
    ValidatableResponse vr =
            given(spec)
                    .body(updateUser)
                    .when()
                    .put("/"+userName)
                    .then()
                    .log().all();
    vr.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/PutUser.json"));
    vr.extract().body().as(UpdateUserResponseDTO.class);
    return vr;
  }

  public ValidatableResponse deleteUser(String userByName){
    ValidatableResponse vr =
            given(spec)
                    .when()
                    .delete("/"+userByName)
                    .then()
                    .log().all();
//    vr.extract().body().as(DeleteUserResponseDTO.class);
    return vr;
  }
}
