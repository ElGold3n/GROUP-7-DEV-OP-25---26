# Strictly for DevOps 25-26 group 7 project collaboration.

## Project Overview

This project is a Java-based application designed to provide easy access to population information from a MySQL database (world.sql). 
The system generates various reports on countries, cities, capital cities, populations, and languages as specified by the organization. 
This project is being developed using the Scrum methodology and adheres strictly to a robust DevOps and CI/CD pipeline utilizing GitFlow, Maven, Docker, and GitHub Actions. 
Reports include sorted lists of populations, top N results, and aggregated population statistics.
## Features
The system is tasked with generating reports across four major categories: Countries, Cities, Capital Cities, and Aggregate Population Data.
### 1. Country Reports
  #### A. All Countries
List all countries in the world, sorted by population (largest to smallest).
  ###### Output
       Code, Name, Continent, Region, Population, Capital
  #### B. Continent/Regions reports       
List all countries in a specified Continent or Region, sorted by population.
###### Output
       Code, Name, Continent, Region, Population, Capital
  #### C. Top N Countries      
Top N populated countries in the world (user-defined N).
###### Output
       Code, Name, Continent, Region, Population, Capital
  #### D. Top N by Location     
Top N populated countries in a specified Continent or Region (user-defined N).
###### Output
       Code, Name, Continent, Region, Population, Capital
       
### 2. City Reports
  #### A. All Cities
List all cities in the world, sorted by population (largest to smallest).
  ###### Output
       Name, Country, District, Population
   #### B. Geographical City report
List all cities in a specified Continent, Region, Country, or District, sorted by population.
  ###### Output
       Name, Country, District, Population
  #### C. Top N Cities
Top N populated cities in the world (user-defined N).
  ###### Output
       Name, Country, District, Population
  #### D. Top N by Population
Top N populated cities in a specified Continent, Region, Country, or District (user-defined N).
  ###### Output
       Name, Country, District, Population     

### 3. Capital City Reports
  #### A. All Capital Cities
List all capital cities in the world, sorted by population.
  ###### Output
      Name, Country, Population
   #### B. Continent/Region Capital Reports
List all capital cities in a specified Continent or Region, sorted by population.
  ###### Output
       Name, Country, Population
  #### C. Top N Capital Cities
Top N populated capital cities in the world (user-defined N).
  ###### Output
       Name, Country, Population
  #### D. Top N by Location
Top N populated capital cities in a specified Continent or Region (user-defined N).
  ###### Output
       Name, Country, Population    

### 4. Aggregate Population & Language Reports
  #### A. Population Breakdown
Provides Total Population, City Population (including %), and Non-City Population (including %) for a specified Continent, Region, or Country.
  ###### Output
        Name of Location, Total Population, City Population, % In Cities, Non-City Population, % Not In Cities
   #### B. Simple Population Access
Direct access to the population figure for the World, a Continent, a Region, a Country, a District, or a City.
  ###### Output
        Single Population Value
  #### C. Language Speakers
Provides the number of speakers for Chinese, English, Hindi, Spanish, and Arabic globally, sorted from greatest to smallest, including the percentage of the world population that speaks the language.
  ###### Output
       Language, Speakers, % of World Population

  ## Requirements

![workflow](https://github.com/ElGold3n/GROUP-7-DEV-OP-25---26/actions/workflows/main.yml/badge.svg)

[![LICENSE](https://img.shields.io/github/license/ElGold3n/devops.svg?style=flat-square)](https://github.com/ElGold3n/devops/blob/master/LICENSE)

[![Releases](https://img.shields.io/github/release/ElGold3n/devops/all.svg?style=flat-square)](https://github.com/ElGold3n/devops/releases)


