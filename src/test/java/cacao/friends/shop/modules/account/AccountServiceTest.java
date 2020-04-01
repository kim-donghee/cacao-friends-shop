package cacao.friends.shop.modules.account;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.account.form.JoinForm;
import cacao.friends.shop.modules.address.form.AddressForm;

@ActiveProfiles("local")
@SpringBootTest
@Transactional
public class AccountServiceTest {
	
	@Autowired AccountService accountService;
	
	@Autowired AccountRepository accountRepository;
	
	@PersistenceContext EntityManager em;
	
	@Test
	void 저장() {
		JoinForm joinForm = new JoinForm();
		joinForm.setUsername("dong");
		joinForm.setPassword("12345678");
		joinForm.setEmail("dong@dong.com");
		
		Account newAccount = accountService.saveNewAccount(joinForm);
		
		accountService.complateJoin(newAccount);
		
		assertNotNull(newAccount.getEmailCheckToken());
		assertTrue(newAccount.isEmailVerified());
	}
	
	@Test
	void 저장_주소수정() {
		JoinForm joinForm = new JoinForm();
		joinForm.setUsername("dong");
		joinForm.setPassword("12345678");
		joinForm.setEmail("dong@dong.com");
		
		Account newAccount = accountService.saveNewAccount(joinForm);
		
		em.flush();
		em.clear();
		
		AddressForm addressForm = new AddressForm();
		addressForm.setCity("city");
		addressForm.setStreet("street");
		addressForm.setZipcode("zipcode");
		addressForm.setEtc("etc");
		
		accountService.updateAddress(newAccount, addressForm);
		
		em.flush();
		em.clear();
		
		Account findAccount = accountRepository.findById(1L).get();
		
		assertNotNull(findAccount.getAddress());
	}

}
