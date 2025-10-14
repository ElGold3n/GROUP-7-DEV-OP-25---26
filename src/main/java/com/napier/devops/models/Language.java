package com.napier.devops.models;

public class Language {
    private String language;
    private long speakers;
    private long totalPopulation;
    private long globalPopulation;

    public Language(String language, long speakers, long totalPopulation) {
        this.language = language;
        this.speakers = speakers;
        this.totalPopulation = totalPopulation;
    }

    public String getLanguage() { return language; }
    public long getSpeakers() { return speakers; }
    public long getTotalPopulation() { return totalPopulation; }
    public long getGlobalPopulation() { return globalPopulation; }

    public double getPercentage() {
        return totalPopulation == 0 ? 0 : (speakers * 100.0 / totalPopulation);
    }

    public double getPercentageOfGlobal() {
        return globalPopulation == 0 ? 0 : (speakers * 100.0 / globalPopulation);
    }

    public void setGlobalPopulation(long globalPopulation) {
        this.globalPopulation = globalPopulation;
    }


}

