package com.napier.devops.dao;

import com.napier.devops.models.CapitalCity;
import java.sql.*;
import java.util.*;

public class CapitalCityDAO {
    private final Connection conn;

    public CapitalCityDAO(Connection conn) {
        this.conn = conn;
    }

    public List<CapitalCity> getCapitalCitiesByPopulation() {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, country.Region, city.Population " +
                "FROM country JOIN city ON country.Capital = city.ID " +
                "ORDER BY city.Population DESC";
        return queryCapitals(sql);
    }

    public List<CapitalCity> getCapitalCitiesByPopulation(int n) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, country.Region, city.Population " +
                "FROM country JOIN city ON country.Capital = city.ID " +
                "ORDER BY city.Population DESC LIMIT ?";
        return queryCapitals(sql, n);
    }

    public List<CapitalCity> getCapitalCitiesInContinent(String continent) {
        String sql = "SELECT city.ID, city.Name,  country.Name AS Country, country.Continent, country.Region, city.Population " +
                "FROM country JOIN city ON country.Capital = city.ID " +
                "WHERE country.Continent = ? ORDER BY city.Population DESC";
        return queryCapitals(sql, continent);
    }

    public List<CapitalCity> getCapitalCitiesInContinent(String continent, int n) {
        String sql = "SELECT city.ID, city.Name,  country.Name AS Country, country.Continent, country.Region, city.Population " +
                "FROM country JOIN city ON country.Capital = city.ID " +
                "WHERE country.Continent = ? ORDER BY city.Population DESC LIMIT ?";
        return queryCapitals(sql, continent, n);
    }

    public List<CapitalCity> getCapitalCitiesInRegion(String region) {
        String sql = "SELECT city.ID, city.Name,  country.Name AS Country, country.Continent, country.Region, city.Population " +
                "FROM country JOIN city ON country.Capital = city.ID " +
                "WHERE country.Region = ? ORDER BY city.Population DESC";
        return queryCapitals(sql, region);
    }

    public List<CapitalCity> getCapitalCitiesInRegion(String region, int n) {
        String sql = "SELECT city.ID, city.Name,  country.Name AS Country, country.Continent, country.Region, city.Population " +
                "FROM country JOIN city ON country.Capital = city.ID " +
                "WHERE country.Region = ? ORDER BY city.Population DESC LIMIT ?";
        return queryCapitals(sql, region, n);
    }

    private List<CapitalCity> queryCapitals(String sql, Object... params) {
        List<CapitalCity> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i=0; i<params.length; i++) stmt.setObject(i+1, params[i]);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CapitalCity c = new CapitalCity();
                c.setName(rs.getString("Name"));
                c.setCountry(rs.getString("Country"));
                c.setContinent(rs.getString("Continent"));
                c.setRegion(rs.getString("Region"));
                c.setPopulation(rs.getLong("Population"));
                results.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return results;
    }
}
