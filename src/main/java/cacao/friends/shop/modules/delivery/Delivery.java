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
	
	@Enumerated(EnumType.STRING)
    private DeliveryStatus status;

}
