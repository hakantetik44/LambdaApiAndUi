Feature: LambdaTest E-ticaret Testleri

  @ui
  Scenario: Ürün arama
    Given LambdaTest anasayfasındayım
    When "iphone" için arama yaparım
    Then Arama sonuçları görüntülenir
    And Sonuçlarda "iPhone" bulunur

  @ui
  Scenario: Ürün kategorilerini görüntüleme
    Given LambdaTest anasayfasındayım
    When Kategoriler menüsünü görüntülerim
    Then Tüm ürün kategorileri listelenir
    And "Mega Menu" kategorisi bulunur

  @api
  Scenario: API - Ürün Arama
    Given API testi için request hazırlanır
    When API endpoint'ine "iphone" araması için GET isteği gönderilir
    Then API yanıtı başarılı olmalıdır
    And Arama yanıtı "iPhone" ürünlerini içermeli

  @api
  Scenario: API - Kategori Ürünlerini Görüntüleme
    Given API testi için request hazırlanır
    When API endpoint'ine GET isteği gönderilir
    Then API yanıtı başarılı olmalıdır
    And Arama yanıtı "camera" ürünlerini içermeli

  @api @negative
  Scenario: API - Geçersiz Kategori ID ile Hata Kontrolü
    Given API testi için request hazırlanır
    When API endpoint'ine geçersiz kategori ID'si ile GET isteği gönderilir
    Then API yanıtı 404 hata kodu dönmeli
    And Yanıt hata mesajı içermeli
