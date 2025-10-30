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
        // In CI, these values come from your workflow’s MySQL service
        db.connect("localhost:3306", 30000, "world", "testuser", "testpass");
        conn = db.getConnection();

        countryDAO = new CountryDAO(conn);
        cityDAO = new CityDAO(conn);
        capitalDAO = new CapitalCityDAO(conn);
        populationDAO = new PopulationDAO(conn);
        lookupDAO = new LookupDAO(conn);
        languageDAO = new LanguageDAO(conn);
    }

    @Test
    void testCountryQueryReturnsResults() {
        List<?> countries = countryDAO.getAllCountriesSortedByPopulation();
        assertNotNull(countries);
        assertFalse(countries.isEmpty(), "Expected at least one country from DB");
    }

    @Test
    void testCityQueryByCountry() {
        List<?> cities = cityDAO.getCitiesByCountry("France");
        assertNotNull(cities);
        assertTrue(cities.stream().anyMatch(c -> c.toString().contains("Paris")));
    }

    @AfterAll
    void teardown() {
        if (db != null) {
            db.disconnect();
        }
    }
}
