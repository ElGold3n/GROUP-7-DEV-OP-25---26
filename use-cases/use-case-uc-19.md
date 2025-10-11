USE CASE: UC-19 Language Speakers and % of World

CHARACTERISTIC INFORMATION  
Goal in Context  
As an Analyst, I want to see the number of speakers and percentage of the world population for key languages so that I can understand linguistic distribution.

Scope  
Population Reporting System.

Level  
Primary task.

Preconditions
- Language and population data exist.

Success End Condition  
Report is displayed showing speaker counts and percentages.

Failed End Condition  
No report is produced.

Primary Actor  
Analyst.

Trigger  
User selects “Language Report.”

MAIN SUCCESS SCENARIO
1. System selects key languages: Chinese, English, Hindi, Spanish, Arabic.
2. System calculates speakers = SUM(country.Population * Percentage/100).
3. System calculates % of world population.
4. System sorts by speakers DESC.
5. System displays: Language, Speakers, % of World.

EXTENSIONS
- Missing language data: system skips and notifies user.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
