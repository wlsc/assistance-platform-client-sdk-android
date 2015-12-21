/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.item;

import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.ContentDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
public class ButtonDto extends ContentDto {

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
