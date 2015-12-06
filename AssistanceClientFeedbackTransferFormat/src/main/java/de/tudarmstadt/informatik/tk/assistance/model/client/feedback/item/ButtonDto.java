/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
public class ButtonDto implements IContentDto {

  @SerializedName("caption")
  @Expose
  private String caption;

  @SerializedName("target")
  @Expose
  private String target;

  @SerializedName("priority")
  @Expose
  private Integer priority;

  public ButtonDto(String caption, String target) {
    this.caption = caption;
    this.target = target;
  }

  public ButtonDto(String caption, String target, Integer priority) {
    this.caption = caption;
    this.target = target;
    this.priority = priority;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  @Override
  public String toString() {
    return "ButtonDto [caption=" + caption + ", target=" + target + ", priority=" + priority + "]";
  }
}
