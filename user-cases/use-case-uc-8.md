USE CASE: UC-8 List Cities in a Country by Population

CHARACTERISTIC INFORMATION  
Goal in Context  
As a Policy Maker, I want to list cities in a specific country so that I can analyze its urban distribution.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions
- Valid country name or ISO code provided.
- City data exists in the database.

Success End Condition  
Report is displayed showing cities in the country sorted by population.

Failed End Condition  
No report is produced.

Primary Actor  
Policy Maker.

Trigger  
User inputs country name or code.

MAIN SUCCESS SCENARIO
1. User inputs country name or ISO code.
2. System validates input.
3. System filters `city.CountryCode`.
4. System joins `country` for name.
5. System sorts cities by population DESC.
6. System displays: Name, Country, District, Population.

EXTENSIONS
- Invalid country: system prompts correction.
- No cities found: system displays message.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
