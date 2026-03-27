package com.insurance;

import com.insurance.commandline.*;
import com.insurance.csv.CSVParser;
import com.insurance.output.FileWriter;
import java.io.IOException;

/**
 * Entry point. Parses arguments, constructs dependencies, runs the automator.
 * Contains no business logic — all work is delegated.
 */
public class Main {

  public static void main(String[] args) {
    try {
      ProgramConfig config = new CommandLineParser().parse(args);
       CommunicationAutomator automator =
           new CommunicationAutomator(new CSVParser(), new FileWriter());
      automator.run(config);
    } catch (InvalidArgumentException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    } catch (IOException e) {
      System.err.println("File error: " + e.getMessage());
      System.exit(1);
    }
  }
}