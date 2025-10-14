USE CASE: UC-17 Population Aggregates per Country

CHARACTERISTIC INFORMATION  
Goal in Context  
As a Policy Maker, I want to see population totals, city populations, and non-city populations with percentages per country so that I can analyze national urbanization.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions
- Population and city data exist in the database.

Success End Condition  
Report is displayed showing aggregates and percentages per country.

Failed End Condition  
No report is produced.

Primary Actor  
Policy Maker.

Trigger  
User selects “Country Aggregates” report.

MAIN SUCCESS SCENARIO
1. System groups data by country.
2. System calculates TotalPop, CityPop, NonCityPop.
3. System computes City% and NonCity%.
4. System displays: Country, TotalPop, CityPop, City%, NonCityPop, NonCity%.

EXTENSIONS
- Missing city data: system displays partial results with warning.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
