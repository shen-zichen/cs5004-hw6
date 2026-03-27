package com.insurance.csv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CSVParserTest {

    @TempDir
    Path tempDir;

    CSVParser parser = new CSVParser();

    private Path makeCSV(String content) throws IOException {
        Path file = tempDir.resolve("test.csv");
        Files.writeString(file, content);
        return file;
    }

    @Test
    void parsesHeadersCorrectly() throws IOException {
        Path csv = makeCSV(
                "\"first_name\",\"last_name\",\"email\"\n" +
                        "\"Art\",\"Venere\",\"art@venere.org\"\n"
        );
        List<Map<String, String>> rows = parser.parse(csv.toString());
        assertTrue(rows.get(0).containsKey("first_name"));
        assertTrue(rows.get(0).containsKey("last_name"));
        assertTrue(rows.get(0).containsKey("email"));
    }

    @Test
    void parsesBasicRow() throws IOException {
        Path csv = makeCSV(
                "\"first_name\",\"last_name\",\"email\"\n" +
                        "\"Art\",\"Venere\",\"art@venere.org\"\n"
        );
        List<Map<String, String>> rows = parser.parse(csv.toString());
        assertEquals(1, rows.size());
        assertEquals("Art", rows.get(0).get("first_name"));
        assertEquals("Venere", rows.get(0).get("last_name"));
        assertEquals("art@venere.org", rows.get(0).get("email"));
    }

    @Test
    void handlesCommaInsideQuotedField() throws IOException {
        Path csv = makeCSV(
                "\"first_name\",\"company_name\"\n" +
                        "\"Art\",\"Chemel, James L Cpa\"\n"
        );
        List<Map<String, String>> rows = parser.parse(csv.toString());
        assertEquals("Chemel, James L Cpa", rows.get(0).get("company_name"));
    }

    @Test
    void parsesMultipleRows() throws IOException {
        Path csv = makeCSV(
                "\"first_name\",\"last_name\"\n" +
                        "\"Art\",\"Venere\"\n" +
                        "\"James\",\"Butt\"\n"
        );
        List<Map<String, String>> rows = parser.parse(csv.toString());
        assertEquals(2, rows.size());
        assertEquals("Art", rows.get(0).get("first_name"));
        assertEquals("James", rows.get(1).get("first_name"));
    }

    @Test
    void emptyFileReturnsEmptyList() throws IOException {
        Path csv = makeCSV("");
        List<Map<String, String>> rows = parser.parse(csv.toString());
        assertTrue(rows.isEmpty());
    }

    @Test
    void stripsQuotesFromValues() throws IOException {
        Path csv = makeCSV(
                "\"first_name\"\n" +
                        "\"Art\"\n"
        );
        List<Map<String, String>> rows = parser.parse(csv.toString());
        assertEquals("Art", rows.get(0).get("first_name"));
        assertFalse(rows.get(0).get("first_name").contains("\""));
    }

    @Test
    void skipsBlankLines() throws IOException {
        Path csv = makeCSV(
                "\"first_name\",\"last_name\"\n" +
                        "\"Art\",\"Venere\"\n" +
                        "\n" +
                        "\"James\",\"Butt\"\n"
        );
        List<Map<String, String>> rows = parser.parse(csv.toString());
        assertEquals(2, rows.size());
    }
}