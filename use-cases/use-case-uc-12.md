USE CASE: UC-12 List Capital Cities in a Continent by Population

CHARACTERISTIC INFORMATION  
Goal in Context  
As a General User, I want to list capital cities in a specific continent so that I can compare capitals within that scope.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions
- Valid continent name provided.

Success End Condition  
Filtered report is displayed.

Failed End Condition  
No report is produced.

Primary Actor  
General User.

Trigger  
User inputs continent name.

MAIN SUCCESS SCENARIO
1. User inputs continent name.
2. System filters `country.Continent`.
3. System joins `city` for capital population.
4. System sorts results by population DESC.
5. System displays: Name, Country, Population.

EXTENSIONS
- Invalid continent: system prompts correction.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
