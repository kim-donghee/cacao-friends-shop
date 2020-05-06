package cacao.friends.shop.modules.item;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryExtension {
	
	@EntityGraph(attributePaths = { "banners" })
	Item findWithBannersById(Long id);
	
	@EntityGraph(attributePaths = { "itemCategorys" })
	Item findWithCategorysById(Long id);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "DELETE FROM item WHERE published = false", nativeQuery = true)
	void undisclosedRemove();

}
