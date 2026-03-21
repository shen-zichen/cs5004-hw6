package com.insurance.csv;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Parses a CSV file into a list of rows.
 * Each row is represented as a Map from column header to cell value.
 * The first line of the CSV is treated as the header row.
 */
public interface ICSVParser {

  /**
   * Parses the CSV file at the given path.
   *
   * @param filePath absolute or relative path to the CSV file
   * @return a List of Maps, one Map per data row (not including the header);
   *         keys are the column headers (e.g. "first_name", "email"),
   *         values are the cell contents with surrounding quotes stripped
   * @throws IOException if the file cannot be read
   */
  List<Map<String, String>> parse(String filePath) throws IOException;
}