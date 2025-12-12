@echo off
echo Running DemoMilestone2...
echo.

cd /d "%~dp0"

REM Compile the Java files
javac -d target/classes -sourcepath src/main/java src/main/java/model/*.java src/main/java/controller/*.java

REM Run the demo
java -cp target/classes controller.DemoMilestone2

echo.
pause

