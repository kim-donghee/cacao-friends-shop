package cacao.friends.shop.modules.category;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByParentCategoryIsNull();
	
	@EntityGraph(attributePaths = "childCategorys")
	List<Category> findWithChildByParentCategoryIsNull();
	
	@EntityGraph(attributePaths = "childCategorys")
	List<Category> findAllWithChildBy();

	List<Category> findByParentCategoryId(Long id);
	
}
