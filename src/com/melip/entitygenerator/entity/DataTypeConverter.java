package com.melip.entitygenerator.entity;

/**
 * データ型をJavaの型に変換するクラスです。
 */
public class DataTypeConverter {

  /** String型に該当するもの */
  private static final String[] PREFIX_STRING = {"CHAR", "VARCHAR"};
  /** Integer型に該当するもの */
  private static final String[] PREFIX_INTEGER = {"INT"};
  /** Float型に該当するもの */
  private static final String[] PREFIX_FLOAT = {"DOUBLE"};
  /** Date型に該当するもの */
  private static final String[] PREFIX_DATE = {"TIMESTAMP"};

  /** String型 */
  private static final String TYPE_STRING = "String";
  /** Integer型 */
  private static final String TYPE_INTEGER = "Integer";
  /** Float型 */
  private static final String TYPE_FLOAT = "Float";
  /** Date型 */
  private static final String TYPE_DATE = "Date";

  /**
   * プライベートコンストラクタ
   */
  private DataTypeConverter() {}

  /**
   * データ型をJavaの型に変換します。
   * 
   * @param dataType データ型
   * @return Javaの型
   */
  public static String convert(String dataType) {

    String upperDataType = dataType.toUpperCase();

    if (isString(upperDataType)) {
      return TYPE_STRING;
    } else if (isInteger(upperDataType)) {
      return TYPE_INTEGER;
    } else if (isFloat(upperDataType)) {
      return TYPE_FLOAT;
    } else if (isDate(upperDataType)) {
      return TYPE_DATE;
    } else {
      return null;
    }
  }

  /**
   * String型か判定します。
   * 
   * @param dataType データ型
   * @return String型の場合true、違う場合false
   */
  private static boolean isString(String dataType) {
    return containsTypeArray(dataType, PREFIX_STRING);
  }

  /**
   * Integer型か判定します。
   * 
   * @param dataType データ型
   * @return Integer型の場合true、違う場合false
   */
  private static boolean isInteger(String dataType) {
    return containsTypeArray(dataType, PREFIX_INTEGER);
  }

  /**
   * Float型か判定します。
   * 
   * @param dataType データ型
   * @return Float型の場合true、違う場合false
   */
  private static boolean isFloat(String dataType) {
    return containsTypeArray(dataType, PREFIX_FLOAT);
  }

  /**
   * Date型か判定します。
   * 
   * @param dataType データ型
   * @return Date型の場合true、違う場合false
   */
  private static boolean isDate(String dataType) {
    return containsTypeArray(dataType, PREFIX_DATE);
  }

  /**
   * データ型が型配列に含まれているかチェックします。
   * 
   * @param dataType データ型
   * @param typeArray 型配列
   * @return データ型が型配列に含まれている場合true、含まれていない場合false
   */
  private static boolean containsTypeArray(String dataType, String[] typeArray) {

    for (int i = 0; i < typeArray.length; i++) {
      if (dataType.startsWith(typeArray[i])) {
        return true;
      }
    }

    return false;
  }
}
