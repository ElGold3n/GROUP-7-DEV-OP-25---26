package com.napier.devops.dao;

import com.napier.devops.models.Country;
import java.sql.*;
import java.util.*;

public class CountryDAO {
    private final Connection conn;

    public CountryDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Country> getCountriesByPopulation() {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID ORDER BY country.Population DESC";
        return queryCountries(sql);
    }

    public List<Country> getCountriesByPopulation(int n) {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID ORDER BY country.Population DESC LIMIT ?";
        return queryCountries(sql, n);
    }

    public List<Country> getCountriesInContinent(String continent) {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID WHERE Continent=? ORDER BY country.Population DESC";
        return queryCountries(sql, continent);
    }

    public List<Country> getCountriesInContinent(String continent, int n) {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID WHERE Continent=? ORDER BY country.Population DESC LIMIT ?";
        return queryCountries(sql, continent, n);
    }

    public List<Country> getCountriesInRegion(String region) {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID WHERE Region=? ORDER BY country.Population DESC";
        return queryCountries(sql, region);
    }

    public List<Country> getCountriesInRegion(String region, int n) {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID WHERE Region=? ORDER BY country.Population DESC Limit ?";
        return queryCountries(sql, region, n);
    }

    private List<Country> queryCountries(String sql, Object... params) {
        List<Country> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i=0; i<params.length; i++) stmt.setObject(i+1, params[i]);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Country c = new Country();
                c.setCode(rs.getString("Code"));
                c.setName(rs.getString("Name"));
                c.setContinent(rs.getString("Continent"));
                c.setRegion(rs.getString("Region"));
                c.setPopulation(rs.getLong("Population"));
                c.setCapital(rs.getString("Capital"));
                results.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return results;
    }
}
