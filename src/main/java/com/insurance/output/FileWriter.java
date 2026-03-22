package com.insurance.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Writes text files to an output directory, creating the directory if it does not exists.
 */
public class FileWriter implements IFileWriter {

  @Override
  public void write(String outputDir, String filename, String content) throws IOException {
    Path dirPath = Paths.get(outputDir);
    Files.createDirectories(dirPath);

    Path filePath = dirPath.resolve(filename);
    try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
      writer.write(content);
    }
  }
}
