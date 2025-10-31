USE CASE: UC-2 List Countries in a Continent by Population

CHARACTERISTIC INFORMATION  
Goal in Context  
As a Policy Maker, I want to list countries in a specific continent so that I can compare populations within that continent.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions  
Valid continent name provided.

Success End Condition  
Filtered report is displayed.

Failed End Condition  
No report is produced.

Primary Actor  
Policy Maker.

Trigger  
User inputs continent name.

MAIN SUCCESS SCENARIO
1. User inputs continent name.
2. System validates input against DB.
3. System filters `country` table by continent.
4. System joins `city` for capital name.
5. System sorts by population DESC.
6. System displays: Code, Name, Continent, Region, Population, Capital.

EXTENSIONS
- Invalid continent: system prompts correction.
- No countries found: system displays message.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
