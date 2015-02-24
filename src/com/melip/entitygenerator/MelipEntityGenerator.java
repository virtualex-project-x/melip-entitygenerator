package com.melip.entitygenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.melip.entitygenerator.entity.Domain;
import com.melip.entitygenerator.entity.DomainCreator;
import com.melip.entitygenerator.entity.Entity;
import com.melip.entitygenerator.entity.EntityCreator;
import com.melip.entitygenerator.template.EntityTemplate;

/**
 * テーブル定義書からエンティティクラスを生成します。<br>
 */
public class MelipEntityGenerator {

  private static final Logger log = LoggerFactory.getLogger(MelipEntityGenerator.class);

  /** プロパティファイルのパス */
  private static final String PATH_PROP_FILE = "setting/entitygenerator.properties";
  /** プロパティファイルのキー：テーブル定義書のパス */
  private static final String PROP_KEY_EXCEL_PATH = "excel.path";
  /** プロパティファイルのキー：ドメイン定義シート */
  private static final String PROP_KEY_EXCEL_DOMAIN_SHEET = "excel.domain.sheet";
  /** プロパティファイルのキー：エンティティクラス生成除外シート */
  private static final String PROP_KEY_EXCEL_IGNORE_SHEETS = "excel.ignore.sheets";
  /** プロパティファイルのキー：エンティティクラス生成除外フィールド */
  private static final String PROP_KEY_EXCEL_IGNORE_FIELDS = "excel.ignore.fields";
  /** プロパティファイルのキー：エンティティクラスのパッケージ名 */
  private static final String PROP_KEY_ENTITY_PACKAGE_NAME = "entity.package.name";
  /** プロパティファイルのキー：エンティティクラスの親クラス名 */
  private static final String PROP_KEY_ENTITY_PARENT_CLASS_NAME = "entity.parent.class.name";
  /** プロパティファイルのキー：エンティティクラスの出力先 */
  private static final String PROP_KEY_ENTITY_OUTPUT_PATH = "entity.output.path";

  /** プロパティ */
  private Properties props = null;
  /** テーブル定義書 */
  private File excelFile = null;
  /** エンティティクラスの出力先 */
  private File outputDir = null;
  /** カウンタ */
  private int counter = 0;

