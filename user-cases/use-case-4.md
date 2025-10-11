USE CASE: 4 View Cities by Population

CHARACTERISTIC INFORMATION  
Goal in Context  
As a data analyst I want to view cities sorted by population within a selected scope so that I can analyze urban distribution.

Scope  
World Population Reporting System.

Level  
Primary task.

Preconditions  
City data exists in the database. Scope is selected.

Success End Condition  
A report is displayed showing cities sorted by population.

Failed End Condition  
No report is produced.

Primary Actor  
Data Analyst.

Trigger  
User selects city report type and scope.

MAIN SUCCESS SCENARIO  
User selects scope (world, continent, region, country, district).  
System filters cities.  
System sorts cities by population descending.  
System displays report.

EXTENSIONS  
No cities found: system displays message.  
Invalid scope: system prompts correction.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
