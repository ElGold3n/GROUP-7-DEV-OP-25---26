USE CASE: UC-5 List All Cities by Population (World)

CHARACTERISTIC INFORMATION  
Goal in Context  
As an Analyst, I want to list all cities in the world by population so that I can analyze global urban populations.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions  
City data exists in the database.

Success End Condition  
Report is displayed showing cities sorted by population.

Failed End Condition  
No report is produced.

Primary Actor  
Analyst.

Trigger  
User selects “All Cities” report.

MAIN SUCCESS SCENARIO
1. User selects report type.
2. System queries `city` table.
3. System joins `country` for country name.
4. System sorts cities by population DESC.
5. System displays: Name, Country, District, Population.

EXTENSIONS
- No city data: system displays error.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
