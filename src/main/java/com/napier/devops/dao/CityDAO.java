package com.napier.devops.dao;

import com.napier.devops.models.City;

import java.sql.*;
import java.util.*;

public class CityDAO {
    private final Connection conn;

    public CityDAO(Connection conn) {
        this.conn = conn;
    }

    public List<City> getCitiesByPopulation() {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode=country.Code ORDER BY city.Population DESC";
        return queryCities(sql);
    }

    public List<City> getCitiesByPopulation(int n) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode=country.Code ORDER BY city.Population DESC LIMIT ?";
        return queryCities(sql, n);
    }

    public List<City> getCitiesInContinent(String continent) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Continent=? ORDER BY city.Population DESC";
        return queryCities(sql, continent);
    }

    public List<City> getCitiesInContinent(String continent, int n) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Continent=? ORDER BY city.Population DESC LIMIT ?";
        return queryCities(sql, continent, n);
    }

    public List<City> getCitiesInRegion(String region) {
        String sql = "SELECT city.ID, city.Name, city.CountryCode, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Region = ? ORDER BY city.Population DESC";
        return queryCities(sql, region);
    }

    public List<City> getCitiesInRegion(String region, int n) {
        String sql = "SELECT city.ID, city.Name, city.CountryCode, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Region = ? ORDER BY city.Population DESC LIMIT ?";
        return queryCities(sql, region, n);
    }

    public List<City> getCitiesInCountry(String country) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Code=? ORDER BY city.Population DESC";
        return queryCities(sql, country);
    }

    public List<City> getCitiesInCountry(String country, int n) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Code=? ORDER BY city.Population DESC";
        return queryCities(sql, country, n);
    }

    public List<City> getCitiesInDistrict(String district) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE city.District=? ORDER BY city.Population DESC";
        return queryCities(sql, district);
    }

    public List<City> getCitiesInDistrict(String district, int n) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE city.District=? ORDER BY city.Population DESC LIMIT ?";
        return queryCities(sql, district, n);
    }


    private List<City> queryCities(String sql, Object... params) {
        List<City> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i=0; i<params.length; i++) stmt.setObject(i+1, params[i]);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                City c = new City();
                c.setName(rs.getString("Name"));
                c.setCountry(rs.getString("Country"));
                c.setDistrict(rs.getString("District"));
                c.setPopulation(rs.getLong("Population"));
                results.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return results;
    }
}
