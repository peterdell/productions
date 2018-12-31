echo on
setlocal
echo Example Script to run AtariROMMaker for all files in a folder and its subfolders
echo The AtariRomMaker.jar must be in the same folder as this script.
set /p FOLDER=Enter the folder:
if X"%FOLDER%"==X"" goto :eof

echo Converting files in %FOLDER% and its subfolders.
for /f "usebackq delims=|" %%f in (`dir /s /b "%FOLDER%\*.car"`) do (
  set FILE=%%f
  call :convert
 )
pause
goto :eof

:convert
 echo Converting %FILE%
 java -jar AtariRomMaker.jar -load:"%FILE%" -convertToROM -save:"%FILE%.rom"
goto :eof


