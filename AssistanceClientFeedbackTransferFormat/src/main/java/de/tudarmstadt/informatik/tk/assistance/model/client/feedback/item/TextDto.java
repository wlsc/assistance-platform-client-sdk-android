/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback.item;

import java.util.Arrays;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.enums.TextAlignment;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
public class TextDto implements IContentDto {

  @SerializedName("style")
  @Expose
  private String[] style;

  @SerializedName("highlighted")
  @Expose
  private Boolean highlighted;

  @SerializedName("alignment")
  @Expose
  private TextAlignment alignment;

  @SerializedName("target")
  @Expose
  private String target;

  @SerializedName("priority")
  @Expose
  private Integer priority;

  public TextDto(String[] style, Boolean highlighted, TextAlignment alignment) {
    this.style = style;
    this.highlighted = highlighted;
    this.alignment = alignment;
  }

  public TextDto(String[] style, Boolean highlighted, TextAlignment alignment, String target,
      Integer priority) {
    this.style = style;
    this.highlighted = highlighted;
    this.alignment = alignment;
    this.target = target;
    this.priority = priority;
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

  public TextAlignment getAlignment() {
    return alignment;
  }

  public void setAlignment(TextAlignment alignment) {
    this.alignment = alignment;
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
    return "TextDto [style=" + Arrays.toString(style) + ", highlighted=" + highlighted
        + ", alignment=" + alignment + ", target=" + target + ", priority=" + priority + "]";
  }
}
