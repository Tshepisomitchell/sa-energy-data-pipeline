package za.co.tshepiso.energy.report;

import za.co.tshepiso.energy.model.MunicipalitySummary;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class CsvReportExporter {

    public void export(
            List<MunicipalitySummary> summaries,
            Path outputPath
    ) throws IOException {

        Path parentDirectory = outputPath.getParent();

        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        try (BufferedWriter writer =
                     Files.newBufferedWriter(outputPath)) {

            writer.write(
                    "municipality,reading_count," +
                            "total_usage_kwh,total_cost"
            );
            writer.newLine();

            for (MunicipalitySummary summary : summaries) {
                String row = String.format(
                        Locale.US,
                        "%s,%d,%.2f,%.2f",
                        summary.municipality(),
                        summary.readingCount(),
                        summary.totalUsageKwh(),
                        summary.totalCost()
                );

                writer.write(row);
                writer.newLine();
            }
        }
    }
}
