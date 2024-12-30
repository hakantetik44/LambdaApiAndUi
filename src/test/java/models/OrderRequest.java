package models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRequest {
    private String cartId;
    private Address shippingAddress;
    private String paymentMethod;
    private CardInfo cardInfo;
    
    // Ek alanlar eklenebilir
    private String notes;
    private boolean giftWrap;
    private String couponCode;
}
