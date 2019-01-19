@echo off

REM Setup and paths.
cd C:\jac\system\Java\Programming\Repositories\Productions\com.wudsn.productions.atari800.thecartstudio\asm\thecart-studio
setlocal
set MADS=C:\jac\system\Atari800\Tools\ASM\MADS\mads.exe
set TARGET=..\..\src\data
set BINARY=..\..\bin\data
call :mads Atari800
call :mads Atari5200
del %TARGET%\*.lab 2>nul
del %TARGET%\*.lst 2>nul
copy /Y %TARGET%\CartridgeTypeSampleCreator-*.rom %BINARY%
goto :eof

:mads
set ROM=CartridgeTypeSampleCreator-%1.rom
echo Creating %ROM%.
%MADS% CartridgeTypeSampleCreator-%1.asm -o:%TARGET%\%ROM%
if ERRORLEVEL 1 goto :mads_error
goto :eof

:mads_error
echo ERROR: MADS compilation errors occurred. Check error messages above.
exit

