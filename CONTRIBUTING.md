# Contributing Guidelines

Welcome to the **World Reporting System** project by **Group7**  
SET09803 2025‑6 TR1 001 – DevOps Global Online

We are building this project collaboratively as part of our DevOps coursework.  
These guidelines ensure consistency, reproducibility, and professionalism across all contributions.

---

## 👥 Who Can Contribute
- Members of **Group7** only.
- Each member is expected to contribute code, documentation, and testing.
- Collaboration should follow agreed workflows and respect peer review.

---

## 📋 Workflow

1. **Fork & Clone**
   - Fork the repository to your own GitHub account.
   - Clone your fork locally:
     ```bash
     git clone https://github.com/<your-username>/world-reporting-system.git
     ```

2. **Branching**
   - Create a new branch for each feature or fix:
     ```
     feature/add-language-endpoint
     fix/sql-ordering-bug
     test/increase-menu-coverage
     ```
   - Branch from `main`.

3. **Coding Standards**
   - Use **Java 17**.
   - Follow DAO patterns for database access.
   - Ensure reproducibility in builds (Docker, CI/CD).
   - Document public methods with Javadoc.

4. **Testing**
   - Write **JUnit 5** unit tests with **Mockito**.
   - Integration tests should use **Failsafe** (`*IT.java`).
   - Cover edge cases, negative scenarios, and resource handling.
   - Target ≥70% branch coverage for `MenuManager`.

5. **Commit Messages**
   - Use clear, conventional commits:
     ```
     feat: add endpoint for regional capital cities
     fix: normalize continent ordering in SQL queries
     test: add negative case for invalid district input
     docs: update README with CI/CD instructions
     ```

6. **Pull Requests**
   - Push your branch to your fork.
   - Open a PR against `main`.
   - Include:
     - Summary of changes
     - Testing performed
     - Impact on CI/CD or deployment
     - Notes for reviewers

---

## 🧪 Testing Standards
- Run unit tests:
  ```bash
  mvn clean test
