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
  private String modulePackageName;

  @SerializedName("content")
  @Expose
  private ContentDto content;

  @SerializedName("created")
  @Expose
  private String created;

  public ClientFeedbackDto(String modulePackageName, ContentDto content, String created) {
    this.modulePackageName = modulePackageName;
    this.content = content;
    this.created = created;
  }

  public String getModulePackageName() {
    return modulePackageName;
  }

  public void setModulePackageName(String modulePackageName) {
    this.modulePackageName = modulePackageName;
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
    return "ClientFeedbackDto [modulePackageName=" + modulePackageName + ", content=" + content
        + ", created=" + created + "]";
  }
}
