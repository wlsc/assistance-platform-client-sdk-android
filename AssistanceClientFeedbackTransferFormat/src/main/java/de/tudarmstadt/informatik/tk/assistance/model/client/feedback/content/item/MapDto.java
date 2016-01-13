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
public class MapDto extends ContentDto {

  public Double[][] getPoints() {
    return points;
  }

  public Boolean getShowUserLocation() {
    return showUserLocation;
  }

  public Integer getPriority() {
    return priority;
  }

  public String getTarget() {
    return target;
  }

  @Override
  public String toString() {
    return "MapDto [points=" + Arrays.toString(points) + ", showUserLocation=" + showUserLocation
        + ", priority=" + priority + ", target=" + target + "]";
  }
}
