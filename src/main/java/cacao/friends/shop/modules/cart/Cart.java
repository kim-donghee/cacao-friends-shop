package cacao.friends.shop.modules.cart;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import cacao.friends.shop.modules.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder 
public class Cart {
	
	@Id @GeneratedValue
	private Long id;
	
	@JoinColumn(unique = true)
	@ManyToOne(fetch = FetchType.EAGER)
	private Member member;
	
	@Builder.Default
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CartItem> cartItems = new ArrayList<>();
	
	//===비즈니스 로직===//
	// 카트 항목 추가
	public void addCartItem(CartItem cartItem) {
		if(cartItems.contains(cartItem))
			return;
		this.cartItems.add(cartItem);
		cartItem.setCart(this);
	}
	
	// 카트 항목 삭제
	public void removeCartItem(CartItem cartItem) {
		if(!cartItems.contains(cartItem))
			return;
		this.cartItems.remove(cartItem);
		cartItem.setCart(null);
	}
	
	// 카트 전체 항목 가격
	public long totalPrice() {
		long totalPrice = 0;
		for(CartItem cartItem : cartItems) {
			totalPrice += cartItem.getPrice();
		}
		return totalPrice;
	}
	
	// 카트가 비어있는지 여부
	public boolean isEmpty() {
		return this.cartItems.isEmpty();
	}
	
	public int cartItemCount() {
		return this.cartItems.size();
	}

}