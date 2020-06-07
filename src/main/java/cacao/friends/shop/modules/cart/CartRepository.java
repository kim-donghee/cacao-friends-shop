package cacao.friends.shop.modules.cart;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.member.Member;

@Transactional(readOnly = true)
public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositoryExtension {
	
	@EntityGraph(attributePaths = { "cartItems", "cartItems.item" })
	Cart findWithItemByMember(Member member);
	
}
