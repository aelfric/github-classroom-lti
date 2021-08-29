package com.frankriccobono;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.security.UnauthorizedException;
import oauth.signpost.exception.OAuthException;
import org.h2.mvstore.DataUtils;
import org.imsglobal.lti.launch.LtiOauthVerifier;
import org.imsglobal.lti.launch.LtiVerificationException;
import org.imsglobal.lti.launch.LtiVerificationResult;
import org.imsglobal.lti.launch.LtiVerifier;
import org.imsglobal.pox.IMSPOXRequest;
import org.jboss.resteasy.annotations.Form;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/hello")
public class ExampleResource {

  @Inject
  Template github;

  @Inject
  Template setup;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String hello() {
    return "Hello RESTEasy";
  }

  @POST
  @Path("/recordRepoUrl")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response launchLti(@FormParam("url") String url, @FormParam("redirect_url") String redirectUrl) {
    System.out.println(url);
    System.out.println(redirectUrl);
    final String encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8);
    try {
      return Response.temporaryRedirect(
          new URI(redirectUrl + "?return_type=url&url="+encodedUrl)
      ).build();
    } catch (URISyntaxException e) {
      throw new BadRequestException(e);
    }
  }

  @POST
  @Path("/launch")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response launchLti(@Form LtiLaunch body, @Context HttpServletRequest request) {

    LtiVerifier ltiVerifier = new LtiOauthVerifier();
    String key = request.getParameter("oauth_consumer_key");
    String secret = "stevens";
    try {
      final LtiVerificationResult verify = ltiVerifier.verify(request,secret);

//        IMSPOXRequest.sendReplaceResult(
//            body.lis_outcome_service_url,
//            "stevens",
//            "stevens",
//            body.lis_result_sourcedid,
//            "1.0"
//        );
      if(verify.getSuccess()) {
        if(body.roles.contains("Learner")) {
          return Response.ok(
              github
                  .data("redirect", body.ext_content_return_url)
          ).build();
        } else {
          return Response.ok(
              setup
                  .data("resourceId", body.resource_link_id)
          ).build();
        }
      } else {
        System.out.println(verify.getMessage());
        throw new UnauthorizedException();
      }

    } catch (LtiVerificationException e) {
      throw new BadRequestException(e);
    }

  }
}