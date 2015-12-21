package de.tudarmstadt.informatik.tk.assistance.model.client.feedback;

import java.util.List;
import java.util.function.Consumer;

import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.enums.FeedbackItemType;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.enums.GroupAlignment;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.enums.TextAlignment;

public class ContentFactory {	
	private static ContentDto createContent(FeedbackItemType type, Consumer<ContentDto> adjustMethod) {
		ContentDto c = new ContentDto();
		
		c.setType(type.getValue());
		
		adjustMethod.accept(c);
		
		return c;
	}
	
	public static ContentDto createButton(String caption, String target) {
		return createContent(FeedbackItemType.BUTTON, (c) -> {
			c.setCaption(caption);
			c.setTarget(target);
		});
	}

	public static ContentDto createGroup(GroupAlignment alignment,
			List<ContentDto> content) {
		return createContent(FeedbackItemType.GROUP, (c) -> {
			c.setAlignment(alignment.getValue());
			c.setContent(content);
		});
	}

	public static ContentDto createImage(String source, String target, Integer priority) {
		return createContent(FeedbackItemType.GROUP, (c) -> {
			c.setSource(source);
			c.setTarget(target);
			c.setPriority(priority);
		});
	}

	public static ContentDto createMap(String[][] points) {
		return createMap(points, true);
	}

	public static ContentDto createMap(String[][] points, Boolean showUserLocation) {
		return createMap(points, showUserLocation, 0, null);
	}

	public static ContentDto createMap(String[][] points, Boolean showUserLocation,
			Integer priority, String target) {
		return createContent(FeedbackItemType.GROUP, (c) -> {
			c.setPoints(points);
			c.setShowUserLocation(showUserLocation);
			c.setPriority(priority);
			c.setTarget(target);
		});
	}

	public static ContentDto createText(String caption, String[] style,
			Boolean highlighted, TextAlignment alignment) {
		return createText(caption, style, highlighted, alignment, null, 0);
	}

	public static ContentDto createText(String caption, String[] style,
			Boolean highlighted, TextAlignment alignment, String target,
			Integer priority) {
		return createContent(FeedbackItemType.GROUP, (c) -> {
			c.setCaption(caption);
			c.setStyle(style);
			c.setHighlighted(highlighted);
			c.setAlignment(alignment.getValue());
			c.setPriority(priority);
			c.setTarget(target);
		});
	}
}
