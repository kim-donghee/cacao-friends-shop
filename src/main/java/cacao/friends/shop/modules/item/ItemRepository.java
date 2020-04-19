package cacao.friends.shop.modules.item;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ItemRepository extends JpaRepository<Item, Long> {
	
	@EntityGraph(attributePaths = { "banners" })
	Item findWithBannersById(Long id);
	
	@EntityGraph(attributePaths = { "categorys" })
	Item findWithCategorysById(Long id);

}
