package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String fullName;
    private String streetAddress;
    private String city;
    private String district;
    private String zipCode;
    private String country;
    private String phone;
    
    // Convenience constructor for required fields only
    public Address(String fullName, String streetAddress, String city, String district, String zipCode) {
        this.fullName = fullName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.district = district;
        this.zipCode = zipCode;
        this.country = "Turkey"; // Default value
    }
}
