# Strictly for DevOps 25-26 group 7 project collaboration.

## Project Overview

This project is a Java-based application designed to provide easy access to population information from a MySQL database (world.sql). 
The system generates various reports on countries, cities, capital cities, populations, and languages as specified by the organization. 
This project is being developed using the Scrum methodology and adheres strictly to a robust DevOps and CI/CD pipeline utilizing GitFlow, Maven, Docker, and GitHub Actions. 
Reports include sorted lists of populations, top N results, and aggregated population statistics.
## Product Backlog

The product backlog is managed using **GitHub Issues** integrated with **Zube.io**.

Each user story is defined as a GitHub Issue (linked to Zube), prioritized, and assigned to team members.  
You can view the full backlog here: https://zube.io/group-7-dev-op-25-26/world-population-reporting-system/w/workspace-1/kanban.

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
Provides the number of speakers for Chinese, English, Hindi, Spanish, and Arabic languages globally, sorted from the largest to the smallest, including the percentage of the world population that speaks each language.
  ###### Output
       Language, Speakers, % of World Population

  ## Requirements and implementation details
  
 1. Build Tool: Maven is used for dependency management and building the project.
 2. Build Artifact: The project MUST build to a self-contained JAR file.
 3. Containerization: A Dockerfile is provided for packaging the application and its dependencies into a working container.
 4. CI/CD Pipeline: GitHub Actions is configured to run on pushes/PRs to the automatically developed and release branches.
 5. CI/CD Steps: The pipeline includes steps to: 1. Build the Maven JAR. 2. Run tests. 3. Build and tag the Docker image.
  
 
  ## Development Workflow & Governance
  
  1. Branching Strategy: GitFlow is mandatory. Core branches include master (production-ready), develop (integration), and release/* (staging for releases).
  2. Project Tracking: All development work is managed via GitHub Issues.
  3. Task Definition: All tasks MUST be defined as User Stories in the Product Backlog.
  4. Backlog Management: The Product Backlog is managed and prioritized through Zube.io (integrated with GitHub Issues).
  5. Visual Workflow: A Kanban/Project Board tracks the overall project flow, and Sprint Boards track active sprint tasks.
  6. Governance: A Code of Conduct is defined and enforced to maintain a professional and inclusive environment.
  7. Releases: The first official release v0.1-alpha-2 must be tagged on GitHub from the release branch.


### Project Badges

_The following badges are used to track the project's current status._

- **Build Status** – shows whether the latest build of the project passes all automated builds on GitHub Actions.



![workflow](https://github.com/ElGold3n/GROUP-7-DEV-OP-25---26/actions/workflows/main.yml/badge.svg)



- **Release** – Outlines the latest version of the project that has been officially released (including pre-releases).
  

[![Release](https://img.shields.io/github/v/release/ElGold3n/GROUP-7-DEV-OP-25---26?include_prereleases&style=plastic-square)](https://github.com/ElGold3n/GROUP-7-DEV-OP-25---26/releases)


- **License** – Represents the license under which the project is released.


[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=plastic-square)](https://www.apache.org/licenses/LICENSE-2.0)


- **Zube.io** – Gives a visual representation of the Scrum/Kanban project board specifically set up for team task tracking.



[![Zube Project](https://img.shields.io/badge/Project-Zube.io-purple?style=plastic-square)](https://zube.io/group-7-dev-op-25-26/WorldPopulationReportingSystem)

______________________________________________________________________________________________________________

## Build Status

| Branch | Status                                                                                                                                                              |
|---------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Master  | ![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/ElGold3n/GROUP-7-DEV-OP-25---26/main.yml?branch=master&style=social-square) |
| Develop | ![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/ElGold3n/GROUP-7-DEV-OP-25---26/main.yml?branch=develop&style=for-the-badge-square)     |

## _Code Coverage_ 
_The following tracked project's code coverage is an estimation not actuality,will change when i get the codes running properly*_

| Branch | Coverage | 
|---------|-----------|
| Master  |![Coverage Status](https://img.shields.io/badge/Coverage-80%25-brightgreen)|

## _Release & License_

| Type    | Badge                                                                                                                                                                                           |
|---------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Release | [![Releases](https://img.shields.io/github/release/ElGold3n/GROUP-7-DEV-OP-25---26/all.svg?style=plastic-square)](https://github.com/ElGold3n/GROUP-7-DEV-OP-25---26/releases)                  |
| License | [![LICENSE](https://img.shields.io/github/license/ElGold3n/GROUP-7-DEV-OP-25---26.svg?style=plastic-square&color=blue)](https://github.com/ElGold3n/GROUP-7-DEV-OP-25---26/blob/master/LICENSE) |

