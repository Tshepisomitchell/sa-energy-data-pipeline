package za.co.tshepiso.energy.parser;

import za.co.tshepiso.energy.model.EnergyReading;
import za.co.tshepiso.energy.validation.EnergyReadingValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CsvEnergyParser {

    private final EnergyReadingValidator validator;

    public CsvEnergyParser(EnergyReadingValidator validator) {
        this.validator = validator;
    }

    public List<EnergyReading> parse(Path csvPath) throws IOException {
        List<EnergyReading> validReadings = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (lineNumber == 1 || line.isBlank()) {
                    continue;
                }

                try {
                    EnergyReading reading = parseLine(line);

                    List<String> errors = validator.validate(reading);

                    if (errors.isEmpty()) {
                        validReadings.add(reading);
                    } else {
                        System.out.println(
                                "Rejected line " + lineNumber + ": " +
                                        String.join(", ", errors)
                        );
                    }
                } catch (IllegalArgumentException exception) {
                    System.out.println(
                            "Rejected line " + lineNumber + ": " +
                                    exception.getMessage()
                    );
                }
            }
        }

        return validReadings;
    }

    private EnergyReading parseLine(String line) {
        String[] values = line.split(",", -1);

        if (values.length != 6) {
            throw new IllegalArgumentException(
                    "Expected 6 columns but found " + values.length
            );
        }

        try {
            int readingId = Integer.parseInt(values[0].trim());
            String householdId = values[1].trim();
            LocalDate readingDate = LocalDate.parse(values[2].trim());
            double usageKwh = Double.parseDouble(values[3].trim());
            double costPerKwh = Double.parseDouble(values[4].trim());
            String municipality = values[5].trim();

            return new EnergyReading(
                    readingId,
                    householdId,
                    readingDate,
                    usageKwh,
                    costPerKwh,
                    municipality
            );
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid numeric value");
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Invalid date");
        }
    }
}
