package com.melip.entitygenerator.entity;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excelのテーブル定義書からドメインを生成するクラスです。
 */
public class DomainCreator extends AbstractCreator {

  private Logger log = LoggerFactory.getLogger(DomainCreator.class);

  /** ドメインの開始行番号 */
  private static final int ROW_DOMAIN_START = 2;
  /** ドメイン名の列番号 */
  private static final int COL_DOMAIN_NAME = 1;
  /** データ型の列番号 */
  private static final int COL_DATA_TYPE = 2;

  /**
   * コンストラクタ
   */
  public DomainCreator() {}

  /**
   * コンストラクタ
   * 
   * @param sheet テーブル定義書のシート
   */
  public DomainCreator(Sheet sheet) {
    super(sheet);
  }

  /**
   * エンティティを生成します。
   * 
   * @return エンティティ
   */
  public Domain createDomain() {

    Map<String, String> domainMap = new HashMap<String, String>();

    int rowNum = ROW_DOMAIN_START;
    while (true) {
      Row row = getSheet().getRow(rowNum);
      // 行末
      if (isEmptyRow(row)) {
        break;
      }

      String domainName = getDomainName(row);
      String dataType = getDataType(row);
      domainMap.put(domainName, dataType);

      rowNum++;
    }

    Domain domain = new Domain();
    domain.setDomainMap(domainMap);

    return domain;
  }

  /**
   * ドメイン名を取得します。
   * 
   * @param row 行
   * @return ドメイン名
   */
  private String getDomainName(Row row) {

    String domainName = row.getCell(COL_DOMAIN_NAME).getStringCellValue();
    if (StringUtils.isEmpty(domainName)) {
      log.error("エンティティ論理名は必須です。行[" + (row.getRowNum() + 1) + "]");
      throw new IllegalStateException();
    }

    return domainName;
  }

  /**
   * データ型を取得します。
   * 
   * @param row 行
   * @return データ型
   */
  private String getDataType(Row row) {

    String dataType = row.getCell(COL_DATA_TYPE).getStringCellValue();
    if (StringUtils.isEmpty(dataType)) {
      log.error("データ型は必須です。行[" + (row.getRowNum() + 1) + "]");
      throw new IllegalStateException();
    }

    return dataType;
  }

}
