package com.napier.devops.dao;

import com.napier.devops.models.Population;
import java.sql.*;
import java.util.*;

public class PopulationDAO {
    private final Connection conn;

    public PopulationDAO(Connection conn) {
        this.conn = conn;
    }

    public long getGlobalPopulation() {
        String sql = "SELECT SUM(country.Population) AS globalPopulation FROM country";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("globalPopulation");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }




    public List<Population> getGlobalPopulations() {
        String sql = "SELECT c.Continent, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Continent = c.Continent) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Continent = c.Continent)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS non_city_population " +

                "FROM country c " +
                "GROUP BY c.Continent " +
                "ORDER BY total_population DESC";
        return queryPopulation(sql);
    }

    public List<Population> getGlobalPopulations(int n) {
        String sql = "SELECT c.Continent, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Continent = c.Continent) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Continent = c.Continent)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS non_city_population " +

                "FROM country c " +
                "GROUP BY c.Continent " +
                "ORDER BY total_population DESC " +
                "LIMIT ?";
        return queryPopulation(sql, n);
    }

    public List<Population> getContinentPopulations(String name) {
        String sql = "SELECT c.Continent, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Continent = c.Continent) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Continent = c.Continent)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS non_city_population " +

                "FROM country c " +
                "WHERE c.Continent = ? " +
                "GROUP BY c.Continent " +
                "ORDER BY total_population DESC";
        return queryPopulation(sql, name);
    }

    public List<Population> getContinentPopulations(String name, int n) {
        String sql = "SELECT c.Continent, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Continent = c.Continent) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Continent = c.Continent)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS non_city_population " +

                "FROM country c " +
                "WHERE c.Continent = ? " +
                "GROUP BY c.Continent " +
                "ORDER BY total_population DESC " +
                "LIMIT ?";
        return queryPopulation(sql, name, n);
    }

    public List<Population> getRegionPopulations() {
        String sql = "SELECT c.Region, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Region = c.Region) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Region = c.Region)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS non_city_population " +

                "FROM country c " +
                "GROUP BY c.Region, total_population " +
                "ORDER BY total_population DESC";
        return queryPopulation(sql);
    }

    public List<Population> getRegionPopulations(int n) {
        String sql = "SELECT c.Region, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Region = c.Region) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Region = c.Region)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS non_city_population " +

                "FROM country c " +
                "GROUP BY c.Region " +
                "ORDER BY total_population DESC " +
                "LIMIT ?";
        return queryPopulation(sql, n);
    }

    public List<Population> getRegionPopulations(String name) {
        String sql = "SELECT c.Region, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Region = c.Region) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Region = c.Region)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS non_city_population " +

                "FROM country c " +
                "WHERE c.Region = ? " +
                "GROUP BY c.Region " +
                "ORDER BY total_population DESC";
        return queryPopulation(sql, name);
    }

    public List<Population> getRegionPopulations(String name, int n) {
        String sql = "SELECT c.Region, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Region = c.Region) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Region = c.Region)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS non_city_population " +

                "FROM country c " +
                "WHERE c.Region = ? " +
                "GROUP BY c.Region " +
                "ORDER BY total_population DESC " +
                "LIMIT ?";
        return queryPopulation(sql, name, n);
    }

    public List<Population> getCountryPopulations() {
        String sql = "SELECT c.Name AS Country, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Code = c.Code) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.CountryCode  = c.Code) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Region = c.Region)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Code = c.Code) AS non_city_population " +

                "FROM country c " +
                "GROUP BY c.Code, total_population " +
                "ORDER BY total_population DESC";
        return queryPopulation(sql);
    }

    public List<Population> getCountryPopulations(int n) {
        String sql = "SELECT c.Name AS Country, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Code = c.Code) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.CountryCode  = c.Code) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Region = c.Region)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Code = c.Code) AS non_city_population " +

                "FROM country c " +
                "GROUP BY c.Code, total_population " +
                "ORDER BY total_population DESC " +
                "LIMIT ?";
        return queryPopulation(sql, n);
    }

    public List<Population> getCountryPopulations(String name) {
        String sql = "SELECT c.Name AS Country, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Code = c.Code) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.CountryCode  = c.Code) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Region = c.Region)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Code = c.Code) AS non_city_population " +

                "FROM country c " +
                "WHERE c.Name = ?" +
                "GROUP BY c.Code, total_population " +
                "ORDER BY total_population DESC";
        return queryPopulation(sql, name);
    }

    public List<Population> getCountryPopulations(String name, int n) {
        String sql = "SELECT c.Name AS Country, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Code = c.Code) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.CountryCode  = c.Code) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Region = c.Region)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Code = c.Code) AS non_city_population " +

                "FROM country c " +
                "WHERE c.Name = ?" +
                "GROUP BY c.Code, total_population " +
                "ORDER BY total_population DESC " +
                "LIMIT ?";
        return queryPopulation(sql, name, n);
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