  /**
   * メイン
   * 
   * @param args 引数（不要）
   */
  public static void main(String[] args) {

    MelipEntityGenerator generator = new MelipEntityGenerator();

    log.info("エンティティクラス生成処理を開始します。");
    try {
      if (generator.generate()) {
        log.info("【◎】エンティティクラス生成処理が正常終了しました。エンティティ数[" + generator.getCounter() + "]");
      } else {
        log.error("【☓】エンティティクラス生成処理が異常終了しました。");
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error("【☓】エンティティクラス生成処理が異常終了しました。");
    }
  }

  /**
   * エンティティクラス生成処理
   * 
   * @throws Exception
   */
  public boolean generate() throws Exception {

    // プロパティファイルの読み込み
    log.info("プロパティファイル[" + PATH_PROP_FILE + "]の読み込みを開始します。");
    readProp();
    log.info("プロパティファイルの読み込みが終了しました。");

    // プロパティ必須チェック
    log.info("プロパティの必須チェックを開始します。");
    if (!checkPropRequired()) {
      return false;
    }
    log.info("プロパティの必須チェックが終了しました。");

    // テーブル定義書のチェック
    log.info("テーブル定義書[" + getExcelPath() + "]のチェックを開始します。");
    if (!checkExcelFile()) {
      return false;
    }
    log.info("テーブル定義書のチェックが終了しました。");

    // エンティティクラス出力先のチェック
    log.info("エンティティクラス出力先[" + getEntityOutputPath() + "]のチェックを開始します。");
    if (!checkOutputPath()) {
      return false;
    }
    log.info("エンティティクラス出力先のチェックが終了しました。");

    // エンティティクラス作成
    log.info("エンティティクラスの生成を開始します。");
    if (!makeEntityClass()) {
      return false;
    }
    log.info("エンティティクラスの生成が終了しました。");

    return true;
  }

  /**
   * プロパティファイルを読み込みます。
   * 
   * @throws IOException
   */
  private void readProp() throws IOException {

    File prop = new File(getClass().getClassLoader().getResource(PATH_PROP_FILE).getPath());
    props = new Properties();
    try {
      props.load(new InputStreamReader(new FileInputStream(prop), StandardCharsets.UTF_8));
    } catch (FileNotFoundException e) {
      log.error("プロパティファイル[" + PATH_PROP_FILE + "]が存在しません。");
      throw e;
    } catch (IOException e) {
      log.error("プロパティファイル[" + PATH_PROP_FILE + "]読み込み時に入出力エラーが発生しました。");
      throw e;
    }
  }

  /**
   * プロパティの必須チェックを実施します。
   * 
   * @return チェックOKの場合true、NGの場合false
   */
  private boolean checkPropRequired() {

    boolean result = true;

    // テーブル定義書のパス
    if (StringUtils.isEmpty(getExcelPath())) {
      log.error("テーブル定義書のパス[" + PROP_KEY_EXCEL_PATH + "]が設定されていません。");
      result = false;
    }

    // エンティティクラスのパッケージ名
    if (StringUtils.isEmpty(getEntityPackageName())) {
      log.error("エンティティクラスのパッケージ名[" + PROP_KEY_ENTITY_PACKAGE_NAME + "]が設定されていません。");
      result = false;
    }

    // エンティティクラスの出力先
    if (StringUtils.isEmpty(getEntityOutputPath())) {
      log.error("エンティティクラスの出力先[" + PROP_KEY_ENTITY_OUTPUT_PATH + "]が設定されていません。");
      result = false;
    }

    return result;
  }

  /**
   * テーブル定義書の存在チェック、読み込み可能チェックを行います。
   * 
   * @return チェックOKの場合true、NGの場合false
   */
  private boolean checkExcelFile() {

    String excelPath = getExcelPath();
    File excel = new File(excelPath);
    if (!excel.exists()) {
      log.error("テーブル定義書[" + excelPath + "]が存在しません。");
      return false;
    } else if (!excel.canRead()) {
      log.error("テーブル定義書[" + excelPath + "]が読み込めません。");
      return false;
    } else {
      setExcelFile(excel);
      return true;
    }
  }

  /**
   * エンティティクラスの出力先のディレクトリチェック、書き込み可能チェックを行います。
   * 
   * @return チェックOKの場合true、NGの場合false
   */
  private boolean checkOutputPath() {

    String outputPath = getEntityOutputPath();
    File outputDir = new File(outputPath);
    if (!outputDir.exists()) {
      log.error("エンティティクラスの出力先[" + outputPath + "]が存在しません。");
      return false;
    } else if (!outputDir.isDirectory()) {
      log.error("エンティティクラスの出力先[" + outputPath + "]はディレクトリではありません。");
      return false;
    } else if (!outputDir.canWrite()) {
      log.error("エンティティクラスの出力先[" + outputPath + "]に書き込めません。");
      return false;
    } else {
      setOutputDir(outputDir);
      return true;
    }
  }

  /**
   * エンティティクラスを生成します。
   * 
   * @return 正常終了した場合true、Excelの状態が不正だった場合はfalse
   * @throws InvalidFormatException
   * @throws IOException
   */
  private boolean makeEntityClass() throws InvalidFormatException, IOException {

    try {
      Workbook book = WorkbookFactory.create(getExcelFile());

      // ドメインの取得
      Sheet domainSheet = book.getSheet(getExcelDomainSheet());
      Domain domain = null;
      if (null != domainSheet) {
        log.info("ドメイン情報の読み込みを開始します。シート[" + domainSheet.getSheetName() + "]");
        DomainCreator domainCreator = new DomainCreator(domainSheet);
        domain = domainCreator.createDomain();
        log.info("ドメイン情報の読み込みが終了しました。");
      }

      // エンティティシートの処理
      log.info("エンティティ情報の読み込みを開始します。");
      for (int i = 0; i < book.getNumberOfSheets(); i++) {
        Sheet sheet = book.getSheetAt(i);
        if (isIgnoreSheet(sheet)) {
          log.info("シート[" + sheet.getSheetName() + "]は除外対象のためスキップします。");
          continue;
        }

        log.info("シート[" + sheet.getSheetName() + "]の処理を開始します。");
        EntityCreator entityCreator = new EntityCreator(sheet, getExcelIgnoreFields(), domain);
        Entity entity = entityCreator.createEntity();
        EntityTemplate entityTemplate =
            new EntityTemplate(entity, getEntityPackageName(), getEntityParentClassName());
        String contents = entityTemplate.makeString();
        outputEntityClassFile(contents, entityTemplate.getEntity().getClassName());
        log.info("シート[" + sheet.getSheetName() + "]の処理が終了しました。");
        setCounter(getCounter() + 1);
      }
      log.info("エンティティ情報の読み込みが終了しました。");
    } catch (IllegalStateException e) {
      // Excelの状態が不正だった場合は、DomainCreator、EntityCreatorからIllegalStateExceptionが投げられる
      return false;
    } catch (InvalidFormatException e) {
      log.error("テーブル定義書[" + getExcelFile().getPath() + "]は不正な無効なフォーマットです。");
      throw e;
    } catch (IOException e) {
      log.error("テーブル定義書[" + getExcelFile().getPath() + "]の処理中に入出力エラーが発生しました。");
      throw e;
    }

    return true;
  }

  /**
   * シートがエンティティクラス生成対象外か判定します。
   * 
   * @param sheet シート
   * @return エンティティクラス生成対象外の場合true、対象の場合false
   */
  private boolean isIgnoreSheet(Sheet sheet) {
    return getIgnoreSheetList().contains(sheet.getSheetName());
  }

  /**
   * エンティティクラス生成除外シートリストを取得します。
   * 
   * @return エンティティクラス生成除外シートリスト
   */
  private List<String> getIgnoreSheetList() {

    List<String> ignoreSheetList = new ArrayList<String>();
    String ignoreSheetsProp = getExcelIgnoreSheets();
    if (StringUtils.isNotEmpty(ignoreSheetsProp)) {
      ignoreSheetList = Arrays.asList(ignoreSheetsProp.split(EntityGeneratorUtils.SEPARATOR_ITEM));
    }

    return ignoreSheetList;
  }

  /**
   * エンティティクラスファイルを出力します。
   * 
   * @param contents エンティティクラスの文字列
   * @param fileName ファイル名
   * @throws FileNotFoundException
   */
  private void outputEntityClassFile(String contents, String fileName) throws FileNotFoundException {

    String filePath =
        getEntityOutputPath() + EntityGeneratorUtils.SEPARATOR_PATH + fileName
            + EntityGeneratorUtils.EXTENSION_JAVA;

    PrintWriter writer = null;
    try {
      writer =
          new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath),
              StandardCharsets.UTF_8));
      writer.print(contents);
    } catch (FileNotFoundException e) {
      log.error("出力先ファイル[" + filePath + "]に書き込めません。");
      throw e;
    } finally {
      if (null != writer) {
        writer.close();
      }
    }
  }

  /**
   * テーブル定義書のパスを取得します。
   * 
   * @return テーブル定義書のパス
   */
  private String getExcelPath() {
    return getProps().getProperty(PROP_KEY_EXCEL_PATH);
  }

  /**
   * ドメイン定義シートを取得します。
   * 
   * @return ドメイン定義シート
   */
  private String getExcelDomainSheet() {
    return getProps().getProperty(PROP_KEY_EXCEL_DOMAIN_SHEET);
  }

  /**
   * エンティティクラス生成除外シートリストを取得します。
   * 
   * @return エンティティクラス生成除外シートリスト
   */
  private String getExcelIgnoreSheets() {
    return getProps().getProperty(PROP_KEY_EXCEL_IGNORE_SHEETS);
  }

  /**
   * エンティティクラス生成除外フィールドを取得します。
   * 
   * @return エンティティクラス生成除外フィールド
   */
  private String getExcelIgnoreFields() {
    return getProps().getProperty(PROP_KEY_EXCEL_IGNORE_FIELDS);
  }

  /**
   * エンティティクラスのパッケージ名を取得します。
   * 
   * @return エンティティクラスのパッケージ名
   */
  private String getEntityPackageName() {
    return getProps().getProperty(PROP_KEY_ENTITY_PACKAGE_NAME);
  }

  /**
   * エンティティクラスの親クラス名を取得します。
   * 
   * @return エンティティクラスの親クラス名
   */
  private String getEntityParentClassName() {
    return getProps().getProperty(PROP_KEY_ENTITY_PARENT_CLASS_NAME);
  }

  /**
   * エンティティクラスの出力先を取得します。
   * 
   * @return エンティティクラスの出力先
   */
  private String getEntityOutputPath() {
    return getProps().getProperty(PROP_KEY_ENTITY_OUTPUT_PATH);
  }

  /**
   * プロパティを取得します。
   * 
   * @return プロパティ
   */
  public Properties getProps() {
    return props;
  }

  /**
   * プロパティを設定します。
   * 
   * @param props プロパティ
   */
  public void setProps(Properties props) {
    this.props = props;
  }

  /**
   * テーブル定義書を取得します。
   * 
   * @return テーブル定義書
   */
  public File getExcelFile() {
    return excelFile;
  }

  /**
   * テーブル定義書を設定します。
   * 
   * @param excelFile テーブル定義書
   */
  public void setExcelFile(File excelFile) {
    this.excelFile = excelFile;
  }

  /**
   * エンティティクラスの出力先を取得します。
   * 
   * @return エンティティクラスの出力先
   */
  public File getOutputDir() {
    return outputDir;
  }

  /**
   * エンティティクラスの出力先を設定します。
   * 
   * @param outputDir エンティティクラスの出力先
   */
  public void setOutputDir(File outputDir) {
    this.outputDir = outputDir;
  }

  /**
   * カウンタを取得します。
   * 
   * @return カウンタ
   */
  public int getCounter() {
    return counter;
  }

  /**
   * カウンタを設定します。
   * 
   * @param counter カウンタ
   */
  public void setCounter(int counter) {
    this.counter = counter;
  }
}
