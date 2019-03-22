package com.ifengxue.plugin.entity;

public class Column {

  /**
   * Database field name
   */
  private String columnName;
  /**
   * Entity field name
   */
  private String fieldName;
  /**
   * Field order
   */
  private int sort;
  /**
   * Database data type
   */
  private String dbDataType;
  /**
   * Java data type
   */
  private Class<?> javaDataType;
  /**
   * Is is primary key
   */
  private boolean primary;
  /**
   * Is it allow null
   */
  private boolean nullable;
  /**
   * Is it a self-incrementing field
   */
  private boolean autoIncrement;
  /**
   * Is there a default value
   */
  private boolean hasDefaultValue;
  /**
   * If default value is a string, it will be "default value"
   */
  private String defaultValue;
  /**
   * Field comment
   */
  private String columnComment;

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public int getSort() {
    return sort;
  }

  public void setSort(int sort) {
    this.sort = sort;
  }

  public String getDbDataType() {
    return dbDataType;
  }

  public void setDbDataType(String dbDataType) {
    this.dbDataType = dbDataType;
  }

  public Class<?> getJavaDataType() {
    return javaDataType;
  }

  public void setJavaDataType(Class<?> javaDataType) {
    this.javaDataType = javaDataType;
  }

  public boolean isPrimary() {
    return primary;
  }

  public void setPrimary(boolean primary) {
    this.primary = primary;
  }

  public boolean isNullable() {
    return nullable;
  }

  public void setNullable(boolean nullable) {
    this.nullable = nullable;
  }

  public boolean isAutoIncrement() {
    return autoIncrement;
  }

  public void setAutoIncrement(boolean autoIncrement) {
    this.autoIncrement = autoIncrement;
  }

  public boolean isHasDefaultValue() {
    return hasDefaultValue;
  }

  public void setHasDefaultValue(boolean hasDefaultValue) {
    this.hasDefaultValue = hasDefaultValue;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public String getColumnComment() {
    return columnComment;
  }

  public void setColumnComment(String columnComment) {
    this.columnComment = columnComment;
  }

  @Override
  public String toString() {
    return "Column{" +
        "columnName='" + columnName + '\'' +
        ", fieldName='" + fieldName + '\'' +
        ", sort=" + sort +
        ", dbDataType='" + dbDataType + '\'' +
        ", javaDataType=" + javaDataType +
        ", primary=" + primary +
        ", nullable=" + nullable +
        ", autoIncrement=" + autoIncrement +
        ", hasDefaultValue=" + hasDefaultValue +
        ", defaultValue='" + defaultValue + '\'' +
        ", columnComment='" + columnComment + '\'' +
        '}';
  }
}
