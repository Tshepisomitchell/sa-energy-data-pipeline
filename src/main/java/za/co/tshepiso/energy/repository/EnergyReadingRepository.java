package za.co.tshepiso.energy.repository;

import za.co.tshepiso.energy.model.EnergyReading;
import za.co.tshepiso.energy.model.MunicipalitySummary;

import java.sql.SQLException;
import java.util.List;

public interface EnergyReadingRepository {

    void createTable() throws SQLException;

    void saveAll(List<EnergyReading> readings)
            throws SQLException;

    List<EnergyReading> findAll() throws SQLException;

    int count() throws SQLException;

    double calculateTotalUsage() throws SQLException;

    double calculateTotalCost() throws SQLException;

    List<MunicipalitySummary> summarizeByMunicipality()
            throws SQLException;
}