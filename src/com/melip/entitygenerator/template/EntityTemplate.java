package com.melip.entitygenerator.template;

import java.io.IOException;

import com.melip.entitygenerator.entity.Entity;
import com.melip.entitygenerator.entity.Field;

/**
 * エンティティのテンプレートです。
 */
public class EntityTemplate extends Template {

  /** テンプレートファイルパス */
  private static final String TEMPLATE_FILE_PATH = "setting/entity.template";

  /** 変数：パッケージ名 */
  public static final String VAR_PACKAGE_NAME = "${packageName}";
  /** 変数：エンティティ論理名 */
  public static final String VAR_PACKAGE_ENTITY_LOGICAL_NAME = "${entityLogicalName}";
  /** 変数：クラス名 */
  public static final String VAR_PACKAGE_CLASS_NAME = "${className}";
  /** 変数：親クラス名 */
  public static final String VAR_PACKAGE_PARENT_CLASS_NAME = "${parentClassName}";
  /** 変数：定数定義 */
  public static final String VAR_PACKAGE_CONSTANTS_DEFINITION = "${constantsDefinition}";
  /** 変数：フィールド定義 */
  public static final String VAR_PACKAGE_FIELD_DEFINITION = "${fieldDefinition}";
  /** 変数：Getter/Setter定義 */
  public static final String VAR_PACKAGE_GETTER_SETTER_DEFINITION = "${getterSetterDefinition}";

  /** エンティティ */
  private Entity entity = null;
  /** パッケージ名 */
  private String packageName = null;
  /** 親クラス名 */
  private String parentClassName = null;

  /**
   * コンストラクタ
   */
  public EntityTemplate() {
    super();
  }

  /**
   * コンストラクタ
   * 
   * @param entity エンティティ
   * @param packageName パッケージ名
   * @param className クラス名
   * @param parentClassName 親クラス名
   */
  public EntityTemplate(Entity entity, String packageName, String parentClassName) {
    this.entity = entity;
    this.packageName = packageName;
    this.parentClassName = parentClassName;
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
    replaceVariable(sb, VAR_PACKAGE_NAME, getPackageName());
    replaceVariable(sb, VAR_PACKAGE_ENTITY_LOGICAL_NAME, getEntity().getLogicalName());
    replaceVariable(sb, VAR_PACKAGE_CLASS_NAME, getEntity().getClassName());
    replaceVariable(sb, VAR_PACKAGE_PARENT_CLASS_NAME, getParentClassName());

    StringBuilder constantsBuilder = new StringBuilder();
    StringBuilder fieldBuilder = new StringBuilder();
    StringBuilder getterSetterBuilder = new StringBuilder();
    for (Field field : getEntity().getFieldList()) {
      Template constantsTemplate = new ConstantsTemplate(field);
      constantsBuilder.append(constantsTemplate.makeString());
      Template fieldTemplate = new FieldTemplate(field);
      fieldBuilder.append(fieldTemplate.makeString());
      Template getterSetterTemplate = new GetterSetterTemplate(field);
      getterSetterBuilder.append(getterSetterTemplate.makeString());
    }
    replaceVariable(sb, VAR_PACKAGE_CONSTANTS_DEFINITION, constantsBuilder.toString());
    replaceVariable(sb, VAR_PACKAGE_FIELD_DEFINITION, fieldBuilder.toString());
    replaceVariable(sb, VAR_PACKAGE_GETTER_SETTER_DEFINITION, getterSetterBuilder.toString());

    return sb.toString();
  }

  /**
   * エンティティを取得します。
   * 
   * @return エンティティ
   */
  public Entity getEntity() {
    return entity;
  }

  /**
   * エンティティを設定します。
   * 
   * @param entity エンティティ
   */
  public void setEntity(Entity entity) {
    this.entity = entity;
  }

  /**
   * パッケージ名を取得します。
   * 
   * @return パッケージ名
   */
  public String getPackageName() {
    return packageName;
  }

  /**
   * パッケージ名を設定します。
   * 
   * @param packageName パッケージ名
   */
  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  /**
   * 親クラス名を取得します。
   * 
   * @return 親クラス名
   */
  public String getParentClassName() {
    return parentClassName;
  }

  /**
   * 親クラス名を設定します。
   * 
   * @param parentClassName 親クラス名
   */
  public void setParentClassName(String parentClassName) {
    this.parentClassName = parentClassName;
  }

}
