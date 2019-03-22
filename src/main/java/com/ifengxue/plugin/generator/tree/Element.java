package com.ifengxue.plugin.generator.tree;

import com.ifengxue.plugin.generator.tree.visitor.Visitor;
import java.util.List;

public interface Element {

  String LINE_SEPARATOR = "\r\n";
  String SERIAL_VERSION_UID = "serialVersionUID";

  void setLineSeparator(String lineSeparator);

  /**
   * Set the indentation mode
   */
  void setIndent(Indent indent);

  /**
   * Set parent element
   */
  void setParent(Element parent);

  /**
   * Get the parent element of the current element
   *
   * @return Return null if the current element has no parent
   */
  Element parent();

  /**
   * Look up from the current element and return the matching element
   */
  Element parents(String name);

  /**
   * Add a child node to the current node
   */
  void addChild(Element child);

  /**
   * Get the immediate child of the current element
   */
  List<Element> children();

  /**
   * Get the first sibling element with the same name as the sibling element
   */
  Element firstSibling(String name);

  /**
   * Get all siblings with the same name as the sibling
   */
  List<Element> siblings(String name);

  /**
   * Get all the sibling elements of a statistic element
   */
  List<Element> siblings();

  /**
   * Get the current element name
   */
  String name();

  /**
   * Generate source code
   */
  String toJavaCode();

  void accept(Visitor visitor);

  enum Indent {
    TAB {
      @Override
      public String getIndent() {
        return "\t";
      }
    },
    TWO_SPACE {
      @Override
      public String getIndent() {
        return "  ";
      }
    },
    FOUR_SPACE {
      @Override
      public String getIndent() {
        return "    ";
      }
    };

    public abstract String getIndent();

    public String getDoubleIndent() {
      return getIndent() + getIndent();
    }

    public static Indent findByDTDDeclare(String dtdDeclare) {
      if ("tab".equals(dtdDeclare)) {
        return TAB;
      }
      if ("2space".equals(dtdDeclare)) {
        return TWO_SPACE;
      }
      if ("4space".equals(dtdDeclare)) {
        return FOUR_SPACE;
      }
      return FOUR_SPACE;
    }
  }

  class KeyValuePair {

    private final String key;
    private final String value;

    private KeyValuePair(String key, String value) {
      this.key = key;
      this.value = value;
    }

    /**
     * Value is generated in the normal form, such as number, boolean...
     */
    public static KeyValuePair newKeyValuePair(String key, String value) {
      return new KeyValuePair(key, value);
    }

    /**
     * Value is generated as a string
     */
    public static KeyValuePair newKeyAndStringValuePair(String key, String value) {
      return new KeyValuePair(key, "\"" + value + "\"");
    }

    public String getKey() {
      return key;
    }

    public String getValue() {
      return value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      KeyValuePair that = (KeyValuePair) o;

      return key.equals(that.key) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
      int result = key.hashCode();
      result = 31 * result + value.hashCode();
      return result;
    }
  }
}
