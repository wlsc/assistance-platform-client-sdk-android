/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content;

import java.util.Arrays;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 17.12.2015
 */
public class ContentDto {

  @SerializedName("type")
  @Expose
  protected String type;

  @SerializedName("caption")
  @Expose
  protected String caption;

  @SerializedName("target")
  @Expose
  protected String target;

  @SerializedName("priority")
  @Expose
  protected Integer priority;

  @SerializedName("alignment")
  @Expose
  protected String alignment;

  @SerializedName("content")
  @Expose
  protected List<ContentDto> content;

  @SerializedName("source")
  @Expose
  protected String source;

  @SerializedName("points")
  @Expose
  protected Double[][] points;

  @SerializedName("showUserLocation")
  @Expose
  protected Boolean showUserLocation;

  @SerializedName("style")
  @Expose
  protected String[] style;

  @SerializedName("highlighted")
  @Expose
  protected Boolean highlighted;

  public ContentDto() {}

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

  public String getAlignment() {
    return alignment;
  }

  public void setAlignment(String alignment) {
    this.alignment = alignment;
  }

  public List<ContentDto> getContent() {
    return content;
  }

  public void setContent(List<ContentDto> content) {
    this.content = content;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public Double[][] getPoints() {
    return points;
  }

  public void setPoints(Double[][] points) {
    this.points = points;
  }

  public Boolean getShowUserLocation() {
    return showUserLocation;
  }

  public void setShowUserLocation(Boolean showUserLocation) {
    this.showUserLocation = showUserLocation;
  }

  public String[] getStyle() {
    return style;
  }

  public void setStyle(String[] style) {
    this.style = style;
  }

  public Boolean getHighlighted() {
    return highlighted;
  }

  public void setHighlighted(Boolean highlighted) {
    this.highlighted = highlighted;
  }

  @Override
  public String toString() {
    return "ContentDto [type=" + type + ", caption=" + caption + ", target=" + target
        + ", priority=" + priority + ", alignment=" + alignment + ", content=" + content
        + ", source=" + source + ", points=" + Arrays.toString(points) + ", showUserLocation="
        + showUserLocation + ", style=" + Arrays.toString(style) + ", highlighted=" + highlighted
        + "]";
  }
}
