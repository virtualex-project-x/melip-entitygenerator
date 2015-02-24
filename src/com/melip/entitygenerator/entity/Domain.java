package com.melip.entitygenerator.entity;

import java.util.Map;

/**
 * ドメインを表すクラスです。
 */
public class Domain {

  /** ドメインマップ */
  private Map<String, String> domainMap = null;

  /**
   * ドメインからデータ型を取得します。
   * 
   * @param domain ドメイン
   * @return データ型
   */
  public String getDataType(String domain) {
    return getDomainMap().get(domain);
  }

  /**
   * ドメインマップを取得します。
   * 
   * @return ドメインマップ
   */
  public Map<String, String> getDomainMap() {
    return domainMap;
  }

  /**
   * ドメインマップを設定します。
   * 
   * @param domainMap ドメインマップ
   */
  public void setDomainMap(Map<String, String> domainMap) {
    this.domainMap = domainMap;
  }

}
