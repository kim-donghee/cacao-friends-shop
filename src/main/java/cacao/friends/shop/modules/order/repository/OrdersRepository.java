package cacao.friends.shop.modules.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.member.Member;
import cacao.friends.shop.modules.order.Orders;

@Transactional(readOnly = true)
public interface OrdersRepository extends JpaRepository<Orders, Long>, OrdersRepositoryExtension {
	
	@EntityGraph(attributePaths = { "ordersItems.item", "delivery"})
	List<Orders> findWithItemAndDeliveryByMemberOrderByOrderedAtDesc(Member member);
	
	@EntityGraph(attributePaths = { "ordersItems.item", "delivery"})
	Orders findWithItemAndDeliveryById(Long id);
	
}
