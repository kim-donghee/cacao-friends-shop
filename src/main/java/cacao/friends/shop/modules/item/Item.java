package cacao.friends.shop.modules.item;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import cacao.friends.shop.modules.characterKind.CharacterKind;
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
	
	//=== 기본 정보 ===//
	@Column(nullable = false, length = 20)
	private String name;
	
	@Builder.Default
	@Column(nullable = false)
	private Integer price = 0;
	
	@Builder.Default
	@Column(nullable = false)
	private Integer stockQuantity = 0;
	
	@Lob @Basic
	@Column(nullable = false)
	private String detail;
	
	@Column(nullable = false, length = 100)
	private String shortDescript;
	
	//=== 배너  ===//
	@Lob @Basic
	private String mainBanner;
	
	@Builder.Default
	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ItemBanner> banners = new HashSet<>();
	
	//=======//
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true)
	private CharacterKind character;
	
	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private Set<ItemCategory> itemCategories = new HashSet<>();
	
	//=== 상품 상태 ===//
	@Builder.Default
	private boolean published = false;	// 공개 여부
	
	@Builder.Default
	private boolean paused = false;		// 일시 정지 여부
	
	@Builder.Default
	private boolean closed = false;		// 종료 여부
	
	private LocalDateTime publishedDateTime;	// 상품 공개 일시
	
	private LocalDateTime pausedDateTime;		// 일시 정지 일시
	
	private LocalDateTime closedDateTime;		// 상품 판매 종료 일시
	
	
	//===비즈니스 로직===//
	// 재고 추가
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}
	
	// 재고 감소 (부족하면 에러 발생)
	public void removeStock(int quantity) {
		int resultStock = this.stockQuantity - quantity;
		if(resultStock < 0) 
			throw new NotEnoughStockException(this.name + " 의 재고가 부족합니다.");
		this.stockQuantity = resultStock;
	}
	
	// 제목 이미지 추가
	public void addBanner(ItemBanner banner) {
		if(this.banners.size() == 0) {
			this.mainBanner = banner.getImage();
		}
		
		this.banners.add(banner);
		banner.setItem(this);
	}
	
	// 제목 이미지 삭제
	public void removeBanner(ItemBanner banner) {
		this.banners.remove(banner);
		banner.setItem(null);
		
		if(banner.equalsImage(this.mainBanner)) {
			this.mainBanner = null;
			if(this.banners.iterator().hasNext()) {
				updateMainBanner(this.banners.iterator().next().getImage());;
			}
		}
	}
	
	// 메인 제목 이미지 변경
	public void updateMainBanner(String mainBanner) {
		this.mainBanner = mainBanner;
	}
	
	// 카테고리 추가
	public void addCategory(ItemCategory itemCategory) {
		this.itemCategories.add(itemCategory);
		itemCategory.setItem(this);
	}
	
	// 카테고리 삭제
	public void removeCategory(ItemCategory itemCategory) {
		this.itemCategories.remove(itemCategory);
		itemCategory.setItem(null);
	}
	
	// 상품 공개
	public void publish() {
		if(this.published || this.closed) 
			throw new RuntimeException("상품을 공개할 수 없는 상태입니다. 상품이 이미 공개되었거나 판매 종료되었습니다.");
		this.published = true;
		this.publishedDateTime = LocalDateTime.now();
	}
	
	// 상품 판매 종료
	public void close() {
		if(!this.published || this.closed) 
			throw new RuntimeException("상품을 판매 종료할 수 없는 상태입니다. 상품이 공개되지않았거나 이미 판매 종료되었습니다.");
		this.closed = true;
		this.closedDateTime = LocalDateTime.now();
	}
	
	// 상품 판매 정지
	public void pause() {
		if(this.paused) 
			throw new RuntimeException("상품을 판매 일시 정지할 수 없는 상태입니다. 상품이 이미 판매 정지입니다.");
		this.paused = true;
		this.pausedDateTime = LocalDateTime.now();
	}
	
	// 상품 판매 정지 -> 재개
	public void resum() {
		if(!this.paused)
			throw new RuntimeException("상품을 판매할 수 없는 상태입니다. 상품이 이미 판매 중입니다.");
		this.paused = false;
	}

}
