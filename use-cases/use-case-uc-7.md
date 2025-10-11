USE CASE: UC-7 List Cities in a Region by Population

CHARACTERISTIC INFORMATION  
Goal in Context  
As a Policy Maker, I want to list cities in a specific region so that I can analyze urban populations at that scope.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions
- Valid region name provided.
- City and country data exist in the database.

Success End Condition  
Report is displayed showing cities in the region sorted by population.

Failed End Condition  
No report is produced.

Primary Actor  
Policy Maker.

Trigger  
User inputs region name.

MAIN SUCCESS SCENARIO
1. User inputs region name.
2. System validates input.
3. System filters `country.Region`.
4. System joins `city` and `country`.
5. System sorts cities by population DESC.
6. System displays: Name, Country, District, Population.

EXTENSIONS
- Invalid region: system prompts correction.
- No cities found: system displays message.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
