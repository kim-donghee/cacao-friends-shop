package cacao.friends.shop.modules.tag;

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

@Entity
@Getter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder 
public class Tag {
	
	@Id @GeneratedValue
	private Long id;
	
	@Column(unique = true, nullable = false, length = 20)
	private String name;
	
	@Lob
	private String image;

}
