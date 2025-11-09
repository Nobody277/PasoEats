@echo off
setlocal enabledelayedexpansion
cls
echo 1. Push now with "update"
echo 2. Push with custom message
echo.
set/p choice=Enter your choice: 
if "%choice%"=="1" (
    git add .
    git commit -m "update"
    git push origin develop
) else if "%choice%"=="2" (
    set/p msg=Enter your message:
    git add .
    git commit -m "!msg!"
    git push origin develop
) else (
    echo Invalid choice
    pause
)
git commit -m "update"
git push origin develop
pause