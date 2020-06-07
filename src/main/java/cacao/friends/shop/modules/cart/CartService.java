package cacao.friends.shop.modules.cart;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.member.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
	
	private final CartRepository cartRepository;
	
	public Cart newCart(Member currentMember) {
		return cartRepository.save(Cart.builder().member(currentMember).build());
	}
	
	public void addCartItem(Cart cart, Item item, int quantity) {
		CartItem cartItem = CartItem.builder().item(item).quantity(quantity).build();
		cart.addCartItem(cartItem);
	}
	
	public void changeQuantity(CartItem cartItem, int quantity) {
		cartItem.changeQuantity(quantity);
	}
	
	public void removeItem(CartItem removeCartItem) {
		cartRepository.removeItem(removeCartItem);
	}
	
	public void removeItem(List<CartItem> removeCartItems) {
		cartRepository.removeItem(removeCartItems);
	}
	
	public void removeItemAll(Member currentMember) {
		cartRepository.removeItemAll(currentMember);
	}

}
