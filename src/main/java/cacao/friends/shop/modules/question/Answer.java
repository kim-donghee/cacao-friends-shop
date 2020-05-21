package cacao.friends.shop.modules.question;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder 
public class Answer {
	
	@Id @GeneratedValue
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Question question;
	
	@Lob @Basic
	private String detail;
	
	@Builder.Default
	private LocalDateTime createdDateAt = LocalDateTime.now();	// 응답 일시

}
