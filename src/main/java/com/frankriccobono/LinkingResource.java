package com.frankriccobono;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.UnauthorizedException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.imsglobal.lti.launch.LtiOauthVerifier;
import org.imsglobal.lti.launch.LtiVerificationException;
import org.imsglobal.lti.launch.LtiVerificationResult;
import org.imsglobal.lti.launch.LtiVerifier;
import org.jboss.resteasy.annotations.Form;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
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
    final String encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8);
    try {
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
      @FormParam("invitation_url") String invitationUrl
  ) {
    AssignmentLink link = entityManager.find(AssignmentLink.class, assignmentId);
    if(link == null) {
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
  @Path("/instructor/launch")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance launchLtiForInstructor(
      @Form LtiLaunchParams body,
      @Context HttpServletRequest request
  ) {
    validateOauth(request);
    if (body.roles.contains("Instructor")) {
      AssignmentLink link = entityManager.find(AssignmentLink.class, body.assignmentId);
      return setup
          .data("resourceId", body.assignmentId)
          .data("inviteUrl", link == null ? "" : link.invitationLink);
    } else {
      throw new ForbiddenException("This view is only for instructors");
    }
  }

  @POST
  @Path("/student/launch")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance launchLtiForStudent(
      @Form LtiLaunchParams body,
      @Context HttpServletRequest request
  ) {
    validateOauth(request);
    AssignmentLink link = entityManager.find(AssignmentLink.class, body.assignmentId);

    if (body.roles.contains("Learner")) {
      return github
          .data("redirect", body.extContentReturnUrl)
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
        System.err.println(verify.getMessage());
        throw new UnauthorizedException(verify.getMessage());
      }
    } catch (LtiVerificationException e) {
      throw new BadRequestException(e);
    }
  }
}