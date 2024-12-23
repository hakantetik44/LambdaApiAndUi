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
    When "/index.php?route=product/search&search=iphone" adresine GET isteği gönderiyorum
    Then Yanıt durum kodu 200 olmalı
    And Yanıt "iPhone" ürünlerini içermeli

  @api
  Scenario: API - Kategori Ürünlerini Görüntüleme
    When "/index.php?route=product/category&path=33" adresine GET isteği gönderiyorum
    Then Yanıt durum kodu 200 olmalı
    And Yanıt "camera" ürünlerini içermeli
