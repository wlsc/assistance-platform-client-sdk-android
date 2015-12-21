/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.item;

import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.ContentDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
public class ImageDto extends ContentDto {

  public String getSource() {
    return source;
  }

  public String getTarget() {
    return target;
  }

  public Integer getPriority() {
    return priority;
  }

  @Override
  public String toString() {
    return "ImageDto [source=" + source + ", target=" + target + ", priority=" + priority + "]";
  }
}
