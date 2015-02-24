package com.melip.entitygenerator.template;

import java.io.IOException;

import com.melip.entitygenerator.entity.Field;


/**
 * 定数定義のテンプレートです。
 */
public class ConstantsTemplate extends Template {

  /** テンプレートファイルパス */
  private static final String TEMPLATE_FILE_PATH = "setting/constants.template";

  /** 変数：フィールド論理名 */
  public static final String VAR_FIELD_LOGICAL_NAME = "${fieldLogicalName}";
  /** 変数：フィールド物理名 */
  public static final String VAR_FIELD_PHYSICAL_NAME = "${fieldPhysicalName}";
  /** 変数：フィールド物理名キャメルケース */
  public static final String VAR_FIELD_CAMEL_CASE = "${fieldCamelCase}";

  /** フィールド */
  private Field field = null;

  /**
   * コンストラクタ
   */
  public ConstantsTemplate() {
    super();
  }

  /**
   * コンストラクタ
   * 
   * @param field フィールド
   */
  public ConstantsTemplate(Field field) {
    this.field = field;
  }

  /**
   * @see com.melip.entitygenerator.template.Template#getTemplateFilePath()
   */
  @Override
  protected String getTemplateFilePath() {
    return TEMPLATE_FILE_PATH;
  }

  /**
   * @see com.melip.entitygenerator.template.Template#makeString()
   */
  @Override
  public String makeString() throws IOException {

    StringBuilder sb = new StringBuilder(readTemplate());
    replaceVariable(sb, VAR_FIELD_LOGICAL_NAME, getField().getLogicalName());
    replaceVariable(sb, VAR_FIELD_PHYSICAL_NAME, getField().getPhysicalName());
    replaceVariable(sb, VAR_FIELD_CAMEL_CASE, getField().getCamelCase());

    return sb.toString();
  }

  /**
   * フィールドを取得します。
   * 
   * @return フィールド
   */
  public Field getField() {
    return field;
  }

  /**
   * フィールドを設定します。
   * 
   * @param field フィールド
   */
  public void setField(Field field) {
    this.field = field;
  }

}
