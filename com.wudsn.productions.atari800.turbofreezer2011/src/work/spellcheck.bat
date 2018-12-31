SET DETEX=C:\jac\system\PC\Tools\TEX\detex.exe

SET PLAIN=..\FreezerManualDe.txt
cd de
call :tex2plain
cd ..

SET PLAIN=..\FreezerManualEn.txt
cd en
call :tex2plain
cd ..
goto :eof

:tex2plain
echo >%PLAIN%
%DETEX% bookinfo.tex >>%PLAIN%
%DETEX% cartemu.tex >>%PLAIN%
%DETEX% debugger.tex >>%PLAIN%
%DETEX% dos-debugger.tex >>%PLAIN%
%DETEX% dos.tex >>%PLAIN%
%DETEX% flash-rom.tex >>%PLAIN%
%DETEX% installation.tex >>%PLAIN%
%DETEX% links.tex >>%PLAIN%
%DETEX% oldrunner.tex >>%PLAIN%
%DETEX% preface.tex >>%PLAIN%
%DETEX% ram-extension.tex >>%PLAIN%
%DETEX% techinfo.tex >>%PLAIN%
%DETEX% titlepage.tex >>%PLAIN%
%DETEX% usage.tex >>%PLAIN%
%DETEX% warranty.tex >>%PLAIN%
goto :eof