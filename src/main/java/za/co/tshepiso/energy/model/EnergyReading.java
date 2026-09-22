package za.co.tshepiso.energy.model;

import java.time.LocalDate;

public class EnergyReading {

    private final int readingId;
    private final String householdId;
    private final LocalDate readingDate;
    private final double usageKwh;
    private final double costPerKwh;
    private final String municipality;

    public EnergyReading(
            int readingId,
            String householdId,
            LocalDate readingDate,
            double usageKwh,
            double costPerKwh,
            String municipality
    ) {
        this.readingId = readingId;
        this.householdId = householdId;
        this.readingDate = readingDate;
        this.usageKwh = usageKwh;
        this.costPerKwh = costPerKwh;
        this.municipality = municipality;
    }

    public int getReadingId() {
        return readingId;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public LocalDate getReadingDate() {
        return readingDate;
    }

    public double getUsageKwh() {
        return usageKwh;
    }

    public double getCostPerKwh() {
        return costPerKwh;
    }

    public String getMunicipality() {
        return municipality;
    }

    public double calculateCost() {
        return usageKwh * costPerKwh;
    }

    @Override
    public String toString() {
        return "EnergyReading{" +
                "readingId=" + readingId +
                ", householdId='" + householdId + '\'' +
                ", readingDate=" + readingDate +
                ", usageKwh=" + usageKwh +
                ", costPerKwh=" + costPerKwh +
                ", municipality='" + municipality + '\'' +
                '}';
    }
}
