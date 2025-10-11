USE CASE: UC-15 Population Aggregates per Continent

CHARACTERISTIC INFORMATION  
Goal in Context  
As a Policy Maker, I want to see population totals, city populations, and non-city populations with percentages per continent so that I can understand urbanization trends.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions
- Population and city data exist in the database.

Success End Condition  
Report is displayed showing aggregates and percentages.

Failed End Condition  
No report is produced.

Primary Actor  
Policy Maker.

Trigger  
User selects “Continent Aggregates” report.

MAIN SUCCESS SCENARIO
1. System groups data by continent.
2. System calculates TotalPop, CityPop, NonCityPop.
3. System computes City% and NonCity%.
4. System displays: Continent, TotalPop, CityPop, City%, NonCityPop, NonCity%.

EXTENSIONS
- Missing city data: system displays partial results with warning.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
