package com.frankriccobono;

import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AssignmentLink {
  @Id
  @NaturalId
  public String assignmentId;

  public String invitationLink;

  protected AssignmentLink(){

  }

  public AssignmentLink(String assignmentId, String invitationUrl) {
    this.assignmentId = assignmentId;
    this.invitationLink = invitationUrl;
  }
}
