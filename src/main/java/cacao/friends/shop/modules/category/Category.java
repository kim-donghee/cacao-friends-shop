package cacao.friends.shop.modules.category;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder 
public class Category {
	
	@Id @GeneratedValue
	private Long id;
	
	@Column(unique = true, nullable = false, length = 20)
	private String name;
	
	@ManyToOne
	private Category parentCategory;
	
	@OneToMany(mappedBy = "parentCategory")
	@Builder.Default
	private Set<Category> childCategorys = new HashSet<>();
	
	//===비즈니스 로직===//
	// 하위 카테고리 추가
	public void addChildCategory(Category childCategory) {
		this.childCategorys.add(childCategory);
		childCategory.parentCategory = this;
	}
	
	// 하위 카테고리 삭제
	public void removeChildCategory(Category childCategory) {
		this.childCategorys.remove(childCategory);
		childCategory.parentCategory = null;
	}
	
	// 상위 카테고리 확인
	public boolean isParentCategory() {
		return this.parentCategory == null;
	}	
	
}
