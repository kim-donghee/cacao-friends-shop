package cacao.friends.shop.modules.order;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.member.Member;

@Transactional(readOnly = true)
public interface OrdersRepository extends JpaRepository<Orders, Long>, OrdersRepositoryExtension {
	
	@EntityGraph(attributePaths = { "ordersItems.item", "delivery"})
	List<Orders> findWithItemAndDeliveryByMemberOrderByOrderedAtDesc(Member member);
	
	@EntityGraph(attributePaths = { "ordersItems.item", "delivery"})
	Orders findWithItemAndDeliveryByIdAndMember(Long id, Member member);
	
}
