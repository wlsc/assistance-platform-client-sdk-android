/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.enums;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 17.12.2015
 */
public enum FeedbackItemType {

  GROUP("group"), BUTTON("button"), IMAGE("image"), MAP("map"), TEXT("text");

  private String value;

  private FeedbackItemType(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  public static FeedbackItemType getEnum(String enumString) {

    for (FeedbackItemType entry : FeedbackItemType.values()) {
      if (entry.value.equalsIgnoreCase(enumString)) {
        return entry;
      }
    }

    return null;
  }
}
