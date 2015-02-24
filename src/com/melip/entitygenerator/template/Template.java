package com.melip.entitygenerator.template;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.melip.entitygenerator.EntityGeneratorUtils;


/**
 * テンプレートを表すクラスです。
 */
public abstract class Template {

  private static final Logger log = LoggerFactory.getLogger(Template.class);

  /**
   * テンプレートファイルを読み込みます。
   * 
   * @return テンプレート文字列
   * @throws IOException
   */
  public String readTemplate() throws IOException {

    StringBuilder sb = new StringBuilder();
    BufferedReader reader = null;
    try {
      reader =
          new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(
              getTemplateFilePath()), StandardCharsets.UTF_8));
      String line = reader.readLine();
      while (line != null) {
        sb.append(line).append(EntityGeneratorUtils.SEPARATOR_LINE);
        line = reader.readLine();
      }
    } catch (FileNotFoundException e) {
      log.error("テンプレートファイル[" + getTemplateFilePath() + "]が見つかりません。");
      throw e;
    } catch (IOException e) {
      log.error("テンプレートファイル[" + getTemplateFilePath() + "]読み込み時にエラーが発生しました。");
      throw e;
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        log.error("テンプレートファイル[" + getTemplateFilePath() + "]クローズ時にエラーが発生しました。");
        throw e;
      }
    }

    return sb.toString();
  }

  /**
   * テンプレート内の変数を指定の文字列で置き換えます。
   * 
   * @param template テンプレート
   * @param varName 変数名
   * @param replaceVal 置き換え後の文字列
   */
  public void replaceVariable(StringBuilder template, String varName, String replaceVal) {

    int index = template.indexOf(varName);
    while (index != -1) {
      template.replace(index, index + varName.length(), replaceVal);
      index = template.indexOf(varName);
    }
  }

  /**
   * テンプレートファイルのパスを取得します。
   * 
   * @return テンプレートファイルのパス
   */
  protected abstract String getTemplateFilePath();

  /**
   * テンプレートから文字列を作成します。
   * 
   * @return テンプレートから作成された文字列
   */
  public abstract String makeString() throws IOException;

}
