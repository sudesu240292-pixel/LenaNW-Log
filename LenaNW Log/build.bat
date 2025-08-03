@echo off
echo LenaNW Log Sistemi Build Script
echo ================================

REM Maven'ın yüklü olup olmadığını kontrol et
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo Maven bulunamadı! Maven'ı yükleyin veya PATH'e ekleyin.
    echo https://maven.apache.org/download.cgi adresinden indirebilirsiniz.
    pause
    exit /b 1
)

REM Java'nın yüklü olup olmadığını kontrol et
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo Java bulunamadı! Java 8 veya üzerini yükleyin.
    echo https://adoptium.net/ adresinden indirebilirsiniz.
    pause
    exit /b 1
)

echo Maven ve Java bulundu, build başlıyor...
echo.

REM Clean ve package
mvn clean package

if %errorlevel% neq 0 (
    echo Build başarısız! Hataları kontrol edin.
    pause
    exit /b 1
)

echo.
echo Build başarılı!
echo JAR dosyası target/LenaNW-Log-1.0.0.jar konumunda oluşturuldu.
echo.
echo Kurulum için:
echo 1. target/LenaNW-Log-1.0.0.jar dosyasını plugins/ klasörüne kopyalayın
echo 2. Sunucuyu yeniden başlatın
echo 3. config.yml dosyasını düzenleyin
echo.
pause 