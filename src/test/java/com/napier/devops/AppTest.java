package com.napier.devops;

import com.napier.devops.dao.*;
import com.napier.devops.db.Database;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

public class AppTest {
    static App app;
    static CountryDAO countryDAO ;
    static CityDAO cityDAO;
    static CapitalCityDAO capitalDAO;
    static PopulationDAO populationDAO;
    static LookupDAO lookupDAO;
    static LanguageDAO languageDAO;
    static Database db;
    static Connection conn;

    static void init() {
        app = new App();
        db = new Database();
        db.connect();
        conn = db.getConnection();

        // Wire DAOs
        countryDAO = new CountryDAO(conn);
        cityDAO = new CityDAO(conn);
        capitalDAO = new CapitalCityDAO(conn);
        populationDAO = new PopulationDAO(conn);
        lookupDAO = new LookupDAO(conn);
        languageDAO = new LanguageDAO(conn);
    }

    @Test
    public void testApp() {
        init();
        app.runBatchReports(countryDAO, cityDAO, capitalDAO, populationDAO, languageDAO);
    }
}
