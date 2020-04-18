package cacao.friends.shop.modules.item;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
	
	@EntityGraph(attributePaths = { "banners" })
	Item findWithBannersById(Long id);
	
	@EntityGraph(attributePaths = { "categorys" })
	Item findWithCategorysById(Long id);

}
