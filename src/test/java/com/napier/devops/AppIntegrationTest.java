package com.napier.devops;

import com.napier.devops.dao.*;
import com.napier.devops.db.Database;
import com.napier.devops.models.*;
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
        if (conn == null) throw new IllegalStateException("Database connection failed");

        countryDAO = new CountryDAO(conn);
        cityDAO = new CityDAO(conn);
        capitalDAO = new CapitalCityDAO(conn);
        populationDAO = new PopulationDAO(conn);
        lookupDAO = new LookupDAO(conn);
        languageDAO = new LanguageDAO(conn);
    }

    // ---------------------------
    // CountryDAO coverage
    // ---------------------------

    @Test void testCountryByPopulation_all() {
        List<Country> rows = countryDAO.getCountriesByPopulation();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().allMatch(r -> r.getPopulation() >= 0));
    }

    @Test void testCountryByPopulation_limit() {
        List<Country> rows = countryDAO.getCountriesByPopulation(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCountryInContinent_all() {
        List<Country> rows = countryDAO.getCountriesInContinent();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    @Test void testCountryInContinent_limit() {
        List<Country> rows = countryDAO.getCountriesInContinent(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCountryInContinent_byName() {
        List<Country> rows = countryDAO.getCountriesInContinent("Asia");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().allMatch(r -> "Asia".equals(r.getContinent())));
    }

    @Test void testCountryInContinent_byName_limit() {
        List<Country> rows = countryDAO.getCountriesInContinent("Europe", 3);
        assertNotNull(rows);
        assertTrue(rows.size() <= 3);
        assertTrue(rows.stream().allMatch(r -> "Europe".equals(r.getContinent())));
    }

    @Test void testCountryInRegion_all() {
        List<Country> rows = countryDAO.getCountriesInRegion();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    @Test void testCountryInRegion_limit() {
        List<Country> rows = countryDAO.getCountriesInRegion(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCountryInRegion_byName() {
        List<Country> rows = countryDAO.getCountriesInRegion("Caribbean");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().allMatch(r -> "Caribbean".equals(r.getRegion())));
    }

    @Test void testCountryInRegion_byName_limit() {
        List<Country> rows = countryDAO.getCountriesInRegion("Caribbean", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
        assertTrue(rows.stream().allMatch(r -> "Caribbean".equals(r.getRegion())));
    }

    // ---------------------------
    // CityDAO coverage
    // ---------------------------

    @Test void testCityByPopulation_all() {
        List<City> rows = cityDAO.getCitiesByPopulation();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    @Test void testCityByPopulation_limit() {
        List<City> rows = cityDAO.getCitiesByPopulation(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCityByContinent_all() {
        List<City> rows = cityDAO.getCitiesByContinent();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    @Test void testCityByContinent_limit() {
        List<City> rows = cityDAO.getCitiesByContinent(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCitiesInContinent_byName_all() {
        List<City> rows = cityDAO.getCitiesInContinent("Europe");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().allMatch(r -> r.getCountry() != null));
    }

    @Test void testCitiesInContinent_byName_limit() {
        List<City> rows = cityDAO.getCitiesInContinent("Asia", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCitiesInRegion_all() {
        List<City> rows = cityDAO.getCitiesInRegion("Caribbean");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    @Test void testCitiesInRegion_limit() {
        List<City> rows = cityDAO.getCitiesInRegion("Caribbean", 3);
        assertNotNull(rows);
        assertTrue(rows.size() <= 3);
    }

    @Test void testCitiesInCountryByCode_all() {
        List<City> rows = cityDAO.getCitiesInCountryByCode("FRA");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().allMatch(r -> r.getCountry() != null));
    }

    @Test void testCitiesInCountryByCode_limit() {
        List<City> rows = cityDAO.getCitiesInCountryByCode("USA", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCitiesInCountryByName_all() {
        List<City> rows = cityDAO.getCitiesInCountryByName("France");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().anyMatch(r -> "Paris".equals(r.getName())));
    }

    @Test void testCitiesInCountryByName_limit() {
        List<City> rows = cityDAO.getCitiesInCountryByName("France", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCitiesInDistrict_all() {
        List<City> rows = cityDAO.getCitiesInDistrict("California");
        assertNotNull(rows);
    }

    @Test void testCitiesInDistrict_limit() {
        List<City> rows = cityDAO.getCitiesInDistrict("California", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    // ---------------------------
    // CapitalCityDAO coverage
    // ---------------------------

    @Test void testCapitalByPopulation_all() {
        List<CapitalCity> rows = capitalDAO.getCapitalCitiesByPopulation();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    @Test void testCapitalByPopulation_limit() {
        List<CapitalCity> rows = capitalDAO.getCapitalCitiesByPopulation(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCapitalInContinent_all() {
        List<CapitalCity> rows = capitalDAO.getCapitalCitiesInContinent("Europe");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().allMatch(r -> "Europe".equals(r.getContinent())));
    }

    @Test void testCapitalInContinent_limit() {
        List<CapitalCity> rows = capitalDAO.getCapitalCitiesInContinent("Europe", 3);
        assertNotNull(rows);
        assertTrue(rows.size() <= 3);
        assertTrue(rows.stream().allMatch(r -> "Europe".equals(r.getContinent())));
    }

    @Test void testCapitalInRegion_all() {
        List<CapitalCity> rows = capitalDAO.getCapitalCitiesInRegion("Caribbean");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().allMatch(r -> "Caribbean".equals(r.getRegion())));
    }

    @Test void testCapitalInRegion_limit() {
        List<CapitalCity> rows = capitalDAO.getCapitalCitiesInRegion("Caribbean", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
        assertTrue(rows.stream().allMatch(r -> "Caribbean".equals(r.getRegion())));
    }

    // ---------------------------
    // PopulationDAO coverage
    // ---------------------------

    @Test void testGlobalPopulation_scalar() {
        long world = populationDAO.getGlobalPopulation();
        assertTrue(world > 0);
    }

    @Test void testGlobalPopulations_all() {
        List<Population> rows = populationDAO.getGlobalPopulations();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    @Test void testGlobalPopulations_limit() {
        List<Population> rows = populationDAO.getGlobalPopulations(3);
        assertNotNull(rows);
        assertTrue(rows.size() <= 3);
    }

    @Test void testContinentPopulation_byName_all() {
        List<Population> rows = populationDAO.getContinentPopulations("Asia");
        assertNotNull(rows);
        assertEquals(1, rows.size());
        assertTrue(rows.get(0).getTotalPopulation() >= rows.get(0).getCityPopulation());
    }

    @Test void testContinentPopulation_byName_limit() {
        List<Population> rows = populationDAO.getContinentPopulations("Europe", 1);
        assertNotNull(rows);
        assertTrue(rows.size() <= 1);
    }

    @Test void testRegionPopulation_all() {
        List<Population> rows = populationDAO.getRegionPopulations();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    @Test void testRegionPopulation_limit() {
        List<Population> rows = populationDAO.getRegionPopulations(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testRegionPopulation_byName_all() {
        List<Population> rows = populationDAO.getRegionPopulations("Caribbean");
        assertNotNull(rows);
        assertEquals(1, rows.size());
        assertTrue(rows.get(0).getTotalPopulation() >= rows.get(0).getCityPopulation());
    }

    @Test void testRegionPopulation_byName_limit() {
        List<Population> rows = populationDAO.getRegionPopulations("Caribbean", 1);
        assertNotNull(rows);
        assertTrue(rows.size() <= 1);
    }

    @Test void testCountryPopulation_all() {
        List<Population> rows = populationDAO.getCountryPopulations();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    @Test void testCountryPopulation_limit() {
        List<Population> rows = populationDAO.getCountryPopulations(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCountryPopulation_byCode_all() {
        List<Population> rows = populationDAO.getCountryPopulationByCode("FRA");
        assertNotNull(rows);
        assertEquals(1, rows.size());
        assertTrue(rows.get(0).getTotalPopulation() >= rows.get(0).getCityPopulation());
    }

    @Test void testCountryPopulation_byCode_limit() {
        List<Population> rows = populationDAO.getCountryPopulationByCode("GBR", 1);
        assertNotNull(rows);
        assertTrue(rows.size() <= 1);
    }

    @Test void testCountryPopulation_byName_all() {
        List<Population> rows = populationDAO.getCountryPopulations("France");
        assertNotNull(rows);
        assertEquals(1, rows.size());
        assertTrue(rows.get(0).getTotalPopulation() >= rows.get(0).getCityPopulation());
    }

    @Test void testCountryPopulation_byName_limit() {
        List<Population> rows = populationDAO.getCountryPopulations("France", 1);
        assertNotNull(rows);
        assertTrue(rows.size() <= 1);
    }

    // ---------------------------
    // LookupDAO coverage
    // ---------------------------

    @Test void testLookup_allContinents() {
        List<Lookup> rows = lookupDAO.getAllContinents();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().anyMatch(l -> "Europe".equals(l.getValue())));
    }

    @Test void testLookup_allRegions() {
        List<Lookup> rows = lookupDAO.getAllRegions();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().anyMatch(l -> "Caribbean".equals(l.getValue())));
    }

    @Test void testLookup_allCountries() {
        List<Lookup> rows = lookupDAO.getAllCountries();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().anyMatch(l -> "France".equals(l.getValue())));
    }

    @Test void testLookup_allDistricts() {
        List<Lookup> rows = lookupDAO.getAllDistricts();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    @Test void testLookup_districtsByCountryCode() {
        List<Lookup> rows = lookupDAO.getDistrictsByCountryCode("FRA");
        assertNotNull(rows);
    }

    @Test void testLookup_districtsByCountryName() {
        List<Lookup> rows = lookupDAO.getDistrictsByCountryName("France");
        assertNotNull(rows);
    }

    @Test void testLookup_searchContinents() {
        List<Lookup> rows = lookupDAO.searchContinents("Amer");
        assertNotNull(rows);
        assertTrue(rows.stream().anyMatch(l -> l.getValue().contains("America")));
    }

    @Test void testLookup_searchRegions() {
        List<Lookup> rows = lookupDAO.searchRegions("Carib");
        assertNotNull(rows);
        assertTrue(rows.stream().anyMatch(l -> l.getValue().contains("Caribbean")));
    }

    @Test void testLookup_searchCountries() {
        List<Lookup> rows = lookupDAO.searchCountries("Fran");
        assertNotNull(rows);
        assertTrue(rows.stream().anyMatch(l -> l.getValue().contains("France")));
    }

    @Test void testLookup_searchDistricts() {
        List<Lookup> rows = lookupDAO.searchDistricts("York");
        assertNotNull(rows);
    }

    // ---------------------------
    // LanguageDAO coverage
    // ---------------------------

    @Test void testLanguageByPopulation_all() {
        List<Language> rows = languageDAO.getLanguagesByPopulation();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        // percent_of_global injected as string formatting, ensure numeric parse OK
        assertDoesNotThrow(() -> Double.parseDouble(rows.get(0).getPercentOfGlobalPopulation()));
    }

    @Test void testLanguageByPopulation_limit() {
        List<Language> rows = languageDAO.getLanguagesByPopulation(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testLanguageByContinent_all() {
        List<Language> rows = languageDAO.getLanguagesByContinent("Asia");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertEquals("Asia", rows.get(0).getScopeName());
    }

    @Test void testLanguageByContinent_limit() {
        List<Language> rows = languageDAO.getLanguagesByContinent("Europe", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testLanguageByRegion_all() {
        List<Language> rows = languageDAO.getLanguagesByRegion("Caribbean");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertEquals("Caribbean", rows.get(0).getScopeName());
    }

    @Test void testLanguageByRegion_limit() {
        List<Language> rows = languageDAO.getLanguagesByRegion("Caribbean", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testLanguageByCountry_all() {
        List<Language> rows = languageDAO.getLanguagesByCountry("India");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertEquals("India", rows.get(0).getScopeName());
        assertTrue(rows.stream().anyMatch(l -> "Hindi".equalsIgnoreCase(l.getLanguage())));
    }

    @Test void testLanguageByCountry_limit() {
        List<Language> rows = languageDAO.getLanguagesByCountry("India", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testLanguageByCountryCode_all() {
        // Example code for France; adjust if your dataset differs
        List<Language> rows = languageDAO.getLanguagesByCountryCode("FRA");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertEquals("France", rows.get(0).getScopeName());
    }

    @Test void testLanguageByCountryCode_limit() {
        List<Language> rows = languageDAO.getLanguagesByCountryCode("FRA", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @AfterAll
    void teardown() {
        if (db != null) db.disconnect();
    }
}
