package za.co.tshepiso.energy.validation;

import za.co.tshepiso.energy.model.EnergyReading;

import java.util.ArrayList;
import java.util.List;

public class EnergyReadingValidator {

    public List<String> validate(EnergyReading reading) {
        List<String> errors = new ArrayList<>();

        if (reading.getReadingId() <= 0) {
            errors.add("Reading ID must be greater than zero");
        }

        if (reading.getHouseholdId() == null
                || reading.getHouseholdId().isBlank()) {
            errors.add("Household ID is required");
        }

        if (reading.getReadingDate() == null) {
            errors.add("Reading date is required");
        }

        if (reading.getUsageKwh() < 0) {
            errors.add("Usage cannot be negative");
        }

        if (reading.getCostPerKwh() <= 0) {
            errors.add("Cost per kWh must be greater than zero");
        }

        if (reading.getMunicipality() == null
                || reading.getMunicipality().isBlank()) {
            errors.add("Municipality is required");
        }

        return errors;
    }

    public boolean isValid(EnergyReading reading) {
        return validate(reading).isEmpty();
    }
}