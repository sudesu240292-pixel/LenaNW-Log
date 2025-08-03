@echo off
echo LenaNW Log Sistemi - Docker Build Script
echo =========================================

REM Docker'ın yüklü olup olmadığını kontrol et
docker --version >nul 2>nul
if %errorlevel% neq 0 (
    echo Docker bulunamadı! Docker Desktop'ı yükleyin.
    echo https://www.docker.com/products/docker-desktop/ adresinden indirebilirsiniz.
    pause
    exit /b 1
)

echo Docker bulundu, build başlıyor...
echo.

REM Docker image oluştur ve JAR dosyasını çıkar
docker build -t lenanw-log-builder .
docker run --rm -v "%cd%\output:/output" lenanw-log-builder

if %errorlevel% neq 0 (
    echo Build başarısız! Hataları kontrol edin.
    pause
    exit /b 1
)

echo.
echo Build başarılı!
echo JAR dosyası output/LenaNW-Log-1.0.0.jar konumunda oluşturuldu.
echo.
echo Kurulum için:
echo 1. output/LenaNW-Log-1.0.0.jar dosyasını plugins/ klasörüne kopyalayın
echo 2. Sunucuyu yeniden başlatın
echo 3. config.yml dosyasını düzenleyin
echo.
pause 