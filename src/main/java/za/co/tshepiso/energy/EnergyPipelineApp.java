package za.co.tshepiso.energy;

import za.co.tshepiso.energy.model.EnergyReading;
import za.co.tshepiso.energy.parser.CsvEnergyParser;
import za.co.tshepiso.energy.validation.EnergyReadingValidator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class EnergyPipelineApp {

    public static void main(String[] args) {
        Path inputFile = Path.of("data", "energy_usage.csv");

        EnergyReadingValidator validator = new EnergyReadingValidator();
        CsvEnergyParser parser = new CsvEnergyParser(validator);

        try {
            List<EnergyReading> readings = parser.parse(inputFile);

            System.out.println();
            System.out.println("SA ENERGY DATA PIPELINE");
            System.out.println("=======================");
            System.out.println("Valid records: " + readings.size());

            double totalUsage = readings.stream()
                    .mapToDouble(EnergyReading::getUsageKwh)
                    .sum();

            double totalCost = readings.stream()
                    .mapToDouble(EnergyReading::calculateCost)
                    .sum();

            System.out.printf("Total usage: %.2f kWh%n", totalUsage);
            System.out.printf("Total estimated cost: R%.2f%n", totalCost);

            System.out.println();
            System.out.println("VALID READINGS");

            readings.forEach(System.out::println);

        } catch (IOException exception) {
            System.err.println(
                    "Could not read the input file: " +
                            exception.getMessage()
            );
        }
    }
}
