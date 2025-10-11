USE CASE: 6 View Capital Cities by Population

CHARACTERISTIC INFORMATION  
Goal in Context  
As a general user I want to view capital cities sorted by population so that I can understand major administrative centers globally.

Scope  
World Population Reporting System.

Level  
Primary task.

Preconditions  
Capital city data exists in the database. Scope is selected.

Success End Condition  
A report is displayed showing capital cities sorted by population.

Failed End Condition  
No report is produced.

Primary Actor  
General User.

Trigger  
User selects capital city report type and scope.

MAIN SUCCESS SCENARIO  
User selects scope (world, continent, region).  
System filters capital cities.  
System sorts results by population descending.  
System displays report.

EXTENSIONS  
No capital cities found: system displays message.  
Invalid scope: system prompts correction.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
