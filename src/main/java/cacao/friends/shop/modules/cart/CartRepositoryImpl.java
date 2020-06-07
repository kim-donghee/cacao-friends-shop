package cacao.friends.shop.modules.cart;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.JPQLQuery;

import cacao.friends.shop.modules.item.QItem;
import cacao.friends.shop.modules.member.Member;

public class CartRepositoryImpl extends QuerydslRepositorySupport implements CartRepositoryExtension {

	public CartRepositoryImpl() {
		super(Cart.class);
	}
	
	@Override
	public void removeItem(CartItem removeCartItem) {
		QCartItem cartItem = QCartItem.cartItem;
		delete(cartItem)
			.where(cartItem.eq(removeCartItem))
			.execute();
	}
	
	@Override
	public void removeItem(List<CartItem> removeCartItems) {
		QCartItem cartItem = QCartItem.cartItem;
		delete(cartItem)
			.where(cartItem.in(removeCartItems))
			.execute();
	}
	
	@Override
	public void removeItemAll(Member currentMember) {
		QCart cart = QCart.cart;
		QCartItem cartItem = QCartItem.cartItem;
		
		JPQLQuery<Long> query = getQuerydsl().createQuery()
			.select(cartItem.id)
			.from(cart)
			.innerJoin(cart.cartItems, cartItem)
			.where(cart.member.eq(currentMember));
		
		delete(cartItem)
			.where(cartItem.id.in(query))
			.execute();
	}
	
	@Override
	public CartItem findCartItem(Member currentMember, Long cartItemId) {
		QCart cart = QCart.cart;
		QCartItem cartItem = QCartItem.cartItem;
		return from(cartItem)
			.innerJoin(cartItem.cart, cart).fetchJoin()
			.where(cart.member.eq(currentMember)
				.and(cartItem.id.eq(cartItemId))).fetchOne();
	}
	
	@Override
	public List<CartItem> findCartItem(Member currentMember, List<Long> cartItemIds) {
		QCart cart = QCart.cart;
		QCartItem cartItem = QCartItem.cartItem;
		return from(cartItem)
			.innerJoin(cartItem.cart, cart).fetchJoin()
			.where(cart.member.eq(currentMember)
				.and(cartItem.id.in(cartItemIds))).fetch();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CartItem> findCartItemWithItem(Member currentMember, List<Long> cartItemIds) {
		QCart cart = QCart.cart;
		QCartItem cartItem = QCartItem.cartItem;
		QItem item = QItem.item;
		return from(cartItem)
			.innerJoin(cartItem.cart, cart).fetchJoin()
			.innerJoin(cartItem.item, item).fetchJoin()
			.where(cart.member.eq(currentMember)
				.and(cartItem.id.in(cartItemIds))).fetch();
	}
	
	@Override
	public Long countCartItem(Member currentMember) {
		QCart cart = QCart.cart;
		QCartItem cartItem = QCartItem.cartItem;
		
		return getQuerydsl().createQuery()
			.select(cartItem.count())
			.from(cart)
			.innerJoin(cart.cartItems, cartItem)
			.where(cart.member.eq(currentMember))
			.fetchOne();
	}
	
}
