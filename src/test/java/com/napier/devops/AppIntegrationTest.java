package com.napier.devops;

import com.napier.devops.dao.*;
import com.napier.devops.db.Database;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppIntegrationTest {

    private Database db;
    private Connection conn;

    private CountryDAO countryDAO;
    private CityDAO cityDAO;
    private CapitalCityDAO capitalDAO;
    private PopulationDAO populationDAO;
    private LookupDAO lookupDAO;
    private LanguageDAO languageDAO;

    @BeforeAll
    void setupDatabase() {
        db = new Database();
        db.connect("localhost:33060", 30000, "world", "root", "P@ssw0rd!");
        conn = db.getConnection();

        if (conn == null) {
            throw new IllegalStateException("Database connection failed. Cannot proceed with tests.");
        }

        countryDAO = new CountryDAO(conn);
        cityDAO = new CityDAO(conn);
        capitalDAO = new CapitalCityDAO(conn);
        populationDAO = new PopulationDAO(conn);
        lookupDAO = new LookupDAO(conn);
        languageDAO = new LanguageDAO(conn);
    }

    @Test
    void testCountryQueryReturnsResults() {
        List<?> countries = countryDAO.getCountriesByPopulation();
        assertNotNull(countries);
        assertFalse(countries.isEmpty(), "Expected at least one country from DB");
    }

    @Test
    void testCityQueryByCountry() {
        List<?> cities = cityDAO.getCitiesInCountryByName("France");
        assertNotNull(cities);
        assertTrue(cities.stream().anyMatch(c -> c.toString().contains("Paris")));
    }

    @Test
    void testCapitalCityQueryByCountry() {
        List<?> capitals = capitalDAO.getCapitalCitiesInRegion("Caribbean");
        assertNotNull(capitals);
        assertTrue(capitals.stream().anyMatch(c -> c.toString().contains("Barbados")));
    }

    @Test
    void testPopulationQueryByCountry() {
        List<?> populations = populationDAO.getCountryPopulations(5);
        assertNotNull(populations);
        assertFalse(populations.isEmpty(), "Expected at least one population from DB");
    }

    @Test
    void testLanguageQueryReturnsResults() {
        List<?> languages = languageDAO.getLanguagesByPopulation();
        assertNotNull(languages);
        assertFalse(languages.isEmpty(), "Expected at least one language from DB");
    }

    @AfterAll
    void teardown() {
        if (db != null) {
            db.disconnect();
        }
    }
}
