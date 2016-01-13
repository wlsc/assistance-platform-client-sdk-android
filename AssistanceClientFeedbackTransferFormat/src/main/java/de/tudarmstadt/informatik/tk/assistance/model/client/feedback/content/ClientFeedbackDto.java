/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
public class ClientFeedbackDto {

  @SerializedName("moduleId")
  @Expose
  private String moduleId;

  @SerializedName("content")
  @Expose
  private ContentDto content;

  @SerializedName("created")
  @Expose
  private String created;

  public ClientFeedbackDto(String modulePackageName, ContentDto content, String created) {
    this.moduleId = modulePackageName;
    this.content = content;
    this.created = created;
  }

  public String getModuleId() {
    return moduleId;
  }

  public void setModuleId(String modulePackageName) {
    this.moduleId = modulePackageName;
  }

  public ContentDto getContent() {
    return content;
  }

  public void setContent(ContentDto content) {
    this.content = content;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  @Override
  public String toString() {
    return "ClientFeedbackDto [modulePackageName=" + moduleId + ", content=" + content
        + ", created=" + created + "]";
  }
}
