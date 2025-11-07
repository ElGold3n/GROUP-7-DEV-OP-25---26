package com.napier.devops.models;


public class Language {

    private boolean isContinent;
    private boolean isRegion;
    private boolean isCountry;

    private String language;
    private long speakers;

    private String continent;
    private String region;
    private String country;

    private String percentOfGlobalPopulation;
    private String percentOfContinentPopulation;
    private String percentOfRegionPopulation;
    private String percentOfCountryPopulation;

    private long globalPopulation; // optional context
    private double globalPopulationPercentage;

    private String scopeType;   // e.g. "Global", "Continent", "Region", "Country"
    private String scopeName;   // e.g. "Asia", "Caribbean", "France"


    // --- Constructors ---

    // Global
    public Language(String language, long speakers, String percentOfGlobalPopulation) {
        this.isContinent = false;
        this.isRegion    = false;
        this.isCountry   = false;

        this.language = language;
        this.speakers = speakers;
        this.percentOfGlobalPopulation = percentOfGlobalPopulation;
    }

    // Continent / Region / Country
    public Language(String type, String language, long speakers,
                    String percentOfPopulation, String percentOfGlobalPopulation, String location) {
        if (type.equals("Continent")) {
            setContinent(location, true);
            this.percentOfContinentPopulation = percentOfPopulation;
        }

        if (type.equals("Region")) {
            setRegion(location, true);
            this.isContinent = false;
            this.isCountry   = false;
            this.percentOfRegionPopulation = percentOfPopulation;
        }

        if (type.equals("Country")) {
            setCountry(location, true);
            this.isContinent = false;
            this.isRegion    = false;
            this.percentOfCountryPopulation = percentOfPopulation;
       }
        this.language = language;
        this.speakers = speakers;
        this.percentOfGlobalPopulation = percentOfGlobalPopulation;
        setScopeType(type);
        setScopeName(location);
    }

    public Language(String language, long speakers, double globalPopulationPercentage) {
        this.language = language;
        this.speakers = speakers;
        this.globalPopulationPercentage = globalPopulationPercentage;
    }


    // --- Getters/Setters ---
    public boolean isContinent() { return isContinent; }
    public boolean isRegion() { return isRegion; }
    public boolean isCountry() { return isCountry; }

    public String getLanguage() { return language; }
    public long getSpeakers() { return speakers; }
    public String getContinent() { return continent; }
    public String getRegion() { return region; }
    public String getCountry() { return country; }
    public String getPercentOfGlobalPopulation() { return percentOfGlobalPopulation; }
    public String getPercentOfContinentPopulation() { return percentOfContinentPopulation; }
    public String getPercentOfRegionPopulation() { return percentOfRegionPopulation; }
    public String getPercentOfCountryPopulation() { return percentOfCountryPopulation; }

    public void setContinent(String continent, boolean isContinent) {
        this.isContinent = isContinent;
        this.continent = continent;
    }

    public void setRegion(String region, boolean isRegion) {
        this.isRegion = isRegion;
        this.region = region;
    }
    public void setCountry(String country, boolean isCountry) {
        this.isCountry = isCountry;
        this.country = country;
    }
    public void setGlobalPopulation(long globalPopulation) {
        this.globalPopulation = globalPopulation;
    }
    public long getGlobalPopulation() { return globalPopulation; }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

}
