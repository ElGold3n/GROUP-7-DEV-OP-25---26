USE CASE: UC-22 GitHub Actions CI/CD

CHARACTERISTIC INFORMATION  
Goal in Context  
As a DevOps Engineer, I want GitHub Actions to build and test the project automatically so that integration is continuous and reliable.

Scope  
DevOps Infrastructure.

Level  
System-level task.

Preconditions
- GitHub Actions workflow file exists.
- Build and test scripts defined.

Success End Condition  
Build and tests run automatically on push/PR.

Failed End Condition  
Workflow fails or does not trigger.

Primary Actor  
DevOps Engineer.

Trigger  
Code is pushed or pull request opened.

MAIN SUCCESS SCENARIO
1. Developer pushes code.
2. GitHub Actions triggers workflow.
3. Workflow builds JAR and Docker image.
4. Workflow runs unit tests.
5. Workflow reports status on GitHub.

EXTENSIONS
- Build/test failure: workflow reports failure.
- Workflow misconfigured: system logs error.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
