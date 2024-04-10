package services;

import static io.restassured.RestAssured.given;

import dto.*;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ServiceApi {
  private final String baseurl = System.getProperty("base.url", "https://petstore.swagger.io/v2");
  protected RequestSpecification spec;

  public ServiceApi() {
    spec = given()
            .baseUri(baseurl)
            .contentType(ContentType.JSON)
            .log().all();
  }

}
