package com.insurance.commandline;

/**
 * Thrown when the program is invoked with an illegal combination
 * of command-line arguments. The message should be human-readable
 * and describe specifically what was wrong.
 */
public class InvalidArgumentException extends Exception {

  /**
   * @param message a specific error message, e.g.
   *   "--email provided but --email-template was not given."
   */
  public InvalidArgumentException(String message) {
    super(message);
  }
}