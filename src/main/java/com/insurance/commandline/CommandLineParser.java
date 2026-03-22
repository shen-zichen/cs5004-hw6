package com.insurance.commandline;

import com.insurance.ProgramConfig;

public class CommandLineParser {

  /**
   * Parse args[] and returns a valid ProgramConfig
   *
   * @param args the raw command-line arguments from main()
   * @return a fully validated ProgramConfig
   * @throws InvalidArgumentException if arguments are missing or invalid
   */
  public ProgramConfig parse(String[] args) throws InvalidArgumentException {
    // walk args[], build config, validate combinations
    String outputDir = null;
    String csvFilePath = null;
    boolean generateEmail = false;
    String emailTemplatePath = null;
    boolean generateLetter = false;
    String letterTemplatePath = null;

    int i = 0;
    while (i < args.length) {
      switch (args[i]) {
        case "--output-dir":
          outputDir = getNextArg(args, i, "--output-dir");
          i += 2;
          break;
        case "--csv-file":
          csvFilePath = getNextArg(args, i, "--csv-file");
          i += 2;
          break;
        case "--email":
          generateEmail = true;
          i++;
          break;
        case "--email-template":
          emailTemplatePath = getNextArg(args, i, "--email-template");
          i += 2;
          break;
        case "--letter":
          generateLetter = true;
          i++;
          break;
        case "--letter-template":
          letterTemplatePath = getNextArg(args, i, "--letter-template");
          i += 2;
          break;
        default:
          printUsage("Unknown argument: " + args[i]);
          throw new InvalidArgumentException("Unknown argument: " + args[i]);
      }
    }

    // Validate required fields and dependent combinations
    if (csvFilePath == null) {
      printUsage("--csv-file is required");
      throw new InvalidArgumentException("--csv-file is required");
    }
    if (outputDir == null) {
      printUsage("--output-dir is required");
      throw new InvalidArgumentException("--output-dir is required");
    }
    if (!generateEmail && !generateLetter) {
      printUsage("At leas one of --email or --letter required");
      throw new InvalidArgumentException("--email or --letter required");
    }
    if (generateEmail && emailTemplatePath == null) {
      printUsage("--email provided but --email-template was not given");
      throw new InvalidArgumentException("--email-template was not given");
    }
    if (generateLetter && letterTemplatePath == null) {
      printUsage("--letter provided but --letter-template was not given");
      throw new InvalidArgumentException("--letter-template was not given");
    }

    return new ProgramConfig(outputDir, csvFilePath,
        generateEmail, emailTemplatePath,
        generateLetter, letterTemplatePath);
  }

  /**
   * Retrieves the value immediately following a flag in the args array.
   *
   * @param args  the full arguments array
   * @param index the index of the flag
   * @param flag  the flag name, used in the error message
   * @return the value at index + 1
   * @throws InvalidArgumentException if no value follows the flag
   */
  private String getNextArg(String[] args, int index, String flag) throws InvalidArgumentException {
    if (index + 1 >= args.length || args[index + 1].startsWith("--")) {
      printUsage(flag + " requires a value.");
      throw new InvalidArgumentException(flag + " requires a value.");
    }
    return args[index + 1];
  }

  /**
   * Prints a usage message to System.err with the error, all flags, and two usage examples.
   *
   * @param errorMessage the specific error that occurred
   */
  private void printUsage(String errorMessage) {
    System.err.println("Error: " + errorMessage);
    System.err.println();

    System.err.println("Usage:");
    System.err.println("  --email                        Generate email messages.");
    System.err.println("  --email-template <path>        Path to the email template file.");
    System.err.println("  --letter                       Generate letters.");
    System.err.println("  --letter-template <path>       Path to the letter template file.");
    System.err.println(
        "  --output-dir <path>            Folder to store generated files (required).");
    System.err.println("  --csv-file <path>              CSV file to process (required).");
    System.err.println();

    System.err.println("Examples:");
    System.err.println(
        "  --email --email-template email-template.txt --output-dir emails --csv-file customers.csv");
    System.err.println(
        "  --letter --letter-template letter-template.txt --output-dir letters --csv-file customers.csv");
  }
}