package com.melip.entitygenerator.entity;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.CaseFormat;

/**
 * エンティティを表すクラスです。
 */
public class Entity {

  /** テーブル名サフィックス（マスタ） */
  private static final String PREFIX_MASTER = "M";
  /** テーブル名サフィックス（トランザクション） */
  private static final String PREFIX_TRANSACTION = "T";

  /** 論理名 */
  private String logicalName = null;
  /** 物理名 */
  private String physicalName = null;
  /** フィールドリスト */
  private List<Field> fieldList = null;

  /**
   * クラス名を取得します。
   * 
   * @return クラス名
   */
  public String getClassName() {

    String tmpPhysicalName = getPhysicalName();
    if (getPhysicalName().startsWith(PREFIX_MASTER)) {
      tmpPhysicalName = getPhysicalName().replaceFirst(PREFIX_MASTER, StringUtils.EMPTY);
    } else if (getPhysicalName().startsWith(PREFIX_TRANSACTION)) {
      tmpPhysicalName = getPhysicalName().replaceFirst(PREFIX_TRANSACTION, StringUtils.EMPTY);
    }

    return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tmpPhysicalName);
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
   * フィールドリストを取得します。
   * 
   * @return フィールドリスト
   */
  public List<Field> getFieldList() {
    return fieldList;
  }

  /**
   * フィールドリストを設定します。
   * 
   * @param fieldList フィールドリスト
   */
  public void setFieldList(List<Field> fieldList) {
    this.fieldList = fieldList;
  }

}
