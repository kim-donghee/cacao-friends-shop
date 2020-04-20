package cacao.friends.shop.modules.order;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cacao.friends.shop.modules.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "orders")
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder 
public class Orders {
	
	@Id @GeneratedValue
	private Long id;
	
	@Lob
	private String image;				// 주문 상품 이미지
	
	@Column(nullable = false)
	private String name;				// 주문 이름 (상품 여러 개일 경우 A상품 외 2개)
	
	private boolean orderUserAccounted;	// 로그인 여부
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;
	
	private String email;				// 비 로그인시 이메일
	
	@OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private Set<OrdersItem> ordersItems = new HashSet<>();
	
	//===비즈니스 로직===//
	public void addItem(OrdersItem ordersItem) {
		ordersItems.add(ordersItem);
		ordersItem.setOrders(this);
	}
	
	public void removeItem(OrdersItem ordersItem) {
		ordersItems.remove(ordersItem);
		ordersItem.setOrders(null);
	}
	
	// 주문
	public void order() {
		if(this.ordersItems.isEmpty()) {
			throw new RuntimeException("주문은 하나 이상의 아이템이 필요합니다.");
		}
		
		this.ordersItems.forEach(oi -> {
			oi.order();
		});
		
		int itemCount = this.ordersItems.size();
		OrdersItem ordersItem = this.ordersItems.iterator().next();
		String firstItemName = ordersItem.getItemName();
		
		if(itemCount > 1) {
			this.name = firstItemName + " 외 " + itemCount + " 개";
		}
		else {
			this.name = firstItemName;
		}
		
		this.orderStatus = OrderStatus.ORDER;
	}
	
	// 주문 취소
	public void cancel() {
		this.ordersItems.forEach(oi -> {
			oi.cancel();
		});
		
		orderStatus = OrderStatus.CANCEL;
	}

}
