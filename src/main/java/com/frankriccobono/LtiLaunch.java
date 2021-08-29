package com.frankriccobono;

import javax.ws.rs.FormParam;

public class LtiLaunch {
  @FormParam("oauth_consumer_key")
  public String oauth_consumer_key;
  @FormParam("oauth_signature_method")
  public String oauth_signature_method;
  @FormParam("oauth_timestamp")
  public String oauth_timestamp;
  @FormParam("oauth_nonce")
  public String oauth_nonce;
  @FormParam("oauth_version")
  public String oauth_version;
  @FormParam("context_id")
  public String context_id;
  @FormParam("context_label")
  public String context_label;
  @FormParam("context_title")
  public String context_title;
  @FormParam("custom_canvas_api_domain")
  public String custom_canvas_api_domain;
  @FormParam("custom_canvas_course_id")
  public String custom_canvas_course_id;
  @FormParam("custom_canvas_enrollment_state")
  public String custom_canvas_enrollment_state;
  @FormParam("custom_canvas_user_id")
  public String custom_canvas_user_id;
  @FormParam("custom_canvas_user_login_id")
  public String custom_canvas_user_login_id;
  @FormParam("custom_canvas_workflow_state")
  public String custom_canvas_workflow_state;
  @FormParam("ext_roles")
  public String ext_roles;
  @FormParam("launch_presentation_document_target")
  public String launch_presentation_document_target;
  @FormParam("launch_presentation_height")
  public String launch_presentation_height;
  @FormParam("launch_presentation_locale")
  public String launch_presentation_locale;
  @FormParam("launch_presentation_return_url")
  public String launch_presentation_return_url;
  @FormParam("launch_presentation_width")
  public String launch_presentation_width;
  @FormParam("lis_person_contact_email_primary")
  public String lis_person_contact_email_primary;
  @FormParam("lis_person_name_family")
  public String lis_person_name_family;
  @FormParam("lis_person_name_full")
  public String lis_person_name_full;
  @FormParam("lis_person_name_given")
  public String lis_person_name_given;
  @FormParam("lis_person_sourcedid")
  public String lis_person_sourcedid;
  @FormParam("lti_message_type")
  public String lti_message_type;
  @FormParam("lti_version")
  public String lti_version;
  @FormParam("oauth_callback")
  public String oauth_callback;
  @FormParam("resource_link_id")
  public String resource_link_id;
  @FormParam("resource_link_title")
  public String resource_link_title;
  @FormParam("roles")
  public String roles;
  @FormParam("tool_consumer_info_product_family_code")
  public String tool_consumer_info_product_family_code;
  @FormParam("tool_consumer_info_version")
  public String tool_consumer_info_version;
  @FormParam("tool_consumer_instance_contact_email")
  public String tool_consumer_instance_contact_email;
  @FormParam("tool_consumer_instance_guid")
  public String tool_consumer_instance_guid;
  @FormParam("tool_consumer_instance_name")
  public String tool_consumer_instance_name;
  @FormParam("user_id")
  public String user_id;
  @FormParam("user_image")
  public String user_image;
  @FormParam("lis_outcome_service_url")
  public String lis_outcome_service_url;
  @FormParam("lis_result_sourcedid")
  public String lis_result_sourcedid;
  @FormParam("ext_content_return_url")
  public String ext_content_return_url;
  @FormParam("oauth_signature")
  String oauth_signature;

  @Override
  public String toString() {
    return "LtiLaunch{" +
        "oauth_consumer_key='" + oauth_consumer_key + '\'' +
        ", oauth_signature_method='" + oauth_signature_method + '\'' +
        ", oauth_timestamp='" + oauth_timestamp + '\'' +
        ", oauth_nonce='" + oauth_nonce + '\'' +
        ", oauth_version='" + oauth_version + '\'' +
        ", context_id='" + context_id + '\'' +
        ", context_label='" + context_label + '\'' +
        ", context_title='" + context_title + '\'' +
        ", custom_canvas_api_domain='" + custom_canvas_api_domain + '\'' +
        ", custom_canvas_course_id='" + custom_canvas_course_id + '\'' +
        ", custom_canvas_enrollment_state='" + custom_canvas_enrollment_state + '\'' +
        ", custom_canvas_user_id='" + custom_canvas_user_id + '\'' +
        ", custom_canvas_user_login_id='" + custom_canvas_user_login_id + '\'' +
        ", custom_canvas_workflow_state='" + custom_canvas_workflow_state + '\'' +
        ", ext_roles='" + ext_roles + '\'' +
        ", launch_presentation_document_target='" + launch_presentation_document_target + '\'' +
        ", launch_presentation_height='" + launch_presentation_height + '\'' +
        ", launch_presentation_locale='" + launch_presentation_locale + '\'' +
        ", launch_presentation_return_url='" + launch_presentation_return_url + '\'' +
        ", launch_presentation_width='" + launch_presentation_width + '\'' +
        ", lis_person_contact_email_primary='" + lis_person_contact_email_primary + '\'' +
        ", lis_person_name_family='" + lis_person_name_family + '\'' +
        ", lis_person_name_full='" + lis_person_name_full + '\'' +
        ", lis_person_name_given='" + lis_person_name_given + '\'' +
        ", lis_person_sourcedid='" + lis_person_sourcedid + '\'' +
        ", lti_message_type='" + lti_message_type + '\'' +
        ", lti_version='" + lti_version + '\'' +
        ", oauth_callback='" + oauth_callback + '\'' +
        ", resource_link_id='" + resource_link_id + '\'' +
        ", resource_link_title='" + resource_link_title + '\'' +
        ", roles='" + roles + '\'' +
        ", tool_consumer_info_product_family_code='" + tool_consumer_info_product_family_code + '\'' +
        ", tool_consumer_info_version='" + tool_consumer_info_version + '\'' +
        ", tool_consumer_instance_contact_email='" + tool_consumer_instance_contact_email + '\'' +
        ", tool_consumer_instance_guid='" + tool_consumer_instance_guid + '\'' +
        ", tool_consumer_instance_name='" + tool_consumer_instance_name + '\'' +
        ", user_id='" + user_id + '\'' +
        ", user_image='" + user_image + '\'' +
        ", oauth_signature='" + oauth_signature + '\'' +
        '}';
  }
}
