package com.frankriccobono;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.filter.session.SessionFilter;
import org.imsglobal.lti.launch.LtiOauthSigner;
import org.imsglobal.lti.launch.LtiSigner;
import org.imsglobal.lti.launch.LtiSigningException;
import org.junit.jupiter.api.Test;

import java.util.Map;

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
    Map<String, String> signedParameters = signedLtiParams(
        "Instructor",
        "newId",
        "http://localhost:8081/lti/teacher/launch");

    given()
        .when()
        .formParams(signedParameters)
        .post("/lti/teacher/launch")
        .then()
        .body(containsString("newId"))
        .body(containsString("Not Linked"))
        .statusCode(200);
  }

  @Test
  public void testEmptyStudentView() throws LtiSigningException {
    Map<String, String> signedParameters = signedLtiParams(
        "Learner",
        "someId",
        "http://localhost:8081/lti/student/launch");

    given()
        .when()
        .formParams(signedParameters)
        .post("/lti/student/launch")
        .then()
        .statusCode(200);
  }


  @Test
  public void testAlreadyStudentView() throws LtiSigningException {
    Map<String, String> signedParameters = signedLtiParams(
        "Learner",
        "someId",
        "http://localhost:8081/lti/student/launch");

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
    Map<String, String> signedParameters = signedLtiParams(
        "Instructor",
        "someId",
        "http://localhost:8081/lti/teacher/launch");

    given()
        .when()
        .formParams(signedParameters)
        .post("/lti/teacher/launch")
        .then()
        .body(containsString("https://github.com/abcd"))
        .statusCode(200);
  }

  @Test
  void canLinkToNewAssignment() throws LtiSigningException {
    LtiSigner ltiSigner = new LtiOauthSigner();
    SessionFilter sessionFilter = new SessionFilter();
    Map<String, String> signedParameters = ltiSigner.signParameters(
        Map.of(
            "roles", "Instructor",
            "custom_canvas_assignment_id", "someId"
        ),
        "stevens",
        "stevens",
        "http://localhost:8081/lti/teacher/launch",
        "POST");

    given()
        .when()
        .filter(sessionFilter)
        .formParams(signedParameters)
        .post("/lti/teacher/launch");

    given()
        .when()
        .filter(sessionFilter)
        .formParam("assignment_id", "newId")
        .formParam("invitation_url", "https://github.com/xyz")
        .post("/lti/link")
        .then()
        .body(containsString("https://github.com/xyz"))
        .statusCode(200);
  }

  @Test
  void canLinkToExistingAssignment() throws LtiSigningException {
    SessionFilter sessionFilter = new SessionFilter();

    Map<String, String> signedParameters = signedLtiParams(
        "Instructor",
        "someId",
        "http://localhost:8081/lti/teacher/launch");

    given()
        .filter(sessionFilter)
        .when()
        .formParams(signedParameters)
        .post("/lti/teacher/launch");
    given()
        .when()
        .filter(sessionFilter)
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

  private Map<String, String> signedLtiParams(String role, String assignmentId, String url) throws LtiSigningException {
    LtiSigner ltiSigner = new LtiOauthSigner();
    return ltiSigner.signParameters(
        Map.of(
            "roles", role,
            "custom_canvas_assignment_id", assignmentId
        ),
        "stevens",
        "stevens",
        url,
        "POST");
  }

}