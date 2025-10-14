USE CASE: UC-1 List All Countries by Population (World)

CHARACTERISTIC INFORMATION  
Goal in Context  
As an Analyst, I want to list all countries in the world by population so that I can see the largest to smallest.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions
- Country data exists in the database.
- City table contains capital names.

Success End Condition  
Report is displayed showing all countries sorted by population.

Failed End Condition  
No report is produced.

Primary Actor  
Analyst.

Trigger  
User selects “All Countries” report.

MAIN SUCCESS SCENARIO
1. User selects report type.
2. System queries `country` table.
3. System joins `city` table for capital name.
4. System sorts countries by population DESC.
5. System displays: Code, Name, Continent, Region, Population, Capital.

EXTENSIONS
- No country data: system displays error.
- Capital name missing: system displays “Unknown”.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
