USE CASE: UC-9 List Cities in a District by Population

CHARACTERISTIC INFORMATION  
Goal in Context  
As a Policy Maker, I want to list cities in a specific district so that I can analyze populations at a finer level.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions
- Valid district name provided.
- City data exists in the database.

Success End Condition  
Report is displayed showing cities in the district sorted by population.

Failed End Condition  
No report is produced.

Primary Actor  
Policy Maker.

Trigger  
User inputs district name.

MAIN SUCCESS SCENARIO
1. User inputs district name.
2. System validates input.
3. System filters `city.District`.
4. System joins `country` for name.
5. System sorts cities by population DESC.
6. System displays: Name, Country, District, Population.

EXTENSIONS
- Invalid district: system prompts correction.
- No cities found: system displays message.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
