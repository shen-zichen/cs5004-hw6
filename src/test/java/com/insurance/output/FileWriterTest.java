package com.insurance.output;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileWriterTest {

  @TempDir
  Path tempDir;

  FileWriter writer = new FileWriter();

  @Test
  void writesFileWithCorrectContent() throws IOException {
    String outputDir = tempDir.toString();
    writer.write(outputDir, "email_1.txt", "Hello Art!");
    Path file = tempDir.resolve("email_1.txt");
    assertTrue(Files.exists(file));
    assertEquals("Hello Art!", Files.readString(file));
  }

  @Test
  void createsOutputDirIfNotExists() throws IOException {
    String newDir = tempDir.resolve("newFolder").toString();
    writer.write(newDir, "email_1.txt", "content");
    assertTrue(Files.exists(Path.of(newDir)));
  }

  @Test
  void writesMultipleFilesToSameDir() throws IOException {
    String outputDir = tempDir.toString();
    writer.write(outputDir, "email_1.txt", "first");
    writer.write(outputDir, "email_2.txt", "second");
    assertEquals("first", Files.readString(tempDir.resolve("email_1.txt")));
    assertEquals("second", Files.readString(tempDir.resolve("email_2.txt")));
  }
}