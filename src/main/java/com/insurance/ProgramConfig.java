package com.insurance;

/**
 * Holds the validated configuration derived from command-line arguments. All fields are set at
 * construction time and are immutable.
 */
public class ProgramConfig {

  private final String outputDir;
  private final String csvFilePath;
  private final boolean generateEmail;
  private final String emailTemplatePath;   // null if --email not specified
  private final boolean generateLetter;
  private final String letterTemplatePath;  // null if --letter not specified

  /**
   * @param outputDir          path to the output directory (required)
   * @param csvFilePath        path to the CSV file (required)
   * @param generateEmail      true if --email was provided
   * @param emailTemplatePath  path to email template, or null
   * @param generateLetter     true if --letter was provided
   * @param letterTemplatePath path to letter template, or null
   */
  public ProgramConfig(String outputDir, String csvFilePath,
      boolean generateEmail, String emailTemplatePath,
      boolean generateLetter, String letterTemplatePath) {
    this.outputDir = outputDir;
    this.csvFilePath = csvFilePath;
    this.generateEmail = generateEmail;
    this.emailTemplatePath = emailTemplatePath;
    this.generateLetter = generateLetter;
    this.letterTemplatePath = letterTemplatePath;
  }

  public String getOutputDir() {
    return outputDir;
  }

  public String getCsvFilePath() {
    return csvFilePath;
  }

  public boolean isGenerateEmail() {
    return generateEmail;
  }

  public String getEmailTemplatePath() {
    return emailTemplatePath;
  }  // only call if isGenerateEmail()

  public boolean isGenerateLetter() {
    return generateLetter;
  }

  public String getLetterTemplatePath() {
    return letterTemplatePath;
  }  // only call if isGenerateLetter()
}