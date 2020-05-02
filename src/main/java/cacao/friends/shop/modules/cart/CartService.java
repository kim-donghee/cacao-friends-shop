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
	
	public void createCart(Item item, Member member, int quantity) {
		cartRepository.save(Cart.builder().item(item).member(member).quantity(quantity).build());
	}
	
	public void updateCart(Cart cart, int quantity) {
		cart.setQuantity(quantity);
	}
	
	public void remove(Cart cart) {
		cartRepository.delete(cart);
	}
	
	public void remove(List<Cart> carts) {
		cartRepository.deleteAll(carts);
	}
	
	public void removeAll(Member member) {
		cartRepository.deleteByMember(member);
	}

}
