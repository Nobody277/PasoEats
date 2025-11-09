@echo off
cls
echo Merging develop into main...
echo.
echo Checking if develop is up to date...
git checkout develop
git fetch origin develop
git status -uno
echo.
set /p confirm=Have you pushed all changes to develop? (y/n): 
if /i not "%confirm%"=="y" (
    echo Please push to develop first using push.bat
    pause
    exit /b
)
echo.
echo Switching to main...
git checkout main
git pull origin main
echo.
echo Merging develop into main...
git merge develop
echo.
echo Pushing to main...
git push origin main
echo.
echo Merge complete! Switching back to develop...
git checkout develop
pause