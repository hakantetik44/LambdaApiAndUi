package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardInfo {
    private String cardNumber;
    private String expireDate;
    private String cvv;
    private String cardHolderName;
    
    // Convenience constructor for required fields only
    public CardInfo(String cardNumber, String expireDate, String cvv) {
        this.cardNumber = cardNumber;
        this.expireDate = expireDate;
        this.cvv = cvv;
    }
}
