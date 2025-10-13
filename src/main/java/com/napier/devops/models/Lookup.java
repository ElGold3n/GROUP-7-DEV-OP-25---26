package com.napier.devops.models;

public class Lookup {
    private String type;   // e.g. "Continent", "Region", "Country", "District"
    private String value;  // the actual name


    public Lookup(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Override
    public String toString() {
        return String.format("%s: %s", type, value);
    }
}
