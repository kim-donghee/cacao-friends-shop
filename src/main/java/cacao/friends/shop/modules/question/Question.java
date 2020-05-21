package cacao.friends.shop.modules.question;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import cacao.friends.shop.modules.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder 
public class Question {
	
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Member questioner;
	
	@OneToOne(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Answer answer;				// 응답 내용
	
	@Column(nullable = false, length = 50)
	private String title;
	
	@Lob @Basic
	private String detail;
	
	@Builder.Default
	private boolean published = true;	// 공개, 비공개 여부
	
	@Builder.Default
	private boolean answered = false;	// 응답 여부
	
	@Builder.Default
	private LocalDateTime createdDateAt = LocalDateTime.now();	// 질문 일시
	
	public boolean questionerEq(Member questioner) {
		return this.questioner.equals(questioner);
	}

}
