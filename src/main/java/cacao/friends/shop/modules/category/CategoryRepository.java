package cacao.friends.shop.modules.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByParentCategoryIsNull();

	List<Category> findByParentCategoryId(Long id);
	
}
