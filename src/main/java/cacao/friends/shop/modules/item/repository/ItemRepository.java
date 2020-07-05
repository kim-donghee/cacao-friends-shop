package cacao.friends.shop.modules.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.item.Item;

@Transactional(readOnly = true)
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryExtension {
	
	@EntityGraph(attributePaths = { "banners" })
	Item findWithBannersById(Long id);
	
	@EntityGraph(attributePaths = { "itemCategorys" })
	Item findWithCategorysById(Long id);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "DELETE FROM item WHERE published = false", nativeQuery = true)
	void undisclosedRemove();
	
	List<Item> findTop6ByPublishedAndPausedAndClosedOrderByPublishedDateTimeDesc(
			boolean published, boolean paused, boolean closed);

}
