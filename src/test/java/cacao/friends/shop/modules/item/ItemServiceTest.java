package cacao.friends.shop.modules.item;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.item.form.ItemForm;

@Transactional
@SpringBootTest
@ActiveProfiles("local")
public class ItemServiceTest {
	
	@Autowired ItemService itemService;
	
	@PersistenceContext EntityManager em;
	
	@Test
	void 저장_이미지수정() {
		ItemForm form = new ItemForm();
		form.setName("라이언");
		form.setPrice(1000);
		form.setStockQuantity(10);
		form.setShortDescript("라이언");
		form.setDetail("라이언");
		
		Item saveItem = itemService.createItem(form);
		em.flush();
		itemService.addBanner(saveItem, "image : image");
		em.flush();
		em.clear();
	}

}
