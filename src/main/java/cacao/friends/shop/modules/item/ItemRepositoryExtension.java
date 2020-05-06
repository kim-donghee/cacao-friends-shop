package cacao.friends.shop.modules.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ItemRepositoryExtension {
	
	Page<Item> findByCondition(ItemCondition condition, Pageable pageable);

}
