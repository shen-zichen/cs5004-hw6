package com.insurance.template;

import java.util.Map;

/**
 * Fills in a pre-loaded template by replacing [[placeholder]] tokens
 * with values from a data row.
 */
public interface ITemplateProcessor {

  /**
   * Processes one row of data against the template.
   * Every occurrence of [[key]] in the template is replaced with
   * rowData.get(key). If a key is present in the template but missing
   * from rowData, the placeholder is left unchanged.
   *
   * @param rowData a Map from CSV header names to cell values for one row
   * @return the template content with all known placeholders replaced
   */
  String process(Map<String, String> rowData);
}