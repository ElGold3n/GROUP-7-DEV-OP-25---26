USE CASE: UC-13 List Capital Cities in a Region by Population

CHARACTERISTIC INFORMATION  
Goal in Context  
As a General User, I want to list capital cities in a specific region so that I can explore capitals at that scope.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions
- Valid region name provided.

Success End Condition  
Filtered report is displayed.

Failed End Condition  
No report is produced.

Primary Actor  
General User.

Trigger  
User inputs region name.

MAIN SUCCESS SCENARIO
1. User inputs region name.
2. System filters `country.Region`.
3. System joins `city` for capital population.
4. System sorts results by population DESC.
5. System displays: Name, Country, Population.

EXTENSIONS
- Invalid region: system prompts correction.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
