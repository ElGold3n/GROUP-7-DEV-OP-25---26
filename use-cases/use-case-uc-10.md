USE CASE: UC-10 Top N Cities by Population

CHARACTERISTIC INFORMATION  
Goal in Context  
As a General User, I want to list the Top N cities in a specific scope so that I can identify the most populated urban areas.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions
- Valid scope and N provided.
- City data exists in the database.

Success End Condition  
Report is displayed showing top N cities sorted by population.

Failed End Condition  
No report is produced.

Primary Actor  
General User.

Trigger  
User inputs scope and N.

MAIN SUCCESS SCENARIO
1. User inputs scope (world, continent, region, country, district) and N.
2. System validates inputs.
3. System filters city data by scope.
4. System joins `country` for name.
5. System sorts cities by population DESC.
6. System limits results to N.
7. System displays: Name, Country, District, Population.

EXTENSIONS
- Invalid scope or N: system displays error.
- N exceeds available cities: system displays max available.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
