@echo off
cd /d "%~dp0"
echo Compilando o programa...
javac Auto_git.java
if %errorlevel% neq 0 (
    echo Erro na compilação!
    pause
    exit /b
)
echo Iniciando o programa...
java Auto_git
pause