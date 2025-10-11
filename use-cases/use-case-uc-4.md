USE CASE: UC-4 Top N Countries by Population

CHARACTERISTIC INFORMATION  
Goal in Context  
As a General User, I want to retrieve the Top N populated countries in a given scope so that I can focus on the largest populations.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions  
Valid scope and N provided.

Success End Condition  
Report is displayed showing top N countries sorted by population.

Failed End Condition  
No report is produced.

Primary Actor  
General User.

Trigger  
User inputs scope and N.

MAIN SUCCESS SCENARIO
1. User inputs scope (world, continent, region) and N.
2. System validates inputs.
3. System filters `country` table by scope.
4. System joins `city` for capital name.
5. System sorts by population DESC.
6. System limits results to N.
7. System displays report.

EXTENSIONS
- Invalid scope or N: system displays error.
- N exceeds available countries: system displays max available.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
