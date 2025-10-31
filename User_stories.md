# User Stories 

This document contains all *user stories* for the Population Reporting System project.  
The stories are grouped by *Epics* to match the main reporting categories and DevOps workflow.  
Each story follows the standard format:

> **As a** [role], **I want** [feature],  **So that** [benefit].

Each story also includes **Acceptance Criteria** and an **ID** for tracking (e.g., US-1).  
***Use these IDs for GitHub Issues, project boards, commits, and pull requests.

---

##  Epic 1: Country Reports

### US-1 – List all countries by population (world)
**As an** analyst,**I want** to list all countries in the world by population,**So that** I can see the largest to smallest.

**Acceptance Criteria**
- Returns: Code, Name, Continent, Region, Population, Capital  
- Sorted by population (DESC)  
- Data retrieved from `country` table with join to `city` for capital name

---

### US-2 – List countries in a continent by population
**As an** analyst, **I want** to list countries in a specific continent, **So that** I can compare populations within that continent.

**Acceptance Criteria**
- Input: Continent name (validated against DB)  
- Same columns as US-1  
- Sorted by population (DESC)

---

### US-3 – List countries in a region by population
**As an** analyst,**I want** to list countries in a specific region,**So that** I can analyze population distribution in that region.

**Acceptance Criteria**
- Input: Region name  
- Same columns as US-1  
- Sorted by population (DESC)

---

### US-4 – Top N countries (world / continent / region)
**As an** analyst,**I want** to retrieve the Top N populated countries in a given scope,**So that** I can focus on the largest populations.

**Acceptance Criteria**
- Input: Scope (world, continent, region), N (>0)  
- Returns N countries, sorted by population DESC  
- Validates N and scope; errors clearly reported

---

## Epic 2: City Reports

### US-5 – List all cities by population (world) 
**As an** analyst,**I want** to list all cities in the world by population,**So that** I can analyze global urban populations.

**Acceptance Criteria**
- Returns: Name, Country, District, Population  
- Sorted by population DESC  
- Join `city` → `country` for Country name

---

### US-6 – List cities in a continent by population
**As an** analyst, **I want** to list cities in a specific continent,**So that** I can focus on that scope.

**Acceptance Criteria**
- Input: Continent name  
- Returns same columns as US-5  
- Filter by `country.Continent`  
- Sorted DESC

---

### US-7 – List cities in a region by population
**As an** analyst,**I want** to list cities in a specific region,**So that** I can focus on that scope.

**Acceptance Criteria**
- Input: Region name  
- Returns same columns as US-5  
- Filter by `country.Region`  
- Sorted DESC

---

### US-8 – List cities in a country by population
**As an** analyst,**I want** to list cities in a specific country,**So that** I can analyze its urban distribution.

**Acceptance Criteria**
- Input: Country name or ISO code  
- Filter by `city.CountryCode`  
- Sorted DESC

---

### US-9 – List cities in a district by population
**As an** analyst,**I want** to list cities in a specific district,**So that** I can analyze populations at a finer level.

**Acceptance Criteria**
- Input: District name  
- Filter by `city.District`  
- Sorted DESC

---

### US-10 – Top N cities by population (world / continent / region / country / district)
**As an** analyst,**I want** to list the Top N cities in a specific scope,**So that** I can identify the most populated urban areas.

**Acceptance Criteria**
- Inputs: Scope, Name (if needed), N (>0)  
- Returns ≤ N rows, sorted DESC  
- Validates N and scope; clear error messages

---

## Epic 3: Capital Cities Reports

### US-11 – List all capital cities by population (world)
**As an** analyst,**I want** to list all capital cities in the world by population,**So that** I can analyze capital city populations globally.

**Acceptance Criteria**
- Returns: Name, Country, Population  
- Uses `city.ID = country.Capital`  
- Sorted DESC

---

### US-12 – List capital cities in a continent by population
**As an** analyst,**I want** to list capital cities in a specific continent,**So that** I can analyze that scope.

**Acceptance Criteria**
- Input: Continent name  
- Filter by `country.Continent`  
- Sorted DESC

---

### US-13 – List capital cities in a region by population
**As an** analyst,**I want** to list capital cities in a specific region,**So that** I can analyze that scope.

**Acceptance Criteria**
- Input: Region name  
- Filter by `country.Region`  
- Sorted DESC

---

### US-14 – Top N capital cities (world / continent / region)
**As an** analyst,**I want** to list the Top N capital cities in a specific scope, **So that** I can focus on the largest.

**Acceptance Criteria**
- Inputs: Scope, Name (if needed), N (>0)  
- Returns ≤ N rows, sorted DESC  
- Validation errors reported clearly

---

##  Epic 4: Population Aggregates

### US-15 – Population aggregates per continent
**As an** analyst,**I want** to see population totals, city populations, and non-city populations with percentages per continent,**So that** I can understand urbanization trends.

**Acceptance Criteria**
- Returns: Continent, TotalPop, CityPop, City%, NonCityPop, NonCity%  
- CityPop = SUM of all cities in that continent  
- Percentages calculated to 1 decimal place, should sum ≈ 100%

---

### US-16 – Population aggregates per region
**As an** analyst,**I want** the same metrics per region,**So that** I can analyze regional urbanization.

**Acceptance Criteria**
- Same as US-15 but grouped by Region

---

### US-17 – Population aggregates per country
**As an** analyst,  
**I want** the same metrics per country,  
**So that** I can analyze national urbanization.

**Acceptance Criteria**
- Same as US-15 but grouped by Country

---

### US-18 – Population of specific entities (world / continent / region / country / district / city)
**As an** analyst,**I want** to query the population of a single entity,**So that** I can retrieve exact numbers easily.

**Acceptance Criteria**
- Inputs: entity type + name/code  
- Returns a single numeric value  
- Handles unknown or invalid inputs gracefully

---

## Epic 5: Language Reports

### US-19 – Language speakers and % of world
**As an** analyst,**I want** to see the number of speakers and percentage of the world population for key languages,**So that** I can understand linguistic distribution.

**Acceptance Criteria**
- Languages: Chinese, English, Hindi, Spanish, Arabic  
- Speakers calculated as `SUM(country.Population * Percentage/100)`  
- Sorted by speakers DESC  
- Returns: Language, Speakers, % of World (1 decimal place)

---

## Epic 6: DevOps & Infrastructure

### US-20 – Build self-contained JAR
**As a** DevOps engineer, **I want** the project to build a single executable JAR, **So that** it can run consistently in any environment.

**Acceptance Criteria**
- `mvn -DskipTests package assembly:single` builds the JAR  
- Output is stored in `target/`  
- README includes build instructions

---

### US-21 – Containerize app and DB with Docker Compose
**As a** developer,**I want** to run the app and database with a single command,**So that** onboarding and testing are simple.

**Acceptance Criteria**
- `docker compose up --build` brings up DB + app  
- App reads DB connection from environment variables  
- DB initializes with `world.sql`  
- Health checks ensure DB readiness before app starts

---

### US-22 – GitHub Actions CI
**As a** DevOps engineer,**I want** to build the JAR and Docker image automatically on GitHub,**So that** code quality and builds are consistent.

**Acceptance Criteria**
- Workflow triggers on push/PR to `develop`, `release/**`, `master`  
- JAR built and uploaded as artifact  
- Docker image builds successfully

---

### US-23 – GitFlow branching
**As a** maintainer, **I want** to follow GitFlow, **So that** development and releases are structured.

**Acceptance Criteria**
- Branches: `develop`, `master`, `release/*`  
- First release tagged

---

### US-24 – Templates & Code of Conduct
**As a** maintainer,**I want** contribution guidelines and templates, **So that** contributions are consistent.

**Acceptance Criteria**
- CODE OF CONDUCT.md present  
- Issue templates (bug, feature)  
- PR template  
- Linked in README

---

### US-25 – Project boards & Zube.io integration
**As a** project manager,**I want** project boards and Zube.io integration,**So that** progress is visible and trackable.

**Acceptance Criteria**
- GitHub Projects board: Backlog → Ready → In Progress → Review → Done  
- Zube.io synced with GitHub Issues

---

# Definition of Done (All Stories)
- Input validation implemented  
- SQL queries use prepared statements  
- Clear error messages for invalid inputs  
- Graceful handling of empty results  
- Unit/integration tests where applicable  
- CI build passing  
- Documentation updated

---

 
 *Each of these user stories should be created as GitHub Issues for proper tracking. Use the story ID (e.g., US-1) as the Issue title prefix.*
