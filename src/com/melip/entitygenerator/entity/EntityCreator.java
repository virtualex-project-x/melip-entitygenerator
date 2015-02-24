package com.melip.entitygenerator.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.melip.entitygenerator.EntityGeneratorUtils;

/**
 * Excelのテーブル定義書からエンティティを生成するクラスです。
 */
public class EntityCreator extends AbstractCreator {

  private Logger log = LoggerFactory.getLogger(EntityCreator.class);

  /** エンティティ論理名の行番号 */
  private static final int ROW_ENTITY_LOGICAL_NAME = 4;
  /** エンティティ論理名の列番号 */
  private static final int COL_ENTITY_LOGICAL_NAME = 2;
  /** エンティティ物理名の行番号 */
  private static final int ROW_ENTITY_PHYSICAL_NAME = 5;
  /** エンティティ物理名の列番号 */
  private static final int COL_ENTITY_PHYSICAL_NAME = 2;
  /** フィールドの開始行番号 */
  private static final int ROW_FIELD_START = 13;
  /** フィールド論理名の列番号 */
  private static final int COL_FIELD_LOGICAL_NAME = 1;
  /** フィールド物理名の列番号 */
  private static final int COL_FIELD_PHYSICAL_NAME = 2;
  /** データ型の列番号 */
  private static final int COL_FIELD_DATA_TYPE = 3;
  /** デフォルト値の列番号 */
  private static final int COL_FIELD_DEFAULT_VALUE = 5;

  /** ドメインのプレフィックス */
  private static final String PREFIX_DOMAIN = "*";
  /** ドメインのプレフィックス（正規表現） */
  private static final String PREFIX_DOMAIN_REGEX = "\\*";

  /** エンティティクラス生成対象外フィールド */
  private String ignoreField = null;
  /** ドメイン */
  private Domain domain = null;

  /**
   * コンストラクタ
   */
  public EntityCreator() {}

  /**
   * コンストラクタ
   * 
   * @param sheet テーブル定義書のシート
   */
  public EntityCreator(Sheet sheet) {
    this(sheet, null, null);
  }

  /**
   * コンストラクタ
   * 
   * @param sheet テーブル定義書のシート
   * @param ignoreField エンティティクラス生成対象外フィールド
   * @param domain ドメイン
   */
  public EntityCreator(Sheet sheet, String ignoreField, Domain domain) {

    super(sheet);
    this.ignoreField = ignoreField;
    this.domain = domain;
  }

  /**
   * エンティティを生成します。
   * 
   * @return エンティティ
   */
  public Entity createEntity() {

    Entity entity = new Entity();
    entity.setLogicalName(getEntityLogicalName());
    entity.setPhysicalName(getEntityPhysicalName());
    entity.setFieldList(getFieldList());

    return entity;
  }

  /**
   * エンティティ論理名を取得します。
   * 
   * @return エンティティ論理名
   */
  private String getEntityLogicalName() {

    String entityLogicalName =
        getSheet().getRow(ROW_ENTITY_LOGICAL_NAME).getCell(COL_ENTITY_LOGICAL_NAME)
            .getStringCellValue();
    if (StringUtils.isEmpty(entityLogicalName)) {
      log.error("エンティティ論理名は必須です。");
      throw new IllegalStateException();
    }

    return entityLogicalName;
  }

  /**
   * エンティティ物理名を取得します。
   * 
   * @return エンティティ物理名
   */
  private String getEntityPhysicalName() {

    String entityPhysicalName =
        getSheet().getRow(ROW_ENTITY_PHYSICAL_NAME).getCell(COL_ENTITY_PHYSICAL_NAME)
            .getStringCellValue();
    if (StringUtils.isEmpty(entityPhysicalName)) {
      log.error("エンティティ物理名は必須です。");
      throw new IllegalStateException();
    }

    return entityPhysicalName;
  }

  /**
   * フィールドリストを取得します。
   * 
   * @param ignoreFields 除外フィールド論理名
   * @return フィールドリスト
   */
  private List<Field> getFieldList() {

    List<Field> fieldList = new ArrayList<Field>();

    int rowNum = ROW_FIELD_START;
    while (true) {
      Row row = getSheet().getRow(rowNum);
      // 行末
      if (isEmptyRow(row)) {
        break;
      }
      // 除外対象
      if (isIgnoreField(getFieldLogicalName(row))) {
        rowNum++;
        continue;
      }

      Field field = new Field();
      field.setLogicalName(getFieldLogicalName(row));
      field.setPhysicalName(getFieldPhysicalName(row));
      field.setDataType(getDataType(row));
      field.setDefaultValue(getFieldDefaultValue(row));
      field.setFieldType(getFieldType(row));
      fieldList.add(field);

      rowNum++;
    }

    return fieldList;
  }

