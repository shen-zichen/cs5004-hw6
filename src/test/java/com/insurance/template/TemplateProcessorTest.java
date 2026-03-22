package com.insurance.template;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TemplateProcessorTest {

  @TempDir
  Path tempDir;

  private TemplateProcessor makeProcessor(String content) throws IOException {
    Path templateFile = tempDir.resolve("template.txt");
    Files.writeString(templateFile, content);
    return new TemplateProcessor(templateFile.toString());
  }

  @Test
  void replaceSinglePlaceholder() throws IOException {
    TemplateProcessor processor = makeProcessor("Hello [[first_name]]!");
    String result = processor.process(Map.of("first_name", "Art"));
    assertEquals("Hello Art!" + System.lineSeparator(), result);
  }

  @Test
  void replaceMultiplePlaceholders() throws IOException {
    TemplateProcessor processor = makeProcessor("Dear [[first_name]] [[last_name]],");
    String result = processor.process(Map.of("first_name", "Art", "last_name", "Venere"));
    assertEquals("Dear Art Venere," + System.lineSeparator(), result);
  }

  @Test
  void unknownPlaceholderLeftUnchanged() throws IOException {
    TemplateProcessor processor = makeProcessor("Hello [[unknown]]!");
    String result = processor.process(Map.of("first_name", "Art"));
    assertTrue(result.contains("[[unknown]]"));
  }

  @Test
  void emptyRowDataLeavesAllPlaceholders() throws IOException {
    TemplateProcessor processor = makeProcessor("[[first_name]] [[last_name]]");
    String result = processor.process(Map.of());
    assertTrue(result.contains("[[first_name]]"));
    assertTrue(result.contains("[[last_name]]"));
  }

  @Test
  void templateWithNoPlaceholders() throws IOException {
    TemplateProcessor processor = makeProcessor("No placeholders here.");
    String result = processor.process(Map.of("first_name", "Art"));
    assertTrue(result.contains("No placeholders here."));
  }
}