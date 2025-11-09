@echo off
cls
echo Creating Pull Request from develop to main...
echo.
echo Checking if develop is up to date...
git checkout develop
git fetch origin develop
git status -uno
echo.
set /p confirm=Is develop branch up to date? (y/n): 
if /i not "%confirm%"=="y" (
    echo Push to develop first with push.bat
    pause
)
echo.
set /p title=Enter PR title: 
set /p description=Enter PR description: 
echo.
echo Creating pull request...
gh pr create --base main --head develop --title "%title%" --body "%description%"
pause