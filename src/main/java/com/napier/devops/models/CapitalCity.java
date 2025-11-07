package com.napier.devops.models;

public class CapitalCity {
    private String name;
    private String country;
    private String continent;
    private String region;
    private long population;

    public CapitalCity( String name, String country, String continent, String region, long population) {
        this.name = name;
        this.country = country;
        this.continent = continent;
        this.region = region;
        this.population = population;
    }

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getContinent() { return continent; }
    public void setContinent(String continent) { this.continent = continent; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public long getPopulation() { return population; }
    public void setPopulation(long population) { this.population = population; }

    @Override
    public String toString() {
        return String.format("%s (Capital of %s) | Pop: %d", name, country, population);
    }
}
