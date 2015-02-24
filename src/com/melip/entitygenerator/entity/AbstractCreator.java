package com.melip.entitygenerator.entity;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Excelのテーブル定義書からオブジェクトを生成する基底クラスです。
 */
public abstract class AbstractCreator {

  /** テーブル定義書のシート */
  private Sheet sheet = null;

  /**
   * コンストラクタ
   */
  public AbstractCreator() {}

  /**
   * コンストラクタ
   * 
   * @param sheet テーブル定義書のシート
   */
  public AbstractCreator(Sheet sheet) {
    this.sheet = sheet;
  }

  /**
   * 空行か判定します。
   * 
   * @param row 行
   * @return 空行の場合true、空行でない場合false
   */
  protected boolean isEmptyRow(Row row) {
    // 空行はnullとなる
    return null == row;
  }

  /**
   * テーブル定義書のシートを取得します。
   * 
   * @return テーブル定義書のシート
   */
  public Sheet getSheet() {
    return sheet;
  }

  /**
   * テーブル定義書のシートを設定します。
   * 
   * @param sheet テーブル定義書のシート
   */
  public void setSheet(Sheet sheet) {
    this.sheet = sheet;
  }

}
