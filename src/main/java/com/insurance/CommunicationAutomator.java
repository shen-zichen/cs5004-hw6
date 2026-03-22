package com.insurance;

import com.insurance.csv.ICSVParser;
import com.insurance.output.IFileWriter;
import com.insurance.template.ITemplateProcessor;
import com.insurance.template.TemplateProcessor;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Orchestrates the full pipeline: reads the CSV, loads templates, processes each row, and writes
 * one output file per customer per type
 */
public class CommunicationAutomator {

  private final ICSVParser csvParser;
  private final IFileWriter fileWriter;

  /**
   * Constructs an automator with injected dependencies. Using interfaces here allows unit testing
   * with stubs.
   *
   * @param csvParser
   * @param fileWriter
   */
  public CommunicationAutomator(ICSVParser csvParser, IFileWriter fileWriter) {
    this.csvParser = csvParser;
    this.fileWriter = fileWriter;
  }

  /**
   * Runs the full pipeline based on the given config
   *
   * @param config a valid ProgramConfig
   * @throws IOException if any file cannot be read or written
   */
  public void run(ProgramConfig config) throws IOException {
    List<Map<String, String>> rows = csvParser.parse(config.getCsvFilePath());

    if (config.isGenerateEmail()) {
      ITemplateProcessor emailProcessor = new TemplateProcessor(config.getEmailTemplatePath());
      processRows(rows, emailProcessor, config.getOutputDir(), "email");
    }

    if (config.isGenerateLetter()) {
      ITemplateProcessor letterProcessor = new TemplateProcessor(config.getLetterTemplatePath());
      processRows(rows, letterProcessor, config.getOutputDir(), "letter");
    }
  }

  /**
   * Processes all rows against one template and writes output files. Output files are named e.g.
   * [prefix]_1.txt
   *
   * @param rows
   * @param templateProcessor
   * @param outputDir
   * @param prefix
   * @throws IOException
   */
  private void processRows(List<Map<String, String>> rows, ITemplateProcessor templateProcessor,
      String outputDir, String prefix) throws IOException {
    for (int i = 0; i < rows.size(); i++) {
      String content = templateProcessor.process(rows.get(i));
      String filename = prefix + "_" + (i + 1) + ".txt";
      fileWriter.write(outputDir, filename, content);
    }
  }
}
