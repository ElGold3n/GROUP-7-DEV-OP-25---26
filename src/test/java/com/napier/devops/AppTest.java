package com.napier.devops;

import com.napier.devops.dao.*;
import com.napier.devops.db.Database;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.sql.Connection;

import static javax.management.Query.times;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppTest {

    private App app;
    private Database db;
    private Connection conn;

    private CountryDAO countryDAO;
    private CityDAO cityDAO;
    private CapitalCityDAO capitalDAO;
    private PopulationDAO populationDAO;
    private LookupDAO lookupDAO;
    private LanguageDAO languageDAO;

    @BeforeEach
    void setUp() {
        // Mock the database and connection
        db = mock(Database.class);
        conn = mock(Connection.class);

        when(db.getConnection()).thenReturn(conn);

        // Wire DAOs with mocked connection
        countryDAO = new CountryDAO(conn);
        cityDAO = new CityDAO(conn);
        capitalDAO = new CapitalCityDAO(conn);
        populationDAO = new PopulationDAO(conn);
        lookupDAO = new LookupDAO(conn);
        languageDAO = new LanguageDAO(conn);

        app = new App();
    }

    @Test
    void testAppInitialisesDAOs() {
        assertNotNull(countryDAO);
        assertNotNull(cityDAO);
        assertNotNull(capitalDAO);
        assertNotNull(populationDAO);
        assertNotNull(lookupDAO);
        assertNotNull(languageDAO);
    }

    @Test
    void testDatabaseConnectionIsMocked() {
        assertEquals(conn, db.getConnection());
        verify(db, times(1)).getConnection();
    }
}
