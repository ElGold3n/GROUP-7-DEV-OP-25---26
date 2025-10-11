USE CASE: UC-11 List All Capital Cities by Population (World)

CHARACTERISTIC INFORMATION  
Goal in Context  
As a General User, I want to list all capital cities in the world by population so that I can explore global capitals.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions
- Capital city data exists in the database.

Success End Condition  
Report is displayed showing all capital cities sorted by population.

Failed End Condition  
No report is produced.

Primary Actor  
General User.

Trigger  
User selects “All Capital Cities” report.

MAIN SUCCESS SCENARIO
1. User selects report type.
2. System queries `country.Capital`.
3. System joins `city` for population.
4. System sorts results by population DESC.
5. System displays: Name, Country, Population.

EXTENSIONS
- Missing capital ID: system skips and logs warning.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
