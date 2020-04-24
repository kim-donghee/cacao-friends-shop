package cacao.friends.shop.modules.item;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cacao.friends.shop.modules.category.Category;

@SuppressWarnings("serial")
public class ItemSpec {
	
	public static Specification<Item> itemCondition(ItemCondition itemCondition) {
		return new Specification<Item>() {
			@Override
			public Predicate toPredicate(
					Root<Item> root, 
					CriteriaQuery<?> query, 
					CriteriaBuilder cb) {
				
				List<Predicate> predicates = new ArrayList<>();
				
				if(itemCondition.getKeyword() != null)
					predicates.add(cb.like(root.get("name"), "%" + itemCondition.getKeyword() + "%"));
				
				if(itemCondition.getPublished() != null)
					predicates.add(cb.equal(root.get("published"), itemCondition.getPublished()));
				
				if(itemCondition.getClosed() != null)
					predicates.add(cb.equal(root.get("closed"), itemCondition.getClosed()));
				
				if(itemCondition.getPaused() != null)
					predicates.add(cb.equal(root.get("paused"), itemCondition.getPaused()));
				
				if(itemCondition.getCharacterId() != null) {
					predicates.add(cb.equal(root.get("character"), itemCondition.getCharacterId()));
				}

				if(itemCondition.getCategoryId() != null) {
					Join<Item, ItemCategory> itemCategory = root.join("itemCategorys");
					Join<ItemCategory, Category> category = itemCategory.join("category");
					
					query.distinct(true);
					
					predicates.add(cb.equal(category.get("parentCategory"), itemCondition.getCategoryId()));
					
					if(itemCondition.getSubCategoryId() != null) {
						predicates.add(cb.equal(category.get("id"), itemCondition.getSubCategoryId()));
					}
				}
				
				return cb.and(predicates.toArray(new Predicate[0]));
			}
		};
	}
	
	public static Specification<Item> searchUndisclosed() {
		return new Specification<Item>() {
			@Override
			public Predicate toPredicate(
					Root<Item> root, 
					CriteriaQuery<?> query, 
					CriteriaBuilder cb) {
				return cb.equal(root.get("published"), false);
			}
		};
	}
	
}
