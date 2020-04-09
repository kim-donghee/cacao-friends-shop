package cacao.friends.shop.modules.item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import cacao.friends.shop.modules.CharacterKind.CharacterKind;
import cacao.friends.shop.modules.category.Category;
import cacao.friends.shop.modules.item.exception.NotEnoughStockException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder 
public class Item {
	
	@Id @GeneratedValue
	private Long id;
	
	@Column(unique = true, nullable = false, length = 50)
	private String name;
	
	@Builder.Default
	@Column(nullable = false)
	private Integer price = 0;
	
	@Builder.Default
	@Column(nullable = false)
	private Integer stockQuantity = 0;
	
	@Lob
	@Column(nullable = false)
	private String detail;
	
	@Lob
	@ElementCollection
	@Builder.Default
	private List<String> titleImages = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true)
	private CharacterKind tag;
	
	@OneToMany
	@Builder.Default
	private Set<Category> categorys = new HashSet<>();
	
	//===비즈니스 로직===//
	// 재고 추가
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}
	
	// 재고 감소 (부족하면 에러 발생)
	public void removeStock(int quantity) {
		int resultStock = this.stockQuantity - quantity;
		if(resultStock < 0) {
			throw new NotEnoughStockException(this.name + " 의 제고가 부족합니다.");
		}
	}
	
	// 카테고리 추가
	public void addCategory(Category category) {
		this.categorys.add(category);
	}
	
	// 카테고리 삭제
	public void removeCategory(Category category) {
		this.categorys.remove(category);
	}

}
