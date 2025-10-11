USE CASE: 5 View Top N Cities

CHARACTERISTIC INFORMATION  
Goal in Context  
As a policy maker I want to view the top N cities by population in a selected scope so that I can identify urban hotspots.

Scope  
World Population Reporting System.

Level  
Primary task.

Preconditions  
Valid number N is entered. Scope is selected.

Success End Condition  
A report is displayed showing the top N cities sorted by population.

Failed End Condition  
No report is produced.

Primary Actor  
Policy Maker.

Trigger  
User inputs N and selects scope.

MAIN SUCCESS SCENARIO  
User inputs N and selects scope.  
System queries city data.  
System sorts cities by population descending.  
System displays top N results.

EXTENSIONS  
N exceeds available cities: system displays maximum available.  
Invalid input: system prompts correction.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
