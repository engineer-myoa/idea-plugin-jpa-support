package com.ifengxue.plugin.entity;

import java.util.List;

public class Table {

  /**
   * Whether it is selected, the class can be generated after selection
   */
  private boolean selected;

  /**
   * Table Name
   */
  private String tableName;

  /**
   * Table comment
   */
  private String tableComment;

  /**
   * Database Name
   */
  private String tableSchema;

  /**
   * Entity Name
   */
  private String entityName;

  /**
   * Package Name
   */
  private String packageName;

  /**
   * Primary key type
   */
  private Class<?> primaryKeyClassType;

  private List<Column> columns;

  public static Table from(TableSchema tableSchema, String entityName, boolean selected) {
    Table table = new Table();
    table.setTableName(tableSchema.getTableName());
    table.setTableComment(tableSchema.getTableComment());
    table.setTableSchema(tableSchema.getTableSchema());
    table.setEntityName(entityName);
    table.setSelected(selected);
    return table;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getTableComment() {
    return tableComment;
  }

  public void setTableComment(String tableComment) {
    this.tableComment = tableComment;
  }

  public String getTableSchema() {
    return tableSchema;
  }

  public void setTableSchema(String tableSchema) {
    this.tableSchema = tableSchema;
  }

  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public List<Column> getColumns() {
    return columns;
  }

  public void setColumns(List<Column> columns) {
    this.columns = columns;
  }

  public Class<?> getPrimaryKeyClassType() {
    return primaryKeyClassType;
  }

  public void setPrimaryKeyClassType(Class<?> primaryKeyClassType) {
    this.primaryKeyClassType = primaryKeyClassType;
  }

  @Override
  public String toString() {
    return "Table{" +
        "selected=" + selected +
        ", tableName='" + tableName + '\'' +
        ", tableComment='" + tableComment + '\'' +
        ", tableSchema='" + tableSchema + '\'' +
        ", entityName='" + entityName + '\'' +
        ", packageName='" + packageName + '\'' +
        ", columns=" + columns +
        '}';
  }
}
