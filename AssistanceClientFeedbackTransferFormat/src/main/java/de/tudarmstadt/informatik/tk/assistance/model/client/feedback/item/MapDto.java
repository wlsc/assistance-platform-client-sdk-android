/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback.item;

import java.util.Arrays;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
@Deprecated
public class MapDto {

  @SerializedName("points")
  @Expose
  private String[][] points;

  @SerializedName("showUserLocation")
  @Expose
  private Boolean showUserLocation;

  @SerializedName("priority")
  @Expose
  private Integer priority;

  @SerializedName("target")
  @Expose
  private String target;

  public MapDto(String[][] points) {
    this.points = points;
  }

  public MapDto(String[][] points, Boolean showUserLocation) {
    this.points = points;
    this.showUserLocation = showUserLocation;
  }

  public MapDto(String[][] points, Boolean showUserLocation, Integer priority, String target) {
    this.points = points;
    this.showUserLocation = showUserLocation;
    this.priority = priority;
    this.target = target;
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

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  @Override
  public String toString() {
    return "MapDto [points=" + Arrays.toString(points) + ", showUserLocation=" + showUserLocation
        + ", priority=" + priority + ", target=" + target + "]";
  }
}
