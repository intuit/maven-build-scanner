REM setup.bat
@echo off
setlocal

echo ----------------- 1/3 Starting local containers (web server mongo db) ------------------------------
call docker-compose up --build -d

echo ----------------- 2/3 Building Maven extension and copying to - {maven.home}/lib/ext ---------------
call mvn package -DskipTests

echo ----------------- finding maven home...
call :GETMAVENHOME 
set /p mvn-home=<mvn-home.txt
del mvn-home.txt
set mvn-home=%mvn-home:..=%
set mvn-ext-home=%mvn-home%lib\ext
set mvn-ext-home=%mvn-ext-home:bin\=%
set mvn-scanner-jar=maven-build-scanner-1.0.0-SNAPSHOT-jar-with-dependencies.jar
echo ----------------- copy jar to maven extension directory...
ROBOCOPY %cd%\target\ %mvn-ext-home% %mvn-scanner-jar%

echo ----------------- 3/3 Creating your first scan -----------------------------------------------------
call :SETENV
call mvn install -DskipTests

echo Ready

echo To perform a scan, run:
echo ----------------------------------------------------
echo - set MAVEN_BUILD_SCANNER 1                        -
echo - mvn "<goals>"                                    -
echo ----------------------------------------------------
echo Open http://localhost:3000 to see scan results.
echo ----------------------------------------------------

Goto :Eof

:SETENV
call set MAVEN_BUILD_SCANNER 1
exit /b 0

:GETMAVENHOME
echo -------- saving maven home to temp file...
call mvn help:evaluate -Dexpression=maven.home -DforceStdout -q > mvn-home.txt
exit /b 0 