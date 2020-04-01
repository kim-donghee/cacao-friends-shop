package cacao.friends.shop.modules.account;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import cacao.friends.shop.modules.address.Address;
import cacao.friends.shop.modules.address.form.AddressForm;
import cacao.friends.shop.modules.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
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
	
	private LocalDateTime joinedAt;	// 가입 이메일 인증 일시
	
	private String emailCheckToken;	// 이메일 인증 토큰
	
	private LocalDateTime emailCheckTokenGeneratedAt; // 이메일 검증 토큰 발급 일시
	
	private boolean emailVerified;		// 이메일 인증 여부
	
	@Builder.Default
	private boolean itemCreatedByEmail	= false; // 선호 캐릭터 이메일 알림 여부
	
	@Builder.Default
	private boolean itemCreatedByWeb = true;	// 선호 캐릭터 웹 알림 여부
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Tag pickTag;	// 선호 캐릭터
	
	//===비즈니스 로직===//
	// 토큰 발급
	public void generateEmailToken() {
		this.emailCheckToken = UUID.randomUUID().toString();
		this.emailCheckTokenGeneratedAt = LocalDateTime.now();
	}
	
	// 토큰 인증 성공
	public void completeJoin() {
		this.joinedAt = LocalDateTime.now();
		this.emailVerified = true;
	}
	
	// 토큰 인증
	public boolean isValidToken(String token) {
		return this.emailCheckToken.equals(token);
	}
	
	// 주소 변경
	public void updateAddress(AddressForm addressForm) {
		this.address = new Address(addressForm.getCity(), 
				addressForm.getStreet(), addressForm.getZipcode(), addressForm.getEtc());
	}
	
}
