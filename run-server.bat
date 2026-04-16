@echo off
setlocal EnableExtensions EnableDelayedExpansion

set "JAR_NAME=rongthanonline-0.0.1-SNAPSHOT.jar"
set "SERVER_DIR=%~dp0server"
set "EXIT_CODE=0"
set "SERVER_PID="
set "JAVA_BIN="
set "JCMD_BIN=jcmd"
set "CPANEL_URL=http://localhost:707/cpanel/index.html?v=20260416b"

if not exist "%SERVER_DIR%\mvnw.cmd" (
    echo [ERROR] Khong tim thay thu muc server hoac file mvnw.cmd.
    set "EXIT_CODE=1"
    goto :end
)

cd /d "%SERVER_DIR%"

set "JAVA_HOME=C:\Program Files\Java\jdk-19"
if not exist "%JAVA_HOME%\bin\java.exe" (
    echo [ERROR] Khong tim thay JDK 19 tai: "%JAVA_HOME%"
    set "EXIT_CODE=1"
    goto :end
)
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "JAVA_BIN=%JAVA_HOME%\bin\java.exe"
set "JCMD_BIN=jcmd"

if defined DB_PASSWORD (
    set "SPRING_DATASOURCE_PASSWORD=%DB_PASSWORD%"
) else if not defined SPRING_DATASOURCE_PASSWORD (
    set "SPRING_DATASOURCE_PASSWORD=admin"
)
set "SPRING_DATASOURCE_USERNAME=root"
set "SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/rto"

for /f "tokens=1" %%I in ('jcmd -l 2^>nul ^| findstr /i "%JAR_NAME%"') do set "SERVER_PID=%%I"
if defined SERVER_PID echo Existing server process detected (PID %SERVER_PID%). Stopping it before rebuild...
if defined SERVER_PID taskkill /PID %SERVER_PID% /F >nul 2>&1

echo [1/2] Building latest server jar...
call ".\mvnw.cmd" -q -DskipTests package
if errorlevel 1 goto :build_failed

echo [2/2] Starting server...
start "" powershell -NoProfile -ExecutionPolicy Bypass -Command "$base='%CPANEL_URL%'; for ($i=0; $i -lt 60; $i++) { try { $resp=Invoke-WebRequest -UseBasicParsing 'http://localhost:707/cpanel' -TimeoutSec 2; if ($resp.StatusCode -eq 200) { Start-Process ($base + '&t=' + [DateTimeOffset]::Now.ToUnixTimeMilliseconds()); break } } catch {}; Start-Sleep -Milliseconds 1000 }"
"%JAVA_BIN%" -Dfile.encoding=UTF-8 -jar ".\target\%JAR_NAME%"
set "EXIT_CODE=%ERRORLEVEL%"
if not "%EXIT_CODE%"=="0" echo Server stopped with error code %EXIT_CODE%.
goto :end

:build_failed
echo.
echo Build failed. Server was not started.
set "EXIT_CODE=1"

:end
pause
endlocal & exit /b %EXIT_CODE%
