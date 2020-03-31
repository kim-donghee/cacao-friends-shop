package cacao.friends.shop.modules.account;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cacao.friends.shop.modules.address.Address;

@SpringBootTest
public class AccountTest {
	
	@DisplayName("회원 가입 - 이메일 인증 성공")
	@Test
	void 이메일_인증_성공() {
		Account account = Account
				.builder()
				.username("dong")
				.password("1234")
				.email("dong@dong.com")
				.address(new Address("city", "street", "zipcode", "etc"))
				.build();
		account.generateEmailToken();
		account.completeJoin();
		account.updateAddress("city", "street", "zipcode", "etc");
		
		assertTrue(account.isEmailVerified());
	}
	
	@DisplayName("회원 가입 - 이메일 인증 실패")
	@Test
	void 이메일_인증_실패() {
		Account account = Account
				.builder()
				.username("dong")
				.password("1234")
				.email("dong@dong.com")
				.build();
		account.generateEmailToken();
//		account.completeJoin();
		account.updateAddress("city", "street", "zipcode", "etc");
		
		assertFalse(account.isEmailVerified());
	}

}
