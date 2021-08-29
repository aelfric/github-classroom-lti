package com.frankriccobono;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthMessage;
import oauth.signpost.signature.OAuthMessageSigner;
import org.imsglobal.lti.launch.*;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class LinkingResourceTest {

  @Test
  public void testHelloEndpoint() {
    given()
        .when().get("/lti")
        .then()
        .statusCode(200)
        .body(is("Hello RESTEasy"));
  }

  @Test
  public void testEmptyInstructorView() throws LtiSigningException {
    LtiSigner ltiSigner = new LtiOauthSigner();
    Map<String, String> signedParameters = ltiSigner.signParameters(
        Map.of(
            "roles","Instructor",
            "custom_canvas_assignment_id","newId"
        ),
        "stevens",
        "stevens",
        "http://localhost:8081/lti/instructor/launch",
        "POST");

    given()
        .when()
        .formParams(signedParameters)
        .post("/lti/instructor/launch")
        .then()
        .body(containsString("newId"))
        .body(containsString("Not Linked"))
        .statusCode(200);
  }

  @Test
  public void testEmptyStudentView() throws LtiSigningException {
    LtiSigner ltiSigner = new LtiOauthSigner();
    Map<String, String> signedParameters = ltiSigner.signParameters(
        Map.of(
            "roles","Learner",
            "custom_canvas_assignment_id","someId"
        ),
        "stevens",
        "stevens",
        "http://localhost:8081/lti/student/launch",
        "POST");

    given()
        .when()
        .formParams(signedParameters)
        .post("/lti/student/launch")
        .then()
        .statusCode(200);
  }


  @Test
  public void testAlreadyStudentView() throws LtiSigningException {
    LtiSigner ltiSigner = new LtiOauthSigner();
    Map<String, String> signedParameters = ltiSigner.signParameters(
        Map.of(
            "roles","Learner",
            "custom_canvas_assignment_id","someId"
        ),
        "stevens",
        "stevens",
        "http://localhost:8081/lti/student/launch",
        "POST");

    given()
        .when()
        .formParams(signedParameters)
        .post("/lti/student/launch")
        .then()
        .body(containsString("https://github.com/abcd"))
        .statusCode(200);
  }

  @Test
  public void testAlreadyInstructorView() throws LtiSigningException {
    LtiSigner ltiSigner = new LtiOauthSigner();
    Map<String, String> signedParameters = ltiSigner.signParameters(
        Map.of(
            "roles","Instructor",
            "custom_canvas_assignment_id","someId"
        ),
        "stevens",
        "stevens",
        "http://localhost:8081/lti/instructor/launch",
        "POST");

    given()
        .when()
        .formParams(signedParameters)
        .post("/lti/instructor/launch")
        .then()
        .body(containsString("https://github.com/abcd"))
        .statusCode(200);
  }

  @Test
  void canLinkToNewAssignment() {
    given()
        .when()
        .formParam("assignment_id", "newId")
        .formParam("invitation_url", "https://github.com/xyz")
        .post("/lti/link")
        .then()
        .body(containsString("https://github.com/xyz"))
        .statusCode(200);
  }

  @Test
  void canLinkToExistingAssignment() {
    given()
        .when()
        .formParam("assignment_id", "someId2")
        .formParam("invitation_url", "https://github.com/xyz")
        .post("/lti/link")
        .then()
        .body(containsString("https://github.com/xyz"))
        .statusCode(200);
  }

  @Test
  void canRedirect() {
    given()
        .when()
        .formParam("url", "https://canvas.instructure.com")
        .formParam("redirect_url", "https://github.com/xyz")
        .redirects()
        .follow(false)
        .post("/lti/go")
        .then()
        .statusCode(307)
        .header("location", containsString("github"));
  }
}