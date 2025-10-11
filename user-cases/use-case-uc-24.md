USE CASE: UC-24 Templates & Code of Conduct

CHARACTERISTIC INFORMATION  
Goal in Context  
As a Maintainer, I want to provide templates and a code of conduct so that contributions are consistent and respectful.

Scope  
Project Governance.

Level  
System-level task.

Preconditions
- Repository exists.
- Maintainer has write access.

Success End Condition  
Templates and code of conduct are available in `.github/`.

Failed End Condition  
Contributors lack guidance.

Primary Actor  
Maintainer.

Trigger  
Maintainer adds templates and conduct file.

MAIN SUCCESS SCENARIO
1. Maintainer creates issue and PR templates.
2. Maintainer adds `CODE_OF_CONDUCT.md`.
3. Contributors use templates for issues/PRs.
4. Community follows conduct guidelines.

EXTENSIONS
- Contributor ignores template: maintainer requests update.
- Code of conduct violation: maintainer enforces policy.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
