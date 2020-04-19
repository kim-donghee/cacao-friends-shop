package cacao.friends.shop.modules.notification;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import cacao.friends.shop.modules.account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder 
public class Notification {
	
	@Id @GeneratedValue
	private Long id;
	
	private String title;
	
	private String link;
	
	private String message;
	
	@Builder.Default
	private boolean checked = false;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Account account;
	
	private LocalDateTime createdDateTime;
	
	//===비즈니스 로직===//
	public void read() {
		this.checked = true;
	}

}
