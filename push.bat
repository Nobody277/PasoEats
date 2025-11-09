@echo off
setlocal enabledelayedexpansion
cls
echo Available branches:
echo.
git branch
echo.
set /p branch=Enter branch name to push to (1 or 2): 
echo.
echo 1. Push with "update" message
echo 2. Push with custom message
echo.
set /p choice=Enter your choice (1 or 2): 
echo.
if "%choice%"=="1" (
    git add .
    git commit -m "update"
    git push origin %branch%
) else if "%choice%"=="2" (
    set /p msg=Enter your commit message: 
    git add .
    git commit -m "!msg!"
    git push origin %branch%
) else (
    echo Invalid choice
)
pause