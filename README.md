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

## Kurulum ve Kullanım Talimatları

### Java 17 JDK Kurulumu

```bash
brew install openjdk@17
```

### Maven Kurulumu

```bash
brew install maven
```

### Allure Command Line Tool Kurulumu

```bash
brew install allure
```

### Projeyi Klonlama

```bash
git clone [proje-url]
cd LambdaApiAndUi
```

### Maven Bağımlılıkları Yüklenmesi

```bash
mvn clean install
```

### Testlerin Çalıştırılması

#### Tüm Testleri Çalıştırma

```bash
mvn clean test
```

#### Belirli Bir Tag İle Testleri Çalıştırma

```bash
mvn clean test -Dcucumber.filter.tags="@OrderAPI"
```

### Allure Raporu Oluşturma

#### Testleri Çalıştırma

```bash
mvn clean test
```

#### Allure Raporunu Oluşturma

```bash
allure generate allure-results --clean -o allure-report
```

#### Raporu Görüntüleme

```bash
allure serve allure-results
```

### Proje Yapısı

```
src
├── test
│   ├── java
│   │   ├── models          # Request/Response modelleri
│   │   ├── stepdefinitions # Cucumber step tanımlamaları
│   │   └── utils          # Yardımcı sınıflar
│   └── resources
│       ├── features       # Cucumber feature dosyaları
│       └── config         # Konfigürasyon dosyaları
```

### Test Raporları

Allure raporları aşağıdaki bilgileri içerir:
- Test senaryolarının durumu (Başarılı/Başarısız)
- Çalışma süresi
- Hata detayları
- Screenshot'lar (UI testleri için)
- Request/Response detayları (API testleri için)

### Notlar

- API testleri için config.properties dosyasında base URL ve diğer yapılandırmaları ayarlayın
- UI testleri için WebDriver yapılandırmasını kontrol edin
- Test verilerini feature dosyalarında güncelleyin

### Hızlı Test Çalıştırma (run-tests.sh)

Projenin kök dizininde bulunan `run-tests.sh` script'i, test sürecini otomatize etmek için kullanılabilir. Bu script:

1. Önceki test sonuçlarını temizler
2. Testleri çalıştırır
3. Allure raporunu oluşturur ve otomatik olarak açar

Script'i çalıştırmak için:
```bash
# Önce script'i çalıştırılabilir yapın
chmod +x run-tests.sh

# Script'i çalıştırın
./run-tests.sh
```

### Önemli Noktalar

1. **Paralel Test Çalıştırma**:
   - TestNG XML dosyasında parallel attribute'u kullanılarak testler paralel çalıştırılabilir
   - Thread count ayarlanarak aynı anda çalışacak test sayısı belirlenebilir

2. **Test Verileri**:
   - Test verileri Cucumber feature dosyalarında saklanır
   - Hassas veriler (API anahtarları, şifreler) config.properties dosyasında tutulmalıdır

3. **Cross Browser Testing**:
   - WebDriver yapılandırması sayesinde farklı tarayıcılarda (Chrome, Firefox, Safari) testler çalıştırılabilir
   - Browser seçimi config.properties dosyasından yapılabilir

4. **Raporlama**:
   - Allure raporları test sonuçlarını detaylı bir şekilde gösterir
   - Her test için:
     * Test adımları ve durumları
     * Çalışma süresi
     * Screenshot'lar
     * API request/response detayları
     * Hata mesajları ve stack trace
   - Raporlar filtrelenebilir ve kategorize edilebilir

5. **CI/CD Entegrasyonu**:
   - Jenkins, GitLab CI veya GitHub Actions ile entegre edilebilir
   - Her commit sonrası otomatik test çalıştırma ayarlanabilir
   - Test sonuçları ve raporlar otomatik olarak paylaşılabilir

6. **Hata Ayıklama**:
   - Test logları `target/logs` dizininde tutulur
   - Her test çalışması için ayrı log dosyası oluşturulur
   - Log seviyesi config.properties dosyasından ayarlanabilir

7. **Maintenance**:
   - Page Object Model kullanılarak UI elementleri merkezi olarak yönetilir
   - API endpoint'leri ve request/response modelleri ayrı paketlerde tutulur
   - Test verileri feature dosyalarında saklanarak kolay güncelleme sağlanır

### Best Practices

1. Her test bağımsız olmalı ve kendi test verilerini oluşturmalı
2. Test sonrası temizlik işlemleri yapılmalı
3. Assertion mesajları açıklayıcı olmalı
4. Screenshot'lar kritik adımlarda alınmalı
5. API testlerinde şema validasyonu yapılmalı
6. Timeout değerleri config dosyasında tutulmalı
7. Paralel test çalıştırmada thread-safe implementasyon kullanılmalı
