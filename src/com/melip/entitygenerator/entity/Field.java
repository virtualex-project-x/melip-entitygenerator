package com.melip.entitygenerator.entity;

import com.google.common.base.CaseFormat;

/**
 * フィールドを表すクラスです。
 */
public class Field {

  /** 論理名 */
  private String logicalName = null;
  /** 物理名 */
  private String physicalName = null;
  /** データ型 */
  private String dataType = null;
  /** デフォルト値 */
  private String defaultValue = null;
  /** フィールドタイプ */
  private String fieldType = null;

  /**
   * フィールド物理名のキャメルケースを取得します。
   * 
   * @return キャメルケース
   */
  public String getCamelCase() {
    return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, getPhysicalName());
  }

  /**
   * フィールド物理名のパスカルケースを取得します。
   * 
   * @return パスカルケース
   */
  public String getPascalCase() {
    return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, getPhysicalName());
  }

  /**
   * 論理名を取得します。
   * 
   * @return 論理名
   */
  public String getLogicalName() {
    return logicalName;
  }

  /**
   * 論理名を設定します。
   * 
   * @param logicalName 論理名
   */
  public void setLogicalName(String logicalName) {
    this.logicalName = logicalName;
  }

  /**
   * 物理名を取得します。
   * 
   * @return 物理名
   */
  public String getPhysicalName() {
    return physicalName;
  }

  /**
   * 物理名を設定します。
   * 
   * @param physicalName 物理名
   */
  public void setPhysicalName(String physicalName) {
    this.physicalName = physicalName;
  }

  /**
   * データ型を取得します。
   * 
   * @return データ型
   */
  public String getDataType() {
    return dataType;
  }

  /**
   * データ型を設定します。
   * 
   * @param dataType データ型
   */
  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  /**
   * デフォルト値を取得します。
   * 
   * @return デフォルト値
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * デフォルト値を設定します。
   * 
   * @param defaultValue デフォルト値
   */
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  /**
   * フィールドタイプを取得します。
   * 
   * @return フィールドタイプ
   */
  public String getFieldType() {
    return fieldType;
  }

  /**
   * フィールドタイプを設定します。
   * 
   * @param fieldType フィールドタイプ
   */
  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }

}
