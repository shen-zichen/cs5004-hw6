package com.insurance;

import com.insurance.csv.ICSVParser;
import com.insurance.output.IFileWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommunicationAutomatorTest {

  @TempDir
  Path tempDir;

  // Stub CSV parser — returns fake rows without needing a real file
  ICSVParser stubParser = (path) -> List.of(
      Map.of("first_name", "Art", "last_name", "Venere", "email", "art@venere.org"),
      Map.of("first_name", "James", "last_name", "Butt", "email", "jbutt@gmail.com")
  );

  // Tracks every write call made by the automator
  List<String> writtenFiles = new ArrayList<>();
  IFileWriter trackingWriter = (dir, filename, content) -> writtenFiles.add(filename);

  @Test
  void emailOnlyGeneratesEmailFiles() throws IOException {
    ProgramConfig config = makeEmailConfig();
    new CommunicationAutomator(stubParser, trackingWriter).run(config);
    assertEquals(2, writtenFiles.size());
    assertTrue(writtenFiles.get(0).startsWith("email_"));
    assertTrue(writtenFiles.get(1).startsWith("email_"));
  }

  @Test
  void letterOnlyGeneratesLetterFiles() throws IOException {
    ProgramConfig config = makeLetterConfig();
    new CommunicationAutomator(stubParser, trackingWriter).run(config);
    assertEquals(2, writtenFiles.size());
    assertTrue(writtenFiles.get(0).startsWith("letter_"));
  }

  @Test
  void bothEmailAndLetterGeneratesBothTypes() throws IOException {
    ProgramConfig config = makeBothConfig();
    new CommunicationAutomator(stubParser, trackingWriter).run(config);
    assertEquals(4, writtenFiles.size()); // 2 rows x 2 types
    assertTrue(writtenFiles.stream().anyMatch(f -> f.startsWith("email_")));
    assertTrue(writtenFiles.stream().anyMatch(f -> f.startsWith("letter_")));
  }

  @Test
  void outputContainsReplacedPlaceholders() throws IOException {
    // Use a real template file and real file writer for this one
    Path templateFile = tempDir.resolve("email.txt");
    Files.writeString(templateFile, "Dear [[first_name]] [[last_name]],");

    List<String> contents = new ArrayList<>();
    IFileWriter capturingWriter = (dir, filename, content) -> contents.add(content);

    ProgramConfig config = new ProgramConfig(
        tempDir.toString(), "ignored",
        true, templateFile.toString(),
        false, null
    );

    new CommunicationAutomator(stubParser, capturingWriter).run(config);
    assertTrue(contents.get(0).contains("Art Venere"));
    assertTrue(contents.get(1).contains("James Butt"));
  }

  // --- helpers ---

  private ProgramConfig makeEmailConfig() throws IOException {
    Path t = tempDir.resolve("email.txt");
    Files.writeString(t, "Hi [[first_name]]");
    return new ProgramConfig(tempDir.toString(), "ignored", true, t.toString(), false, null);
  }

  private ProgramConfig makeLetterConfig() throws IOException {
    Path t = tempDir.resolve("letter.txt");
    Files.writeString(t, "Dear [[first_name]]");
    return new ProgramConfig(tempDir.toString(), "ignored", false, null, true, t.toString());
  }

  private ProgramConfig makeBothConfig() throws IOException {
    Path et = tempDir.resolve("email.txt");
    Path lt = tempDir.resolve("letter.txt");
    Files.writeString(et, "Hi [[first_name]]");
    Files.writeString(lt, "Dear [[first_name]]");
    return new ProgramConfig(tempDir.toString(), "ignored", true, et.toString(), true, lt.toString());
  }
}