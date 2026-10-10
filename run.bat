@echo off
setlocal
echo =========================================================================
echo    Student Services Desktop App (Register Student Feature)
echo    Isaac Miti (202509238) ^| ICT261 Advanced Java Programming
echo    Lecturer: Mr. E. Nyirenda ^| Mulungushi University
echo =========================================================================
echo.

set M2_REPO=%USERPROFILE%\.m2\repository

set CP=target\classes;^
%M2_REPO%\org\openjfx\javafx-controls\21.0.2\javafx-controls-21.0.2-win.jar;^
%M2_REPO%\org\openjfx\javafx-controls\21.0.2\javafx-controls-21.0.2.jar;^
%M2_REPO%\org\openjfx\javafx-graphics\21.0.2\javafx-graphics-21.0.2-win.jar;^
%M2_REPO%\org\openjfx\javafx-graphics\21.0.2\javafx-graphics-21.0.2.jar;^
%M2_REPO%\org\openjfx\javafx-base\21.0.2\javafx-base-21.0.2-win.jar;^
%M2_REPO%\org\openjfx\javafx-base\21.0.2\javafx-base-21.0.2.jar;^
%M2_REPO%\org\openjfx\javafx-fxml\21.0.2\javafx-fxml-21.0.2-win.jar;^
%M2_REPO%\org\openjfx\javafx-fxml\21.0.2\javafx-fxml-21.0.2.jar;^
%M2_REPO%\com\h2database\h2\2.2.224\h2-2.2.224.jar

echo [1/2] Launching Student Services App directly with Java 21...
java -cp "%CP%" com.studentservices.Launcher

if %ERRORLEVEL% NEQ 0 (
    echo [2/2] Direct launch exited with error. Trying Maven runner...
    call "%~dp0mvn.cmd" compile javafx:run
)

pause
