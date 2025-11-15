package com.napier.devops;

import com.napier.devops.dao.*;
import com.napier.devops.db.Database;
import com.napier.devops.models.*;
import com.napier.devops.util.TablePrinter;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Test void testDatabaseConnectionFailure() {
        Database badDb = new Database();
        badDb.connect("invalidhost", 1234, "badDB", "badUser", "badPass");
        assertNull(badDb.getConnection());
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

    @Test void testCountriesInContinent_null() {
        var rows = countryDAO.getCountriesInContinent(null);
        assertNotNull(rows);
        assertTrue(rows.isEmpty()); // fallback to all continents
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

    @Test void testCitiesInRegion_null() {
        var rows = cityDAO.getCitiesInRegion(null);
        assertNotNull(rows);
        assertTrue(rows.isEmpty()); // fallback to all regions
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

    @Test void testCapitalInContinent_null() {
        var rows = capitalDAO.getCapitalCitiesInContinent(null);
        assertNotNull(rows);
        assertTrue(rows.isEmpty()); // fallback to all continents
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

    @Test void testRegionPopulation_null() {
        var rows = populationDAO.getRegionPopulations(null);
        assertNotNull(rows);
        assertTrue(rows.isEmpty()); // fallback to all regions
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

    @Test void testLookup_searchDistricts_noMatch() {
        var rows = lookupDAO.searchDistricts("NoSuchDistrict");
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
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
        assertDoesNotThrow(() -> (Double) Double.parseDouble(rows.get(0).getPercentOfGlobalPopulation()));
    }

    @Test void testLanguageByPopulation_limit() {
        var rows = languageDAO.getLanguagesByPopulation(5);
        assertNotNull(rows);
        assertTrue(rows.size() <= 5);
    }

    @Test void testLanguageByPopulation_zeroLimit() {
        var rows = languageDAO.getLanguagesByPopulation(0);
        assertNotNull(rows);
        assertTrue(rows.isEmpty());
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

    @Test void testRunBatchReports_withNullDAOs_shouldFail() {
        assertThrows(NullPointerException.class, () -> {
            App.runBatchReports(null, null, null, null, null);
        });
    }


    @Test
    void testRunBatchReports_printsReports() {
        CountryDAO countryDAO = mock(CountryDAO.class);
        CityDAO cityDAO = mock(CityDAO.class);
        CapitalCityDAO capitalDAO = mock(CapitalCityDAO.class);
        PopulationDAO populationDAO = mock(PopulationDAO.class);
        LanguageDAO languageDAO = mock(LanguageDAO.class);

        when(countryDAO.getCountriesByPopulation(10))
                .thenReturn(List.of(new Country("C1","Testland","Europe","RegionX",1000,"CapitalX")));
        when(cityDAO.getCitiesByPopulation(10))
                .thenReturn(List.of(new City("CityX","Testland","DistrictX","Europe",500)));
        when(capitalDAO.getCapitalCitiesByPopulation(10))
                .thenReturn(List.of(new CapitalCity("CapitalX","Testland","Europe","RegionX",500)));
        when(populationDAO.getGlobalPopulations(10))
                .thenReturn(List.of(new Population("Europe",1000,500,500)));
        when(languageDAO.getLanguagesByPopulation(5))
                .thenReturn(List.of(new Language("English",500,50.0)));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        App.runBatchReports(countryDAO, cityDAO, capitalDAO, populationDAO, languageDAO);

        String output = out.toString();
        assertTrue(output.contains("Top 10 Countries by Population"));
        assertTrue(output.contains("Top 10 Cities by Population"));
        assertTrue(output.contains("Top 10 Capital Cities by Population"));
        assertTrue(output.contains("Top 10 Populations Globally"));
        assertTrue(output.contains("Top 5 Languages"));
    }



    //--------------- Console Tests
    private MenuManager buildMenu(String input,
                                  CountryDAO countryDAO,
                                  CityDAO cityDAO,
                                  CapitalCityDAO capitalDAO,
                                  PopulationDAO populationDAO,
                                  LookupDAO lookupDAO,
                                  LanguageDAO languageDAO,
                                  TablePrinter tablePrinter) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        return new MenuManager(scanner, countryDAO, cityDAO, capitalDAO, populationDAO, lookupDAO, languageDAO);
    }

    // --- Global Reports ---
    @Test
    void testCountriesGlobalReportCallsDAO() {
        CountryDAO mockCountryDAO = mock(CountryDAO.class);
        String input = "1\n1\n1\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mockCountryDAO, mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();
        verify(mockCountryDAO).getCountriesByPopulation();
    }

    @Test
    void testCitiesGlobalReportCallsDAO() {
        CityDAO mockCityDAO = mock(CityDAO.class);
        String input = "2\n1\n1\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mockCityDAO,
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();
        verify(mockCityDAO).getCitiesByPopulation();
    }

    @Test
    void testCapitalsGlobalReportCallsDAO() {
        CapitalCityDAO mockCapitalDAO = mock(CapitalCityDAO.class);
        String input = "3\n1\n1\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mockCapitalDAO, mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();
        verify(mockCapitalDAO).getCapitalCitiesByPopulation();
    }

    @Test
    void testPopulationsGlobalReportCallsDAO() {
        PopulationDAO mockPopDAO = mock(PopulationDAO.class);
        String input = "4\n1\n1\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mockPopDAO,
                mock(LookupDAO.class), mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();
        verify(mockPopDAO).getGlobalPopulations();
    }

    @Test
    void testLanguagesGlobalReportCallsDAO() {
        LanguageDAO mockLangDAO = mock(LanguageDAO.class);
        String input = "5\n1\n1\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mockLangDAO, mock(TablePrinter.class));
        menu.start();
        verify(mockLangDAO).getLanguagesByPopulation();
    }

    // --- Invalid choice coverage ---
    @Test
    void testInvalidChoiceAtMainMenu() {
        String input = "9\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();
    }

    @Test
    void testInvalidChoiceAtSubMenu() {
        String input = "1\n9\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();
    }

    // --- Top N coverage ---
    @Test
    void testTopNCountriesGlobal() {
        CountryDAO mockCountryDAO = mock(CountryDAO.class);
        String input = "1\n1\n2\n5\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mockCountryDAO, mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();
        verify(mockCountryDAO).getCountriesByPopulation(5);
    }

    @Test
    void testTopNCitiesGlobal() {
        CityDAO mockCityDAO = mock(CityDAO.class);
        String input = "2\n1\n2\n3\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mockCityDAO,
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();
        verify(mockCityDAO).getCitiesByPopulation(3);
    }

    @Test
    void testTopNCapitalsGlobal() {
        CapitalCityDAO mockCapitalDAO = mock(CapitalCityDAO.class);
        String input = "3\n1\n2\n2\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mockCapitalDAO, mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();
        verify(mockCapitalDAO).getCapitalCitiesByPopulation(2);
    }

    @Test
    void testTopNInvalidInput() {
        CountryDAO mockCountryDAO = mock(CountryDAO.class);
        String input = "1\n1\n2\nabc\n0\n0\n0\n"; // Countries->Global->Top N->Invalid input->Back->Exit

        MenuManager menu = buildMenu(input, mockCountryDAO, mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class), mock(TablePrinter.class));

        assertThrows(NumberFormatException.class, menu::start);
    }


    // --- Empty DAO results ---
    @Test
    void testEmptyLanguagesGlobal() {
        LanguageDAO mockLangDAO = mock(LanguageDAO.class);
        when(mockLangDAO.getLanguagesByPopulation()).thenReturn(List.of());
        String input = "5\n1\n1\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mockLangDAO, mock(TablePrinter.class));
        menu.start();
        verify(mockLangDAO).getLanguagesByPopulation();
    }

    @Test
    void testEmptyCountriesGlobal() {
        CountryDAO mockCountryDAO = mock(CountryDAO.class);
        when(mockCountryDAO.getCountriesByPopulation()).thenReturn(List.of());
        String input = "1\n1\n1\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mockCountryDAO, mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();
        verify(mockCountryDAO).getCountriesByPopulation();
    }

    // --- Input utilities coverage ---
    @Test
    void testChooseContinentCancel() {
        LookupDAO mockLookupDAO = mock(LookupDAO.class);
        when(mockLookupDAO.getAllContinents()).thenReturn(List.of(new Lookup("Europe")));
        String input = "1\n2\n1\n0\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mockLookupDAO, mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();
        verify(mockLookupDAO).getAllContinents();
    }

    @Test
    void testChooseRegionCancel() {
        LookupDAO mockLookupDAO = mock(LookupDAO.class);
        when(mockLookupDAO.getAllRegions()).thenReturn(List.of(new Lookup("Asia")));
        String input = "1\n3\n1\n0\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mockLookupDAO, mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();
        verify(mockLookupDAO).getAllRegions();
    }

    @Test
    void testChooseCountryInvalidCode() {
        LookupDAO mockLookupDAO = mock(LookupDAO.class);
        when(mockLookupDAO.getAllCountries()).thenReturn(List.of(new Lookup("USA", "United States")));

        // Sequence: Cities->Country->All->Invalid code->Retry->Valid code->Back->Exit
        String input = "2\n4\n1\nXYZ\nUSA\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mockLookupDAO, mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();

        verify(mockLookupDAO, atLeastOnce()).getAllCountries();
    }

    // --- District tests split ---
    @Test
    void testChooseDistrictInvalidInput() {
        LookupDAO mockLookupDAO = mock(LookupDAO.class);
        when(mockLookupDAO.getAllCountries()).thenReturn(List.of(new Lookup("USA", "United Stated")));
        when(mockLookupDAO.getDistrictsByCountryCode("USA"))
                .thenReturn(List.of(new Lookup("District","California")));

        String input = "2\n5\n1\nUSA\nabc\n0\n0\n0\n0\n"; // invalid district input

        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mockLookupDAO, mock(LanguageDAO.class), mock(TablePrinter.class));

        menu.start();
        verify(mockLookupDAO, atLeastOnce()).getDistrictsByCountryCode("USA");
    }

    @Test
    void testChooseDistrictValidInput() {
        LookupDAO mockLookupDAO = mock(LookupDAO.class);
        when(mockLookupDAO.getAllCountries()).thenReturn(List.of(new Lookup("USA", "United States")));
        when(mockLookupDAO.getDistrictsByCountryCode("USA"))
                .thenReturn(List.of(new Lookup("California")));

        // Sequence: Cities->District->All->Country->Valid district input->Back->Exit
        String input = "2\n5\n1\nUSA\n1\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mockLookupDAO, mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();

        verify(mockLookupDAO, times(1)).getDistrictsByCountryCode("USA");
    }

    // --- Populations by Continent & Region ---
    @Test
    void testPopulationsByContinent() {
        PopulationDAO mockPopDAO = mock(PopulationDAO.class);
        LookupDAO mockLookupDAO = mock(LookupDAO.class);
        when(mockLookupDAO.getAllContinents()).thenReturn(List.of(new Lookup("Continent","Europe")));

        String input = "4\n2\n1\n1\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mockPopDAO,
                mockLookupDAO, mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();
        verify(mockPopDAO).getContinentPopulations("Europe");
    }

    @Test
    void testPopulationsByRegion() {
        PopulationDAO mockPopDAO = mock(PopulationDAO.class);
        LookupDAO mockLookupDAO = mock(LookupDAO.class);
        when(mockLookupDAO.getAllRegions()).thenReturn(List.of(new Lookup("Region","Asia")));

        String input = "4\n3\n1\n1\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mockPopDAO,
                mockLookupDAO, mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();
        verify(mockPopDAO).getRegionPopulations("Asia");
    }

    // --- Languages by Continent & Country ---
    @Test
    void testLanguagesByContinent() {
        LanguageDAO mockLangDAO = mock(LanguageDAO.class);
        LookupDAO mockLookupDAO = mock(LookupDAO.class);
        when(mockLookupDAO.getAllContinents()).thenReturn(List.of(new Lookup("Continent","Europe")));

        // Sequence: Main->Languages->Continental->All->Select Europe->Back->Exit
        String input = "5\n2\n1\n1\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mockLookupDAO, mockLangDAO, mock(TablePrinter.class));
        menu.start();

        verify(mockLangDAO).getLanguagesByContinent("Europe");
    }


    @Test
    void testLanguagesByCountry() {
        LanguageDAO mockLangDAO = mock(LanguageDAO.class);
        LookupDAO mockLookupDAO = mock(LookupDAO.class);
        when(mockLookupDAO.getAllCountries()).thenReturn(List.of(new Lookup("USA", "United States")));

        String input = "5\n4\n1\nUSA\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mockLookupDAO, mockLangDAO, mock(TablePrinter.class));
        menu.start();
        verify(mockLangDAO).getLanguagesByCountryCode("USA");
    }

    // --- Capitals by Region ---
    @Test
    void testCapitalsByRegion() {
        CapitalCityDAO mockCapitalDAO = mock(CapitalCityDAO.class);
        LookupDAO mockLookupDAO = mock(LookupDAO.class);
        when(mockLookupDAO.getAllRegions()).thenReturn(List.of(new Lookup("Region", "Africa")
        ));

        // Sequence: Main->Capitals->Regional->All->Select Africa->Back->Exit
        String input = "3\n3\n1\n1\n0\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mockCapitalDAO, mock(PopulationDAO.class),
                mockLookupDAO, mock(LanguageDAO.class), mock(TablePrinter.class));
        menu.start();

        verify(mockCapitalDAO).getCapitalCitiesInRegion("Africa");
    }






    @AfterAll
    void teardown() {
        if (db != null) db.disconnect();
    }
}
