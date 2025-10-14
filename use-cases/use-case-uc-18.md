USE CASE: UC-18 Population of Specific Entity

CHARACTERISTIC INFORMATION  
Goal in Context  
As an Analyst, I want to query the population of a single entity so that I can retrieve exact numbers easily.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions
- Valid entity type and name/code provided.

Success End Condition  
Population value is returned.

Failed End Condition  
No result is returned or invalid input error.

Primary Actor  
Analyst.

Trigger  
User inputs entity type and name/code.

MAIN SUCCESS SCENARIO
1. User inputs entity type (world, continent, region, country, district, city) and name/code.
2. System validates input.
3. System queries appropriate table.
4. System returns population value.

EXTENSIONS
- Invalid input: system displays error.
- Entity not found: system displays “No data found.”

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
