package za.co.tshepiso.energy.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.tshepiso.energy.model.EnergyReading;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnergyReadingValidatorTest {

    private EnergyReadingValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EnergyReadingValidator();
    }

    @Test
    void shouldAcceptValidReading() {
        EnergyReading reading = new EnergyReading(
                1,
                "HH001",
                LocalDate.of(2026, 9, 15),
                12.5,
                3.10,
                "Johannesburg"
        );

        assertTrue(validator.isValid(reading));
        assertTrue(validator.validate(reading).isEmpty());
    }

    @Test
    void shouldRejectMissingHouseholdId() {
        EnergyReading reading = new EnergyReading(
                1,
                "",
                LocalDate.of(2026, 9, 15),
                12.5,
                3.10,
                "Johannesburg"
        );

        List<String> errors = validator.validate(reading);

        assertFalse(validator.isValid(reading));
        assertTrue(errors.contains("Household ID is required"));
    }

    @Test
    void shouldRejectNegativeUsage() {
        EnergyReading reading = new EnergyReading(
                1,
                "HH001",
                LocalDate.of(2026, 9, 15),
                -5.0,
                3.10,
                "Johannesburg"
        );

        List<String> errors = validator.validate(reading);

        assertTrue(errors.contains("Usage cannot be negative"));
    }

    @Test
    void shouldRejectZeroCostPerKwh() {
        EnergyReading reading = new EnergyReading(
                1,
                "HH001",
                LocalDate.of(2026, 9, 15),
                12.5,
                0,
                "Johannesburg"
        );

        List<String> errors = validator.validate(reading);

        assertTrue(
                errors.contains(
                        "Cost per kWh must be greater than zero"
                )
        );
    }

    @Test
    void shouldReportMultipleValidationErrors() {
        EnergyReading reading = new EnergyReading(
                0,
                "",
                null,
                -1,
                0,
                ""
        );

        List<String> errors = validator.validate(reading);

        assertEquals(6, errors.size());
    }
}
