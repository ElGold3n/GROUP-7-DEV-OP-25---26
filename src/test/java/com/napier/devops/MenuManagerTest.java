package com.napier.devops;

import com.napier.devops.dao.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.mockito.Mockito.*;

class MenuManagerTest {

    private MenuManager buildMenu(String input,
                                  CountryDAO countryDAO,
                                  CityDAO cityDAO,
                                  CapitalCityDAO capitalDAO,
                                  PopulationDAO populationDAO,
                                  LookupDAO lookupDAO,
                                  LanguageDAO languageDAO) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        return new MenuManager(scanner, countryDAO, cityDAO, capitalDAO, populationDAO, lookupDAO, languageDAO);
    }

    @Test
    void testCountriesGlobalReportCallsDAO() {
        CountryDAO mockCountryDAO = mock(CountryDAO.class);

        // Simulate: Main->Countries->Global->All->Back->Exit
        String input = "1\n1\n1\n0\n0\n0\n";
        MenuManager menu = buildMenu(input, mockCountryDAO, mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class));

        menu.start();

        // Verify DAO method was called
        verify(mockCountryDAO).getCountriesByPopulation();
    }

    @Test
    void testCitiesGlobalReportCallsDAO() {
        CityDAO mockCityDAO = mock(CityDAO.class);

        String input = "2\n1\n1\n0\n0\n0\n"; // Main->Cities->Global->All->Back->Exit
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mockCityDAO,
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class));

        menu.start();

        verify(mockCityDAO).getCitiesByPopulation();
    }

    @Test
    void testCapitalsGlobalReportCallsDAO() {
        CapitalCityDAO mockCapitalDAO = mock(CapitalCityDAO.class);

        String input = "3\n1\n1\n0\n0\n0\n"; // Main->Capitals->Global->All->Back->Exit
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mockCapitalDAO, mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class));

        menu.start();

        verify(mockCapitalDAO).getCapitalCitiesByPopulation();
    }

    @Test
    void testPopulationsGlobalReportCallsDAO() {
        PopulationDAO mockPopDAO = mock(PopulationDAO.class);

        String input = "4\n1\n1\n0\n0\n0\n"; // Main->Populations->Global->All->Back->Exit
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mockPopDAO,
                mock(LookupDAO.class), mock(LanguageDAO.class));

        menu.start();

        verify(mockPopDAO).getGlobalPopulations();
    }

    @Test
    void testLanguagesGlobalReportCallsDAO() {
        LanguageDAO mockLangDAO = mock(LanguageDAO.class);

        String input = "5\n1\n1\n0\n0\n0\n"; // Main->Languages->Global->All->Back->Exit
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mockLangDAO);

        menu.start();

        verify(mockLangDAO).getLanguagesByPopulation();
    }
}
