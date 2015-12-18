/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.model.client.feedback.enums;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
public enum GroupAlignment {
  HORIZONTAL("horizontal"), VERTICAL("vertical");

  private String value;

  private GroupAlignment(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  public static GroupAlignment getEnum(String enumString) {

    for (GroupAlignment entry : GroupAlignment.values()) {
      if (entry.value.equalsIgnoreCase(enumString)) {
        return entry;
      }
    }

    return null;
  }
}
