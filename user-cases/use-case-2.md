USE CASE: 3 View Top N Populated Countries

CHARACTERISTIC INFORMATION  
Goal in Context  
As a general user I want to view the top N populated countries in a selected scope so that I can understand global population rankings.

Scope  
World Population Reporting System.

Level  
Primary task.

Preconditions  
Valid number N is entered. Scope (world, continent, region) is selected.

Success End Condition  
A report is displayed showing the top N countries sorted by population.

Failed End Condition  
No report is produced.

Primary Actor  
General User.

Trigger  
User inputs N and selects scope.

MAIN SUCCESS SCENARIO  
User inputs N and selects scope.  
System queries country data.  
System sorts countries by population descending.  
System displays top N results.

EXTENSIONS  
N exceeds available countries: system displays maximum available.  
Invalid input: system prompts correction.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
