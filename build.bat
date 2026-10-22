@echo off
setlocal
chcp 65001 >nul
cd /d "%~dp0"

if not exist "cls" mkdir "cls"
if not exist "events" mkdir "events"

type nul > sources.tmp
for /r "src" %%F in (*.java) do echo "%%F">>sources.tmp
javac -encoding UTF-8 -d cls @sources.tmp
set "RESULTADO=%errorlevel%"
del sources.tmp
if not "%RESULTADO%"=="0" exit /b %RESULTADO%

java -Dfile.encoding=UTF-8 -cp cls App
exit /b %errorlevel%
