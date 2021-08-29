package com.frankriccobono;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class LinkingResourceTest {

  @Test
  public void testHelloEndpoint() {
    given()
        .when().get("/hello")
        .then()
        .statusCode(200)
        .body(is("Hello RESTEasy"));
  }

//  @Test
//  public void testEmptyInstructorView(){
//    given()
//        .when()
//        .post("/lti/instructor/launch")
//        .then()
//        .statusCode(200);
//  }
}