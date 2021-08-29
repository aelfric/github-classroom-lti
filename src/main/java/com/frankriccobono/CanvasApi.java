package com.frankriccobono;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


@Path("/api/v1")
@RegisterRestClient
public interface CanvasApi {
/**
 * /api/v1/courses/:course_id/assignments/:assignment_id/submissions
 */
  @GET
  @Path("courses/{course_id}/assignments/{assignment_id}/submissions")
  String getByName(@PathParam("course_id") String courseId, @PathParam("assignment_id") String assignmentId);

}
