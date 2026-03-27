package com.insurance.csv;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Parses a CSV file into a list of rows. Each row is a Map from
 * column header name to cell value.
 * example input: "James", "Butt", “Chemel, James L Cpa", "New Orleans"
 * example output: [{first_name=James, last_name=Butt, Company_name=James L Cpa, location=New Orleans}
 */
public interface ICSVParser {

  /**
   * @param filePath path to the CSV file
   * @return one Map per data row; keys are header names, values have quotes stripped
   * @throws IOException if the file cannot be read
   */
  List<Map<String, String>> parse(String filePath) throws IOException;
}