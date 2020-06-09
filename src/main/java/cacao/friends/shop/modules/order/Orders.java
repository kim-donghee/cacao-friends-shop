package cacao.friends.shop.modules.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import cacao.friends.shop.modules.delivery.Delivery;
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
	
	//===기본 정보===//
	@Lob
	private String image;				// 주문 상품 이미지

	private String name;				// 주문 이름 (상품 여러 개일 경우 A상품 외 2개)
	
//	private boolean orderUserAccounted;	// 로그인 여부
//	
//	private String email;
	
	private LocalDateTime orderedAt;	// 주문 일시
	
	private LocalDateTime canceledAt;	// 주문 취소 일시
	
	//=== 연관관계 ===//
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;
	
	@OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Delivery delivery;			// 배송
	
	@OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<OrdersItem> ordersItems = new ArrayList<>();
	
	//===비즈니스 로직===//
	public void addItem(OrdersItem ordersItem) {
		if(this.ordersItems.contains(ordersItem))
			return;
		this.ordersItems.add(ordersItem);
		ordersItem.setOrders(this);
	}
	
	public void removeItem(OrdersItem ordersItem) {
		if(!this.ordersItems.contains(ordersItem))
			return;
		this.ordersItems.remove(ordersItem);
		ordersItem.setOrders(null);
	}
	
	public void updateDelivery(Delivery delivery) {
		this.delivery = delivery;
		delivery.setOrder(this);
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
		OrdersItem ordersItem = this.ordersItems.get(0);
		String firstItemName = ordersItem.getItemName();
		String firstItemMainBanner = ordersItem.getItemMainBanner();
		
		if(itemCount > 1) {
			this.name = firstItemName + " 외 " + (itemCount - 1) + " 개";
		}
		else {
			this.name = firstItemName;
		}
		
		this.image = firstItemMainBanner;
		
		this.orderedAt = LocalDateTime.now();
		
		this.orderStatus = OrderStatus.ORDER;
	}
	
	// 주문 취소
	public void cancel() {
		this.delivery.cancel();
		
		this.ordersItems.forEach(oi -> {
			oi.cancel();
		});
		
		this.canceledAt = LocalDateTime.now();
		
		this.orderStatus = OrderStatus.CANCEL;
	}
	
	//
	public void comp() {
		if(this.orderStatus == OrderStatus.CANCEL)
			throw new RuntimeException("주문 취소된 상품입니다.");
		delivery.comp();
	}
	
	// 현재고객이 주문고객가 동일여부
	public boolean memberEq(Member member) {
		return this.member.equals(member);
	}

}
