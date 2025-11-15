---
name: Bug report
about: Create a report to help us improve
title: "[BUG]"
labels: ''
assignees: albakrio, ElGold3n, whashby

---

# Issue: Bug Report
Create a report to help us improve

---

## 🐛 Describe the bug
When selecting **District Reports → All cities in a district**, entering a valid country code (`USA`) results in repeated "Invalid country code" errors. The system does not proceed to district selection, and the DAO call `lookupDAO.getDistrictsByCountryCode("USA")` is never invoked.

---

## 🔄 To Reproduce
Steps to reproduce the behavior:
1. Go to **Main Menu → Cities → District Reports → All cities in a district**.
2. Enter `USA` when prompted for the country code.
3. Enter any district name (e.g., `California`).
4. Observe repeated "Invalid country code" messages and failure to load districts.

---

## ✅ Expected behavior
- The system should recognize `USA` as a valid country code.
- It should call `lookupDAO.getDistrictsByCountryCode("USA")`.
- The district list should be displayed, allowing the user to select a district.

---

## 📸 Screenshots
If applicable, add screenshots showing the repeated "Invalid country code" error message.

---

## 💻 Desktop
- **OS:** Windows 11  
- **Browser:** IntelliJ embedded console / Jetty server  
- **Version:** Java 17, Maven Surefire 3.2.5  

---

## 📱 Smartphone
*(Not applicable for this bug — desktop only)*

---

## ➕ Additional context
- This issue appears to be caused by a mismatch between the test input (`USA`) and the mock data returned by `lookupDAO.getAllCountries()`.  
- The mock currently returns `new Lookup("Country", "United States")` instead of `new Lookup("USA", "United States")`.  
- Fixing the mock data or adjusting the input sequence should resolve the problem.

---

## 🏷️ Optional additional items
- **Issue default title:** `District Reports: Invalid country code error when using "USA"`  
- **Assignees:** Group7 team members working on DAO and MenuManager tests  
- **Labels:** `bug`, `tests`, `dao`, `menu-manager`
