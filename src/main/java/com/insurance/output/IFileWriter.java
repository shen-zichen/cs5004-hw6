package com.insurance.output;

import java.io.IOException;

/**
 * Writes a single text document to the output directory.
 */
public interface IFileWriter {

  /**
   * Writes content to a file named [filename] inside [outputDir].
   * Creates outputDir if it does not already exist.
   *
   * @param outputDir path to the output folder (created if absent)
   * @param filename  name of the file to write (e.g. "email_1.txt")
   * @param content   the full text content to write
   * @throws IOException if the file cannot be written
   */
  void write(String outputDir, String filename, String content) throws IOException;
}