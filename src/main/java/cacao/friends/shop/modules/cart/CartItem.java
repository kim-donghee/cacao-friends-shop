package cacao.friends.shop.modules.cart;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import cacao.friends.shop.modules.item.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder 
public class CartItem {
	
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Item item;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Cart cart;
	
	@Column(nullable = false)
	private Integer quantity;
	
	//===비즈니스 로직===//
	public int getPrice() {
		return item.getPrice();
	}
	
	public String getName() {
		return item.getName();
	}
	
	public String getShortDescript() {
		return item.getShortDescript();
	}
	
	public String getMainBanner() {
		return item.getMainBanner();
	}
	
	public void changeQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
