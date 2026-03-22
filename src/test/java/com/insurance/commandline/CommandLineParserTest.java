package com.insurance.commandline;

import com.insurance.ProgramConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandLineParserTest {

  CommandLineParser parser = new CommandLineParser();

  @Test
  void validEmailArgs() throws InvalidArgumentException {
    String[] args = {
        "--email", "--email-template", "email.txt",
        "--csv-file", "customers.csv",
        "--output-dir", "output/"
    };
    ProgramConfig config = parser.parse(args);
    assertTrue(config.isGenerateEmail());
    assertEquals("email.txt", config.getEmailTemplatePath());
    assertEquals("customers.csv", config.getCsvFilePath());
    assertEquals("output/", config.getOutputDir());
  }

  @Test
  void validLetterArgs() throws InvalidArgumentException {
    String[] args = {
        "--letter", "--letter-template", "letter.txt",
        "--csv-file", "customers.csv",
        "--output-dir", "output/"
    };
    ProgramConfig config = parser.parse(args);
    assertTrue(config.isGenerateLetter());
    assertFalse(config.isGenerateEmail());
  }

  @Test
  void validBothEmailAndLetter() throws InvalidArgumentException {
    String[] args = {
        "--email", "--email-template", "email.txt",
        "--letter", "--letter-template", "letter.txt",
        "--csv-file", "customers.csv",
        "--output-dir", "output/"
    };
    ProgramConfig config = parser.parse(args);
    assertTrue(config.isGenerateEmail());
    assertTrue(config.isGenerateLetter());
  }

  @Test
  void missingCsvFile() {
    String[] args = {
        "--email", "--email-template", "email.txt",
        "--output-dir", "output/"
    };
    assertThrows(InvalidArgumentException.class, () -> parser.parse(args));
  }

  @Test
  void missingOutputDir() {
    String[] args = {
        "--email", "--email-template", "email.txt",
        "--csv-file", "customers.csv"
    };
    assertThrows(InvalidArgumentException.class, () -> parser.parse(args));
  }

  @Test
  void emailFlagWithoutTemplate() {
    String[] args = {
        "--email",
        "--csv-file", "customers.csv",
        "--output-dir", "output/"
    };
    assertThrows(InvalidArgumentException.class, () -> parser.parse(args));
  }

  @Test
  void letterFlagWithoutTemplate() {
    String[] args = {
        "--letter",
        "--csv-file", "customers.csv",
        "--output-dir", "output/"
    };
    assertThrows(InvalidArgumentException.class, () -> parser.parse(args));
  }

  @Test
  void neitherEmailNorLetter() {
    String[] args = {
        "--csv-file", "customers.csv",
        "--output-dir", "output/"
    };
    assertThrows(InvalidArgumentException.class, () -> parser.parse(args));
  }

  @Test
  void unknownFlag() {
    String[] args = {
        "--email", "--email-template", "email.txt",
        "--csv-file", "customers.csv",
        "--output-dir", "output/",
        "--unknown-flag"
    };
    assertThrows(InvalidArgumentException.class, () -> parser.parse(args));
  }
}