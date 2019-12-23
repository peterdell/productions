@echo off
REM Run without parameters to assemble the menu.
REM Run with parameter START to addtionally create a ".car" and ".atr" file and run the ".car" file.

REM Setup and paths.
cd C:\jac\system\Java\Programming\Repositories\Productions\com.wudsn.productions.atari800.thecartstudio\asm\thecart-menu
setlocal
set MADS=C:\jac\system\Atari800\Tools\ASM\MADS\mads.exe 
set OUT=..\..\tst\out\
set EXAMPLES=C:\jac\system\Atari800\Tools\ROM\TheCartStudio\Examples
REM set EMU=C:\jac\system\Atari800\Tools\EMU\Atari800\atari800.exe
set EMU=C:\jac\system\Atari800\Tools\EMU\Altirra\Altirra.exe  

REM Compile current ASM source. The "cartmenu.rom" is always taken from the ".jar" which again takes it from "data/cartmenu.rom" during build.
echo Compiling menu.
%MADS% CartMenu-Extended.asm -o:cartmenu-extended.rom -p -t:CartMenu-Extended.lab -l:CartMenu-Extended.lst
if ERRORLEVEL 1 goto :mads_error

REM Generate .CAR files from workbooks.
if NOT X%1==XSTART goto :eof

REM set WORKBOOK=CAR-Correct-Supported-1MB
set WORKBOOK=CAR-Correct-Supported-128MB
REM set WORKBOOK=CAR-Correct-Small-128MB
REM set WORKBOOK=CAR-Atarimax-Single
REM set WORKBOOK=Atarimax-Menus

REM Copy working versions from last ANT build to temp folder.
copy /Y %OUT%\TheCartStudio.jar %TEMP%
copy CartMenu-Extended.rom %TEMP%\cartmenu-extended.rom

REM Start the The!Cart Studio in console mode to export ".car" and ".atr" file.
echo Exporting workbook %WORKBOOK% as ".car" and ".atr" file
java -jar %TEMP%\TheCartStudio.jar -open:%EXAMPLES%\Workbooks\%WORKBOOK%.tcw -exportToCarImage:%EXAMPLES%\Exports\%WORKBOOK%.car -exportToAtrImage:%EXAMPLES%\Exports\%WORKBOOK%.atr
%EMU% %EXAMPLES%\Exports\%WORKBOOK%.car

goto :eof

:mads_error
echo ERROR: MADS compilation errors occurred. Check error messages above.
exit