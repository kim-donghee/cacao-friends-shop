package cacao.friends.shop.modules.cart;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.member.Member;

@Transactional(readOnly = true)
public interface CartRepository extends JpaRepository<Cart, Long> {
	
	@EntityGraph(attributePaths = { "item" })
	List<Cart> findWithItemByMember(Member member);
	
	@EntityGraph(attributePaths = { "item" })
	List<Cart> findAllById(Iterable<Long> ids);
	
	long countByMember(Member member);
	
//	@Modifying
//	@Qualifier("")
	void deleteByMember(Member member);

}
