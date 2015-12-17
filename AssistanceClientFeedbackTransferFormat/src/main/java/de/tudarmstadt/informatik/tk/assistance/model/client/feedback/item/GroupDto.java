/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback.item;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.ContentDto;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.enums.GroupAlignment;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
@Deprecated
public class GroupDto {

  @SerializedName("alignment")
  @Expose
  private GroupAlignment alignment;

  @SerializedName("content")
  @Expose
  private List<ContentDto> content;

  public GroupDto(List<ContentDto> content) {
    this.content = content;
  }

  public GroupDto(GroupAlignment alignment, List<ContentDto> content) {
    this.alignment = alignment;
    this.content = content;
  }

  public GroupAlignment getAlignment() {
    return alignment;
  }

  public void setAlignment(GroupAlignment alignment) {
    this.alignment = alignment;
  }

  public List<ContentDto> getContent() {
    return content;
  }

  public void setContent(List<ContentDto> content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "GroupDto [alignment=" + alignment + ", content=" + content + "]";
  }
}
