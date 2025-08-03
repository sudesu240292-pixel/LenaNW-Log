#!/bin/bash

echo "LenaNW Log Sistemi Build Script"
echo "================================"

# Maven'ın yüklü olup olmadığını kontrol et
if ! command -v mvn &> /dev/null; then
    echo "Maven bulunamadı! Maven'ı yükleyin."
    echo "Ubuntu/Debian: sudo apt install maven"
    echo "CentOS/RHEL: sudo yum install maven"
    echo "macOS: brew install maven"
    exit 1
fi

# Java'nın yüklü olup olmadığını kontrol et
if ! command -v java &> /dev/null; then
    echo "Java bulunamadı! Java 8 veya üzerini yükleyin."
    echo "Ubuntu/Debian: sudo apt install openjdk-8-jdk"
    echo "CentOS/RHEL: sudo yum install java-1.8.0-openjdk"
    echo "macOS: brew install openjdk@8"
    exit 1
fi

echo "Maven ve Java bulundu, build başlıyor..."
echo

# Clean ve package
mvn clean package

if [ $? -ne 0 ]; then
    echo "Build başarısız! Hataları kontrol edin."
    exit 1
fi

echo
echo "Build başarılı!"
echo "JAR dosyası target/LenaNW-Log-1.0.0.jar konumunda oluşturuldu."
echo
echo "Kurulum için:"
echo "1. target/LenaNW-Log-1.0.0.jar dosyasını plugins/ klasörüne kopyalayın"
echo "2. Sunucuyu yeniden başlatın"
echo "3. config.yml dosyasını düzenleyin"
echo 