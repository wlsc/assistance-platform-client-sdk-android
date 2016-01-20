/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.item;

import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.ContentDto;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.enums.FeedbackItemType;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
public class ButtonDto extends ContentDto {

  public ButtonDto() {
    this.type = FeedbackItemType.BUTTON.getValue();
  }

  public String getCaption() {
    return caption;
  }

  public String getTarget() {
    return target;
  }

  public Integer getPriority() {
    return priority;
  }

  @Override
  public String toString() {
    return "ButtonDto [caption=" + caption + ", target=" + target + ", priority=" + priority + "]";
  }
}
