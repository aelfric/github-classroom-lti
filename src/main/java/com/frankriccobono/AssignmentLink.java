package com.frankriccobono;

import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AssignmentLink {
  @Id
  @NaturalId
  @Column(name = "assignment_id")
  public String assignmentId;

  @Column(name = "invitation_link")
  public String invitationLink;

  protected AssignmentLink(){

  }

  public AssignmentLink(String assignmentId, String invitationUrl) {
    this.assignmentId = assignmentId;
    this.invitationLink = invitationUrl;
  }
}
