package com.ifengxue.plugin.util;

import com.intellij.openapi.diagnostic.Logger;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * String tool class
 */
public class StringHelper {

  private static Logger log = Logger.getInstance(StringHelper.class);
  private static final Map<Class<?>, Class<?>> WRAPPER_DATA_TYPE_AND_PRIMITIVE_DATA_TYPE = new HashMap<>();

  static {
    WRAPPER_DATA_TYPE_AND_PRIMITIVE_DATA_TYPE.put(Boolean.class, boolean.class);
    WRAPPER_DATA_TYPE_AND_PRIMITIVE_DATA_TYPE.put(Integer.class, int.class);
    WRAPPER_DATA_TYPE_AND_PRIMITIVE_DATA_TYPE.put(Float.class, float.class);
    WRAPPER_DATA_TYPE_AND_PRIMITIVE_DATA_TYPE.put(Long.class, long.class);
    WRAPPER_DATA_TYPE_AND_PRIMITIVE_DATA_TYPE.put(Double.class, double.class);
  }

  /**
   * Parsing the basic type of encapsulation type
   */
  public static Class<?> getWrapperClass(Class<?> clazz) {
    Map<String, Class<?>> clazzWrapper = new HashMap<>();
    clazzWrapper.put("clazz", clazz);
    WRAPPER_DATA_TYPE_AND_PRIMITIVE_DATA_TYPE.forEach((key, value) -> {
      if (value.equals(clazz)) {
        clazzWrapper.put("clazz", key);
      }
    });
    return clazzWrapper.get("clazz");
  }

  /**
   * Get the basic data type of the package type
   */
  public static Class<?> getPrimitiveClass(Class<?> clazz) {
    return WRAPPER_DATA_TYPE_AND_PRIMITIVE_DATA_TYPE.getOrDefault(clazz, clazz);
  }

  public static Class<?> parseJavaDataType(String dbDataType, String columnName, boolean isUseWrapper) {
    dbDataType = dbDataType.toUpperCase();
    Class<?> javaDataType;
    switch (dbDataType) {
      case "CLOB":
      case "TEXT":
      case "VARCHAR":
      case "CHAR":
        javaDataType = String.class;
        break;
      case "BLOB":
        javaDataType = byte[].class;
        break;
      case "ID":
      case "BIGINT":
      case "INTEGER":
        javaDataType = Long.class;
        break;
      case "TINYINT":
      case "SMALLINT":
      case "INT":
      case "MEDIUMINT":
        javaDataType = Integer.class;
        break;
      case "BIT":
        javaDataType = Boolean.class;
        break;
      case "FLOAT":
        javaDataType = Float.class;
        break;
      case "DOUBLE":
        javaDataType = Double.class;
        break;
      case "DECIMAL":
        javaDataType = BigDecimal.class;
        break;
      case "DATE":
      case "TIME":
      case "YEAR":
      case "DATETIME":
        javaDataType = Date.class;
        break;
      case "TIMESTAMP":
        javaDataType = Timestamp.class;
        break;
      default:
        javaDataType = String.class;
        log.warn("Unsupported Database Type:" + dbDataType + "，Replace with String");
        break;
    }
    if (columnName.startsWith("is_")) {
      javaDataType = Boolean.class;
    }
    if (isUseWrapper) {
      return javaDataType;
    }
    return WRAPPER_DATA_TYPE_AND_PRIMITIVE_DATA_TYPE.getOrDefault(javaDataType, javaDataType);
  }

  /**
   * Parse the entity name
   */
  public static String parseEntityName(String className) {
    className = parseSimpleName(className);
    String fieldName = parseFieldName(className);
    return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
  }

  /**
   * Consistent with StringHelper.class.getSimpleName()
   */
  public static String parseSimpleName(String className) {
    if (className.contains(".")) {
      return className.substring(className.lastIndexOf('.') + 1);
    }
    return className;
  }

  /**
   * Parse column name <br>
   * column_name -> columnName <br>
   * column_name_ -> columnName <br>
   * _column_name_ -> columnName <br>
   */
  public static String parseFieldName(String columnName) {
    if (columnName.startsWith("is_")) {
      columnName = columnName.substring("is_".length());
    }
    if (!columnName.contains("_")) {
      return columnName;
    } else {
      int length = columnName.length();
      StringBuilder sb = new StringBuilder(length);
      for (int i = 0; i < length; i++) {
        if ('_' == columnName.charAt(i) && i < length - 1) {
          sb.append(String.valueOf(columnName.charAt(++i)).toUpperCase());
        } else {
          sb.append(columnName.charAt(i));
        }
      }
      String fieldName = sb.toString().replace("_", "");
      char first = fieldName.charAt(0);
      if (first < 'a') {
        fieldName = fieldName.replaceFirst(String.valueOf(first), String.valueOf(first).toLowerCase());
      }
      return fieldName;
    }
  }

