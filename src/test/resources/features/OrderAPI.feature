@OrderAPI
Feature: Sipariş API'si Testleri

  @TC_ORDER_001
  Scenario: Başarılı Sipariş Oluşturma
    Given Kullanıcı API için giriş yapar
    When Sepete ürün eklenir
      | productId | 1     |
      | quantity  | 2     |
      | color     | BLACK |
    And Sipariş oluşturulur
      | fullName      | Mehmet Yılmaz       |
      | streetAddress | Atatürk Cad. No:123 |
      | city          | İstanbul            |
      | district      | Kadıköy             |
      | zipCode       | 34700               |
      | paymentMethod | CREDIT_CARD         |
      | cardNumber    | 4532111111111112    |
      | expireDate    | 12/24               |
      | cvv           | 123                 |
    Then Sipariş başarıyla oluşturulmalı
    And Sipariş detayları doğru olmalı
      | status      | PENDING |
      | itemCount   | 1       |
      | totalAmount | >0      |
