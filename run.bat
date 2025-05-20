@echo off

REM Membuat folder output
@REM mkdir bin

REM Compile semua file .java dari src dan subfolder
javac -d bin src\Main.java src\model\*.java src\parser\*.java src\solver\*.java src\heuristic\*.java

REM Jalankan program dengan classpath ke folder bin
java -cp bin Main
