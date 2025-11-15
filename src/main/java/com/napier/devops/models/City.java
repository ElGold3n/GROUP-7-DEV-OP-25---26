package com.napier.devops.models;

public class City {
    private String name;
    private String continent;
    private String country;
    private String district;
    private long population;

    public City(String name, String district, String country, String continent, long population) {
        setName(name);
        setDistrict(district);
        setCountry(country);
        setContinent(continent);
        setPopulation(population);
    }

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContinent() { return continent; }
    public void setContinent(String continent) { this.continent = continent; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public long getPopulation() { return population; }
    public void setPopulation(long population) { this.population = population; }

}
