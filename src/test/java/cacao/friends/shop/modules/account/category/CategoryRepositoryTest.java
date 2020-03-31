package cacao.friends.shop.modules.account.category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.category.Category;
import cacao.friends.shop.modules.category.CategoryRepository;

@Transactional
@SpringBootTest
public class CategoryRepositoryTest {
	
	@Autowired CategoryRepository categoryRepository;
	
	@PersistenceContext EntityManager em;
	
	@Test @Rollback(false)
	void 저장() {
		Category category1 = Category
				.builder()
				.name("category")
				.build();
		Category category2 = Category
				.builder()
				.name("category2")
				.build();
		
		categoryRepository.save(category1);
		
		em.flush();
		
		category1.addChildCategory(category2);
		
		em.flush();
		em.clear();
		
		assertEquals(category1.getParentCategoryId(), 0L);
		assertEquals(category2.getParentCategoryId(), 1L);
		
		Category findCategory1 = categoryRepository.findById(1L).get();
		Category findCategory2 = categoryRepository.findById(2L).get();
		
		findCategory1.removeChildCategory(findCategory2);
		
		em.flush();
		em.clear();
		
		assertEquals(findCategory1.getParentCategoryId(), 0L);
		assertEquals(findCategory2.getParentCategoryId(), 0L);
	}

}
