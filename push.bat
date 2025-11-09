@echo off
setlocal enabledelayedexpansion
cls
echo Pushing to develop branch...
echo.
git checkout develop
echo.
echo 1. Push with "update" message
echo 2. Push with custom message
echo.
set /p choice=Enter your choice (1 or 2): 
echo.
if "%choice%"=="1" (
    git add .
    git commit -m "update"
    git push origin develop
) else if "%choice%"=="2" (
    set /p msg=Enter your commit message: 
    git add .
    git commit -m "!msg!"
    git push origin develop
) else (
    echo Invalid choice
)
pause