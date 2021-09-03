package com.frankriccobono;

import javax.ws.rs.FormParam;

/**
 * When the LMS first contacts this application, it sends a number of attributes via POST request.  This
 * class extracts relevant ones used in this application.
 */
public class LtiLaunchParams {
  @FormParam("context_id")
  public String contextId;

  @FormParam("context_label")
  public String contextLabel;

  @FormParam("context_title")
  public String contextTitle;

  @FormParam("roles")
  public String roles;

  @FormParam("user_id")
  public String userId;

  @FormParam("ext_content_return_url")
  public String extContentReturnUrl;

  @FormParam("custom_canvas_assignment_id")
  public String assignmentId;

  boolean hasRole(String learner) {
    return roles.contains(learner);
  }
}
