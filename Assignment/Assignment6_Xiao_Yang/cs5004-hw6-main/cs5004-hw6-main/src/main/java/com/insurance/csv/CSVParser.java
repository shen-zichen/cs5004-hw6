package com.insurance.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses a CSV file where values are enclosed in double quotes and separated
 * by commas. Correctly handles commas that appear inside quoted fields.
 */
public class CSVParser implements ICSVParser {

    @Override
    public List<Map<String, String>> parse(String filePath) throws IOException {
        List<Map<String, String>> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filePath, StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();
            if (headerLine == null) {
                return rows;
            }

            String[] headers = parseLine(headerLine);
            for (int i = 0; i < headers.length; i++) {
                headers[i] = stripQuotes(headers[i].trim());
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] values = parseLine(line);
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    String value = i < values.length ? stripQuotes(values[i].trim()) : "";
                    row.put(headers[i], value);
                }
                rows.add(row);
            }
        }

        return rows;
    }

    private String[] parseLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                insideQuotes = !insideQuotes;
                current.append(c);
            } else if (c == ',' && !insideQuotes) {
                tokens.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        tokens.add(current.toString());
        return tokens.toArray(new String[0]);
    }

    private String stripQuotes(String rawValue) {
        if (rawValue.startsWith("\"") && rawValue.endsWith("\"") && rawValue.length() >= 2) {
            return rawValue.substring(1, rawValue.length() - 1);
        }
        return rawValue;
    }
}