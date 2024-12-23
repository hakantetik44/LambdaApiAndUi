# LambdaTest UI ve API Test Otomasyonu

Bu proje, LambdaTest e-ticaret web sitesinin UI ve API testlerini içeren bir test otomasyon framework'üdür.

## Özellikler

- Selenium WebDriver ile UI testleri
- REST Assured ile API testleri
- Cucumber BDD framework'ü
- Allure raporlama
- Video kaydı özelliği
- Page Object Model (POM) tasarım deseni
- Paralel test çalıştırma desteği

## Teknolojiler

- Java 17
- Maven
- Selenium WebDriver 4.15.0
- REST Assured 5.3.2
- Cucumber 7.14.0
- Allure Reports 2.24.0
- Monte Screen Recorder (Video kaydı için)
- JAVE (Video dönüştürme için)

## Önkoşullar

- Java 17 veya üzeri
- Maven
- Chrome tarayıcı

## Kurulum

1. Projeyi klonlayın:
```bash
git clone [proje-url]
```

2. Bağımlılıkları yükleyin:
```bash
mvn clean install -DskipTests
```

## Testleri Çalıştırma

### Tüm Testleri Çalıştırma
```bash
mvn clean test
```

### UI Testlerini Çalıştırma
```bash
mvn clean test -Dcucumber.filter.tags="@ui"
```

### API Testlerini Çalıştırma
```bash
mvn clean test -Dcucumber.filter.tags="@api"
```

## Test Senaryoları

### UI Test Senaryoları
1. Ürün Arama:
   - Anasayfaya gitme
   - Ürün arama
   - Sonuçları kontrol etme

2. Kategori Görüntüleme:
   - Anasayfaya gitme
   - Kategoriler menüsünü açma
   - Kategori listesini kontrol etme

### API Test Senaryoları
1. Ürün Arama API'si:
   - Arama endpoint'ine GET isteği
   - Durum kodu kontrolü
   - Yanıt içeriği kontrolü

2. Kategori Listeleme API'si:
   - Kategori endpoint'ine GET isteği
   - Durum kodu kontrolü
   - Kategori içeriği kontrolü

## Proje Yapısı

```
src
├── test
│   ├── java
│   │   ├── pages           # Page Object Model sınıfları
│   │   │   └── LambdaTestPage.java
│   │   ├── stepdefinitions # Step definition dosyaları
│   │   │   ├── ApiSteps.java
│   │   │   ├── Hooks.java
│   │   │   └── LambdaTestUISteps.java
│   │   ├── utilities      # Yardımcı sınıflar
│   │   │   ├── ConfigReader.java
│   │   │   └── VideoRecorder.java
│   │   └── runners        # Test runner sınıfları
│   │       └── TestRunner.java
│   └── resources
│       ├── features      # Cucumber feature dosyaları
│       │   └── LambdaTest.feature
│       └── config.properties
```

## Raporlama

Test sonuçlarını Allure raporu olarak görüntülemek için:
```bash
mvn allure:serve
```

## Video Kaydı

- UI testleri sırasında otomatik olarak video kaydı alınır
- Videolar `target/videos` klasöründe saklanır
- Başarısız testlerin videoları Allure raporuna eklenir

## Katkıda Bulunma

1. Fork edin
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Değişikliklerinizi commit edin (`git commit -m 'feat: Add amazing feature'`)
4. Branch'inizi push edin (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun
