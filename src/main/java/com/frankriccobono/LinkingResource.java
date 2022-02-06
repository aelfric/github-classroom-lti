package com.frankriccobono;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.UnauthorizedException;
import net.oauth.server.OAuthServlet;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.imsglobal.lti.launch.LtiOauthVerifier;
import org.imsglobal.lti.launch.LtiVerificationException;
import org.imsglobal.lti.launch.LtiVerificationResult;
import org.imsglobal.lti.launch.LtiVerifier;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.Form;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Path("/lti")
public class LinkingResource {
  private static final Logger LOG = Logger.getLogger(LinkingResource.class);
  private static final String INSTRUCTOR = "Instructor";
  private static final String LEARNER = "Learner";

  @SuppressWarnings("CdiInjectionPointsInspection")
  @Inject
  Template github;

  @SuppressWarnings("CdiInjectionPointsInspection")
  @Inject
  Template setup;

  @Inject
  EntityManager entityManager;

  @ConfigProperty(name = "lti.secret")
  String secret;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String hello() {
    return "Hello RESTEasy";
  }

  @POST
  @Path("/go")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public Response recordRepoUrl(@FormParam("url") String url, @FormParam("redirect_url") String redirectUrl) {
    try {
      // Classroom will automatically create a pull request for instructor feedback.  This will
      // construct that link from student's repo URL
      final URI feedbackUrl = new URI(url + "/")
          .resolve("pull/1");

      final String encodedUrl = URLEncoder.encode(
          feedbackUrl.toString(),
          StandardCharsets.UTF_8
      );
      return Response.temporaryRedirect(
          new URI(redirectUrl + "?return_type=url&url=" + encodedUrl)
      ).build();
    } catch (URISyntaxException e) {
      throw new BadRequestException(e);
    }
  }

  @POST
  @Path("/link")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  @Transactional
  public TemplateInstance linkToAssignment(
      @FormParam("assignment_id") String assignmentId,
      @FormParam("invitation_url") String invitationUrl,
      @Context HttpServletRequest request
  ) {
    final HttpSession session = request.getSession();
    if (!INSTRUCTOR.equals(session.getAttribute("role"))) {
      throw new ForbiddenException("Must be an instructor to update link");
    }

    AssignmentLink link = entityManager.find(AssignmentLink.class, assignmentId);
    if (link == null) {
      link = new AssignmentLink(assignmentId, invitationUrl);
    } else {
      link.invitationLink = invitationUrl;
    }
    entityManager.persist(link);
    return setup
        .data("resourceId", assignmentId)
        .data("inviteUrl", invitationUrl);
  }

  @POST
  @Path("/teacher/launch")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance launchLtiForInstructor(
      @Form LtiLaunchParams ltiParams,
      @Context HttpServletRequest request
  ) {
    validateOauth(request);
    if (ltiParams.hasRole(INSTRUCTOR)) {
      final HttpSession session = request.getSession();
      session.setAttribute("role", INSTRUCTOR);
      AssignmentLink link = entityManager.find(AssignmentLink.class, ltiParams.assignmentId);
      return setup
          .data("resourceId", ltiParams.assignmentId)
          .data("inviteUrl", link == null ? "" : link.invitationLink);
    } else {
      throw new ForbiddenException("This view is only for teachers");
    }
  }

  @POST
  @Path("/student/launch")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance launchLtiForStudent(
      @Form LtiLaunchParams ltiParams,
      @Context HttpServletRequest request
  ) {
    validateOauth(request);
    AssignmentLink link = entityManager.find(AssignmentLink.class, ltiParams.assignmentId);

    if (ltiParams.hasRole(LEARNER)) {
      return github
          .data("redirect", ltiParams.extContentReturnUrl)
          .data("inviteUrl", link == null ? "" : link.invitationLink);
    } else {
      throw new ForbiddenException("This view is only for students");
    }
  }

  private void validateOauth(HttpServletRequest request) {
    LtiVerifier ltiVerifier = new LtiOauthVerifier();
    try {
      final LtiVerificationResult verify = ltiVerifier.verify(request, secret);
      if (!verify.getSuccess()) {
        LOG.warn("Invalid OAuth " + verify.getMessage() + " for " + OAuthServlet.getRequestURL(request));
        throw new UnauthorizedException(verify.getMessage());
      }
    } catch (LtiVerificationException e) {
      throw new BadRequestException(e);
    }
  }
}