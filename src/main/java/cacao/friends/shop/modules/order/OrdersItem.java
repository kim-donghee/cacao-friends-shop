package cacao.friends.shop.modules.order;

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
public class OrdersItem {
	
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Orders orders;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Item item;
	
	private Integer price;				// 상품 하나 가격

	private Integer quantity;			// 주문 갯수
	
	private String itemName;			// 상품 이름
	
	public OrdersItem(Item item, Integer quantity) {
		this.item = item;
		this.price = item.getPrice();
		this.quantity = quantity;
		this.itemName = item.getName();
	}
	
	//===비즈니스 로직====//
	public void order() {
		this.item.removeStock(this.quantity);
	}
	
	public void cancel() {
		this.item.addStock(this.quantity);
	}
	
	public Integer getTotalPrice() {
		return this.price * this.quantity;
	}
	
	public String getItemMainBanner() {
		return item.getMainBanner();
	}

}
