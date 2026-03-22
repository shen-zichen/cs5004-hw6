package com.insurance.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads a template file once at construction, then fills in [[placeholder]] tokens on demand for
 * each CSV row.
 */
public class TemplateProcessor implements ITemplateProcessor {

  private static final Pattern PLACEHOLDER = Pattern.compile("\\[\\[([a-zA-Z0-9_]+)\\]\\]");
  private final String templateContent;

  /**
   * Reads and caches the template file. The file is never re-read after construction
   *
   * @param templatePath path to the template text file
   * @throws IOException if the file cannot be read
   */
  public TemplateProcessor(String templatePath) throws IOException {
    StringBuilder sb = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(
        new FileReader(templatePath, StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append(System.lineSeparator());
      }
    }
    this.templateContent = sb.toString();
  }

  @Override
  public String process(Map<String, String> rowData) {
    Matcher matcher = PLACEHOLDER.matcher(templateContent);
    StringBuilder result = new StringBuilder();
    while (matcher.find()) {
      String key = matcher.group(1);
      String value = rowData.getOrDefault(key, matcher.group(0));
      matcher.appendReplacement(result, Matcher.quoteReplacement(value));
    }
    matcher.appendTail(result);
    return result.toString();
  }
}
