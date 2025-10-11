USE CASE: UC-6 List Cities in a Continent by Population

CHARACTERISTIC INFORMATION  
Goal in Context  
As a Policy Maker, I want to list cities in a specific continent so that I can analyze urban populations at that scope.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions
- Valid continent name provided.
- City and country data exist in the database.

Success End Condition  
Report is displayed showing cities in the continent sorted by population.

Failed End Condition  
No report is produced.

Primary Actor  
Policy Maker.

Trigger  
User inputs continent name.

MAIN SUCCESS SCENARIO
1. User inputs continent name.
2. System validates input.
3. System filters `country.Continent`.
4. System joins `city` and `country`.
5. System sorts cities by population DESC.
6. System displays: Name, Country, District, Population.

EXTENSIONS
- Invalid continent: system prompts correction.
- No cities found: system displays message.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
