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
public class ImageDto implements IContentDto {

  @SerializedName("source")
  @Expose
  private String source;

  @SerializedName("target")
  @Expose
  private String target;

  @SerializedName("priority")
  @Expose
  private Integer priority;

  public ImageDto(String source) {
    this.source = source;
  }

  public ImageDto(String source, String target, Integer priority) {
    this.source = source;
    this.target = target;
    this.priority = priority;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
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
    return "ImageDto [source=" + source + ", target=" + target + ", priority=" + priority + "]";
  }
}
