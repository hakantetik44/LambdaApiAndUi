#!/bin/bash

echo "🧹 Önceki test sonuçları temizleniyor..."
mvn clean

echo "🚀 Testler çalıştırılıyor..."
mvn test

echo "📊 Allure raporu oluşturuluyor ve açılıyor..."
mvn allure:serve
