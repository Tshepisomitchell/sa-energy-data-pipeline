# SA Energy Data Pipeline

A Java ETL application that imports South African electricity usage data, validates the records, stores valid data in SQLite, performs SQL-based analytics, and exports a municipality summary report.

## Project purpose

Energy-usage datasets may contain missing, malformed, or invalid records. This project demonstrates how a data engineering pipeline can clean raw data before storing and analysing it.

The application follows the ETL process:

1. **Extract** electricity readings from a CSV file.
2. **Transform** the data into Java objects and validate each record.
3. **Load** valid records into a relational SQLite database.
4. **Analyse** consumption and cost by municipality.
5. **Export** the processed results as a new CSV report.

## Features

- CSV data ingestion
- Data-type conversion
- Missing-value validation
- Invalid date detection
- Negative usage detection
- SQLite database persistence
- Transaction-based batch inserts
- Duplicate-reading protection
- SQL aggregation by municipality
- Total energy and cost calculations
- Processed CSV report generation
- Automated tests with JUnit 5

## Technologies

- Java 21
- Maven
- SQLite
- JDBC
- JUnit 5
- Git and GitHub
- IntelliJ IDEA

## Project structure

```text
sa-energy-data-pipeline/
├── data/
│   └── energy_usage.csv
├── reports/
│   └── municipality_summary.csv
├── src/
│   ├── main/java/za/co/tshepiso/energy/
│   │   ├── EnergyPipelineApp.java
│   │   ├── model/
│   │   ├── parser/
│   │   ├── report/
│   │   ├── repository/
│   │   └── validation/
│   └── test/java/za/co/tshepiso/energy/
├── pom.xml
└── README.md