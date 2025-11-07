package com.napier.devops;

import com.napier.devops.dao.*;
import com.napier.devops.models.Lookup;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.util.List;
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


    // --- Invalid choice coverage ---
    @Test
    void testInvalidChoiceAtMainMenu() {
        String input = "9\n0\n"; // Invalid then Exit
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class));
        menu.start();
        // No exception should be thrown, invalid branch covered
    }

    @Test
    void testInvalidChoiceAtSubMenu() {
        String input = "1\n9\n0\n0\n"; // Main->Countries->Invalid->Back->Exit
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class));
        menu.start();
    }

    // --- Top N coverage ---
    @Test
    void testTopNCountriesGlobal() {
        CountryDAO mockCountryDAO = mock(CountryDAO.class);
        String input = "1\n1\n2\n5\n0\n0\n0\n"; // Main->Countries->Global->Top N->Enter 5->Back->Exit
        MenuManager menu = buildMenu(input, mockCountryDAO, mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class));
        menu.start();
        verify(mockCountryDAO).getCountriesByPopulation(5);
    }

    @Test
    void testTopNCitiesGlobal() {
        CityDAO mockCityDAO = mock(CityDAO.class);
        String input = "2\n1\n2\n3\n0\n0\n0\n"; // Main->Cities->Global->Top N->Enter 3->Back->Exit
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mockCityDAO,
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mock(LanguageDAO.class));
        menu.start();
        verify(mockCityDAO).getCitiesByPopulation(3);
    }

    // --- Empty DAO results ---
    @Test
    void testEmptyLanguagesGlobal() {
        LanguageDAO mockLangDAO = mock(LanguageDAO.class);
        when(mockLangDAO.getLanguagesByPopulation()).thenReturn(List.of());
        String input = "5\n1\n1\n0\n0\n0\n"; // Main->Languages->Global->All->Back->Exit
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mock(LookupDAO.class), mockLangDAO);
        menu.start();
        verify(mockLangDAO).getLanguagesByPopulation();
    }

    // --- Input utilities coverage ---
    @Test
    void testChooseContinentCancel() {
        LookupDAO mockLookupDAO = mock(LookupDAO.class);
        when(mockLookupDAO.getAllContinents()).thenReturn(List.of(new Lookup("Europe")));
        String input = "1\n2\n1\n0\n0\n0\n0\n"; // Countries->Continental->All->Cancel continent->Back->Exit
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mockLookupDAO, mock(LanguageDAO.class));
        menu.start();
        verify(mockLookupDAO).getAllContinents();
    }

    @Test
    void testChooseRegionCancel() {
        LookupDAO mockLookupDAO = mock(LookupDAO.class);
        when(mockLookupDAO.getAllRegions()).thenReturn(List.of(new Lookup("Asia")));
        String input = "1\n3\n1\n0\n0\n0\n0\n"; // Countries->Regional->All->Cancel region->Back->Exit
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mockLookupDAO, mock(LanguageDAO.class));
        menu.start();
        verify(mockLookupDAO).getAllRegions();
    }

    @Test
    void testChooseCountryInvalidCode() {
        LookupDAO mockLookupDAO = mock(LookupDAO.class);
        when(mockLookupDAO.getAllCountries()).thenReturn(List.of(new Lookup("USA", "United States")));
        String input = "2\n4\n1\nXYZ\nUSA\n0\n0\n0\n"; // Cities->Country->All->Invalid code->Retry->Valid code->Back->Exit
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mockLookupDAO, mock(LanguageDAO.class));
        menu.start();
        verify(mockLookupDAO, atLeastOnce()).getAllCountries();
    }

    @Test
    void testChooseDistrictInvalidInput() {
        LookupDAO mockLookupDAO = mock(LookupDAO.class);
        when(mockLookupDAO.getAllCountries()).thenReturn(List.of(new Lookup("USA", "United States")));
        when(mockLookupDAO.getDistrictsByCountryCode("USA"))
                .thenReturn(List.of(new Lookup("California")));
        String input = "2\n5\n1\nUSA\nabc\n0\n0\n0\n0\n"; // Cities->District->All->Country->Invalid district input->Retry->Valid->Back->Exit
        MenuManager menu = buildMenu(input, mock(CountryDAO.class), mock(CityDAO.class),
                mock(CapitalCityDAO.class), mock(PopulationDAO.class),
                mockLookupDAO, mock(LanguageDAO.class));
        menu.start();
        verify(mockLookupDAO, times(2)).getDistrictsByCountryCode("USA");
    }
}