  /**
   * フィールドがエンティティクラス生成対象外か判定します。
   * 
   * @param fieldLogicalName フィールド論理名
   * @return エンティティクラス生成対象外の場合true、対象の場合false
   */
  private boolean isIgnoreField(String fieldLogicalName) {
    return getIgnoreFieldList().contains(fieldLogicalName);
  }

  /**
   * エンティティクラス生成除外フィールドリストを取得します。
   * 
   * @return エンティティクラス生成除外フィールドリスト
   */
  private List<String> getIgnoreFieldList() {

    List<String> ignoreFields = new ArrayList<String>();
    String ignoreFieldProp = getIgnoreField();
    if (StringUtils.isNotEmpty(ignoreFieldProp)) {
      ignoreFields = Arrays.asList(ignoreFieldProp.split(EntityGeneratorUtils.SEPARATOR_ITEM));
    }

    return ignoreFields;
  }

  /**
   * フィールド論理名を取得します。
   * 
   * @param row 行
   * @return フィールド論理名
   */
  private String getFieldLogicalName(Row row) {

    String fieldLogicalName = row.getCell(COL_FIELD_LOGICAL_NAME).getStringCellValue();
    if (StringUtils.isEmpty(fieldLogicalName)) {
      log.error("フィールド論理名は必須です。行[" + (row.getRowNum() + 1) + "]");
      throw new IllegalStateException();
    }

    return fieldLogicalName;
  }

  /**
   * フィールド物理名を取得します。
   * 
   * @param row 行
   * @return フィールド物理名
   */
  private String getFieldPhysicalName(Row row) {

    String fieldPhysicalName = row.getCell(COL_FIELD_PHYSICAL_NAME).getStringCellValue();
    if (StringUtils.isEmpty(fieldPhysicalName)) {
      log.error("フィールド物理名は必須です。行[" + (row.getRowNum() + 1) + "]");
      throw new IllegalStateException();
    }

    return fieldPhysicalName;
  }

  /**
   * フィールドデータ型を取得します。
   * 
   * @param row 行
   * @return フィールドデータ型
   */
  private String getDataType(Row row) {

    String fieldPhysicalName = row.getCell(COL_FIELD_DATA_TYPE).getStringCellValue();
    if (StringUtils.isEmpty(fieldPhysicalName)) {
      log.error("フィールドデータ型は必須です。行[" + (row.getRowNum() + 1) + "]");
      throw new IllegalStateException();
    }

    return fieldPhysicalName;
  }

  /**
   * フィールドデフォルト値を取得します。
   * 
   * @param row 行
   * @return フィールドデフォルト値
   */
  private String getFieldDefaultValue(Row row) {
    return row.getCell(COL_FIELD_DEFAULT_VALUE).getStringCellValue();
  }

  /**
   * フィールドデータ型を取得します。
   * 
   * @param row 行
   * @return フィールドデータ型
   */
  private String getFieldType(Row row) {

    String dataType = getDataType(row);
    if (dataType.startsWith(PREFIX_DOMAIN)) {
      if (null == getDomain()) {
        log.error("ドメイン定義が存在しません。");
        throw new IllegalStateException();
      }
      String domainDataType =
          getDomain().getDataType(dataType.replaceFirst(PREFIX_DOMAIN_REGEX, StringUtils.EMPTY));
      if (StringUtils.isEmpty(domainDataType)) {
        log.error("ドメインに存在しないデータ型が指定されています。行[" + (row.getRowNum() + 1) + "]データ型[" + dataType + "]");
        throw new IllegalStateException();
      } else {
        dataType = domainDataType;
      }
    }

    String fieldType = DataTypeConverter.convert(dataType);
    if (StringUtils.isEmpty(fieldType)) {
      log.error("データ型をJavaの型に変換できませんでした。行[" + (row.getRowNum() + 1) + "]データ型[" + dataType + "]");
      throw new IllegalStateException();
    }

    return fieldType;
  }

  /**
   * エンティティクラス生成対象外フィールドを取得します。
   * 
   * @return エンティティクラス生成対象外フィールド
   */
  public String getIgnoreField() {
    return ignoreField;
  }

  /**
   * エンティティクラス生成対象外フィールドを設定します。
   * 
   * @param ignoreField エンティティクラス生成対象外フィールド
   */
  public void setIgnoreField(String ignoreField) {
    this.ignoreField = ignoreField;
  }

  /**
   * ドメインを取得します。
   * 
   * @return ドメイン
   */
  public Domain getDomain() {
    return domain;
  }

  /**
   * ドメインを設定します。
   * 
   * @param domain ドメイン
   */
  public void setDomain(Domain domain) {
    this.domain = domain;
  }

}
