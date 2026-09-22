package za.co.tshepiso.energy;

import za.co.tshepiso.energy.model.EnergyReading;
import za.co.tshepiso.energy.parser.CsvEnergyParser;
import za.co.tshepiso.energy.repository.EnergyReadingRepository;
import za.co.tshepiso.energy.repository.JdbcEnergyReadingRepository;
import za.co.tshepiso.energy.validation.EnergyReadingValidator;
import za.co.tshepiso.energy.model.MunicipalitySummary;
import za.co.tshepiso.energy.report.CsvReportExporter;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

public class EnergyPipelineApp {

    public static void main(String[] args) {
        Path inputFile = Path.of(
                "data",
                "energy_usage.csv"
        );

        EnergyReadingValidator validator =
                new EnergyReadingValidator();

        CsvEnergyParser parser =
                new CsvEnergyParser(validator);

        EnergyReadingRepository repository =
                new JdbcEnergyReadingRepository();

        try {
            System.out.println("Starting ETL pipeline...");
            System.out.println();

            // Extract and transform
            List<EnergyReading> validReadings =
                    parser.parse(inputFile);

            // Load
            repository.createTable();
            repository.saveAll(validReadings);

            printReport(repository);

        } catch (IOException exception) {
            System.err.println(
                    "CSV processing failed: " +
                            exception.getMessage()
            );
        } catch (SQLException exception) {
            System.err.println(
                    "Database operation failed: " +
                            exception.getMessage()
            );
        }
    }

    private static void printReport(
            EnergyReadingRepository repository
    ) throws SQLException, IOException {

        System.out.println();
        System.out.println("SA ENERGY DATA PIPELINE");
        System.out.println("=======================");

        System.out.println(
                "Records in database: " +
                        repository.count()
        );

        System.out.printf(
                "Total usage: %.2f kWh%n",
                repository.calculateTotalUsage()
        );

        System.out.printf(
                "Total estimated cost: R%.2f%n",
                repository.calculateTotalCost()
        );

        System.out.println();
        System.out.println("MUNICIPALITY SUMMARY");
        System.out.println("--------------------");

        List<MunicipalitySummary> summaries =
                repository.summarizeByMunicipality();

        for (MunicipalitySummary summary : summaries) {
            System.out.printf(
                    "%s: %d readings, %.2f kWh, R%.2f%n",
                    summary.municipality(),
                    summary.readingCount(),
                    summary.totalUsageKwh(),
                    summary.totalCost()
            );
        }

        Path reportPath = Path.of(
                "reports",
                "municipality_summary.csv"
        );

        CsvReportExporter exporter =
                new CsvReportExporter();

        exporter.export(summaries, reportPath);

        System.out.println();
        System.out.println(
                "CSV report created: " +
                        reportPath.toAbsolutePath()
        );
    }
}
