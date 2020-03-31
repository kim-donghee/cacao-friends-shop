package cacao.friends.shop.modules.account;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import cacao.friends.shop.modules.address.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor @Builder 
public class Account {
	
	@Id @GeneratedValue
	private Long id;
	
	@Column(unique = true, nullable = false, length = 20)
	private String email;
	
	@Column(unique = true, nullable = false, length = 20)
	private String username;
	
	@Column(unique = true, nullable = false)
	private String password;
	
	@Embedded
	private Address address;
	
	private LocalDateTime joinedAt;	// 가입 이메일 인증 시간
	
	private String emailCheckToken;	// 이메일 인증 토큰
	
	private LocalDateTime emailCheckTokenGeneratedAt; // 이메일 검증 토큰 발급 시간
	
	private boolean emailVerified;		// 이메일 인증 여부
	
	private boolean itemCreatedByEmail;	// 선호 캐릭터 이메일 알림 여부
	
	private boolean itemCreatedByWeb;	// 선호 캐릭터 웹 알림 여부
	
	//===비즈니스 로직===//
	public void generateEmailToken() {
		this.emailCheckToken = UUID.randomUUID().toString();
		this.emailCheckTokenGeneratedAt = LocalDateTime.now();
	}
	
	public void completeJoin() {
		this.joinedAt = LocalDateTime.now();
		this.emailVerified = true;
	}
	
	public void updateAddress(String city, String street, String zipcode, String etc) {
		this.address = new Address(city, street, zipcode, etc);
	}
	
}