  /**
   * Parse column name<br>
   * prefix_column_name -> columnName <br>
   * prefix_column_name_ -> columnName <br>
   * prefix__column_name_ -> columnName <br>
   *
   * @param columnName Field Name
   * @param removePrefix The prefix to remove
   */
  public static String parseFieldName(String columnName, String removePrefix) {
    if (removePrefix == null || removePrefix.isEmpty() || !columnName.startsWith(removePrefix)) {
      return parseFieldName(columnName);
    }
    return parseFieldName(columnName.substring(removePrefix.length() - 1));
  }

  /**
   * Parse the set method name of the field
   */
  public static String parseSetMethodName(String fieldName) {
    return "set" + firstLetterUpper(fieldName);
  }

  /**
   * Parse the get method name of the field
   */
  public static String parseGetMethodName(String fieldName) {
    return "get" + firstLetterUpper(fieldName);
  }

  /**
   * Parse the is method name of the field
   */
  public static String parseIsMethodName(String fieldName) {
    return "is" + firstLetterUpper(fieldName);
  }

  /**
   * Parse the update method name of the field
   */
  public static String parseUpdateMethodName(String fieldName) {
    return "update" + firstLetterUpper(fieldName);
  }

  /**
   * Parse the method name of the field to enumeration
   */
  public static String parseGetEnumByMethodName(String fieldName) {
    return "getEnumBy" + firstLetterUpper(fieldName);
  }

  /**
   * Parse the method name of the enumeration turn field
   */
  public static String parseSetEnumMethodName(String fieldName) {
    return "set" + firstLetterUpper(fieldName) + "Enum";
  }

  /**
   * Initial capitalization
   */
  public static String firstLetterUpper(String name) {
    return name.replaceFirst(String.valueOf(name.charAt(0)), String.valueOf(name.charAt(0)).toUpperCase());
  }

  /**
   * Initial letter lowercase
   */
  public static String firstLetterLower(String name) {
    return name.replaceFirst(String.valueOf(name.charAt(0)), String.valueOf(name.charAt(0)).toLowerCase());
  }

  /**
   * Package name to folder name
   */
  public static String packageNameToFolder(String packageName) {
    if (packageName.contains(".")) {
      return packageName.replace('.', '/');
    } else {
      return packageName;
    }
  }

  /**
   * Entity package name transfer interface package name
   */
  public static String entityPackageNameToServicePackageName(String entityPackageName) {
    if (entityPackageName.contains(".")) {
      return entityPackageName.substring(0, entityPackageName.lastIndexOf('.')) + ".service";
    } else {
      return "service";
    }
  }

  /**
   * Entity package name
   */
  public static String entityPackageNameToDAOPackageName(String entityPackageName) {
    if (entityPackageName.contains(".")) {
      return entityPackageName.substring(0, entityPackageName.lastIndexOf('.')) + ".dao";
    } else {
      return "dao";
    }
  }

  /**
   * Entity name to mapper package name
   */
  public static String entityPackageNameToMapperPackageName(String entityPackageName) {
    String daoPackageName = entityPackageNameToDAOPackageName(entityPackageName);
    return daoPackageName.substring(0, daoPackageName.length() - "dao".length()) + "mapper";
  }

  /**
   * Repeat splicing <code>times</code> times for <code>repeat</code>
   *
   * @param repeat A string to be repeated
   * @param times Repeat times
   */
  public static String repeat(String repeat, int times) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < times; i++) {
      builder.append(repeat);
    }
    return builder.toString();
  }

  /**
   * Split the current string by <code>lineSeparator</code>,
   * add <code>indent</code> before each split string,
   * then add <code>lineSeparator</code>
   */
  public static String insertIndentBefore(String string, String lineSeparator, String indent) {
    return Arrays.stream(string.split(lineSeparator))
        .map(line -> indent + line)
        .collect(Collectors.joining(lineSeparator));
  }

}