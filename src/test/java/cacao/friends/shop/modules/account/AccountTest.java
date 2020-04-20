package cacao.friends.shop.modules.account;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cacao.friends.shop.modules.address.Address;
import cacao.friends.shop.modules.member.Member;

@SpringBootTest
public class AccountTest {
	
	@DisplayName("회원 가입 - 이메일 인증 성공")
	@Test
	void 이메일_인증_성공() {
		Member member = Member
				.builder()
				.username("dong")
				.password("1234")
				.email("dong@dong.com")
				.address(new Address("city", "street", "zipcode", "etc"))
				.build();
		member.generateEmailToken();
		member.completeJoin();
//		account.updateAddress("city", "street", "zipcode", "etc");
		
		assertTrue(member.isEmailVerified());
	}
	
	@DisplayName("회원 가입 - 이메일 인증 실패")
	@Test
	void 이메일_인증_실패() {
		Member member = Member
				.builder()
				.username("dong")
				.password("1234")
				.email("dong@dong.com")
				.build();
		member.generateEmailToken();
//		account.completeJoin();
//		account.updateAddress("city", "street", "zipcode", "etc");
		
		assertFalse(member.isEmailVerified());
	}

}
