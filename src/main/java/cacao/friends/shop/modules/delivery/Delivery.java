package cacao.friends.shop.modules.delivery;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import cacao.friends.shop.modules.address.Address;
import cacao.friends.shop.modules.address.form.AddressForm;
import cacao.friends.shop.modules.order.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder 
public class Delivery {
	
	@Id @GeneratedValue
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Orders order;
	
	@Embedded
	private Address address;
	
	private String name;		// 받는 사람 이름
	
	private String tel;			// 받는 사람 연란척
	
	private String requestDetails;	// 요청 사항
	
	@Enumerated(EnumType.STRING)
    private DeliveryStatus status;

	public void updateAddress(AddressForm addressForm) {
		this.address = new Address(addressForm.getCity(),
				addressForm.getStreet(), addressForm.getZipcode(), addressForm.getEtc());
	}

	public void cancel() {
		if(status == DeliveryStatus.READY)
			throw new RuntimeException("이미 배송 중이거나 배송 완료된 상품은 취소할 수 없습니다.");
	}

}
