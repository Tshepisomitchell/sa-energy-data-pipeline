package za.co.tshepiso.energy.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import za.co.tshepiso.energy.model.EnergyReading;
import za.co.tshepiso.energy.validation.EnergyReadingValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvEnergyParserTest {

    private CsvEnergyParser parser;

    @TempDir
    Path temporaryDirectory;

    @BeforeEach
    void setUp() {
        parser = new CsvEnergyParser(
                new EnergyReadingValidator()
        );
    }

    @Test
    void shouldParseValidCsvRecord() throws IOException {
        Path csvFile = createCsv("""
                reading_id,household_id,reading_date,usage_kwh,cost_per_kwh,municipality
                1,HH001,2026-09-15,12.5,3.10,Johannesburg
                """);

        List<EnergyReading> readings =
                parser.parse(csvFile);

        assertEquals(1, readings.size());
        assertEquals("HH001", readings.get(0).getHouseholdId());
        assertEquals(12.5, readings.get(0).getUsageKwh());
    }

    @Test
    void shouldRejectNegativeUsage() throws IOException {
        Path csvFile = createCsv("""
                reading_id,household_id,reading_date,usage_kwh,cost_per_kwh,municipality
                1,HH001,2026-09-15,-12.5,3.10,Johannesburg
                """);

        List<EnergyReading> readings =
                parser.parse(csvFile);

        assertEquals(0, readings.size());
    }

    @Test
    void shouldRejectInvalidDate() throws IOException {
        Path csvFile = createCsv("""
                reading_id,household_id,reading_date,usage_kwh,cost_per_kwh,municipality
                1,HH001,not-a-date,12.5,3.10,Johannesburg
                """);

        List<EnergyReading> readings =
                parser.parse(csvFile);

        assertEquals(0, readings.size());
    }

    @Test
    void shouldKeepValidRecordsAndRejectInvalidRecords()
            throws IOException {

        Path csvFile = createCsv("""
                reading_id,household_id,reading_date,usage_kwh,cost_per_kwh,municipality
                1,HH001,2026-09-15,12.5,3.10,Johannesburg
                2,HH002,2026-09-16,-5.0,3.10,Tshwane
                3,HH003,2026-09-17,8.4,2.95,Ekurhuleni
                """);

        List<EnergyReading> readings =
                parser.parse(csvFile);

        assertEquals(2, readings.size());
        assertEquals(1, readings.get(0).getReadingId());
        assertEquals(3, readings.get(1).getReadingId());
    }

    private Path createCsv(String content)
            throws IOException {

        Path csvFile = temporaryDirectory.resolve(
                "test-energy.csv"
        );

        return Files.writeString(csvFile, content);
    }
}
