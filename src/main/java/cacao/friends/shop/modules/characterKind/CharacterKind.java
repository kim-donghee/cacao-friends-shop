package cacao.friends.shop.modules.characterKind;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder 
public class CharacterKind {
	
	@Id @GeneratedValue
	private Long id;
	
	@Column(nullable = false, length = 20)
	private String name;
	
	@Lob
	private String image;

}
