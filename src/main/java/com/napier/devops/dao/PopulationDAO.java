package com.napier.devops.dao;

import com.napier.devops.models.Population;
import java.sql.*;
import java.util.*;

public class PopulationDAO {
    private final Connection conn;

    public PopulationDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Population> getContinentPopulations() {
        String sql = "SELECT country.Continent, " +
                "SUM(country.Population) AS TotalPopulation, " +
                "SUM(city.Population) AS CityPopulation, " +
                "SUM(country.Population) - SUM(city.Population) AS NonCityPopulation " +
                "FROM country " +
                "LEFT JOIN city ON country.Code = city.CountryCode " +
                "GROUP BY country.Continent " +
                "ORDER BY TotalPopulation DESC";
        return queryPopulation(sql);
    }

    public List<Population> getRegionPopulations() {
        String sql = "SELECT country.Region, " +
                "SUM(country.Population) AS TotalPopulation, " +
                "SUM(city.Population) AS CityPopulation, " +
                "SUM(country.Population) - SUM(city.Population) AS NonCityPopulation " +
                "FROM country " +
                "LEFT JOIN city ON country.Code = city.CountryCode " +
                "GROUP BY country.Region " +
                "ORDER BY TotalPopulation DESC";
        return queryPopulation(sql);
    }

    public List<Population> getCountryPopulations() {
        String sql = "SELECT country.Name, " +
                "country.Population AS TotalPopulation, " +
                "SUM(city.Population) AS CityPopulation, " +
                "country.Population - SUM(city.Population) AS NonCityPopulation " +
                "FROM country " +
                "LEFT JOIN city ON country.Code = city.CountryCode " +
                "GROUP BY country.Code, country.Name, country.Population " +
                "ORDER BY TotalPopulation DESC";
        return queryPopulation(sql);
    }

    private List<Population> queryPopulation(String sql, Object... params) {
        List<Population> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Population p = new Population(
                        rs.getString(1),       // label
                        rs.getLong(2),         // total
                        rs.getLong(3),         // city
                        rs.getLong(4)          // non-city
                );
                results.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
