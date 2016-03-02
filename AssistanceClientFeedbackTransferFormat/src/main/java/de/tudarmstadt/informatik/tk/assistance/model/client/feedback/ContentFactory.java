package de.tudarmstadt.informatik.tk.assistance.model.client.feedback;

import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.ContentDto;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.enums.FeedbackItemType;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.enums.GroupAlignment;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.enums.TextAlignment;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.item.ButtonDto;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.item.GroupDto;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.item.ImageDto;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.item.MapDto;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.item.TextDto;

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

  public static ContentDto createMap(Double[][] points) {

    return createMap(points, null, null, null);
  }

  public static ContentDto createMap(Double[][] points, Boolean showUserLocation) {

    return createMap(points, showUserLocation, null, null);
  }

  public static ContentDto createMap(Double[][] points, Boolean showUserLocation, String target) {

    return createMap(points, showUserLocation, target, null);
  }

  public static ContentDto createMap(Double[][] points, Boolean showUserLocation, String target,
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

  public static ContentDto createImage(String source) {
    return createImage(source, null, null);
  }

  public static ContentDto createImage(String source, String target) {
    return createImage(source, target, null);
  }

  public static ContentDto createImage(String source, String target, Integer priority) {

    if (source == null || source.isEmpty()) {
      return null;
    }

    ImageDto image = new ImageDto();

    image.setSource(source);

    if (target != null && !target.isEmpty()) {
      image.setTarget(target);
    }

    if (priority != null) {
      image.setPriority(priority);
    }

    return image;
  }

  public static GroupDto getGroup(ContentDto contentDto) {

    if (contentDto == null) {
      return new GroupDto();
    }

    GroupDto groupDto = new GroupDto();

    groupDto.setAlignment(contentDto.getAlignment());
    groupDto.setContent(contentDto.getContent());

    return groupDto;
  }

  public static ButtonDto getButton(ContentDto contentDto) {

    if (contentDto == null) {
      return new ButtonDto();
    }

    ButtonDto buttonDto = new ButtonDto();

    buttonDto.setCaption(contentDto.getCaption());
    buttonDto.setTarget(contentDto.getTarget());
    buttonDto.setPriority(contentDto.getPriority());

    return buttonDto;
  }

  public static TextDto getText(ContentDto contentDto) {

    if (contentDto == null) {
      return new TextDto();
    }

    TextDto textDto = new TextDto();

    textDto.setCaption(contentDto.getCaption());
    textDto.setStyle(contentDto.getStyle());
    textDto.setHighlighted(contentDto.getHighlighted());
    textDto.setAlignment(contentDto.getAlignment());
    textDto.setTarget(contentDto.getTarget());
    textDto.setPriority(contentDto.getPriority());

    return textDto;
  }

  public static ImageDto getImage(ContentDto contentDto) {

    if (contentDto == null) {
      return new ImageDto();
    }

    ImageDto imageDto = new ImageDto();

    imageDto.setSource(contentDto.getSource());
    imageDto.setTarget(contentDto.getTarget());
    imageDto.setPriority(contentDto.getPriority());

    return imageDto;
  }

  public static MapDto getMap(ContentDto contentDto) {

    if (contentDto == null) {
      return new MapDto();
    }

    MapDto mapDto = new MapDto();

    mapDto.setPoints(contentDto.getPoints());
    mapDto.setShowUserLocation(contentDto.getShowUserLocation());
    mapDto.setPriority(contentDto.getPriority());
    mapDto.setTarget(contentDto.getTarget());

    return mapDto;
  }
}
