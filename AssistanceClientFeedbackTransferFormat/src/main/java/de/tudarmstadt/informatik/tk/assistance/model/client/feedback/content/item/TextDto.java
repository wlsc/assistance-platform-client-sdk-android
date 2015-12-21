/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.item;

import java.util.Arrays;

import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.ContentDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
public class TextDto extends ContentDto {

  public String getCaption() {
    return caption;
  }

  public String[] getStyle() {
    return style;
  }

  public Boolean getHighlighted() {
    return highlighted;
  }

  public String getAlignment() {
    return alignment;
  }

  public String getTarget() {
    return target;
  }

  public Integer getPriority() {
    return priority;
  }

  @Override
  public String toString() {
    return "TextDto [caption=" + caption + ", style=" + Arrays.toString(style) + ", highlighted="
        + highlighted + ", alignment=" + alignment + ", target=" + target + ", priority=" + priority
        + "]";
  }
}
