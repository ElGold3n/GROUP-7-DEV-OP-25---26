---
name: Feature request
about: Suggest an idea for this project
title: ''
labels: 'DevOps, Feature: CI/CD'
assignees: whashby, ElGold3n

---

# Issue: Feature Request
Suggest an idea for this project

---

## 💡 Is your feature request related to a problem? Please describe.
Currently, when running reports (e.g., countries, cities, capitals), users must manually re‑enter parameters each time they navigate back to the menu.  
This is frustrating because it slows down testing and makes the CLI less user‑friendly.

---

## ✅ Describe the solution you'd like
Add a **“repeat last query” option** in the menu system.  
- After running a report, the user can select a shortcut to re‑run the same query without re‑entering parameters.  
- This would improve usability and speed up both manual testing and demonstration workflows.

---

## 🔄 Describe alternatives you've considered
- Allow saving queries to a history file and re‑loading them later.  
- Add command‑line arguments to bypass the menu entirely for automated runs.  
- Provide a “favorites” menu for commonly used queries.

---

## 📄 Additional context
This feature would be especially useful during integration testing and CI/CD runs, where reproducibility and speed are critical.  
It also aligns with our course focus on automation and reducing manual errors.

---

## 🏷️ Optional additional items
- **Issue default title:** `Feature: Add "repeat last query" option to menu system`  
- **Assignees:** Group7 team members working on MenuManager and CLI usability  
- **Labels:** `feature`, `enhancement`, `menu-manager`, `usability`
