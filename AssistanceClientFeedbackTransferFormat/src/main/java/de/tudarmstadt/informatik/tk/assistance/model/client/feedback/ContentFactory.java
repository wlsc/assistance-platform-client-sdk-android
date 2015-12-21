package de.tudarmstadt.informatik.tk.assistance.model.client.feedback;

import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.ContentDto;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.enums.FeedbackItemType;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.enums.GroupAlignment;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.enums.TextAlignment;

public class ContentFactory {

  private ContentFactory() {}

  private static ContentDto createContentType(FeedbackItemType type) {

    ContentDto content = new ContentDto();

    content.setType(type.getValue());

    return content;
  }

  public static ContentDto createGroup(List<ContentDto> content) {

    return createGroup(content, null);
  }

  public static ContentDto createGroup(List<ContentDto> content, GroupAlignment alignment) {

    ContentDto group = createContentType(FeedbackItemType.GROUP);

    group.setContent(content);
    group.setAlignment(alignment == null ? null : alignment.getValue());

    return group;
  }

  public static ContentDto createButton(String caption, String target) {

    return createButton(caption, target, null);
  }

  public static ContentDto createButton(String caption, String target, Integer priority) {

    ContentDto button = createContentType(FeedbackItemType.BUTTON);

    button.setCaption(caption);
    button.setTarget(target);
    button.setPriority(priority);

    return button;
  }

  public static ContentDto createMap(String[][] points) {

    return createMap(points, null, null, null);
  }

  public static ContentDto createMap(String[][] points, Boolean showUserLocation) {

    return createMap(points, showUserLocation, null, null);
  }

  public static ContentDto createMap(String[][] points, Boolean showUserLocation, String target) {

    return createMap(points, showUserLocation, target, null);
  }

  public static ContentDto createMap(String[][] points, Boolean showUserLocation, String target,
      Integer priority) {

    ContentDto map = createContentType(FeedbackItemType.MAP);

    map.setPoints(points);
    map.setShowUserLocation(showUserLocation);
    map.setTarget(target);
    map.setPriority(priority);

    return map;
  }

  public static ContentDto createText(String caption, Boolean highlighted,
      TextAlignment alignment) {

    return createText(caption, highlighted, alignment, null, null, null);
  }

  public static ContentDto createText(String caption, Boolean highlighted, TextAlignment alignment,
      String[] style) {

    return createText(caption, highlighted, alignment, style, null, null);
  }

  public static ContentDto createText(String caption, Boolean highlighted, TextAlignment alignment,
      String[] style, String target) {

    return createText(caption, highlighted, alignment, style, target, null);
  }

  public static ContentDto createText(String caption, Boolean highlighted, TextAlignment alignment,
      String[] style, String target, Integer priority) {

    ContentDto text = createContentType(FeedbackItemType.TEXT);

    text.setCaption(caption);
    text.setHighlighted(highlighted);
    text.setAlignment(alignment == null ? null : alignment.getValue());
    text.setStyle(style);
    text.setTarget(target);
    text.setPriority(priority);

    return text;
  }
}
