/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.item;

import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.ContentDto;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.enums.FeedbackItemType;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
public class GroupDto extends ContentDto {

  public GroupDto() {
    this.type = FeedbackItemType.GROUP.getValue();
  }

  public String getAlignment() {
    return alignment;
  }

  public List<ContentDto> getContent() {
    return content;
  }

  @Override
  public String toString() {
    return "GroupDto [alignment=" + alignment + ", content=" + content + "]";
  }
}
