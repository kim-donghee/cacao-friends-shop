package cacao.friends.shop.modules.item;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder 
public class ItemBanner {
	
	@Id @GeneratedValue
	private Long id;
	
	@Lob @Basic
	private String image;
	
	@ManyToOne
	private Item item;
	
	public boolean equalsImage(String image) {
		return this.image.equals(image);
	}

}
