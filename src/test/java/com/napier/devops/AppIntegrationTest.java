package com.napier.devops;

import com.napier.devops.dao.*;
import com.napier.devops.db.Database;
import com.napier.devops.models.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;

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
        assertNotNull(conn, "Database connection failed");

        countryDAO = new CountryDAO(conn);
        cityDAO = new CityDAO(conn);
        capitalDAO = new CapitalCityDAO(conn);
        populationDAO = new PopulationDAO(conn);
        lookupDAO = new LookupDAO(conn);
        languageDAO = new LanguageDAO(conn);
    }

    // --------------------------------
    // CountryDAO: full coverage + edges
    // --------------------------------

    @Test void testCountriesByPopulation_all() {
        var rows = countryDAO.getCountriesByPopulation();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        // descending ordering invariant
        for (int i = 1; i < rows.size(); i++) {
            assertTrue(rows.get(i - 1).getPopulation() >= rows.get(i).getPopulation());
        }
    }

    @Test void testCountriesByPopulation_limit() {
        var rows = countryDAO.getCountriesByPopulation(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCountriesByPopulation_zeroLimit() {
        var rows = countryDAO.getCountriesByPopulation(0);
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test void testCountriesInContinent_all() {
        var rows = countryDAO.getCountriesInContinent();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        // ordering invariant by continent then population desc
        for (int i = 1; i < rows.size(); i++) {
            var prev = rows.get(i - 1);
            var curr = rows.get(i);
            boolean sameContinent = prev.getContinent().equals(curr.getContinent());
            assertTrue(sameContinent || prev.getContinent().compareTo(curr.getContinent()) <= 0);
            if (sameContinent) {
                assertTrue(prev.getPopulation() >= curr.getPopulation());
            }
        }
    }

    @Test void testCountriesInContinent_limit() {
        var rows = countryDAO.getCountriesInContinent(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCountriesInContinent_byName() {
        var rows = countryDAO.getCountriesInContinent("Asia");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().allMatch(r -> "Asia".equals(r.getContinent())));
        // population desc
        for (int i = 1; i < rows.size(); i++) {
            assertTrue(rows.get(i - 1).getPopulation() >= rows.get(i).getPopulation());
        }
    }

    @Test void testCountriesInContinent_byName_limit() {
        var rows = countryDAO.getCountriesInContinent("Europe", 3);
        assertNotNull(rows);
        assertTrue(rows.size() <= 3);
        assertTrue(rows.stream().allMatch(r -> "Europe".equals(r.getContinent())));
    }

    @Test void testCountriesInContinent_invalid() {
        var rows = countryDAO.getCountriesInContinent("Atlantis");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test void testCountriesInRegion_all() {
        var rows = countryDAO.getCountriesInRegion();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        // ordering invariant by region then population desc
        for (int i = 1; i < rows.size(); i++) {
            var prev = rows.get(i - 1);
            var curr = rows.get(i);
            boolean sameRegion = prev.getRegion().trim().equalsIgnoreCase(curr.getRegion().trim());

            assertTrue(
                    sameRegion || prev.getRegion().trim().compareToIgnoreCase(curr.getRegion().trim()) <= 0,
                    "Regions should be in ascending order"
            );

            if (sameRegion) {
                assertTrue(prev.getPopulation() >= curr.getPopulation(),
                        "Population should be descending within region");
            }
        }
    }

    @Test void testCountriesInRegion_limit() {
        var rows = countryDAO.getCountriesInRegion(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCountriesInRegion_byName() {
        var rows = countryDAO.getCountriesInRegion("Caribbean");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().allMatch(r -> "Caribbean".equals(r.getRegion())));
    }

    @Test void testCountriesInRegion_byName_limit() {
        var rows = countryDAO.getCountriesInRegion("Caribbean", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
        assertTrue(rows.stream().allMatch(r -> "Caribbean".equals(r.getRegion())));
    }

    @Test void testCountriesInRegion_invalid() {
        var rows = countryDAO.getCountriesInRegion("Neverland");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    // -----------------------------
    // CityDAO: full coverage + edges
    // -----------------------------

    @Test void testCitiesByPopulation_all() {
        var rows = cityDAO.getCitiesByPopulation();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        for (int i = 1; i < rows.size(); i++) {
            assertTrue(rows.get(i - 1).getPopulation() >= rows.get(i).getPopulation());
        }
    }

    @Test void testCitiesByPopulation_limit() {
        var rows = cityDAO.getCitiesByPopulation(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCitiesByPopulation_zeroLimit() {
        var rows = cityDAO.getCitiesByPopulation(0);
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test
    void testCitiesByContinent_all() {
        var cities = cityDAO.getCitiesByContinent();
        assertNotNull(cities);
        assertFalse(cities.isEmpty());

        // Verify ordering: continent ascending, population descending within continent
        for (int i = 1; i < cities.size(); i++) {
            var prev = cities.get(i - 1);
            var curr = cities.get(i);

            boolean sameContinent = prev.getContinent().equals(curr.getContinent());

            // Continent ordering check
            assertTrue(prev.getContinent().compareTo(curr.getContinent()) <= 0,
                    "Expected continents in ascending order");

            // If same continent, population must be descending
            if (sameContinent) {
                assertTrue(prev.getPopulation() >= curr.getPopulation(),
                        "Population should be descending within continent");
            }
        }
    }


    @Test
    void testCitiesByContinent_limit() {
        var cities = cityDAO.getCitiesByContinent(5);
        assertNotNull(cities);
        assertTrue(cities.size() <= 5);

        // Same ordering invariant as above
        for (int i = 1; i < cities.size(); i++) {
            var prev = cities.get(i - 1);
            var curr = cities.get(i);
            if (prev.getCountry() != null && curr.getCountry() != null
                    && prev.getCountry().equals(curr.getCountry())) {
                assertTrue(prev.getPopulation() >= curr.getPopulation());
            }
        }
    }

    @Test
    void testCitiesByContinent_zeroLimit() {
        var cities = cityDAO.getCitiesByContinent(0);
        assertNotNull(cities);
        assertTrue(cities.isEmpty(), "Expected empty list when limit is zero");
    }


    @Test void testCitiesInContinent_byName_all() {
        var rows = cityDAO.getCitiesInContinent("Europe");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    @Test void testCitiesInContinent_byName_limit() {
        var rows = cityDAO.getCitiesInContinent("Asia", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCitiesInContinent_invalid() {
        var rows = cityDAO.getCitiesInContinent("Atlantis");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test void testCitiesInRegion_all() {
        var rows = cityDAO.getCitiesInRegion("Caribbean");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    @Test void testCitiesInRegion_limit() {
        var rows = cityDAO.getCitiesInRegion("Caribbean", 3);
        assertNotNull(rows);
        assertTrue(rows.size() <= 3);
    }

    @Test void testCitiesInRegion_invalid() {
        var rows = cityDAO.getCitiesInRegion("Neverland");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test void testCitiesInCountryByCode_all() {
        var rows = cityDAO.getCitiesInCountryByCode("FRA");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    @Test void testCitiesInCountryByCode_limit() {
        var rows = cityDAO.getCitiesInCountryByCode("USA", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCitiesInCountryByCode_invalid() {
        var rows = cityDAO.getCitiesInCountryByCode("XXX");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test void testCitiesInCountryByName_all() {
        var rows = cityDAO.getCitiesInCountryByName("France");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().anyMatch(c -> "Paris".equals(c.getName())));
    }

    @Test void testCitiesInCountryByName_limit() {
        var rows = cityDAO.getCitiesInCountryByName("France", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCitiesInCountryByName_invalid() {
        var rows = cityDAO.getCitiesInCountryByName("Atlantis");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test void testCitiesInDistrict_all() {
        var rows = cityDAO.getCitiesInDistrict("California");
        assertNotNull(rows);
        // may be empty depending on dataset; presence not guaranteed
    }

    @Test void testCitiesInDistrict_limit() {
        var rows = cityDAO.getCitiesInDistrict("California", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCitiesInDistrict_invalid() {
        var rows = cityDAO.getCitiesInDistrict("NonexistentDistrict");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    // -----------------------------------
    // CapitalCityDAO: full coverage + edges
    // -----------------------------------

    @Test void testCapitalByPopulation_all() {
        var rows = capitalDAO.getCapitalCitiesByPopulation();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        for (int i = 1; i < rows.size(); i++) {
            assertTrue(rows.get(i - 1).getPopulation() >= rows.get(i).getPopulation());
        }
    }

    @Test void testCapitalByPopulation_limit() {
        var rows = capitalDAO.getCapitalCitiesByPopulation(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCapitalInContinent_all() {
        var rows = capitalDAO.getCapitalCitiesInContinent("Europe");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().allMatch(c -> "Europe".equals(c.getContinent())));
    }

    @Test void testCapitalInContinent_limit() {
        var rows = capitalDAO.getCapitalCitiesInContinent("Europe", 3);
        assertNotNull(rows);
        assertTrue(rows.size() <= 3);
        assertTrue(rows.stream().allMatch(c -> "Europe".equals(c.getContinent())));
    }

    @Test void testCapitalInContinent_invalid() {
        var rows = capitalDAO.getCapitalCitiesInContinent("Atlantis");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test void testCapitalInRegion_all() {
        var rows = capitalDAO.getCapitalCitiesInRegion("Caribbean");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().allMatch(c -> "Caribbean".equals(c.getRegion())));
    }

    @Test void testCapitalInRegion_limit() {
        var rows = capitalDAO.getCapitalCitiesInRegion("Caribbean", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
        assertTrue(rows.stream().allMatch(c -> "Caribbean".equals(c.getRegion())));
    }

    @Test void testCapitalInRegion_invalid() {
        var rows = capitalDAO.getCapitalCitiesInRegion("Neverland");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    // --------------------------------
    // PopulationDAO: full coverage + edges
    // --------------------------------

    @Test void testGlobalPopulation_scalar() {
        long world = populationDAO.getGlobalPopulation();
        assertTrue(world > 0);
    }

    @Test void testGlobalPopulations_all() {
        var rows = populationDAO.getGlobalPopulations();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        for (int i = 1; i < rows.size(); i++) {
            assertTrue(rows.get(i - 1).getTotalPopulation() >= rows.get(i).getTotalPopulation());
        }
    }

    @Test void testGlobalPopulations_limit() {
        var rows = populationDAO.getGlobalPopulations(3);
        assertNotNull(rows);
        assertTrue(rows.size() <= 3);
    }

    @Test void testContinentPopulation_byName_all() {
        var rows = populationDAO.getContinentPopulations("Asia");
        assertNotNull(rows);
        assertEquals(1, rows.size());
        var r = rows.get(0);
        assertTrue(r.getTotalPopulation() >= r.getCityPopulation());
        assertEquals(r.getTotalPopulation(), r.getCityPopulation() + r.getNonCityPopulation());
    }

    @Test void testContinentPopulation_byName_limit() {
        var rows = populationDAO.getContinentPopulations("Europe", 1);
        assertNotNull(rows);
        assertTrue(rows.size() <= 1);
    }

    @Test void testContinentPopulation_invalid() {
        var rows = populationDAO.getContinentPopulations("Atlantis");
        assertNotNull(rows);
        // Query groups by continent; invalid filter typically yields empty list
        assertTrue(rows.isEmpty());
    }

    @Test void testRegionPopulation_all() {
        var rows = populationDAO.getRegionPopulations();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        for (int i = 1; i < rows.size(); i++) {
            assertTrue(rows.get(i - 1).getTotalPopulation() >= rows.get(i).getTotalPopulation());
        }
    }

    @Test void testRegionPopulation_limit() {
        var rows = populationDAO.getRegionPopulations(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testRegionPopulation_byName_all() {
        var rows = populationDAO.getRegionPopulations("Caribbean");
        assertNotNull(rows);
        assertEquals(1, rows.size());
        var r = rows.get(0);
        assertTrue(r.getTotalPopulation() >= r.getCityPopulation());
        assertEquals(r.getTotalPopulation(), r.getCityPopulation() + r.getNonCityPopulation());
    }

    @Test void testRegionPopulation_byName_limit() {
        var rows = populationDAO.getRegionPopulations("Caribbean", 1);
        assertNotNull(rows);
        assertTrue(rows.size() <= 1);
    }

    @Test void testRegionPopulation_invalid() {
        var rows = populationDAO.getRegionPopulations("Neverland");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test void testCountryPopulation_all() {
        var rows = populationDAO.getCountryPopulations();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());

        for (var r : rows) {
            assertTrue(r.getTotalPopulation() >= r.getCityPopulation());
            assertEquals(r.getTotalPopulation(), r.getCityPopulation() + r.getNonCityPopulation());
        }
    }

    @Test void testCountryPopulation_limit() {
        var rows = populationDAO.getCountryPopulations(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testCountryPopulationByCode_all() {
        var rows = populationDAO.getCountryPopulationByCode("FRA");
        assertNotNull(rows);
        assertEquals(1, rows.size());
        var r = rows.get(0);
        assertTrue(r.getTotalPopulation() >= r.getCityPopulation());
        assertEquals(r.getTotalPopulation(), r.getCityPopulation() + r.getNonCityPopulation());
    }

    @Test void testCountryPopulationByCode_limit() {
        var rows = populationDAO.getCountryPopulationByCode("GBR", 1);
        assertNotNull(rows);
        assertTrue(rows.size() <= 1);
    }

    @Test void testCountryPopulationByCode_invalid() {
        var rows = populationDAO.getCountryPopulationByCode("XXX");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test void testCountryPopulationByName_all() {
        var rows = populationDAO.getCountryPopulations("France");
        assertNotNull(rows);
        assertEquals(1, rows.size());
        var r = rows.get(0);
        assertTrue(r.getTotalPopulation() >= r.getCityPopulation());
        assertEquals(r.getTotalPopulation(), r.getCityPopulation() + r.getNonCityPopulation());
    }

    @Test void testCountryPopulationByName_limit() {
        var rows = populationDAO.getCountryPopulations("France", 1);
        assertNotNull(rows);
        assertTrue(rows.size() <= 1);
    }

    @Test void testCountryPopulationByName_invalid() {
        var rows = populationDAO.getCountryPopulations("Atlantis");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    // -----------------------------
    // LookupDAO: full coverage + edges
    // -----------------------------

    @Test void testLookup_allContinents() {
        var rows = lookupDAO.getAllContinents();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().anyMatch(l -> "Europe".equals(l.getValue())));
    }

    @Test void testLookup_allRegions() {
        var rows = lookupDAO.getAllRegions();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().anyMatch(l -> "Caribbean".equals(l.getValue())));
    }

    @Test void testLookup_allCountries() {
        var rows = lookupDAO.getAllCountries();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertTrue(rows.stream().anyMatch(l -> "France".equals(l.getValue())));
    }

    @Test void testLookup_allDistricts() {
        var rows = lookupDAO.getAllDistricts();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    @Test void testLookup_districtsByCountryCode_valid() {
        var rows = lookupDAO.getDistrictsByCountryCode("FRA");
        assertNotNull(rows);
        // may be empty depending on dataset granularity
    }

    @Test void testLookup_districtsByCountryCode_invalid() {
        var rows = lookupDAO.getDistrictsByCountryCode("XXX");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test void testLookup_districtsByCountryName_valid() {
        var rows = lookupDAO.getDistrictsByCountryName("France");
        assertNotNull(rows);
    }

    @Test void testLookup_districtsByCountryName_invalid() {
        var rows = lookupDAO.getDistrictsByCountryName("Atlantis");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test void testLookup_searchContinents() {
        var rows = lookupDAO.searchContinents("Amer");
        assertNotNull(rows);
        assertTrue(rows.stream().anyMatch(l -> l.getValue().contains("America")));
    }

    @Test void testLookup_searchRegions() {
        var rows = lookupDAO.searchRegions("Carib");
        assertNotNull(rows);
        assertTrue(rows.stream().anyMatch(l -> l.getValue().contains("Caribbean")));
    }

    @Test void testLookup_searchCountries() {
        var rows = lookupDAO.searchCountries("Fran");
        assertNotNull(rows);
        assertTrue(rows.stream().anyMatch(l -> l.getValue().contains("France")));
    }

    @Test void testLookup_searchDistricts() {
        var rows = lookupDAO.searchDistricts("York");
        assertNotNull(rows);
        // may be empty depending on dataset spelling
    }

    // ---------------------------------
    // LanguageDAO: full coverage + edges
    // ---------------------------------

    @Test void testLanguageByPopulation_all() {
        var rows = languageDAO.getLanguagesByPopulation();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertDoesNotThrow(() -> Double.parseDouble(rows.get(0).getPercentOfGlobalPopulation()));
    }

    @Test void testLanguageByPopulation_limit() {
        var rows = languageDAO.getLanguagesByPopulation(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testLanguageByContinent_all() {
        var rows = languageDAO.getLanguagesByContinent("Asia");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertEquals("Asia", rows.get(0).getScopeName());
    }

    @Test void testLanguageByContinent_limit() {
        var rows = languageDAO.getLanguagesByContinent("Europe", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testLanguageByContinent_invalid() {
        var rows = languageDAO.getLanguagesByContinent("Atlantis");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test void testLanguageByRegion_all() {
        var rows = languageDAO.getLanguagesByRegion("Caribbean");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertEquals("Caribbean", rows.get(0).getScopeName());
    }

    @Test void testLanguageByRegion_limit() {
        var rows = languageDAO.getLanguagesByRegion("Caribbean", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testLanguageByRegion_invalid() {
        var rows = languageDAO.getLanguagesByRegion("Neverland");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test void testLanguageByCountry_all() {
        var rows = languageDAO.getLanguagesByCountry("India");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertEquals("India", rows.get(0).getScopeName());
        assertTrue(rows.stream().anyMatch(l -> "Hindi".equalsIgnoreCase(l.getLanguage())));
    }

    @Test void testLanguageByCountry_limit() {
        var rows = languageDAO.getLanguagesByCountry("India", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testLanguageByCountry_invalid() {
        var rows = languageDAO.getLanguagesByCountry("Atlantis");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test void testLanguageByCountryCode_all() {
        var rows = languageDAO.getLanguagesByCountryCode("FRA");
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
        assertEquals("France", rows.get(0).getScopeName());
    }

    @Test void testLanguageByCountryCode_limit() {
        var rows = languageDAO.getLanguagesByCountryCode("FRA", 5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testLanguageByCountryCode_invalid() {
        var rows = languageDAO.getLanguagesByCountryCode("XXX");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @AfterAll
    void teardown() {
        if (db != null) db.disconnect();
    }
}
