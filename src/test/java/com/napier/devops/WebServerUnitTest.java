package com.napier.devops;

import com.napier.devops.dao.*;
import com.napier.devops.models.*;
import com.napier.devops.www.WebServer;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class WebServerUnitTest {
    private CountryDAO countryDAO;
    private CityDAO cityDAO;
    private CapitalCityDAO capitalDAO;
    private PopulationDAO populationDAO;
    private LanguageDAO languageDAO;
    private LookupDAO lookupDAO;
    private WebServer server;
    private Gson gson = new Gson();

    @BeforeEach
    void setup() {
        countryDAO = mock(CountryDAO.class);
        cityDAO = mock(CityDAO.class);
        capitalDAO = mock(CapitalCityDAO.class);
        populationDAO = mock(PopulationDAO.class);
        languageDAO = mock(LanguageDAO.class);
        lookupDAO = mock(LookupDAO.class);

        server = new WebServer(countryDAO, cityDAO, capitalDAO, populationDAO, languageDAO, lookupDAO);
    }

    @Test
    void testCountriesEndpoint_returnsJson() {
        when(countryDAO.getCountriesByPopulation())
                .thenReturn(List.of(new Country("C1","Testland","Europe","RegionX",1000,"CapitalX")));

        var countries = countryDAO.getCountriesByPopulation();
        String json = gson.toJson(countries);

        assertTrue(json.contains("Testland"));
    }

    @Test
    void testCitiesEndpoint_returnsJson() {
        when(cityDAO.getCitiesByPopulation())
                .thenReturn(List.of(new City("CityX","Europe","Testland","DistrictX",500)));

        var cities = cityDAO.getCitiesByPopulation();
        String json = gson.toJson(cities);

        assertTrue(json.contains("CityX"));
    }

    @Test
    void testPopulationsEndpoint_returnsJson() {
        when(populationDAO.getGlobalPopulations())
                .thenReturn(List.of(new Population("Europe",1000,500,500)));

        var pops = populationDAO.getGlobalPopulations();
        String json = gson.toJson(pops);

        assertTrue(json.contains("Europe"));
    }

    @Test
    void testLanguagesEndpoint_returnsJson() {
        when(languageDAO.getLanguagesByPopulation())
                .thenReturn(List.of(new Language("English",500,50.0)));

        var langs = languageDAO.getLanguagesByPopulation();
        String json = gson.toJson(langs);

        assertTrue(json.contains("English"));
    }

    @Test
    void testLookupEndpoint_returnsJson() {
        when(lookupDAO.getAllContinents())
                .thenReturn(List.of(new Lookup("Europe")));

        var continents = lookupDAO.getAllContinents();
        String json = gson.toJson(continents);

        assertTrue(json.contains("Europe"));
    }
}
