USE CASE: UC-14 Top N Capital Cities by Population

CHARACTERISTIC INFORMATION  
Goal in Context  
As a General User, I want to list the Top N capital cities in a specific scope so that I can focus on the largest capitals.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions
- Valid scope and N provided.

Success End Condition  
Report is displayed showing top N capital cities sorted by population.

Failed End Condition  
No report is produced.

Primary Actor  
General User.

Trigger  
User inputs scope and N.

MAIN SUCCESS SCENARIO
1. User inputs scope (world, continent, region) and N.
2. System validates inputs.
3. System filters `country` by scope.
4. System joins `city` for capital population.
5. System sorts results by population DESC.
6. System limits results to N.
7. System displays: Name, Country, Population.

EXTENSIONS
- Invalid scope or N: system displays error.
- N exceeds available cities: system displays max available.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
