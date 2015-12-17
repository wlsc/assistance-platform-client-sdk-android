/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback;

import java.util.Arrays;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.enums.GroupAlignment;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.enums.TextAlignment;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 17.12.2015
 */
public class ContentDto {

  @SerializedName("type")
  @Expose
  private String type;

  @SerializedName("caption")
  @Expose
  private String caption;

  @SerializedName("target")
  @Expose
  private String target;

  @SerializedName("priority")
  @Expose
  private Integer priority;

  @SerializedName("groupAlignment")
  @Expose
  private GroupAlignment groupAlignment;

  @SerializedName("textAlignment")
  @Expose
  private TextAlignment textAlignment;

  @SerializedName("content")
  @Expose
  private List<ContentDto> content;

  @SerializedName("source")
  @Expose
  private String source;

  @SerializedName("points")
  @Expose
  private String[][] points;

  @SerializedName("showUserLocation")
  @Expose
  private Boolean showUserLocation;

  @SerializedName("style")
  @Expose
  private String[] style;

  @SerializedName("highlighted")
  @Expose
  private Boolean highlighted;

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

  public GroupAlignment getGroupAlignment() {
    return groupAlignment;
  }

  public void setGroupAlignment(GroupAlignment groupAlignment) {
    this.groupAlignment = groupAlignment;
  }

  public TextAlignment getTextAlignment() {
    return textAlignment;
  }

  public void setTextAlignment(TextAlignment textAlignment) {
    this.textAlignment = textAlignment;
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

  public String[][] getPoints() {
    return points;
  }

  public void setPoints(String[][] points) {
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
        + ", priority=" + priority + ", groupAlignment=" + groupAlignment + ", textAlignment="
        + textAlignment + ", content=" + content + ", source=" + source + ", points="
        + Arrays.toString(points) + ", showUserLocation=" + showUserLocation + ", style="
        + Arrays.toString(style) + ", highlighted=" + highlighted + "]";
  }
}
