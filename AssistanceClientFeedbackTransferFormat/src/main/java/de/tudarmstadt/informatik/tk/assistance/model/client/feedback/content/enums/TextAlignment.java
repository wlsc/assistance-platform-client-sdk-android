/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.enums;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
public enum TextAlignment {
  LEFT("left"), CENTER("center"), RIGHT("right");

  private String value;

  private TextAlignment(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  public static TextAlignment getEnum(String enumString) {

    for (TextAlignment entry : TextAlignment.values()) {
      if (entry.value.equalsIgnoreCase(enumString)) {
        return entry;
      }
    }

    return null;
  }
}
