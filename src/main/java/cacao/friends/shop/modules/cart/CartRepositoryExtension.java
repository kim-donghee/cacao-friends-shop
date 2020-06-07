package cacao.friends.shop.modules.cart;

import java.util.List;

import cacao.friends.shop.modules.member.Member;

public interface CartRepositoryExtension {
	
	void removeItemAll(Member currentMember);
	
	void removeItem(CartItem removeCartItem);
	
	void removeItem(List<CartItem> removeCartItems);
	
	CartItem findCartItem(Member currentMember, Long cartItemId);
	
	List<CartItem> findCartItem(Member currentMember, List<Long> cartItemIds);
	
	List<CartItem> findCartItemWithItem(Member currentMember, List<Long> cartItemIds);
	
	Long countCartItem(Member currentMember);

}
