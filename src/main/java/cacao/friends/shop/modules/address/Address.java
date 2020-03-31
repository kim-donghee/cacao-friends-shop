package cacao.friends.shop.modules.address;

import java.util.Objects;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable 
@Getter 
@NoArgsConstructor @AllArgsConstructor @Builder
public class Address {
	
	private String city;
	private String street;
	private String zipcode;
	private String etc;		// 상세 주소
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Address other = (Address) obj;
        return Objects.equals(city, other.getCity()) && 
        		Objects.equals(street, other.getStreet()) && 
        		Objects.equals(zipcode, other.getZipcode()) && 
        		Objects.equals(etc, other.getEtc());
    }

}
