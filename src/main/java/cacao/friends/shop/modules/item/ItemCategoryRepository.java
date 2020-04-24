package cacao.friends.shop.modules.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
	
	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM ItemCategory ic WHERE item_id = :item")
	void deleteByItem(Long item);

}
