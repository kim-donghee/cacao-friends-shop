package cacao.friends.shop.modules.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface OrdersRepositoryExtension {
	
	public Page<Orders> findByCondition(OrdersCondition condition, Pageable pageable);

}
