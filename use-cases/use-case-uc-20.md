USE CASE: UC-20 Build Self-Contained JAR

CHARACTERISTIC INFORMATION  
Goal in Context  
As a DevOps Engineer, I want the project to build a single executable JAR so that it can run consistently in any environment.

Scope  
DevOps Infrastructure.

Level  
System-level task.

Preconditions
- Maven and dependencies are configured.

Success End Condition  
JAR is built and stored in `target/`.

Failed End Condition  
Build fails or JAR is not generated.

Primary Actor  
DevOps Engineer.

Trigger  
Developer runs Maven build.

MAIN SUCCESS SCENARIO
1. Developer runs `mvn -DskipTests package assembly:single`.
2. System compiles code and packages dependencies.
3. JAR is created in `target/`.
4. README includes build instructions.

EXTENSIONS
- Build error: system logs and exits with error code.

SUB-VARIATIONS  
None.

SCHEDULE  
DUE DATE: Release 1.0
