package com.napier.devops;

import com.napier.devops.dao.*;
import com.napier.devops.models.Lookup;
import com.napier.devops.util.TablePrinter;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MenuManagerTest {

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


}
