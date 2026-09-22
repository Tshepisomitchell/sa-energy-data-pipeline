package za.co.tshepiso.energy.model;

public record MunicipalitySummary(
        String municipality,
        int readingCount,
        double totalUsageKwh,
        double totalCost
) {
}